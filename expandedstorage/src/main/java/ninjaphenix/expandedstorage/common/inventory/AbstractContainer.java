package ninjaphenix.expandedstorage.common.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import ninjaphenix.expandedstorage.ExpandedStorage;
import ninjaphenix.expandedstorage.common.screen.ScreenMeta;

import javax.annotation.Nullable;

public abstract class AbstractContainer<T extends ScreenMeta> extends Container
{
    public final ITextComponent DISPLAY_NAME;
    public final BlockPos ORIGIN;
    public final T SCREEN_META;
    protected final IInventory INVENTORY;

    public AbstractContainer(final ContainerType<?> type, final int windowId, final BlockPos pos, final IInventory inventory,
                             final PlayerEntity player, final T screenMeta, @Nullable final ITextComponent displayName)
    {
        super(type, windowId);
        ORIGIN = pos;
        INVENTORY = inventory;
        SCREEN_META = screenMeta;
        DISPLAY_NAME = displayName;
        inventory.startOpen(player);
    }

    public static ResourceLocation getTexture(final String prefix, final int width, final int height)
    {
        return ExpandedStorage.getRl(String.format("textures/gui/container/%s_%d_%d.png", prefix, width, height));
    }

    @Override
    public boolean stillValid(final PlayerEntity player) { return INVENTORY.stillValid(player); }

    @Override
    public ItemStack quickMoveStack(final PlayerEntity player, final int index)
    {
        ItemStack stack = ItemStack.EMPTY;
        final Slot slot = slots.get(index);
        if (slot != null && slot.hasItem())
        {
            final ItemStack slotStack = slot.getItem();
            final int inventorySize = INVENTORY.getContainerSize();
            stack = slotStack.copy();
            if (index < inventorySize)
            {
                if (!moveItemStackTo(slotStack, inventorySize, slots.size(), true)) { return ItemStack.EMPTY; }
            }
            else if (!moveItemStackTo(slotStack, 0, inventorySize, false)) { return ItemStack.EMPTY; }
            if (slotStack.isEmpty()) { slot.set(ItemStack.EMPTY); }
            else { slot.setChanged(); }
        }
        return stack;
    }

    @Override
    public void removed(final PlayerEntity player)
    {
        super.removed(player);
        INVENTORY.stopOpen(player);
    }

    public IInventory getInv() { return INVENTORY; }
}