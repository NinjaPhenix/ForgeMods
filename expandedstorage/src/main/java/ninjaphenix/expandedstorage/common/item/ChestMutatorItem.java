package ninjaphenix.expandedstorage.common.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import ninjaphenix.expandedstorage.ExpandedStorage;
import ninjaphenix.expandedstorage.Registries;
import ninjaphenix.expandedstorage.common.block.BaseChestBlock;
import ninjaphenix.expandedstorage.common.block.CursedChestBlock;
import ninjaphenix.expandedstorage.common.block.enums.CursedChestType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.minecraft.state.properties.BlockStateProperties.WATERLOGGED;
import static net.minecraft.util.Rotation.CLOCKWISE_180;
import static net.minecraft.util.Rotation.CLOCKWISE_90;

public class ChestMutatorItem extends ChestModifierItem
{
    private static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final EnumProperty<CursedChestType> TYPE = BaseChestBlock.TYPE;

    public ChestMutatorItem() { super(new Item.Properties().maxStackSize(1).group(ExpandedStorage.group)); }

    @NotNull @Override @SuppressWarnings("ConstantConditions")
    protected ActionResultType useModifierOnChestBlock(@NotNull final ItemUseContext context, @NotNull final BlockState mainState,
            @NotNull final BlockPos mainPos, @Nullable final BlockState otherState, @Nullable final BlockPos otherPos)
    {
        final PlayerEntity player = context.getPlayer();
        final World world = context.getWorld();
        final ItemStack stack = context.getItem();
        switch (getMode(stack))
        {
            case MERGE:
                final CompoundNBT tag = stack.getOrCreateTag();
                if (tag.contains("pos"))
                {
                    if (mainState.get(TYPE) == CursedChestType.SINGLE)
                    {
                        final BlockPos pos = NBTUtil.readBlockPos(tag.getCompound("pos"));
                        final BlockState realOtherState = world.getBlockState(pos);
                        if (realOtherState.getBlock() == mainState.getBlock() && realOtherState.get(FACING) == mainState.get(FACING) &&
                                realOtherState.get(TYPE) == CursedChestType.SINGLE)
                        {
                            if (!world.isRemote)
                            {
                                final BlockPos vec = pos.subtract(mainPos);
                                final int sum = vec.getX() + vec.getY() + vec.getZ();
                                if (sum == 1 || sum == -1)
                                {
                                    final CursedChestType mainChestType = CursedChestBlock.getChestType(mainState.get(FACING),
                                            Direction.getFacingFromVector(vec.getX(), vec.getY(), vec.getZ()));
                                    world.setBlockState(mainPos, mainState.with(TYPE, mainChestType));
                                    world.setBlockState(pos, world.getBlockState(pos).with(TYPE, mainChestType.getOpposite()));
                                    tag.remove("pos");
                                    player.sendStatusMessage(new TranslationTextComponent("tooltip.expandedstorage.chest_mutator.merge_end"), true);
                                    player.getCooldownTracker().setCooldown(this, 5);
                                    return ActionResultType.SUCCESS;
                                }
                            }
                        }
                        return ActionResultType.FAIL;
                    }
                }
                else if (mainState.get(TYPE) == CursedChestType.SINGLE)
                {
                    tag.put("pos", NBTUtil.writeBlockPos(mainPos));
                    player.sendStatusMessage(new TranslationTextComponent("tooltip.expandedstorage.chest_mutator.merge_start"), true);
                    player.getCooldownTracker().setCooldown(this, 5);
                    return ActionResultType.SUCCESS;
                }
                break;
            case UNMERGE:
                if (otherState != null)
                {
                    if (!world.isRemote)
                    {
                        world.setBlockState(mainPos, world.getBlockState(mainPos).with(TYPE, CursedChestType.SINGLE));
                        world.setBlockState(otherPos, world.getBlockState(otherPos).with(TYPE, CursedChestType.SINGLE));
                    }
                    player.getCooldownTracker().setCooldown(this, 5);
                    return ActionResultType.SUCCESS;
                }
                break;
            case ROTATE:
                switch (mainState.get(CursedChestBlock.TYPE))
                {
                    case SINGLE:
                        if (!world.isRemote) { world.setBlockState(mainPos, mainState.rotate(world, mainPos, CLOCKWISE_90)); }
                        player.getCooldownTracker().setCooldown(this, 5);
                        return ActionResultType.SUCCESS;
                    case FRONT:
                    case BACK:
                    case LEFT:
                    case RIGHT:
                        if (!world.isRemote)
                        {
                            world.setBlockState(mainPos, mainState.rotate(world, mainPos, CLOCKWISE_180).with(TYPE, mainState.get(TYPE).getOpposite()));
                            world.setBlockState(otherPos, otherState.rotate(world, otherPos, CLOCKWISE_180).with(TYPE, otherState.get(TYPE).getOpposite()));
                        }
                        player.getCooldownTracker().setCooldown(this, 5);
                        return ActionResultType.SUCCESS;
                    case TOP:
                    case BOTTOM:
                        if (!world.isRemote)
                        {
                            world.setBlockState(mainPos, mainState.rotate(world, mainPos, CLOCKWISE_90));
                            world.setBlockState(otherPos, otherState.rotate(world, otherPos, CLOCKWISE_90));
                        }
                        player.getCooldownTracker().setCooldown(this, 5);
                        return ActionResultType.SUCCESS;
                }
        }
        return ActionResultType.FAIL;
    }

    @NotNull @Override @SuppressWarnings({ "ConstantConditions", "OptionalGetWithoutIsPresent" })
    protected ActionResultType useModifierOnBlock(@NotNull final ItemUseContext context, @NotNull final BlockState state)
    {
        final PlayerEntity player = context.getPlayer();
        final ItemStack stack = context.getItem();
        final World world = context.getWorld();
        final BlockPos mainPos = context.getPos();
        final MutatorMode mode = getMode(stack);
        if (state.getBlock() instanceof ChestBlock)
        {
            if (mode == MutatorMode.MERGE)
            {
                final CompoundNBT tag = stack.getOrCreateTag();
                if (tag.contains("pos"))
                {
                    if (state.get(ChestBlock.TYPE) == ChestType.SINGLE)
                    {
                        final BlockPos otherPos = NBTUtil.readBlockPos(tag.getCompound("pos"));
                        final BlockState realOtherState = world.getBlockState(otherPos);
                        if (realOtherState.getBlock() == state.getBlock() && realOtherState.get(FACING) == state.get(FACING) &&
                                realOtherState.get(ChestBlock.TYPE) == ChestType.SINGLE)
                        {
                            final BlockPos vec = otherPos.subtract(mainPos);
                            int sum = vec.getX() + vec.getY() + vec.getZ();
                            if (sum == 1 || sum == -1)
                            {
                                if (!world.isRemote)
                                {
                                    final Registries.TierData entry = Registries.MODELED.getValue(ExpandedStorage.getRl("wood_chest")).get();
                                    final CursedChestType mainChestType = BaseChestBlock.getChestType(state.get(FACING),
                                            Direction.getFacingFromVector(vec.getX(), vec.getY(), vec.getZ()));
                                    BlockState defState = ForgeRegistries.BLOCKS.getValue(entry.getBlockId()).getDefaultState().with(FACING,
                                            state.get(FACING)).with(WATERLOGGED, state.get(WATERLOGGED)).with(TYPE, mainChestType);
                                    TileEntity blockEntity = world.getTileEntity(mainPos);
                                    NonNullList<ItemStack> invData = NonNullList.withSize(entry.getSlotCount(), ItemStack.EMPTY);
                                    ItemStackHelper.loadAllItems(blockEntity.write(new CompoundNBT()), invData);
                                    world.removeTileEntity(mainPos);
                                    world.setBlockState(mainPos, defState);
                                    blockEntity = world.getTileEntity(mainPos);
                                    blockEntity.read(defState, ItemStackHelper.saveAllItems(blockEntity.write(new CompoundNBT()), invData));
                                    blockEntity = world.getTileEntity(otherPos);
                                    invData = NonNullList.withSize(entry.getSlotCount(), ItemStack.EMPTY);
                                    ItemStackHelper.loadAllItems(blockEntity.write(new CompoundNBT()), invData);
                                    world.removeTileEntity(otherPos);
                                    defState = defState.with(WATERLOGGED, state.get(WATERLOGGED)).with(TYPE, mainChestType.getOpposite());
                                    world.setBlockState(otherPos, defState);
                                    blockEntity = world.getTileEntity(otherPos);
                                    blockEntity.read(defState, ItemStackHelper.saveAllItems(blockEntity.write(new CompoundNBT()), invData));
                                    tag.remove("pos");
                                    player.sendStatusMessage(new TranslationTextComponent("tooltip.expandedstorage.chest_mutator.merge_end"), true);
                                    player.getCooldownTracker().setCooldown(this, 5);
                                }
                                return ActionResultType.SUCCESS;
                            }
                        }
                        return ActionResultType.FAIL;
                    }
                }
                else
                {
                    if (state.get(ChestBlock.TYPE) == ChestType.SINGLE)
                    {
                        tag.put("pos", NBTUtil.writeBlockPos(mainPos));
                        player.sendStatusMessage(new TranslationTextComponent("tooltip.expandedstorage.chest_mutator.merge_start"), true);
                        player.getCooldownTracker().setCooldown(this, 5);
                        return ActionResultType.SUCCESS;
                    }
                }
            }
            else if (mode == MutatorMode.UNMERGE)
            {
                BlockPos otherPos;
                switch (state.get(ChestBlock.TYPE))
                {
                    case LEFT: otherPos = mainPos.offset(state.get(ChestBlock.FACING).rotateY()); break;
                    case RIGHT: otherPos = mainPos.offset(state.get(ChestBlock.FACING).rotateYCCW()); break;
                    default: return ActionResultType.FAIL;
                }
                if (!world.isRemote)
                {
                    world.setBlockState(mainPos, state.with(ChestBlock.TYPE, ChestType.SINGLE));
                    world.setBlockState(otherPos, world.getBlockState(otherPos).with(ChestBlock.TYPE, ChestType.SINGLE));
                }
                player.getCooldownTracker().setCooldown(this, 5);
                return ActionResultType.SUCCESS;
            }
            else if (mode == MutatorMode.ROTATE)
            {
                BlockPos otherPos;
                switch (state.get(ChestBlock.TYPE))
                {
                    case LEFT: otherPos = mainPos.offset(state.get(ChestBlock.FACING).rotateY()); break;
                    case RIGHT: otherPos = mainPos.offset(state.get(ChestBlock.FACING).rotateYCCW()); break;
                    case SINGLE:
                        if (!world.isRemote) { world.setBlockState(mainPos, state.rotate(world, mainPos, CLOCKWISE_90)); }
                        player.getCooldownTracker().setCooldown(this, 5);
                        return ActionResultType.SUCCESS;
                    default: return ActionResultType.FAIL;
                }
                if (!world.isRemote)
                {
                    final BlockState otherState = world.getBlockState(otherPos);
                    world.setBlockState(mainPos, state.rotate(world, mainPos, CLOCKWISE_180).with(ChestBlock.TYPE, state.get(ChestBlock.TYPE).opposite()));
                    world.setBlockState(otherPos, otherState.rotate(world, otherPos, CLOCKWISE_180)
                                                            .with(ChestBlock.TYPE, otherState.get(ChestBlock.TYPE).opposite()));
                }
                player.getCooldownTracker().setCooldown(this, 5);
                return ActionResultType.SUCCESS;
            }
        }
        else if (state.getBlock() == Blocks.ENDER_CHEST)
        {
            if (mode == MutatorMode.ROTATE)
            {
                if (!world.isRemote) { world.setBlockState(mainPos, state.rotate(world, mainPos, CLOCKWISE_90)); }
                player.getCooldownTracker().setCooldown(this, 5);
                return ActionResultType.SUCCESS;
            }
            return ActionResultType.FAIL;
        }
        return super.useModifierOnBlock(context, state);
    }

    @NotNull @Override
    protected ActionResult<ItemStack> useModifierInAir(@NotNull final World world, @NotNull final PlayerEntity player, @NotNull final Hand hand)
    {
        if (player.isCrouching())
        {
            final ItemStack stack = player.getHeldItem(hand);
            final CompoundNBT tag = stack.getOrCreateTag();
            tag.putByte("mode", getMode(stack).next);
            if (tag.contains("pos")) { tag.remove("pos"); }
            if (!world.isRemote) { player.sendStatusMessage(getMode(stack).title, true); }
            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }
        return super.useModifierInAir(world, player, hand);
    }

    @Override
    public void onCreated(@NotNull final ItemStack stack, @NotNull final World world, @NotNull final PlayerEntity player)
    { super.onCreated(stack, world, player); getMode(stack); }

    @NotNull @Override
    public ItemStack getDefaultInstance() { final ItemStack stack = super.getDefaultInstance(); getMode(stack); return stack; }

    @Override
    public void fillItemGroup(@NotNull final ItemGroup itemGroup, @NotNull final NonNullList<ItemStack> stackList)
    { if (isInGroup(itemGroup)) { stackList.add(getDefaultInstance()); } }

    @NotNull
    private MutatorMode getMode(@NotNull final ItemStack stack)
    {
        final CompoundNBT tag = stack.getOrCreateTag();
        if (!tag.contains("mode", 1)) { tag.putByte("mode", (byte) 0); }
        return MutatorMode.values()[tag.getByte("mode")];
    }

    @Override
    public void addInformation(@NotNull final ItemStack stack, @Nullable final World world, @NotNull final List<ITextComponent> tooltip,
            @NotNull final ITooltipFlag flag)
    {
        final MutatorMode mode = getMode(stack);
        tooltip.add(new TranslationTextComponent("tooltip.expandedstorage.tool_mode", mode.title).func_240699_a_(TextFormatting.GRAY));
        tooltip.add(mode.description);
        super.addInformation(stack, world, tooltip, flag);
    }
}