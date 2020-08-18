//package ninjaphenix.expandedstorage.common.item;
//
//import com.mojang.datafixers.util.Pair;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.Blocks;
//import net.minecraft.block.IWaterLoggable;
//import net.minecraft.client.util.ITooltipFlag;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.inventory.ItemStackHelper;
//import net.minecraft.item.ItemStack;
//import net.minecraft.item.ItemUseContext;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraft.state.properties.BlockStateProperties;
//import net.minecraft.state.properties.ChestType;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.ActionResultType;
//import net.minecraft.util.NonNullList;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.registry.SimpleRegistry;
//import net.minecraft.util.text.ITextComponent;
//import net.minecraft.util.text.TextComponentTranslation;
//import net.minecraft.util.text.TextFormatting;
//import net.minecraft.util.text.TranslationTextComponent;
//import net.minecraft.world.World;
//import net.minecraftforge.registries.ForgeRegistries;
//import ninjaphenix.expandedstorage.ExpandedStorage;
//import ninjaphenix.expandedstorage.Registries;
//import ninjaphenix.expandedstorage.common.block.OLD_BaseChestBlock;
//import ninjaphenix.expandedstorage.common.block.entity.OLD_AbstractChestTileEntity;
//import ninjaphenix.expandedstorage.common.block.enums.CursedChestType;
//import org.apache.commons.lang3.tuple.Pair;
//
//import javax.annotation.Nullable;
//import java.util.List;
//
//@SuppressWarnings("ConstantConditions")
//public final class OLD_ChestConversionItem extends OLD_ChestModifierItem
//{
//    private final ITextComponent TOOLTIP;
//    private final ResourceLocation FROM, TO;
//
//    public OLD_ChestConversionItem(final Pair<ResourceLocation, String> from, final Pair<ResourceLocation, String> to)
//    {
//        super();
//        setRegistryName(ExpandedStorage.getRl(from.getRight() + "_to_" + to.getRight() + "_conversion_kit"));
//        FROM = from.getLeft();
//        TO = to.getLeft();
//        final TextComponentTranslation doubleRequiresTwo = new TextComponentTranslation("tooltip.expandedstorage.conversion_kit_double_requires_2");
//        doubleRequiresTwo.getStyle().setColor(TextFormatting.GRAY);
//        TOOLTIP = new TextComponentTranslation(String.format("tooltip.expandedstorage.conversion_kit_%s_%s", from.getRight(), to.getRight()),
//                ExpandedStorage.leftShiftRightClick,
//                doubleRequiresTwo);
//    }
//
//    @SuppressWarnings("unchecked")
//    private void upgradeCursedChest(final World world, final BlockPos pos, final BlockState state)
//    {
//        OLD_AbstractChestTileEntity tileEntity = (OLD_AbstractChestTileEntity) world.getTileEntity(pos);
//        final SimpleRegistry<Registries.TierData> registry = ((OLD_BaseChestBlock<OLD_AbstractChestTileEntity>) state.getBlock()).getDataRegistry();
//        final NonNullList<ItemStack> inventoryData = NonNullList.withSize(registry.getOrDefault(TO).getSlotCount(), ItemStack.EMPTY);
//        ItemStackHelper.loadAllItems(tileEntity.write(new CompoundNBT()), inventoryData);
//        world.removeTileEntity(pos);
//        BlockState newState = ForgeRegistries.BLOCKS.getValue(registry.getOrDefault(TO).getBlockId()).getDefaultState();
//        if (newState.getBlock() instanceof IWaterLoggable)
//        { newState = newState.with(BlockStateProperties.WATERLOGGED, state.get(BlockStateProperties.WATERLOGGED)); }
//        newState = newState.with(BlockStateProperties.HORIZONTAL_FACING, state.get(BlockStateProperties.HORIZONTAL_FACING))
//                           .with(OLD_BaseChestBlock.TYPE, state.get(OLD_BaseChestBlock.TYPE));
//        world.setBlockState(pos, newState);
//        tileEntity = (OLD_AbstractChestTileEntity) world.getTileEntity(pos);
//        tileEntity.read(newState, ItemStackHelper.saveAllItems(tileEntity.write(new CompoundNBT()), inventoryData));
//    }
//
//    private void upgradeChest(final World world, final BlockPos pos, final BlockState state)
//    {
//        TileEntity tileEntity = world.getTileEntity(pos);
//        final NonNullList<ItemStack> inventoryData = NonNullList.withSize(Registries.MODELED.getOrDefault(FROM).getSlotCount(), ItemStack.EMPTY);
//        ItemStackHelper.loadAllItems(tileEntity.write(new CompoundNBT()), inventoryData);
//        world.removeTileEntity(pos);
//        final BlockState newState = ForgeRegistries.BLOCKS.getValue(Registries.MODELED.getOrDefault(TO).getBlockId()).getDefaultState()
//                                                          .with(BlockStateProperties.HORIZONTAL_FACING, state.get(BlockStateProperties.HORIZONTAL_FACING))
//                                                          .with(BlockStateProperties.WATERLOGGED, state.get(BlockStateProperties.WATERLOGGED))
//                                                          .with(OLD_BaseChestBlock.TYPE, CursedChestType.valueOf(state.get(BlockStateProperties.CHEST_TYPE)));
//        world.setBlockState(pos, newState);
//        tileEntity = world.getTileEntity(pos);
//        tileEntity.read(newState, ItemStackHelper.saveAllItems(tileEntity.write(new CompoundNBT()), inventoryData));
//    }
//
//    @Override @SuppressWarnings("unchecked")
//    protected ActionResultType useModifierOnChestBlock(final ItemUseContext context, final BlockState mainState,
//            final BlockPos mainPos, @Nullable final BlockState otherState, @Nullable final BlockPos otherPos)
//    {
//        final World world = context.getWorld();
//        final PlayerEntity player = context.getPlayer();
//        final OLD_BaseChestBlock<OLD_AbstractChestTileEntity> chestBlock = (OLD_BaseChestBlock<OLD_AbstractChestTileEntity>) mainState.getBlock();
//        if (!chestBlock.getRegistryName().equals(chestBlock.getDataRegistry().getOrDefault(FROM).getBlockId())) { return ActionResultType.FAIL; }
//        final ItemStack handStack = player.getHeldItem(context.getHand());
//        if (otherPos == null)
//        {
//            if (!world.isRemote)
//            {
//                upgradeCursedChest(world, mainPos, mainState);
//                handStack.shrink(1);
//            }
//            return ActionResultType.SUCCESS;
//        }
//        else if (handStack.getCount() > 1 || player.isCreative())
//        {
//            if (!world.isRemote)
//            {
//                upgradeCursedChest(world, otherPos, world.getBlockState(otherPos));
//                upgradeCursedChest(world, mainPos, mainState);
//                handStack.shrink(2);
//            }
//            return ActionResultType.SUCCESS;
//        }
//        return ActionResultType.FAIL;
//    }
//
//    @Override
//    protected ActionResultType useModifierOnBlock(final ItemUseContext context, final BlockState state)
//    {
//        if (state.getBlock() == Blocks.CHEST && FROM.equals(ExpandedStorage.getRl("wood_chest")))
//        {
//            final World world = context.getWorld();
//            final BlockPos mainPos = context.getPos();
//            final PlayerEntity player = context.getPlayer();
//            final ItemStack handStack = player.getHeldItem(context.getHand());
//            if (state.get(BlockStateProperties.CHEST_TYPE) == ChestType.SINGLE)
//            {
//                if (!world.isRemote)
//                {
//                    upgradeChest(world, mainPos, state);
//                    handStack.shrink(1);
//                }
//                return ActionResultType.SUCCESS;
//            }
//            else if (handStack.getCount() > 1 || player.isCreative())
//            {
//                final BlockPos otherPos;
//                if (state.get(BlockStateProperties.CHEST_TYPE) == ChestType.RIGHT)
//                { otherPos = mainPos.offset(state.get(BlockStateProperties.HORIZONTAL_FACING).rotateYCCW()); }
//                else if (state.get(BlockStateProperties.CHEST_TYPE) == ChestType.LEFT)
//                { otherPos = mainPos.offset(state.get(BlockStateProperties.HORIZONTAL_FACING).rotateY()); }
//                else { return ActionResultType.FAIL; }
//                if (!world.isRemote)
//                {
//                    upgradeChest(world, otherPos, world.getBlockState(otherPos));
//                    upgradeChest(world, mainPos, state);
//                    handStack.shrink(2);
//                }
//                return ActionResultType.SUCCESS;
//            }
//        }
//        return ActionResultType.FAIL;
//    }
//
//    @Override
//    public void addInformation(final ItemStack stack, final @Nullable World worldIn, final List<String> tooltip, final ITooltipFlag flagIn)
//    {
//        super.addInformation(stack, worldIn, tooltip, flagIn);
//        tooltip.add(TOOLTIP.getFormattedText());
//    }
//}