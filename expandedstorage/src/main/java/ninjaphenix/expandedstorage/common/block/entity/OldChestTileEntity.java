package ninjaphenix.expandedstorage.common.block.entity;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import ninjaphenix.expandedstorage.ModContent;
import ninjaphenix.expandedstorage.Registries;

public class OldChestTileEntity extends AbstractChestTileEntity
{

	public OldChestTileEntity() { this(null); }

	public OldChestTileEntity(final ResourceLocation block) { super(ModContent.OLD_CHEST_TE, block); }

	@Override
	protected void initialize(final ResourceLocation block)
	{
		this.block = block;
		Registries.TierData data = Registries.OLD.getValue(block).get();
		defaultContainerName = data.getContainerName();
		inventorySize = data.getSlotCount();
		inventory = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
		SLOTS = new int[inventorySize];
		for (int i = 0; i < inventorySize; i++) { SLOTS[i] = i; }
	}
}