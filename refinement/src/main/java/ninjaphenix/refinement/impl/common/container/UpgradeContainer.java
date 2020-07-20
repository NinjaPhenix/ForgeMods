package ninjaphenix.refinement.impl.common.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import ninjaphenix.refinement.impl.RefinementContent;

public final class UpgradeContainer extends Container
{
    public UpgradeContainer(final int windowId, final PlayerInventory playerInventory)
    {
        super(RefinementContent.UPGRADE_CONTAINER_TYPE.get(), windowId);
    }

    @Override
    public boolean canInteractWith(final PlayerEntity player)
    {
        return true;
    }
}