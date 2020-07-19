package ninjaphenix.refinement.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import ninjaphenix.refinement.common.container.UpgradeContainer;

public class UpgradeContainerScreen extends ContainerScreen<UpgradeContainer>
{
    public UpgradeContainerScreen(final UpgradeContainer container, final PlayerInventory playerInventory, final ITextComponent title)
    {
        super(container, playerInventory, title);
    }

    @Override
    protected void func_230450_a_(final MatrixStack stack, final float partialTicks, final int mouseX, final int mouseY)
    {
        // drawGuiBackground
    }
}
