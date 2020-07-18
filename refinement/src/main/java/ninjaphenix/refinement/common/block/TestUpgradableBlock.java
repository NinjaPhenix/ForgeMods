package ninjaphenix.refinement.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import ninjaphenix.refinement.common.block.entity.TestUpgradableTileEntity;
import org.jetbrains.annotations.Nullable;

public final class TestUpgradableBlock extends UpgradableBlock
{
    public TestUpgradableBlock(final Properties properties) { super(properties); }

    @Nullable @Override
    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) { return new TestUpgradableTileEntity(); }
}