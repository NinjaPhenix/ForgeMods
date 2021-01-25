package ninjaphenix.expandedstorage.common.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.world.IBlockReader;
import ninjaphenix.expandedstorage.ModContent;
import ninjaphenix.expandedstorage.Registries;
import ninjaphenix.expandedstorage.common.block.entity.CursedChestBlockEntity;
import ninjaphenix.expandedstorage.common.block.enums.CursedChestType;

import javax.annotation.Nullable;

import static net.minecraft.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public final class CursedChestBlock extends ChestBlock<CursedChestBlockEntity>
{
    private static final VoxelShape SINGLE_SHAPE = box(1, 0, 1, 15, 14, 15);
    private static final VoxelShape TOP_SHAPE = box(1, 0, 1, 15, 14, 15);
    private static final VoxelShape BOTTOM_SHAPE = box(1, 0, 1, 15, 16, 15);
    private static final VoxelShape[] HORIZONTAL_VALUES = {
            box(1, 0, 0, 15, 14, 15),
            box(1, 0, 1, 16, 14, 15),
            box(1, 0, 1, 15, 14, 16),
            box(0, 0, 1, 15, 14, 15)
    };

    public CursedChestBlock(final Properties settings)
    {
        super(settings, () -> ModContent.CURSED_CHEST_TE);
        registerDefaultState(defaultBlockState().setValue(HORIZONTAL_FACING, Direction.SOUTH));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(final BlockState state, final IBlockReader world)
    {
        return new CursedChestBlockEntity(getRegistryName());
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(final BlockState state, final IBlockReader view, final BlockPos pos, final ISelectionContext context)
    {
        final CursedChestType type = state.getValue(TYPE);
        if (type == CursedChestType.TOP) { return TOP_SHAPE; }
        else if (type == CursedChestType.BOTTOM) { return BOTTOM_SHAPE; }
        else if (type == CursedChestType.SINGLE) {return SINGLE_SHAPE; }
        else { return HORIZONTAL_VALUES[(state.getValue(HORIZONTAL_FACING).get2DDataValue() + type.getOffset()) % 4]; }
    }

    @Override
    public BlockRenderType getRenderShape(final BlockState state) { return BlockRenderType.ENTITYBLOCK_ANIMATED; }

    @Override
    @SuppressWarnings({"unchecked"})
    public MutableRegistry<Registries.ModeledTierData> getDataRegistry() { return Registries.MODELED; }
}