package ninjaphenix.refinement.impl.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import ninjaphenix.refinement.impl.common.container.UpgradeContainer;

public final class UpgradeContainerScreen extends ContainerScreen<UpgradeContainer>
{
    public UpgradeContainerScreen(final UpgradeContainer container, final PlayerInventory playerInventory, final ITextComponent title)
    {
        super(container, playerInventory, title);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final MatrixStack stack, final float partialTicks, final int x, final int y)
    {

    }

    public static class SpriteButton extends Button
    {
        private final int textureX, textureY;

        public SpriteButton(final int x, final int y, final ITextComponent narrationMessage, final IPressable onPress, final ITooltip onTooltip,
                final int textureX, final int textureY)
        {
            super(x, y, 20, 20, narrationMessage, onPress, onTooltip);
            this.textureX = textureX;
            this.textureY = textureY;
        }

        @Override
        public void renderButton(final MatrixStack stack, final int mouseX, final int mouseY, final float partialTicks)
        {
            blit(stack, x, y, width, height, textureX, getYImage(isHovered()) * height + textureY);
        }
    }
}
