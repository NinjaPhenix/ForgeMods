package ninjaphenix.expandedstorage.common.item;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import ninjaphenix.expandedstorage.ExpandedStorage;
import ninjaphenix.expandedstorage.Registries;
import ninjaphenix.expandedstorage.common.block.BaseChestBlock;
import ninjaphenix.expandedstorage.common.block.entity.AbstractChestTileEntity;
import ninjaphenix.expandedstorage.common.block.enums.CursedChestType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings({ "OptionalGetWithoutIsPresent", "ConstantConditions" })
public class ChestConversionItem extends ChestModifierItem
{
    private final ITextComponent TOOLTIP;
    private final ResourceLocation FROM, TO;

    public ChestConversionItem(@NotNull final Pair<ResourceLocation, String> from, @NotNull final Pair<ResourceLocation, String> to)
    {
        super(new Item.Properties().group(ExpandedStorage.group).maxStackSize(16));
        setRegistryName(ExpandedStorage.getRl(from.getSecond() + "_to_" + to.getSecond() + "_conversion_kit"));
        FROM = from.getFirst();
        TO = to.getFirst();
        TOOLTIP = new TranslationTextComponent(String.format("tooltip.expandedstorage.conversion_kit_%s_%s", from.getSecond(), to.getSecond()),
                ExpandedStorage.leftShiftRightClick,
                new TranslationTextComponent("tooltip.expandedstorage.conversion_kit_double_requires_2")).mergeStyle(TextFormatting.GRAY);
    }

    @SuppressWarnings("unchecked")
    private void upgradeCursedChest(@NotNull final World world, @NotNull final BlockPos pos, @NotNull final BlockState state)
    {
        AbstractChestTileEntity tileEntity = (AbstractChestTileEntity) world.getTileEntity(pos);
        final SimpleRegistry<Registries.TierData> registry = ((BaseChestBlock<AbstractChestTileEntity>) state.getBlock()).getDataRegistry();
        final NonNullList<ItemStack> inventoryData = NonNullList.withSize(registry.getValue(TO).get().getSlotCount(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(tileEntity.write(new CompoundNBT()), inventoryData);
        world.removeTileEntity(pos);
        BlockState newState = ForgeRegistries.BLOCKS.getValue(registry.getValue(TO).get().getBlockId()).getDefaultState();
        if (newState.getBlock() instanceof IWaterLoggable)
        { newState = newState.with(BlockStateProperties.WATERLOGGED, state.get(BlockStateProperties.WATERLOGGED)); }
        newState = newState.with(BlockStateProperties.HORIZONTAL_FACING, state.get(BlockStateProperties.HORIZONTAL_FACING))
                           .with(BaseChestBlock.TYPE, state.get(BaseChestBlock.TYPE));
        world.setBlockState(pos, newState);
        tileEntity = (AbstractChestTileEntity) world.getTileEntity(pos);
        tileEntity.read(newState, ItemStackHelper.saveAllItems(tileEntity.write(new CompoundNBT()), inventoryData));
    }

    private void upgradeChest(@NotNull final World world, @NotNull final BlockPos pos, @NotNull final BlockState state)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        final NonNullList<ItemStack> inventoryData = NonNullList.withSize(Registries.MODELED.getValue(FROM).get().getSlotCount(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(tileEntity.write(new CompoundNBT()), inventoryData);
        world.removeTileEntity(pos);
        final BlockState newState = ForgeRegistries.BLOCKS.getValue(Registries.MODELED.getValue(TO).get().getBlockId()).getDefaultState()
                                                          .with(BlockStateProperties.HORIZONTAL_FACING, state.get(BlockStateProperties.HORIZONTAL_FACING))
                                                          .with(BlockStateProperties.WATERLOGGED, state.get(BlockStateProperties.WATERLOGGED))
                                                          .with(BaseChestBlock.TYPE, CursedChestType.valueOf(state.get(BlockStateProperties.CHEST_TYPE)));
        world.setBlockState(pos, newState);
        tileEntity = world.getTileEntity(pos);
        tileEntity.read(newState, ItemStackHelper.saveAllItems(tileEntity.write(new CompoundNBT()), inventoryData));
    }

    @NotNull @Override @SuppressWarnings("unchecked")
    protected ActionResultType useModifierOnChestBlock(@NotNull final ItemUseContext context, @NotNull final BlockState mainState,
            @NotNull final BlockPos mainPos, @Nullable final BlockState otherState, @Nullable final BlockPos otherPos)
    {
        final World world = context.getWorld();
        final PlayerEntity player = context.getPlayer();
        final BaseChestBlock<AbstractChestTileEntity> chestBlock = (BaseChestBlock<AbstractChestTileEntity>) mainState.getBlock();
        if (!chestBlock.getRegistryName().equals(chestBlock.getDataRegistry().getValue(FROM).get().getBlockId())) { return ActionResultType.FAIL; }
        final ItemStack handStack = player.getHeldItem(context.getHand());
        if (otherPos == null)
        {
            if (!world.isRemote)
            {
                upgradeCursedChest(world, mainPos, mainState);
                handStack.shrink(1);
            }
            return ActionResultType.SUCCESS;
        }
        else if (handStack.getCount() > 1 || player.isCreative())
        {
            if (!world.isRemote)
            {
                upgradeCursedChest(world, otherPos, world.getBlockState(otherPos));
                upgradeCursedChest(world, mainPos, mainState);
                handStack.shrink(2);
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.FAIL;
    }

    @NotNull @Override
    protected ActionResultType useModifierOnBlock(@NotNull final ItemUseContext context, @NotNull final BlockState state)
    {
        if (state.getBlock() == Blocks.CHEST && FROM.equals(ExpandedStorage.getRl("wood_chest")))
        {
            final World world = context.getWorld();
            final BlockPos mainPos = context.getPos();
            final PlayerEntity player = context.getPlayer();
            final ItemStack handStack = player.getHeldItem(context.getHand());
            if (state.get(BlockStateProperties.CHEST_TYPE) == ChestType.SINGLE)
            {
                if (!world.isRemote)
                {
                    upgradeChest(world, mainPos, state);
                    handStack.shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
            else if (handStack.getCount() > 1 || player.isCreative())
            {
                BlockPos otherPos;
                if (state.get(BlockStateProperties.CHEST_TYPE) == ChestType.RIGHT)
                { otherPos = mainPos.offset(state.get(BlockStateProperties.HORIZONTAL_FACING).rotateYCCW()); }
                else if (state.get(BlockStateProperties.CHEST_TYPE) == ChestType.LEFT)
                { otherPos = mainPos.offset(state.get(BlockStateProperties.HORIZONTAL_FACING).rotateY()); }
                else { return ActionResultType.FAIL; }
                if (!world.isRemote)
                {
                    upgradeChest(world, otherPos, world.getBlockState(otherPos));
                    upgradeChest(world, mainPos, state);
                    handStack.shrink(2);
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.FAIL;
    }

    @Override
    public void addInformation(@NotNull final ItemStack stack, @Nullable final World world, @NotNull final List<ITextComponent> tooltip,
            @NotNull final ITooltipFlag flag)
    {
        super.addInformation(stack, world, tooltip, flag);
        tooltip.add(TOOLTIP);
    }
}