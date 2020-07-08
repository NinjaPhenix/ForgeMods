package ninjaphenix.container_library.common.inventory;

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
import ninjaphenix.container_library.common.screen.PagedScreenMeta;
import ninjaphenix.expandedstorage.ModContent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public final class PagedContainer extends AbstractContainer<PagedScreenMeta>
{
    private static final ImmutableSortedMap<Integer, PagedScreenMeta> SIZES = new ImmutableSortedMap.Builder<Integer, PagedScreenMeta>(Integer::compare)
            .put(27, new PagedScreenMeta(9, 3, 1, 27, getTexture("shared", 9, 3), 208, 192)) // Wood
            .put(54, new PagedScreenMeta(9, 6, 1, 54, getTexture("shared", 9, 6), 208, 240)) // Iron / Large Wood
            .put(81, new PagedScreenMeta(9, 9, 1, 81, getTexture("shared", 9, 9), 208, 304)) // Gold
            .put(108, new PagedScreenMeta(9, 6, 2, 108, getTexture("shared", 9, 6), 208, 240)) // Diamond / Large Iron
            .put(162, new PagedScreenMeta(9, 6, 3, 162, getTexture("shared", 9, 6), 208, 240)) // Large Gold
            .put(216, new PagedScreenMeta(9, 8, 3, 216, getTexture("shared", 9, 8), 208, 288)) // Large Diamond
            .build();

    private static PagedScreenMeta getNearestSize(final int invSize)
    {
        PagedScreenMeta val = SIZES.get(invSize);
        if (val != null) { return val; }
        final List<Integer> keys = SIZES.keySet().asList();
        final int index = Collections.binarySearch(keys, invSize);
        final int largestKey = keys.get(Math.abs(index) - 1);
        val = SIZES.get(largestKey);
        if (val != null && largestKey > invSize && largestKey - invSize <= val.WIDTH) { return val; }
        throw new RuntimeException("No screen can show an inventory of size " + invSize + ".");
    }

    public PagedContainer(final int windowId, @NotNull final BlockPos pos, @NotNull final IInventory inventory, @NotNull final PlayerEntity player,
            @Nullable final ITextComponent displayName)
    {
        super(ModContent.PAGED_CONTAINER_TYPE, windowId, pos, inventory, player, getNearestSize(inventory.getSizeInventory()), displayName);
        resetSlotPositions(true);
        final int left = (SCREEN_META.WIDTH * 18 + 14) / 2 - 80, top = 18 + 14 + (SCREEN_META.HEIGHT * 18);
        for (int x = 0; x < 9; x++) { for (int y = 0; y < 3; y++) { addSlot(new Slot(player.inventory, y * 9 + x + 9, left + 18 * x, top + y * 18)); } }
        for (int i = 0; i < 9; i++) { addSlot(new Slot(player.inventory, i, left + 18 * i, top + 58)); }
    }

    public void resetSlotPositions(final boolean makeSlots)
    {
        for (int i = 0; i < INVENTORY.getSizeInventory(); i++)
        {
            final int x = i % SCREEN_META.WIDTH;
            int y = MathHelper.ceil((((double) (i - x)) / SCREEN_META.WIDTH));
            if (y >= SCREEN_META.HEIGHT) { y = (18 * (y % SCREEN_META.HEIGHT)) - 2000; }
            else {y = y * 18;}
            if (makeSlots) { addSlot(new Slot(INVENTORY, i, x * 18 + 8, y + 18)); }
            else { inventorySlots.get(i).yPos = y + 18; }
        }
    }

    public void moveSlotRange(final int min, final int max, final int yChange) { for (int i = min; i < max; i++) { inventorySlots.get(i).yPos += yChange; } }

    public static class Factory implements IContainerFactory<PagedContainer>
    {
        @Override
        public PagedContainer create(final int windowId, @NotNull final PlayerInventory playerInventory, @Nullable final PacketBuffer data)
        {
            if (data != null)
            {
                final int inventorySize = data.readInt(); final BlockPos pos = data.readBlockPos();
                return new PagedContainer(windowId, pos, new Inventory(inventorySize), playerInventory.player, null);
            }
            return null;
        }
    }
}