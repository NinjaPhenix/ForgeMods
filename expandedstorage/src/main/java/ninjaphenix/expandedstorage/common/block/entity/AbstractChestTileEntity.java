package ninjaphenix.expandedstorage.common.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({ "WeakerAccess", "NullableProblems" })
public abstract class AbstractChestTileEntity extends LockableLootTileEntity implements ISidedInventory
{
    protected ITextComponent defaultContainerName;
    protected int inventorySize;
    protected NonNullList<ItemStack> inventory;
    protected int[] SLOTS;
    protected ResourceLocation block;

    @Nullable @Override
    protected Container createMenu(final int windowId, @NotNull final PlayerInventory playerInventory) { return null; }

    public AbstractChestTileEntity(@NotNull final TileEntityType type, @Nullable final ResourceLocation block)
    { super(type); if (block != null) { initialize(block); } }

    @NotNull @Override
    protected ITextComponent getDefaultName() { return defaultContainerName; }

    protected abstract void initialize(@NotNull final ResourceLocation block);

    @NotNull
    public ResourceLocation getBlock() { return block; }

    public void setBlock(@NotNull final ResourceLocation block) { this.block = block; }

    @NotNull @Override
    protected NonNullList<ItemStack> getItems() { return inventory; }

    @Override
    public void setItems(@NotNull final NonNullList<ItemStack> inventory) { this.inventory = inventory; }

    @NotNull @Override
    public int[] getSlotsForFace(@NotNull final Direction direction) { return SLOTS; }

    @Override
    public boolean canInsertItem(final int slot, @NotNull final ItemStack stack, @NotNull final Direction direction)
    { return this.isItemValidForSlot(slot, stack); }

    @Override
    public boolean canExtractItem(final int slot, @NotNull final ItemStack stack, @NotNull final Direction direction) { return true; }

    @Override
    public int getSizeInventory() { return inventorySize; }

    @Override
    public boolean isEmpty() { return inventory.stream().allMatch(ItemStack::isEmpty); }

    @Override
    public void func_230337_a_(@NotNull final BlockState state, @NotNull final CompoundNBT tag) {
        super.func_230337_a_(state, tag);
        this.initialize(new ResourceLocation(tag.getString("type")));
        if (!checkLootAndRead(tag)) { ItemStackHelper.loadAllItems(tag, inventory); }
    }

    @NotNull @Override
    public CompoundNBT write(@NotNull final CompoundNBT tag)
    {
        super.write(tag);
        tag.putString("type", block.toString());
        if (!checkLootAndWrite(tag)) { ItemStackHelper.saveAllItems(tag, inventory); }
        return tag;
    }

    @NotNull @Override
    public CompoundNBT getUpdateTag() { CompoundNBT tag = this.write(new CompoundNBT()); tag.putString("type", block.toString()); return tag; }
}