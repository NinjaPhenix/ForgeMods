package ninjaphenix.refinement_test.common.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import ninjaphenix.refinement_test.common.TestContent;

public class TestContainer extends Container
{
    public TestContainer(final int id) { super(TestContent.TEST_CONTAINER_TYPE, id); }

    @Override
    public boolean canInteractWith(final PlayerEntity playerIn) { return true; }
}
