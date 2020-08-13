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
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"WeakerAccess", "NullableProblems"})
public abstract class BaseChestTileEntity extends LockableLootTileEntity implements ISidedInventory
{
    protected ITextComponent defaultContainerName;
    protected int inventorySize;
    protected NonNullList<ItemStack> inventory;
    protected int[] SLOTS;
    protected ResourceLocation block;

    public BaseChestTileEntity(final TileEntityType type, final @Nullable ResourceLocation block)
    {
        super(type);
        if (block != null)
        {
            initialize(block);
        }
    }

    @Override
    protected @Nullable Container createMenu(final int windowId, final PlayerInventory playerInventory)
    {
        return null;
    }

    @Override
    protected ITextComponent getDefaultName()
    {
        return defaultContainerName;
    }

    protected abstract void initialize(final ResourceLocation block);

    public ResourceLocation getBlock()
    {
        return block;
    }

    public void setBlock(final ResourceLocation block)
    {
        this.block = block;
    }

    @Override
    protected NonNullList<ItemStack> getItems()
    {
        return inventory;
    }

    @Override
    public void setItems(final NonNullList<ItemStack> inventory)
    {
        this.inventory = inventory;
    }

    @Override
    public int[] getSlotsForFace(final Direction direction)
    {
        return SLOTS;
    }

    @Override
    public boolean canInsertItem(final int slot, final ItemStack stack, final Direction direction)
    {
        return isItemValidForSlot(slot, stack);
    }

    @Override
    public boolean canExtractItem(final int slot, final ItemStack stack, final Direction direction)
    {
        return true;
    }

    @Override
    public int getSizeInventory()
    {
        return inventorySize;
    }

    @Override
    public boolean isEmpty()
    {
        return inventory.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public void read(final BlockState state, final CompoundNBT tag)
    {
        super.read(state, tag);
        initialize(new ResourceLocation(tag.getString("type")));
        if (!checkLootAndRead(tag))
        {
            ItemStackHelper.loadAllItems(tag, inventory);
        }
    }

    @Override
    public CompoundNBT write(final CompoundNBT tag)
    {
        super.write(tag);
        tag.putString("type", block.toString());
        if (!checkLootAndWrite(tag))
        {
            ItemStackHelper.saveAllItems(tag, inventory);
        }
        return tag;
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        final CompoundNBT tag = write(new CompoundNBT());
        tag.putString("type", block.toString());
        return tag;
    }
}