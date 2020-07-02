package ninjaphenix.expandedstorage.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.IBlockReader;
import ninjaphenix.expandedstorage.Registries;
import ninjaphenix.expandedstorage.common.block.entity.OldChestTileEntity;

import javax.annotation.Nullable;

public class OldChestBlock extends AbstractChestBlock
{
	public OldChestBlock(final Properties properties, ResourceLocation registryName) { super(properties); setRegistryName(registryName); }

	@Nullable
	@Override
	public TileEntity createTileEntity(final BlockState state, final IBlockReader world)
	{
		final ResourceLocation regName = getRegistryName();
		return new OldChestTileEntity(new ResourceLocation(regName.getNamespace(), regName.getPath().substring(4)));
	}

	@Override
	@SuppressWarnings("unchecked")
	public SimpleRegistry<Registries.TierData> getDataRegistry() { return Registries.OLD; }
}