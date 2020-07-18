package ninjaphenix.refinement_test.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import ninjaphenix.refinement.common.block.UpgradableBlock;
import ninjaphenix.refinement_test.common.block.entity.TestUpgradableTileEntity;

public final class TestUpgradableBlock extends UpgradableBlock
{
    public TestUpgradableBlock(final Properties properties) { super(properties); }

    @Override
    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) { return new TestUpgradableTileEntity(); }
}