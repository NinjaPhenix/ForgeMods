package ninjaphenix.expandedstorage.common.block.entity;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import ninjaphenix.expandedstorage.ModContent;
import ninjaphenix.expandedstorage.Registries;

import javax.annotation.Nullable;

public final class OldChestTileEntity extends AbstractChestTileEntity
{
    public OldChestTileEntity() { this(null); }

    public OldChestTileEntity(@Nullable final ResourceLocation block) { super(ModContent.OLD_CHEST_TE, block); }

    @Override
    protected void initialize(final ResourceLocation block)
    {
        this.block = block;
        final Registries.TierData data = Registries.OLD.getOrDefault(block);
        defaultContainerName = data.getContainerName();
        inventorySize = data.getSlotCount();
        inventory = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
        SLOTS = new int[inventorySize];
        for (int i = 0; i < inventorySize; i++) { SLOTS[i] = i; }
    }
}