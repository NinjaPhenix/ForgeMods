package torcherino.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
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
import org.apache.commons.lang3.StringUtils;
import torcherino.Torcherino;
import torcherino.api.TierSupplier;
import torcherino.block.tile.TorcherinoTileEntity;
import torcherino.config.Config;
import torcherino.network.Networker;

import javax.annotation.Nullable;
import java.util.Random;

import static net.minecraft.state.properties.BlockStateProperties.HORIZONTAL_FACING;
import static net.minecraft.state.properties.BlockStateProperties.POWERED;

public class JackoLanterinoBlock extends HorizontalBlock implements TierSupplier
{
    private final ResourceLocation tierName;

    public JackoLanterinoBlock(final ResourceLocation tierName)
    {
        super(Block.Properties.copy(Blocks.JACK_O_LANTERN).isValidSpawn((state, world, pos, entity) -> true));
        this.tierName = tierName;
    }

    @Override
    public ResourceLocation getTierName() { return tierName; }

    @Override
    public boolean hasTileEntity(final BlockState state) { return true; }

    @Override
    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) { return new TorcherinoTileEntity(); }

    @Override
    protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(HORIZONTAL_FACING, POWERED);
    }

    @Override @SuppressWarnings("deprecation")
    public ActionResultType use(final BlockState state, final World world, final BlockPos pos,
            final PlayerEntity player, final Hand hand, final BlockRayTraceResult hit)
    {
        if (!world.isClientSide) { Networker.INSTANCE.openScreenServer(world, (ServerPlayerEntity) player, pos); }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void setPlacedBy(final World world, final BlockPos pos, final BlockState state, @Nullable final LivingEntity placer, final ItemStack stack)
    {
        if (world.isClientSide) { return; }
        if (stack.hasCustomHoverName())
        {
            final TileEntity tile = world.getBlockEntity(pos);
            if (tile instanceof TorcherinoTileEntity) { ((TorcherinoTileEntity) tile).setCustomName(stack.getDisplayName()); }
        }
        if (Config.INSTANCE.log_placement)
        {
            String prefix = "Something";
            if (placer != null) { prefix = placer.getDisplayName().getString() + "(" + placer.getStringUUID() + ")"; }
            Torcherino.LOGGER.info("[Torcherino] {} placed a {} at {} {} {}.", prefix,
                    StringUtils.capitalize(getDescriptionId().replace("block.torcherino.", "").replace("_", " ")), pos.getX(), pos.getY(), pos.getZ());
        }
    }

    @Override @SuppressWarnings("deprecation")
    public void tick(final BlockState state, final ServerWorld world, final BlockPos pos, final Random random)
    {
        final TileEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof TorcherinoTileEntity) { ((TorcherinoTileEntity) tileEntity).tick(); }
    }

    @Override @SuppressWarnings("deprecation")
    public PushReaction getPistonPushReaction(final BlockState state) { return PushReaction.IGNORE; }

    @Override @SuppressWarnings("deprecation")
    public void onPlace(final BlockState state, final World world, final BlockPos pos, final BlockState oldState, final boolean b)
    {
        final TileEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof TorcherinoTileEntity) { ((TorcherinoTileEntity) tileEntity).setPoweredByRedstone(state.getValue(POWERED)); }
    }

    @Override @SuppressWarnings("ConstantConditions")
    public BlockState getStateForPlacement(final BlockItemUseContext context)
    {
        final boolean powered = context.getLevel().hasNeighborSignal(context.getClickedPos());
        return super.getStateForPlacement(context).setValue(HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite()).setValue(POWERED, powered);
    }

    @Override @SuppressWarnings("deprecation")
    public void neighborChanged(final BlockState state, final World world, final BlockPos pos, final Block block, final BlockPos fromPos, final boolean b)
    {
        if (world.isClientSide) { return; }
        final boolean powered = world.hasNeighborSignal(pos);
        if (state.getValue(POWERED) != powered)
        {
            world.setBlockAndUpdate(pos, state.setValue(POWERED, powered));
            final TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof TorcherinoTileEntity) { ((TorcherinoTileEntity) tileEntity).setPoweredByRedstone(powered); }
        }
    }
}