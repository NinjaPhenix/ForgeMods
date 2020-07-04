package ninjaphenix.expandedstorage.common.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.IBlockReader;
import ninjaphenix.expandedstorage.ModContent;
import ninjaphenix.expandedstorage.Registries;
import ninjaphenix.expandedstorage.common.block.entity.OldChestTileEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OldChestBlock extends BaseChestBlock<OldChestTileEntity>
{
    public OldChestBlock(@NotNull final Properties properties, @NotNull final ResourceLocation registryName)
    {
        super(properties, () -> ModContent.OLD_CHEST_TE);
        setRegistryName(registryName);
    }

    @NotNull @Override @SuppressWarnings("ConstantConditions")
    public TileEntity createTileEntity(@Nullable final BlockState state, @Nullable final IBlockReader world)
    {
        final ResourceLocation registryName = getRegistryName();
        return new OldChestTileEntity(new ResourceLocation(registryName.getNamespace(), registryName.getPath().substring(4)));
    }

    @NotNull @Override @SuppressWarnings("deprecation")
    public BlockRenderType getRenderType(@Nullable final BlockState state) { return BlockRenderType.MODEL; }

    @NotNull @Override @SuppressWarnings("unchecked")
    public SimpleRegistry<Registries.TierData> getDataRegistry() { return Registries.OLD; }
}