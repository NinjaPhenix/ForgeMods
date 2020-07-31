package ninjaphenix.refinement.impl.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import ninjaphenix.refinement.impl.Refinement;
import ninjaphenix.refinement.impl.common.container.UpgradeContainer;

public final class UpgradeContainerScreen extends ContainerScreen<UpgradeContainer>
{
    private static final ResourceLocation UPGRADE_GUI_TEXTURE = Refinement.getRl("textures/gui/container/upgrade.png");
    private static final ResourceLocation TABS_GUI_TEXTURE = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
    private Tab CURRENT_TAB;

    public UpgradeContainerScreen(final UpgradeContainer container, final PlayerInventory playerInventory, final ITextComponent title)
    {
        super(container, playerInventory, title);
        CURRENT_TAB = Tab.NOT_SET;
    }

    @Override
    protected void init()
    {
        super.init();
        initTab(CURRENT_TAB == Tab.NOT_SET ? Tab.UPGRADE : CURRENT_TAB);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final MatrixStack stack, final float partialTicks, final int mouseX, final int mouseY)
    {
        final TextureManager textureManager = getMinecraft().getTextureManager();
        // Reset color
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        // Render Background Tabs
        textureManager.bindTexture(TABS_GUI_TEXTURE);
        if (CURRENT_TAB == Tab.UPGRADE)
        {
            renderTab(stack, Tab.DOWNGRADE);
            renderTab(stack, Tab.INFORMATION);
        }
        else if (CURRENT_TAB == Tab.DOWNGRADE)
        {
            renderTab(stack, Tab.UPGRADE);
            renderTab(stack, Tab.INFORMATION);
        }
        else if (CURRENT_TAB == Tab.INFORMATION)
        {
            renderTab(stack, Tab.UPGRADE);
            renderTab(stack, Tab.DOWNGRADE);
        }
        // Render Background
        textureManager.bindTexture(UPGRADE_GUI_TEXTURE);
        blit(stack, guiLeft, guiTop, 0, 0, xSize, ySize, 208, 240);
        // Render Foreground tab
        textureManager.bindTexture(TABS_GUI_TEXTURE);
        renderTab(stack, CURRENT_TAB);
    }

    private void initTab(final Tab tab)
    {
        if (tab != Tab.NOT_SET)
        {
            // Deconstruct tab
        }
        // Construct Tab
        CURRENT_TAB = tab;
    }

    private void renderTab(final MatrixStack stack, final Tab tab)
    {
        final int tabTop = tab == CURRENT_TAB ? 32 : 0;
        if (tab == Tab.UPGRADE)
        {
            blit(stack, guiLeft, guiTop - 28, 0, tabTop, 28, 32);
        }
        else if (tab == Tab.DOWNGRADE)
        {
            blit(stack, guiLeft + 28, guiTop - 28, 28, tabTop, 28, 32);
        }
        else if (tab == Tab.INFORMATION)
        {
            blit(stack, guiLeft + 56, guiTop - 28, 56, tabTop, 28, 32);
        }
    }

    public static class SpriteButton extends Button
    {
        private final int textureX, textureY;

        public SpriteButton(final int x, final int y, final ITextComponent narrationMessage, final IPressable onPress, final ITooltip onTooltip, final int textureX, final int textureY)
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
