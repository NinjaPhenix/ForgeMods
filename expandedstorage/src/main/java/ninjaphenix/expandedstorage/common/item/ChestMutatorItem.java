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

import javax.annotation.Nullable;
import java.util.List;

import static net.minecraft.state.properties.BlockStateProperties.WATERLOGGED;
import static net.minecraft.util.Rotation.CLOCKWISE_180;
import static net.minecraft.util.Rotation.CLOCKWISE_90;

public final class ChestMutatorItem extends ChestModifierItem
{
    private static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final EnumProperty<CursedChestType> TYPE = BaseChestBlock.TYPE;

    public ChestMutatorItem() { super(new Item.Properties().stacksTo(1).tab(ExpandedStorage.group)); }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected ActionResultType useModifierOnChestBlock(final ItemUseContext context, final BlockState mainState,
                                                       final BlockPos mainPos, @Nullable final BlockState otherState, @Nullable final BlockPos otherPos)
    {
        final PlayerEntity player = context.getPlayer();
        final World world = context.getLevel();
        final ItemStack stack = context.getItemInHand();
        switch (getMode(stack))
        {
            case MERGE:
                final CompoundNBT tag = stack.getOrCreateTag();
                if (tag.contains("pos"))
                {
                    if (mainState.getValue(TYPE) == CursedChestType.SINGLE)
                    {
                        final BlockPos pos = NBTUtil.readBlockPos(tag.getCompound("pos"));
                        final BlockState realOtherState = world.getBlockState(pos);
                        if (realOtherState.getBlock() == mainState.getBlock() && realOtherState.getValue(FACING) == mainState.getValue(FACING) &&
                                realOtherState.getValue(TYPE) == CursedChestType.SINGLE)
                        {
                            if (!world.isClientSide)
                            {
                                final BlockPos vec = pos.subtract(mainPos);
                                final int sum = vec.getX() + vec.getY() + vec.getZ();
                                if (sum == 1 || sum == -1)
                                {
                                    final CursedChestType mainChestType = CursedChestBlock.getChestType(
                                            mainState.getValue(FACING), Direction.getNearest(vec.getX(), vec.getY(), vec.getZ()));
                                    world.setBlockAndUpdate(mainPos, mainState.setValue(TYPE, mainChestType));
                                    world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(TYPE, mainChestType.getOpposite()));
                                    tag.remove("pos");
                                    player.displayClientMessage(new TranslationTextComponent("tooltip.expandedstorage.chest_mutator.merge_end"), true);
                                    player.getCooldowns().addCooldown(this, 5);
                                    return ActionResultType.SUCCESS;
                                }
                            }
                        }
                        return ActionResultType.FAIL;
                    }
                }
                else if (mainState.getValue(TYPE) == CursedChestType.SINGLE)
                {
                    tag.put("pos", NBTUtil.writeBlockPos(mainPos));
                    player.displayClientMessage(new TranslationTextComponent("tooltip.expandedstorage.chest_mutator.merge_start"), true);
                    player.getCooldowns().addCooldown(this, 5);
                    return ActionResultType.SUCCESS;
                }
                break;
            case UNMERGE:
                if (otherState != null)
                {
                    if (!world.isClientSide)
                    {
                        world.setBlockAndUpdate(mainPos, world.getBlockState(mainPos).setValue(TYPE, CursedChestType.SINGLE));
                        world.setBlockAndUpdate(otherPos, world.getBlockState(otherPos).setValue(TYPE, CursedChestType.SINGLE));
                    }
                    player.getCooldowns().addCooldown(this, 5);
                    return ActionResultType.SUCCESS;
                }
                break;
            case ROTATE:
                switch (mainState.getValue(CursedChestBlock.TYPE))
                {
                    case SINGLE:
                        if (!world.isClientSide) { world.setBlockAndUpdate(mainPos, mainState.rotate(world, mainPos, CLOCKWISE_90)); }
                        player.getCooldowns().addCooldown(this, 5);
                        return ActionResultType.SUCCESS;
                    case FRONT:
                    case BACK:
                    case LEFT:
                    case RIGHT:
                        if (!world.isClientSide)
                        {
                            world.setBlockAndUpdate(mainPos, mainState.rotate(world, mainPos, CLOCKWISE_180).setValue(TYPE, mainState.getValue(TYPE).getOpposite()));
                            world.setBlockAndUpdate(otherPos, otherState.rotate(world, otherPos, CLOCKWISE_180).setValue(TYPE, otherState.getValue(TYPE).getOpposite()));
                        }
                        player.getCooldowns().addCooldown(this, 5);
                        return ActionResultType.SUCCESS;
                    case TOP:
                    case BOTTOM:
                        if (!world.isClientSide)
                        {
                            world.setBlockAndUpdate(mainPos, mainState.rotate(world, mainPos, CLOCKWISE_90));
                            world.setBlockAndUpdate(otherPos, otherState.rotate(world, otherPos, CLOCKWISE_90));
                        }
                        player.getCooldowns().addCooldown(this, 5);
                        return ActionResultType.SUCCESS;
                }
        }
        return ActionResultType.FAIL;
    }

    @Override
    @SuppressWarnings({"ConstantConditions"})
    protected ActionResultType useModifierOnBlock(final ItemUseContext context, final BlockState state)
    {
        final PlayerEntity player = context.getPlayer();
        final ItemStack stack = context.getItemInHand();
        final World world = context.getLevel();
        final BlockPos mainPos = context.getClickedPos();
        final MutatorMode mode = getMode(stack);
        if (state.getBlock() instanceof ChestBlock)
        {
            if (mode == MutatorMode.MERGE)
            {
                final CompoundNBT tag = stack.getOrCreateTag();
                if (tag.contains("pos"))
                {
                    if (state.getValue(ChestBlock.TYPE) == ChestType.SINGLE)
                    {
                        final BlockPos otherPos = NBTUtil.readBlockPos(tag.getCompound("pos"));
                        final BlockState realOtherState = world.getBlockState(otherPos);
                        if (realOtherState.getBlock() == state.getBlock() && realOtherState.getValue(FACING) == state.getValue(FACING) &&
                                realOtherState.getValue(ChestBlock.TYPE) == ChestType.SINGLE)
                        {
                            final BlockPos vec = otherPos.subtract(mainPos);
                            final int sum = vec.getX() + vec.getY() + vec.getZ();
                            if (sum == 1 || sum == -1)
                            {
                                if (!world.isClientSide)
                                {
                                    final Registries.TierData entry = Registries.MODELED.get(ExpandedStorage.getRl("wood_chest"));
                                    final CursedChestType mainChestType = BaseChestBlock.getChestType(state.getValue(FACING),
                                                                                                      Direction.getNearest(vec.getX(), vec.getY(), vec.getZ()));
                                    BlockState defState = ForgeRegistries.BLOCKS.getValue(entry.getBlockId())
                                            .defaultBlockState().setValue(FACING, state.getValue(FACING))
                                            .setValue(WATERLOGGED, state.getValue(WATERLOGGED)).setValue(TYPE, mainChestType);
                                    TileEntity blockEntity = world.getBlockEntity(mainPos);
                                    NonNullList<ItemStack> invData = NonNullList.withSize(entry.getSlotCount(), ItemStack.EMPTY);
                                    ItemStackHelper.loadAllItems(blockEntity.save(new CompoundNBT()), invData);
                                    world.removeBlockEntity(mainPos);
                                    world.setBlockAndUpdate(mainPos, defState);
                                    blockEntity = world.getBlockEntity(mainPos);
                                    blockEntity.load(defState, ItemStackHelper.saveAllItems(blockEntity.save(new CompoundNBT()), invData));
                                    blockEntity = world.getBlockEntity(otherPos);
                                    invData = NonNullList.withSize(entry.getSlotCount(), ItemStack.EMPTY);
                                    ItemStackHelper.loadAllItems(blockEntity.save(new CompoundNBT()), invData);
                                    world.removeBlockEntity(otherPos);
                                    defState = defState.setValue(WATERLOGGED, state.getValue(WATERLOGGED)).setValue(TYPE, mainChestType.getOpposite());
                                    world.setBlockAndUpdate(otherPos, defState);
                                    blockEntity = world.getBlockEntity(otherPos);
                                    blockEntity.load(defState, ItemStackHelper.saveAllItems(blockEntity.save(new CompoundNBT()), invData));
                                    tag.remove("pos");
                                    player.displayClientMessage(new TranslationTextComponent("tooltip.expandedstorage.chest_mutator.merge_end"), true);
                                    player.getCooldowns().addCooldown(this, 5);
                                }
                                return ActionResultType.SUCCESS;
                            }
                        }
                        return ActionResultType.FAIL;
                    }
                }
                else
                {
                    if (state.getValue(ChestBlock.TYPE) == ChestType.SINGLE)
                    {
                        tag.put("pos", NBTUtil.writeBlockPos(mainPos));
                        player.displayClientMessage(new TranslationTextComponent("tooltip.expandedstorage.chest_mutator.merge_start"), true);
                        player.getCooldowns().addCooldown(this, 5);
                        return ActionResultType.SUCCESS;
                    }
                }
            }
            else if (mode == MutatorMode.UNMERGE)
            {
                final BlockPos otherPos;
                switch (state.getValue(ChestBlock.TYPE))
                {
                    case LEFT: otherPos = mainPos.relative(state.getValue(ChestBlock.FACING).getClockWise());
                        break;
                    case RIGHT: otherPos = mainPos.relative(state.getValue(ChestBlock.FACING).getCounterClockWise());
                        break;
                    default: return ActionResultType.FAIL;
                }
                if (!world.isClientSide)
                {
                    world.setBlockAndUpdate(mainPos, state.setValue(ChestBlock.TYPE, ChestType.SINGLE));
                    world.setBlockAndUpdate(otherPos, world.getBlockState(otherPos).setValue(ChestBlock.TYPE, ChestType.SINGLE));
                }
                player.getCooldowns().addCooldown(this, 5);
                return ActionResultType.SUCCESS;
            }
            else if (mode == MutatorMode.ROTATE)
            {
                final BlockPos otherPos;
                switch (state.getValue(ChestBlock.TYPE))
                {
                    case LEFT: otherPos = mainPos.relative(state.getValue(ChestBlock.FACING).getClockWise());
                        break;
                    case RIGHT: otherPos = mainPos.relative(state.getValue(ChestBlock.FACING).getCounterClockWise());
                        break;
                    case SINGLE:
                        if (!world.isClientSide) { world.setBlockAndUpdate(mainPos, state.rotate(world, mainPos, CLOCKWISE_90)); }
                        player.getCooldowns().addCooldown(this, 5);
                        return ActionResultType.SUCCESS;
                    default: return ActionResultType.FAIL;
                }
                if (!world.isClientSide)
                {
                    final BlockState otherState = world.getBlockState(otherPos);
                    world.setBlockAndUpdate(mainPos, state.rotate(world, mainPos, CLOCKWISE_180).setValue(ChestBlock.TYPE, state.getValue(ChestBlock.TYPE).getOpposite()));
                    world.setBlockAndUpdate(otherPos, otherState.rotate(world, otherPos, CLOCKWISE_180)
                            .setValue(ChestBlock.TYPE, otherState.getValue(ChestBlock.TYPE).getOpposite()));
                }
                player.getCooldowns().addCooldown(this, 5);
                return ActionResultType.SUCCESS;
            }
        }
        else if (state.getBlock() == Blocks.ENDER_CHEST)
        {
            if (mode == MutatorMode.ROTATE)
            {
                if (!world.isClientSide) { world.setBlockAndUpdate(mainPos, state.rotate(world, mainPos, CLOCKWISE_90)); }
                player.getCooldowns().addCooldown(this, 5);
                return ActionResultType.SUCCESS;
            }
            return ActionResultType.FAIL;
        }
        return super.useModifierOnBlock(context, state);
    }

    @Override
    protected ActionResult<ItemStack> useModifierInAir(final World world, final PlayerEntity player, final Hand hand)
    {
        if (player.isCrouching())
        {
            final ItemStack stack = player.getItemInHand(hand);
            final CompoundNBT tag = stack.getOrCreateTag();
            tag.putByte("mode", getMode(stack).next);
            if (tag.contains("pos")) { tag.remove("pos"); }
            if (!world.isClientSide) { player.displayClientMessage(getMode(stack).title, true); }
            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }
        return super.useModifierInAir(world, player, hand);
    }

    @Override
    public void onCraftedBy(final ItemStack stack, final World world, final PlayerEntity player)
    {
        super.onCraftedBy(stack, world, player);
        getMode(stack);
    }

    @Override
    public ItemStack getDefaultInstance()
    {
        final ItemStack stack = super.getDefaultInstance();
        getMode(stack);
        return stack;
    }

    @Override
    public void fillItemCategory(final ItemGroup itemGroup, final NonNullList<ItemStack> stackList)
    {
        if (allowdedIn(itemGroup)) { stackList.add(getDefaultInstance()); }
    }

    private MutatorMode getMode(final ItemStack stack)
    {
        final CompoundNBT tag = stack.getOrCreateTag();
        if (!tag.contains("mode", 1)) { tag.putByte("mode", (byte) 0); }
        return MutatorMode.values()[tag.getByte("mode")];
    }

    @Override
    public void appendHoverText(final ItemStack stack, @Nullable final World world, final List<ITextComponent> tooltip,
                                final ITooltipFlag flag)
    {
        final MutatorMode mode = getMode(stack);
        tooltip.add(new TranslationTextComponent("tooltip.expandedstorage.tool_mode", mode.title).withStyle(TextFormatting.GRAY));
        tooltip.add(mode.description);
        super.appendHoverText(stack, world, tooltip, flag);
    }
}