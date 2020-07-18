package ninjaphenix.refinement.common.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import ninjaphenix.refinement.common.RefinementContent;

public final class UpgradeContainer extends Container
{
    public UpgradeContainer(final int id)
    {
        super(RefinementContent.UPGRADE_CONTAINER_TYPE, id);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn)
    {
        return true;
    }
}