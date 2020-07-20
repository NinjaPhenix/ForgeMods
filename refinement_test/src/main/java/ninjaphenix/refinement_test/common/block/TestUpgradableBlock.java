package ninjaphenix.refinement_test.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import ninjaphenix.refinement.api.common.block.UpgradableBlock;
import ninjaphenix.refinement_test.common.block.entity.TestUpgradableTileEntity;

public final class TestUpgradableBlock extends UpgradableBlock
{
    public TestUpgradableBlock(final Properties properties)
    {
        super(properties);
    }

    @Override
    public TileEntity createTileEntity(final BlockState state, final IBlockReader world)
    {
        return new TestUpgradableTileEntity();
    }

    @Override @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand,
            final BlockRayTraceResult hit)
    {
        final ActionResultType superResult = super.onBlockActivated(state, world, pos, player, hand, hit);
        if (superResult != ActionResultType.PASS) { return superResult; }
        // todo: open test container ( container with up to 3 slots depending on level )
        return ActionResultType.FAIL;
    }
}