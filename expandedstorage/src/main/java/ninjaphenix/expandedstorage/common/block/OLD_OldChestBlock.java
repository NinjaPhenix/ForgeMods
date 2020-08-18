package ninjaphenix.expandedstorage.common.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import ninjaphenix.expandedstorage.OLD_ModContent;
import ninjaphenix.expandedstorage.Registries;
import ninjaphenix.expandedstorage.common.block.entity.OLD_OldChestTileEntity;

import javax.annotation.Nullable;

public final class OLD_OldChestBlock extends OLD_BaseChestBlock<OLD_OldChestTileEntity>
{
    public OLD_OldChestBlock(final Properties properties, final ResourceLocation registryName)
    {
        super(properties, () -> OLD_ModContent.OLD_CHEST_TE);
        setRegistryName(registryName);
    }

    @Override @SuppressWarnings("ConstantConditions")
    public TileEntity createTileEntity(@Nullable final BlockState state, @Nullable final IBlockReader world)
    {
        final ResourceLocation registryName = getRegistryName();
        return new OLD_OldChestTileEntity(new ResourceLocation(registryName.getNamespace(), registryName.getPath().substring(4)));
    }

    @Override
    protected boolean isBlocked(final IWorld world, final BlockPos pos)
    {
        final BlockPos upPos = pos.up();
        final BlockState upState = world.getBlockState(upPos);
        return (upState.isNormalCube(world, upPos) && upState.getBlock() != this) || world.getEntitiesWithinAABB(CatEntity.class, new AxisAlignedBB(pos.getX(),
                pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1)).stream().anyMatch(CatEntity::func_233684_eK_);
    }

    @Override @SuppressWarnings("deprecation")
    public BlockRenderType getRenderType(@Nullable final BlockState state) { return BlockRenderType.MODEL; }

    @Override @SuppressWarnings("unchecked")
    public SimpleRegistry<Registries.TierData> getDataRegistry() { return Registries.OLD; }
}