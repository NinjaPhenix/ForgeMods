package torcherino.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import torcherino.api.TierSupplier;
import torcherino.block.tile.TorcherinoTileEntity;

import static net.minecraft.state.properties.BlockStateProperties.POWERED;

public final class TorcherinoWallBlock extends WallTorchBlock implements TierSupplier
{
    private final ResourceLocation tierName;

    public TorcherinoWallBlock(final TorcherinoBlock base, final IParticleData flameParticle)
    {
        super(Block.Properties.from(Blocks.WALL_TORCH).lootFrom(base), flameParticle);
        tierName = base.getTierName();
    }

    @Override
    public ResourceLocation getTierName()
    {
        return tierName;
    }

    @Override
    public boolean hasTileEntity(final BlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(final BlockState state, final IBlockReader world)
    {
        return new TorcherinoTileEntity();
    }

    @Override
    protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder)
    {
        super.fillStateContainer(builder);
        builder.add(POWERED);
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult hit)
    {
        return TorcherinoLogic.onBlockActivated(state, world, pos, player, hand, hit);
    }

    @Override
    public void onBlockPlacedBy(final World world, final BlockPos pos, final BlockState state, final @Nullable LivingEntity placer, final ItemStack stack)
    {
        TorcherinoLogic.onBlockPlacedBy(world, pos, state, placer, stack);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(final BlockState state, final ServerWorld world, final BlockPos pos, final Random random)
    {
        TorcherinoLogic.tick(state, world, pos, random);
    }

    @Override
    @SuppressWarnings("deprecation")
    public PushReaction getPushReaction(final BlockState state)
    {
        return PushReaction.IGNORE;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onBlockAdded(final BlockState state, final World world, final BlockPos pos, final BlockState oldState, final boolean isMoving)
    {
        TorcherinoLogic.onBlockAdded(state, world, pos, oldState, isMoving);
    }

    @Override
    public BlockState getStateForPlacement(final BlockItemUseContext context)
    {
        final BlockState state = super.getStateForPlacement(context);
        if (state == null)
        {
            return null;
        }
        return state.with(POWERED, context.getWorld().isSidePowered(context.getPos().offset(state.get(HORIZONTAL_FACING).getOpposite()), state.get(HORIZONTAL_FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(final BlockState state, final World world, final BlockPos pos, final Block block, final BlockPos fromPos, final boolean isMoving)
    {
        TorcherinoLogic.neighborChanged(state, world, pos, block, fromPos, isMoving, () -> world.isSidePowered(pos.offset(state.get(HORIZONTAL_FACING).getOpposite()), state.get(HORIZONTAL_FACING)));
    }
}