package ninjaphenix.expandedstorage.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import ninjaphenix.expandedstorage.ModContent;
import ninjaphenix.expandedstorage.Registries;
import ninjaphenix.expandedstorage.common.block.entity.CursedChestTileEntity;
import ninjaphenix.expandedstorage.common.block.enums.CursedChestType;

import javax.annotation.Nullable;

import static net.minecraft.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public final class CursedChestBlock extends BaseChestBlock<CursedChestTileEntity> implements IWaterLoggable
{
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape SINGLE_SHAPE = Block.box(1, 0, 1, 15, 14, 15);
    private static final VoxelShape TOP_SHAPE = Block.box(1, 0, 1, 15, 14, 15);
    private static final VoxelShape BOTTOM_SHAPE = Block.box(1, 0, 1, 15, 16, 15);
    private static final VoxelShape[] HORIZONTAL_VALUES = {
            Block.box(1, 0, 0, 15, 14, 15),
            Block.box(1, 0, 1, 16, 14, 15),
            Block.box(1, 0, 1, 15, 14, 16),
            Block.box(0, 0, 1, 15, 14, 15)
    };

    public CursedChestBlock(final Properties properties, final ResourceLocation registryName)
    {
        super(properties, () -> ModContent.CURSED_CHEST_TE);
        registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
        setRegistryName(registryName);
    }

    @Override
    public TileEntity createTileEntity(@Nullable final BlockState state, @Nullable final IBlockReader world)
    {
        return new CursedChestTileEntity(getRegistryName());
    }

    @Override
    @SuppressWarnings("deprecation")
    public FluidState getFluidState(final BlockState state)
    {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(final BlockState state,
                               final IBlockReader reader, final BlockPos pos, final ISelectionContext context)
    {
        final CursedChestType type = state.getValue(TYPE);
        if (type == CursedChestType.TOP) { return TOP_SHAPE; }
        else if (type == CursedChestType.BOTTOM) { return BOTTOM_SHAPE; }
        else if (type == CursedChestType.SINGLE) {return SINGLE_SHAPE; }
        else { return HORIZONTAL_VALUES[(state.getValue(HORIZONTAL_FACING).get2DDataValue() + type.getOffset()) % 4]; }
    }

    @Override
    public BlockState getStateForPlacement(final BlockItemUseContext context)
    {
        return super.getStateForPlacement(context).setValue(
                WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
    }

    @Override
    public BlockState updateShape(final BlockState state, final Direction offset, final BlockState offsetState, final IWorld world,
                                  final BlockPos pos, final BlockPos offsetPos)
    {
        if (state.getValue(WATERLOGGED)) { world.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world)); }
        return super.updateShape(state, offset, offsetState, world, pos, offsetPos);
    }

    @Override
    public BlockRenderType getRenderShape(final BlockState state) { return BlockRenderType.ENTITYBLOCK_ANIMATED; }

    @Override
    @SuppressWarnings("unchecked")
    public SimpleRegistry<Registries.ModeledTierData> getDataRegistry() { return Registries.MODELED; }
}