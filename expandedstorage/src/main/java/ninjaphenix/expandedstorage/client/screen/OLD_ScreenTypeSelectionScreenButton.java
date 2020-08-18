package ninjaphenix.expandedstorage.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import ninjaphenix.expandedstorage.ExpandedStorage;
import ninjaphenix.expandedstorage.common.network.OLD_Networker;

public final class OLD_ScreenTypeSelectionScreenButton extends Button
{
    private final ResourceLocation TEXTURE;

    @SuppressWarnings("ConstantConditions")
    public OLD_ScreenTypeSelectionScreenButton(final int x, final int y, final ITooltip onTooltip)
    {
        super(x, y, 12, 12, new TranslationTextComponent("screen.expandedstorage.change_screen_button"), button ->
        {
            Minecraft.getInstance().player.closeScreenAndDropStack();
            OLD_Networker.INSTANCE.requestOpenSelectionScreen();
        }, onTooltip);
        TEXTURE = ExpandedStorage.getRl("textures/gui/select_screen_button.png");
    }

    @Override @SuppressWarnings("deprecation")
    public void renderButton(final MatrixStack stack, final int mouseX, final int mouseY, final float partialTicks)
    {
        Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        blit(stack, x, y, 0, isHovered() ? height : 0, width, height, 16, 32);

    }

    public void renderTooltip(final MatrixStack stack, final int x, final int y)
    { if (isHovered()) { onTooltip.onTooltip(this, stack, x, y); } }
}