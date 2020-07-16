package ninjaphenix.expandedstorage.common.inventory;

import com.google.common.collect.ImmutableSortedMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.IContainerFactory;
import ninjaphenix.expandedstorage.ModContent;
import ninjaphenix.expandedstorage.common.screen.SingleScreenMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public final class SingleContainer extends AbstractContainer<SingleScreenMeta>
{
    private static final ImmutableSortedMap<Integer, SingleScreenMeta> SIZES = new ImmutableSortedMap.Builder<Integer, SingleScreenMeta>(Integer::compare)
            .put(27, new SingleScreenMeta(9, 3, 27, getTexture("shared", 9, 3), 208, 192)) // Wood
            .put(54, new SingleScreenMeta(9, 6, 54, getTexture("shared", 9, 6), 208, 240)) // Iron / Large Wood
            .put(81, new SingleScreenMeta(9, 9, 81, getTexture("shared", 9, 9), 208, 304)) // Gold
            .put(108, new SingleScreenMeta(12, 9, 108, getTexture("shared", 12, 9), 256, 304)) // Diamond / Large Iron
            .put(135, new SingleScreenMeta(15, 9, 135, getTexture("shared", 15, 9), 320, 304)) // Netherite
            .put(162, new SingleScreenMeta(18, 9, 162, getTexture("shared", 18, 9), 368, 304)) // Large Gold
            .put(216, new SingleScreenMeta(18, 12, 216, getTexture("shared", 18, 12), 368, 352)) // Large Diamond
            .put(270, new SingleScreenMeta(18, 15, 270, getTexture("shared", 18, 15), 368, 416)) // Large Netherite
            .build();

    private static SingleScreenMeta getNearestSize(final int invSize)
    {
        SingleScreenMeta val = SIZES.get(invSize);
        if (val != null) { return val; }
        final List<Integer> keys = SIZES.keySet().asList();
        final int index = Collections.binarySearch(keys, invSize);
        final int largestKey = keys.get(Math.abs(index) - 1);
        val = SIZES.get(largestKey);
        if (val != null && largestKey > invSize && largestKey - invSize <= val.WIDTH) { return val; }
        throw new RuntimeException("No screen can show an inventory of size " + invSize + ".");
    }

    public SingleContainer(final int windowId, @NotNull final BlockPos pos, @NotNull final IInventory inventory, @NotNull final PlayerEntity player,
            @Nullable final ITextComponent displayName)
    {
        super(ModContent.SINGLE_CONTAINER_TYPE, windowId, pos, inventory, player, getNearestSize(inventory.getSizeInventory()), displayName);
        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            final int x = i % SCREEN_META.WIDTH, y = (i - x) / SCREEN_META.WIDTH;
            addSlot(new Slot(inventory, i, x * 18 + 8, y * 18 + 18));
        }
        final int left = (SCREEN_META.WIDTH * 18 + 14) / 2 - 80, top = 18 + 14 + (SCREEN_META.HEIGHT * 18);
        for (int x = 0; x < 9; x++) { for (int y = 0; y < 3; y++) { addSlot(new Slot(player.inventory, y * 9 + x + 9, left + 18 * x, top + y * 18)); } }
        for (int i = 0; i < 9; i++) { addSlot(new Slot(player.inventory, i, left + 18 * i, top + 58)); }
    }

    public static class Factory implements IContainerFactory<SingleContainer>
    {
        @Override
        public SingleContainer create(final int windowId, @NotNull final PlayerInventory playerInventory, @Nullable final PacketBuffer data)
        {
            if (data == null) { return null;}
            return new SingleContainer(windowId, data.readBlockPos(), new Inventory(data.readInt()), playerInventory.player, null);
        }
    }
}