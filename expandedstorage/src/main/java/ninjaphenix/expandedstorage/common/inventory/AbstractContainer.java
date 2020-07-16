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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractContainer<T extends ScreenMeta> extends Container
{
    public final ITextComponent DISPLAY_NAME;
    public final BlockPos ORIGIN;
    public final T SCREEN_META;
    protected final IInventory INVENTORY;

    public AbstractContainer(@NotNull final ContainerType<?> type, final int windowId, @NotNull final BlockPos pos, @NotNull final IInventory inventory,
            @NotNull final PlayerEntity player, @NotNull final T screenMeta, @Nullable final ITextComponent displayName)
    {
        super(type, windowId);
        ORIGIN = pos;
        INVENTORY = inventory;
        SCREEN_META = screenMeta;
        DISPLAY_NAME = displayName;
        inventory.openInventory(player);
    }

    @NotNull
    public static ResourceLocation getTexture(@NotNull final String prefix, final int width, final int height)
    { return ExpandedStorage.getRl(String.format("textures/gui/container/%s_%d_%d.png", prefix, width, height)); }

    @Override
    public boolean canInteractWith(@NotNull final PlayerEntity player) { return INVENTORY.isUsableByPlayer(player); }

    @NotNull @Override
    public ItemStack transferStackInSlot(@NotNull final PlayerEntity player, final int index)
    {
        ItemStack stack = ItemStack.EMPTY;
        final Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack())
        {
            final ItemStack slotStack = slot.getStack();
            final int inventorySize = INVENTORY.getSizeInventory();
            stack = slotStack.copy();
            if (index < inventorySize) { if (!mergeItemStack(slotStack, inventorySize, inventorySlots.size(), true)) { return ItemStack.EMPTY; }}
            else if (!mergeItemStack(slotStack, 0, inventorySize, false)) { return ItemStack.EMPTY; }
            if (slotStack.isEmpty()) { slot.putStack(ItemStack.EMPTY); }
            else {slot.onSlotChanged(); }
        }
        return stack;
    }

    @Override
    public void onContainerClosed(@NotNull final PlayerEntity player)
    {
        super.onContainerClosed(player);
        INVENTORY.closeInventory(player);
    }

    @NotNull
    public IInventory getInv() { return INVENTORY; }
}