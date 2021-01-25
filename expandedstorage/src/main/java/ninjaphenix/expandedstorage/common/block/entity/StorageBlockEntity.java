package ninjaphenix.expandedstorage.common.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.INameable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.LockCode;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StorageBlockEntity extends TileEntity implements INameable, IItemHandlerModifiable
{
    protected ITextComponent defaultContainerName;
    protected int inventorySize;
    protected NonNullList<ItemStack> inventory;
    protected int[] SLOTS;
    protected ResourceLocation block;
    private LockCode lockKey = LockCode.NO_LOCK;
    private ITextComponent displayName;

    protected StorageBlockEntity(final TileEntityType<?> blockEntityType, final ResourceLocation block)
    {
        super(blockEntityType);
        if (block != null) { initialize(block); }
    }

    protected abstract void initialize(final ResourceLocation block);

    @Override
    public void load(final BlockState state, final CompoundNBT tag)
    {
        super.load(state, tag);
        lockKey = LockCode.fromTag(tag);
        if (tag.contains("CustomName", 8)) {
            displayName = ITextComponent.Serializer.fromJson(tag.getString("CustomName"));
        }
        initialize(state.getBlock().getRegistryName());
    }

    @Override
    public CompoundNBT save(final CompoundNBT tag)
    {
        super.save(tag);
        lockKey.addToTag(tag);
        if (displayName != null) {
            tag.putString("CustomName", ITextComponent.Serializer.toJson(displayName));
        }
        return tag;
    }

    public boolean canOpen(PlayerEntity player) {
        return LockableTileEntity.canUnlock(player, lockKey, this.getDisplayName());
    }

    public void setCustomName(ITextComponent name) {
        displayName = name;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable Direction side)
    {
        if(!remove && cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) {
            return LazyOptional.of(() -> this);
        }
       return super.getCapability(cap, side);
    }

    @Override
    public ITextComponent getName()
    {
        return null;
    }

    @Override
    public void setStackInSlot(final int slot, @Nonnull final ItemStack stack)
    {

    }

    @Override
    public int getSlots()
    {
        return 0;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(final int slot)
    {
        return null;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(final int slot, @Nonnull final ItemStack stack, final boolean simulate)
    {
        return null;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(final int slot, final int amount, final boolean simulate)
    {
        return null;
    }

    @Override
    public int getSlotLimit(final int slot)
    {
        return 0;
    }

    @Override
    public boolean isItemValid(final int slot, @Nonnull final ItemStack stack)
    {
        return false;
    }
}