package torcherino.block;

import java.util.Random;
import java.util.function.Supplier;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import torcherino.Torcherino;
import torcherino.block.tile.TorcherinoTileEntity;
import torcherino.config.Config;
import torcherino.network.Networker;

import static net.minecraft.state.properties.BlockStateProperties.POWERED;

@SuppressWarnings("unused")
public final class TorcherinoLogic
{
    public static void onBlockPlacedBy(final World world, final BlockPos pos, final BlockState state, final @Nullable LivingEntity placer, final ItemStack stack)
    {
        if (world.isRemote)
        {
            return;
        }
        if (stack.hasDisplayName())
        {
            final TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TorcherinoTileEntity)
            {
                ((TorcherinoTileEntity) tile).setCustomName(stack.getDisplayName());
            }
        }
        if (Config.INSTANCE.logPlacement())
        {
            final String prefix = placer == null ? "Something" : placer.getDisplayName().getString() + "(" + placer.getCachedUniqueIdString() + ")";
            final String blockName = StringUtils.capitalize(state.getBlock().getTranslationKey().replace("block.torcherino.", "").replace("_", " "));
            Torcherino.LOGGER.info("[Torcherino] {} placed a {} at {} {} {}.", prefix, blockName, pos.getX(), pos.getY(), pos.getZ());
        }
    }

    public static void tick(final BlockState state, final ServerWorld world, final BlockPos pos, final Random random)
    {
        final TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TorcherinoTileEntity)
        {
            ((TorcherinoTileEntity) tileEntity).tick();
        }
    }

    public static ActionResultType onBlockActivated(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult hit)
    {
        if (!world.isRemote)
        {
            Networker.INSTANCE.openScreenServer(world, (ServerPlayerEntity) player, pos);
        }
        return ActionResultType.SUCCESS;
    }

    public static void onBlockAdded(final BlockState state, final World world, final BlockPos pos, final BlockState oldState, final boolean isMoving)
    {
        final TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TorcherinoTileEntity)
        {
            ((TorcherinoTileEntity) tileEntity).setPoweredByRedstone(state.get(BlockStateProperties.POWERED));
        }
    }

    public static void neighborChanged(final BlockState state, final World world, final BlockPos pos, final Block block, final BlockPos fromPos, final boolean isMoving, final Supplier<Boolean> isPoweredSupplier)
    {
        if (world.isRemote)
        {
            return;
        }
        final TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TorcherinoTileEntity)
        {
            final Boolean powered = isPoweredSupplier.get();
            if (state.get(POWERED) != powered)
            {
                world.setBlockState(pos, state.with(POWERED, powered));
                ((TorcherinoTileEntity) tileEntity).setPoweredByRedstone(powered);
            }
        }
    }
}