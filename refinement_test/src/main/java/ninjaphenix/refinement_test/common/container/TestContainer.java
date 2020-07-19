package ninjaphenix.refinement_test.common.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import ninjaphenix.refinement_test.common.TestContent;

public class TestContainer extends Container
{
    public TestContainer(final int windowId, final PlayerInventory playerInventory)
    {
        super(TestContent.TEST_CONTAINER_TYPE.get(), windowId);
    }

    @Override
    public boolean canInteractWith(final PlayerEntity player)
    {
        return true;
    }
}
