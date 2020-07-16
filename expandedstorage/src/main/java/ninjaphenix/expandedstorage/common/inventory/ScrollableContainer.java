package ninjaphenix.expandedstorage.common.inventory;

import com.google.common.collect.ImmutableSortedMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.IContainerFactory;
import ninjaphenix.expandedstorage.ModContent;
import ninjaphenix.expandedstorage.common.screen.ScrollableScreenMeta;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.function.IntUnaryOperator;

public final class ScrollableContainer extends AbstractContainer<ScrollableScreenMeta>
{
    // @formatter:off
    private static final ImmutableSortedMap<Integer, ScrollableScreenMeta> SIZES = new ImmutableSortedMap.Builder<Integer, ScrollableScreenMeta>(Integer::compare)
            .put(27, new ScrollableScreenMeta(9, 3, 27, getTexture("shared", 9, 3), 208, 192)) // Wood
            .put(54, new ScrollableScreenMeta(9, 6, 54, getTexture("shared", 9, 6), 208, 240)) // Iron / Large Wood
            .put(81, new ScrollableScreenMeta(9, 9, 81, getTexture("shared", 9, 9), 208, 304)) // Gold
            .put(108, new ScrollableScreenMeta(9, 9, 108, getTexture("shared", 9, 9), 208, 304)) // Diamond / Large Iron
            .put(135, new ScrollableScreenMeta(9, 9, 135, getTexture("shared", 9, 9), 208, 304)) // Netherite
            .put(162, new ScrollableScreenMeta(9, 9, 162, getTexture("shared", 9, 9), 208, 304)) // Large Gold
            .put(216, new ScrollableScreenMeta(9, 9, 216, getTexture("shared", 9, 9), 208, 304)) // Large Diamond
            .put(270, new ScrollableScreenMeta(9, 9, 270, getTexture("shared", 9, 9), 208, 304)) // Large Netherite
            .build();
    // @formatter:on

    public ScrollableContainer(final int windowId, final BlockPos pos, final IInventory inventory, final PlayerEntity player,
            @Nullable final ITextComponent displayName)
    {
        super(ModContent.SCROLLABLE_CONTAINER_TYPE, windowId, pos, inventory, player, getNearestSize(inventory.getSizeInventory()), displayName);
        for (int i = 0; i < INVENTORY.getSizeInventory(); i++)
        {
            final int x = i % SCREEN_META.WIDTH;
            int y = MathHelper.ceil((((double) (i - x)) / SCREEN_META.WIDTH));
            if (y >= SCREEN_META.HEIGHT) { y = -2000; }
            else {y = y * 18 + 18;}
            addSlot(new Slot(INVENTORY, i, x * 18 + 8, y));
        }
        final int left = (SCREEN_META.WIDTH * 18 + 14) / 2 - 80, top = 18 + 14 + (SCREEN_META.HEIGHT * 18);
        for (int x = 0; x < 9; x++) { for (int y = 0; y < 3; y++) { addSlot(new Slot(player.inventory, y * 9 + x + 9, left + 18 * x, top + y * 18)); } }
        for (int i = 0; i < 9; i++) { addSlot(new Slot(player.inventory, i, left + 18 * i, top + 58)); }
    }

    private static ScrollableScreenMeta getNearestSize(final int invSize)
    {
        ScrollableScreenMeta val = SIZES.get(invSize);
        if (val != null) { return val; }
        final List<Integer> keys = SIZES.keySet().asList();
        final int index = Collections.binarySearch(keys, invSize);
        final int largestKey = keys.get(Math.abs(index) - 1);
        val = SIZES.get(largestKey);
        if (val != null && largestKey > invSize && largestKey - invSize <= val.WIDTH) { return val; }
        throw new RuntimeException("No screen can show an inventory of size " + invSize + ".");
    }

    public void moveSlotRange(final int min, final int max, final int yChange) { for (int i = min; i < max; i++) { inventorySlots.get(i).yPos += yChange; } }

    public void setSlotRange(final int min, final int max, final IntUnaryOperator yPos)
    { for (int i = min; i < max; i++) { inventorySlots.get(i).yPos = yPos.applyAsInt(i); } }

    public static class Factory implements IContainerFactory<ScrollableContainer>
    {
        @Nullable @Override
        public ScrollableContainer create(final int windowId, final PlayerInventory playerInventory, @Nullable final PacketBuffer data)
        {
            if (data == null) { return null; }
            return new ScrollableContainer(windowId, data.readBlockPos(), new Inventory(data.readInt()), playerInventory.player, null);
        }
    }
}