package ninjaphenix.refinement.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public abstract class UpgradableBlock extends Block
{
    protected UpgradableBlock(Properties properties) { super(properties); }

    @Override
    public final boolean hasTileEntity(final BlockState state) { return true; }

    @Override
    public abstract TileEntity createTileEntity(final BlockState state, final IBlockReader world);
}