package ninjaphenix.expandedstorage.common.block.entity;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;

import java.util.function.Predicate;
import java.util.function.Supplier;

// todo: Remove, requires ModContent to be rewritten.
public final class CustomTileEntityType<T extends TileEntity> extends TileEntityType<T>
{
    private final Predicate<Block> predicate;

    @SuppressWarnings("ConstantConditions")
    public CustomTileEntityType(final Supplier<? extends T> tileEntityFactory, final Predicate<Block> supportedBlocks, final ResourceLocation registryName)
    {
        super(tileEntityFactory, null, null);
        predicate = supportedBlocks;
        setRegistryName(registryName);
    }

    @Override
    public final boolean isValidBlock(final Block block) { return predicate.test(block); }
}