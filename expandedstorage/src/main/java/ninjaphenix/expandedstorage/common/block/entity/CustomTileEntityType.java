package ninjaphenix.expandedstorage.common.block.entity;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;
import java.util.function.Supplier;

// todo: just use regular tile entity type?
public final class CustomTileEntityType<T extends TileEntity> extends TileEntityType<T>
{
	private final Predicate<Block> predicate;

	public CustomTileEntityType(@NotNull final Supplier<? extends T> tileEntityFactory,
            @NotNull final Predicate<Block> supportedBlocks, @NotNull final ResourceLocation registryName)
	{
        //noinspection ConstantConditions
        super(tileEntityFactory, null, null);
		predicate = supportedBlocks;
		setRegistryName(registryName);
	}

	@Override
	public final boolean isValidBlock(@NotNull final Block block) { return predicate.test(block); }
}
