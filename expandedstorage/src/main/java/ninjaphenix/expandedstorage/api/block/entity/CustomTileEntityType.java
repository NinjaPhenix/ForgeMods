package ninjaphenix.expandedstorage.api.block.entity;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class CustomTileEntityType<T extends TileEntity> extends TileEntityType<T>
{
	Predicate<Block> predicate;

	public CustomTileEntityType(Supplier<? extends T> supplier_1, Predicate<Block> supports, ResourceLocation registryName)
	{
		super(supplier_1, null, null);
		predicate = supports;
		setRegistryName(registryName);
	}

	@Override
	public boolean isValidBlock(final Block block) { return predicate.test(block); }
}
