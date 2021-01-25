package ninjaphenix.expandedstorage.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import ninjaphenix.expandedstorage.ModContent;
import ninjaphenix.expandedstorage.Registries;
import ninjaphenix.expandedstorage.common.block.entity.OldChestBlockEntity;
import javax.annotation.Nullable;

public final class OldChestBlock extends ChestBlock<OldChestBlockEntity>
{
    public OldChestBlock(final Properties settings) { super(settings, () -> ModContent.OLD_CHEST_TE); }

    @Nullable
    @Override
    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) { return new OldChestBlockEntity(getRegistryName()); }

    @Override
    protected boolean isBlocked(final IWorld world, final BlockPos pos)
    {
        final BlockPos upPos = pos.above();
        final BlockState upState = world.getBlockState(upPos);
        return (upState.isRedstoneConductor(world, upPos) && upState.getBlock() != this) ||
                world.getEntitiesOfClass(CatEntity.class, new AxisAlignedBB(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1,
                                                                            pos.getY() + 2, pos.getZ() + 1))
                        .stream().anyMatch(CatEntity::isInSittingPose);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public MutableRegistry<Registries.TierData> getDataRegistry() { return Registries.OLD; }
}