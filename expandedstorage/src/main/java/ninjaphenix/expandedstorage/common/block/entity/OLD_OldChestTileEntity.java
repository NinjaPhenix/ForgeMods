//package ninjaphenix.expandedstorage.common.block.entity;
//
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.NonNullList;
//import net.minecraft.util.ResourceLocation;
//import ninjaphenix.expandedstorage.OLD_ModContent;
//import ninjaphenix.expandedstorage.Registries;
//
//import javax.annotation.Nullable;
//
//public final class OLD_OldChestTileEntity extends OLD_AbstractChestTileEntity
//{
//    public OLD_OldChestTileEntity() { this(null); }
//
//    public OLD_OldChestTileEntity(@Nullable final ResourceLocation block) { super(OLD_ModContent.OLD_CHEST_TE, block); }
//
//    @Override
//    protected void initialize(final ResourceLocation block)
//    {
//        this.block = block;
//        final Registries.TierData data = Registries.OLD.getOrDefault(block);
//        defaultContainerName = data.getContainerName();
//        inventorySize = data.getSlotCount();
//        inventory = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
//        SLOTS = new int[inventorySize];
//        for (int i = 0; i < inventorySize; i++) { SLOTS[i] = i; }
//    }
//}