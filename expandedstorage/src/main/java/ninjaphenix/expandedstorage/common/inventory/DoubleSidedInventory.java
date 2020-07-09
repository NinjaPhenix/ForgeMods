package ninjaphenix.expandedstorage.common.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import org.jetbrains.annotations.NotNull;

public class DoubleSidedInventory implements ISidedInventory
{
	private final ISidedInventory first;
	private final ISidedInventory second;

	public DoubleSidedInventory(@NotNull final ISidedInventory firstInventory, @NotNull final ISidedInventory secondInventory)
	{
		first = firstInventory;
		second = secondInventory;
	}

	@NotNull @Override @SuppressWarnings("NullableProblems")
	public int[] getSlotsForFace(@NotNull final Direction direction)
	{
		final int[] firstSlots = first.getSlotsForFace(direction);
		final int[] secondSlots = second.getSlotsForFace(direction);
		final int[] combined = new int[firstSlots.length + secondSlots.length];
		int index = 0;
		for (int slot : firstSlots) { combined[index++] = slot; }
		for (int slot : secondSlots) { combined[index++] = slot + first.getSizeInventory(); }
		return combined;
	}

	@Override
	public boolean canInsertItem(final int slot, @NotNull final ItemStack stack, @SuppressWarnings("NullableProblems") @NotNull final Direction direction)
	{
		if (slot >= first.getSizeInventory()) { return second.canInsertItem(slot - first.getSizeInventory(), stack, direction); }
		return first.canInsertItem(slot, stack, direction);
	}

	@Override
	public boolean canExtractItem(final int slot, @NotNull final ItemStack stack, @NotNull final Direction direction)
	{
		if (slot >= first.getSizeInventory()) { return second.canExtractItem(slot - first.getSizeInventory(), stack, direction); }
		return first.canExtractItem(slot, stack, direction);
	}

	@Override
	public int getSizeInventory() { return first.getSizeInventory() + second.getSizeInventory(); }

	@Override
	public boolean isEmpty() { return first.isEmpty() && second.isEmpty(); }

	@Override
	public boolean isUsableByPlayer(@NotNull final PlayerEntity player) { return first.isUsableByPlayer(player) && second.isUsableByPlayer(player); }

	@Override
	public void clear() { first.clear(); second.clear(); }

	@Override
	public void markDirty() { first.markDirty(); second.markDirty(); }

	@Override
	public void openInventory(@NotNull final PlayerEntity player) { first.openInventory(player); second.openInventory(player); }

	@Override
	public void closeInventory(@NotNull final PlayerEntity player) { first.closeInventory(player); second.closeInventory(player); }

	public boolean isPart(@NotNull final ISidedInventory inventory) { return first == inventory || second == inventory; }

	@Override
	public int getInventoryStackLimit() { return first.getInventoryStackLimit(); }

    @NotNull @Override
	public ItemStack getStackInSlot(final int slot)
	{
		if (slot >= first.getSizeInventory()) { return second.getStackInSlot(slot - first.getSizeInventory()); }
		return first.getStackInSlot(slot);
	}

    @NotNull @Override
	public ItemStack decrStackSize(final int slot, final int amount)
	{
		if (slot >= first.getSizeInventory()) { return second.decrStackSize(slot - first.getSizeInventory(), amount); }
		return first.decrStackSize(slot, amount);
	}

    @NotNull @Override
	public ItemStack removeStackFromSlot(final int slot)
	{
		if (slot >= first.getSizeInventory()) { return second.removeStackFromSlot(slot - first.getSizeInventory()); }
		return first.removeStackFromSlot(slot);
	}

	@Override
	public void setInventorySlotContents(final int slot, @NotNull final ItemStack stack)
	{
		if (slot >= first.getSizeInventory()) { second.setInventorySlotContents(slot - first.getSizeInventory(), stack); }
		else { first.setInventorySlotContents(slot, stack); }
	}

	@Override
	public boolean isItemValidForSlot(final int slot, @NotNull final ItemStack stack)
	{
		if (slot >= first.getSizeInventory()) { return second.isItemValidForSlot(slot - first.getSizeInventory(), stack); }
		return first.isItemValidForSlot(slot, stack);
	}
}