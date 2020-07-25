package ninjaphenix.refinement.impl.common.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import ninjaphenix.refinement.impl.RefinementContent;

public final class UpgradeContainer extends Container implements IInventory
{
    public UpgradeContainer(final int windowId, final PlayerInventory playerInventory)
    {
        super(RefinementContent.UPGRADE_CONTAINER_TYPE.get(), windowId);
    }

    @Override
    public boolean canInteractWith(final PlayerEntity player)
    {
        return true;
    }

    @Override
    public int getSizeInventory()
    {
        return 9;
    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }

    @Override
    public ItemStack getStackInSlot(int index)
    {
        return null;
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {

    }

    @Override
    public void markDirty()
    {

    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player)
    {
        return false;
    }

    @Override
    public void clear()
    {

    }
}