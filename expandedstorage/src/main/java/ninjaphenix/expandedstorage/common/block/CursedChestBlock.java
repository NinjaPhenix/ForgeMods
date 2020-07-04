package ninjaphenix.expandedstorage.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class CursedChestBlock extends BaseChestBlock<CursedChestTileEntity> implements IWaterLoggable
{
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape SINGLE_SHAPE = Block.makeCuboidShape(1, 0, 1, 15, 14, 15);
    private static final VoxelShape TOP_SHAPE = Block.makeCuboidShape(1, 0, 1, 15, 14, 15);
    private static final VoxelShape BOTTOM_SHAPE = Block.makeCuboidShape(1, 0, 1, 15, 16, 15);
    private static final VoxelShape[] HORIZONTAL_VALUES = {
            Block.makeCuboidShape(1, 0, 0, 15, 14, 15),
            Block.makeCuboidShape(1, 0, 1, 16, 14, 15),
            Block.makeCuboidShape(1, 0, 1, 15, 14, 16),
            Block.makeCuboidShape(0, 0, 1, 15, 14, 15)
    };

    public CursedChestBlock(@NotNull final Properties properties, @NotNull final ResourceLocation registryName)
    {
        super(properties, () -> ModContent.CURSED_CHEST_TE);
        setDefaultState(getDefaultState().with(WATERLOGGED, false));
        setRegistryName(registryName);
    }

    @NotNull @Override
    public TileEntity createTileEntity(@Nullable final BlockState state, @Nullable final IBlockReader world)
    { return new CursedChestTileEntity(getRegistryName()); }

    @NotNull @Override
    public IFluidState getFluidState(@NotNull final BlockState state)
    { return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state); }

    @Override
    protected void fillStateContainer(@NotNull final StateContainer.Builder<Block, BlockState> builder)
    {
        super.fillStateContainer(builder);
        builder.add(WATERLOGGED);
    }

    @NotNull @Override
    public VoxelShape getShape(@NotNull final BlockState state,
            @NotNull final IBlockReader reader, @NotNull final BlockPos pos, @NotNull final ISelectionContext context)
    {
        final CursedChestType type = state.get(TYPE);
        if (type == CursedChestType.TOP) { return TOP_SHAPE; }
        else if (type == CursedChestType.BOTTOM) { return BOTTOM_SHAPE; }
        else if (type == CursedChestType.SINGLE) {return SINGLE_SHAPE; }
        else
        {
            final int offset = (state.get(FACING).getHorizontalIndex() + type.getOffset()) % 4;
            return HORIZONTAL_VALUES[offset];
        }
    }

    @NotNull @Override
    public BlockState getStateForPlacement(@NotNull final BlockItemUseContext context)
    {
        return super.getStateForPlacement(context).with(WATERLOGGED, context.getWorld().getFluidState(context.getPos()).getFluid() == Fluids.WATER);
    }

    @NotNull @Override
    public BlockState updatePostPlacement(@NotNull final BlockState state, @NotNull final Direction offset,
            @NotNull final BlockState offsetState, @NotNull final IWorld world, @NotNull final BlockPos pos, @NotNull final BlockPos offsetPos)
    {
        if (state.get(WATERLOGGED)) { world.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world)); }
        return super.updatePostPlacement(state, offset, offsetState, world, pos, offsetPos);
    }

    @NotNull @Override
    public BlockRenderType getRenderType(@NotNull final BlockState state) { return BlockRenderType.ENTITYBLOCK_ANIMATED; }

    @NotNull @Override @SuppressWarnings("unchecked")
    public SimpleRegistry<Registries.ModeledTierData> getDataRegistry() { return Registries.MODELED; }
}