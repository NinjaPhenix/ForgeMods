package ninjaphenix.expandedstorage.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

@FunctionalInterface // todo: Extends abstract container
public interface OLD_ServerContainerFactory<T /*extends OLD_AbstractContainer<?>*/>
{
    T create(final int windowId, final BlockPos pos, final IInventory inventory, final EntityPlayer player, final ITextComponent displayName);
}