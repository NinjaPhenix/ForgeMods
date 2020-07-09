package ninjaphenix.expandedstorage.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import ninjaphenix.expandedstorage.ExpandedStorage;
import ninjaphenix.expandedstorage.common.network.Networker;
import org.jetbrains.annotations.NotNull;

public class ScreenTypeSelectionScreenButton extends Button
{
    private final ResourceLocation TEXTURE;

    public ScreenTypeSelectionScreenButton(final int x, final int y)
    {
        super(x, y, 12, 12, new TranslationTextComponent("screen.expandedstorage.change_screen_button").getUnformattedComponentText(), button ->
        {
            Minecraft.getInstance().player.closeScreenAndDropStack();
            Networker.INSTANCE.requestOpenSelectionScreen();
        });
        TEXTURE = ExpandedStorage.getRl("textures/gui/select_screen_button.png");
    }

    @Override
    public void renderButton(final int mouseX, final int mouseY, final float partialTicks)
    {
        Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        blit(x, y, 0, isHovered() ? height : 0, width, height, 16, 32);

    }

    public void renderTooltip(final int x, final int y, @NotNull final TooltipRenderer renderer) { if (isHovered()) { renderer.render(getMessage(), x, y); } }

    public interface TooltipRenderer
    {
        void render(@NotNull final String message, final int x, final int y);
    }
}