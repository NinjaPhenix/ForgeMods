package ninjaphenix.expandedstorage.common.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

@FunctionalInterface
public interface ServerContainerFactory<T extends AbstractContainer<?>>
{
    T create(final int windowId, final BlockPos pos, final IInventory inventory, final PlayerEntity player, final ITextComponent displayName);
}