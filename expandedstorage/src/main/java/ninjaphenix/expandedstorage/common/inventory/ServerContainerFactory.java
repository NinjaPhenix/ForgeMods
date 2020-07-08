package ninjaphenix.expandedstorage.common.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ServerContainerFactory<T extends AbstractContainer<?>>
{
    T create(final int windowId, @NotNull final BlockPos pos, @NotNull final IInventory inventory, @NotNull final PlayerEntity player, @NotNull final
    ITextComponent displayName);
}