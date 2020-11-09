package torcherino.block.tile;

import com.mojang.datafixers.types.Type;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import java.util.function.Predicate;
import java.util.function.Supplier;

public final class CustomTileEntityType<T extends TileEntity> extends TileEntityType<T>
{
    private final Predicate<Block> PREDICATE;

    @SuppressWarnings("ConstantConditions")
    public CustomTileEntityType(final Supplier<? extends T> factory, final Predicate<Block> isBlockValid, final Type<?> dataFixerType)
    {
        super(factory, null, dataFixerType);
        PREDICATE = isBlockValid;
    }

    @Override
    public boolean isValid(final Block block) { return PREDICATE.test(block); }
}