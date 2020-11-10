package ninjaphenix.expandedstorage.common.item;

import com.mojang.datafixers.util.Pair;
import java.util.List;
import javax.annotation.Nullable;
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

@SuppressWarnings("ConstantConditions")
public final class ChestConversionItem extends ChestModifierItem
{
    private final ITextComponent TOOLTIP;
    private final ResourceLocation FROM, TO;
    private final ITextComponent DOUBLE_REQUIRES_2 =
            new TranslationTextComponent("tooltip.expandedstorage.conversion_kit_double_requires_2").withStyle(TextFormatting.GRAY);

    public ChestConversionItem(final Pair<ResourceLocation, String> from, final Pair<ResourceLocation, String> to)
    {
        super(new Item.Properties().tab(ExpandedStorage.group).stacksTo(16));
        setRegistryName(ExpandedStorage.getRl(from.getSecond() + "_to_" + to.getSecond() + "_conversion_kit"));
        FROM = from.getFirst();
        TO = to.getFirst();
        TOOLTIP = new TranslationTextComponent(
                String.format("tooltip.expandedstorage.conversion_kit_%s_%s", from.getSecond(), to.getSecond()),
                ExpandedStorage.leftShiftRightClick).withStyle(TextFormatting.GRAY);
    }

    @SuppressWarnings("unchecked")
    private void upgradeCursedChest(final World world, final BlockPos pos, final BlockState state)
    {
        AbstractChestTileEntity tileEntity = (AbstractChestTileEntity) world.getBlockEntity(pos);
        final SimpleRegistry<Registries.TierData> registry = ((BaseChestBlock<AbstractChestTileEntity>) state.getBlock()).getDataRegistry();
        final NonNullList<ItemStack> inventoryData = NonNullList.withSize(registry.get(TO).getSlotCount(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(tileEntity.save(new CompoundNBT()), inventoryData);
        world.removeBlockEntity(pos);
        BlockState newState = ForgeRegistries.BLOCKS.getValue(registry.get(TO).getBlockId()).defaultBlockState();
        if (newState.getBlock() instanceof IWaterLoggable)
        {
            newState = newState.setValue(BlockStateProperties.WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED));
        }
        newState = newState.setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING))
                .setValue(BaseChestBlock.TYPE, state.getValue(BaseChestBlock.TYPE));
        world.setBlockAndUpdate(pos, newState);
        tileEntity = (AbstractChestTileEntity) world.getBlockEntity(pos);
        tileEntity.load(newState, ItemStackHelper.saveAllItems(tileEntity.save(new CompoundNBT()), inventoryData));
    }

    private void upgradeChest(final World world, final BlockPos pos, final BlockState state)
    {
        TileEntity tileEntity = world.getBlockEntity(pos);
        final NonNullList<ItemStack> inventoryData = NonNullList.withSize(Registries.MODELED.get(FROM).getSlotCount(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(tileEntity.save(new CompoundNBT()), inventoryData);
        world.removeBlockEntity(pos);
        final BlockState newState = ForgeRegistries.BLOCKS.getValue(Registries.MODELED.get(TO).getBlockId()).defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING))
                .setValue(BlockStateProperties.WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED))
                .setValue(BaseChestBlock.TYPE, CursedChestType.valueOf(state.getValue(BlockStateProperties.CHEST_TYPE)));
        world.setBlockAndUpdate(pos, newState);
        tileEntity = world.getBlockEntity(pos);
        tileEntity.load(newState, ItemStackHelper.saveAllItems(tileEntity.save(new CompoundNBT()), inventoryData));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ActionResultType useModifierOnChestBlock(final ItemUseContext context, final BlockState mainState,
                                                       final BlockPos mainPos, @Nullable final BlockState otherState, @Nullable final BlockPos otherPos)
    {
        final World world = context.getLevel();
        final PlayerEntity player = context.getPlayer();
        final BaseChestBlock<AbstractChestTileEntity> chestBlock = (BaseChestBlock<AbstractChestTileEntity>) mainState.getBlock();
        if (!chestBlock.getRegistryName().equals(chestBlock.getDataRegistry().get(FROM).getBlockId()))
        {
            return ActionResultType.FAIL;
        }
        final ItemStack handStack = player.getItemInHand(context.getHand());
        if (otherPos == null)
        {
            if (!world.isClientSide)
            {
                upgradeCursedChest(world, mainPos, mainState);
                handStack.shrink(1);
            }
            return ActionResultType.SUCCESS;
        }
        else if (handStack.getCount() > 1 || player.isCreative())
        {
            if (!world.isClientSide)
            {
                upgradeCursedChest(world, otherPos, world.getBlockState(otherPos));
                upgradeCursedChest(world, mainPos, mainState);
                handStack.shrink(2);
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.FAIL;
    }

    @Override
    protected ActionResultType useModifierOnBlock(final ItemUseContext context, final BlockState state)
    {
        if (state.getBlock() == Blocks.CHEST && FROM.equals(ExpandedStorage.getRl("wood_chest")))
        {
            final World world = context.getLevel();
            final BlockPos mainPos = context.getClickedPos();
            final PlayerEntity player = context.getPlayer();
            final ItemStack handStack = player.getItemInHand(context.getHand());
            if (state.getValue(BlockStateProperties.CHEST_TYPE) == ChestType.SINGLE)
            {
                if (!world.isClientSide)
                {
                    upgradeChest(world, mainPos, state);
                    handStack.shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
            else if (handStack.getCount() > 1 || player.isCreative())
            {
                final BlockPos otherPos;
                if (state.getValue(BlockStateProperties.CHEST_TYPE) == ChestType.RIGHT)
                {
                    otherPos = mainPos.relative(state.getValue(BlockStateProperties.HORIZONTAL_FACING).getCounterClockWise());
                }
                else if (state.getValue(BlockStateProperties.CHEST_TYPE) == ChestType.LEFT)
                {
                    otherPos = mainPos.relative(state.getValue(BlockStateProperties.HORIZONTAL_FACING).getClockWise());
                }
                else { return ActionResultType.FAIL; }
                if (!world.isClientSide)
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
    public void appendHoverText(final ItemStack stack, @Nullable final World world, final List<ITextComponent> tooltip,
                                final ITooltipFlag flag)
    {
        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.add(TOOLTIP);
        tooltip.add(DOUBLE_REQUIRES_2);
    }
}