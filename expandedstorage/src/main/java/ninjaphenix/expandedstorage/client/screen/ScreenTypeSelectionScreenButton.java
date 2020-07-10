package ninjaphenix.expandedstorage.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
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

    public ScreenTypeSelectionScreenButton(final int x, final int y, @NotNull final ITooltip onTooltip)
    {
        super(x, y, 12, 12, new TranslationTextComponent("screen.expandedstorage.change_screen_button"), button ->
        {
            Minecraft.getInstance().player.closeScreenAndDropStack();
            Networker.INSTANCE.requestOpenSelectionScreen();
        }, onTooltip);
        TEXTURE = ExpandedStorage.getRl("textures/gui/select_screen_button.png");
    }

    @Override
    public void func_230431_b_(@NotNull final MatrixStack stack, final int mouseX, final int mouseY, final float partialTicks)
    {
        Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.field_230695_q_);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        func_238463_a_(stack, field_230690_l_, field_230691_m_, 0, func_230449_g_() ? field_230689_k_ : 0, field_230688_j_, field_230689_k_, 16, 32);

    }

    public void renderTooltip(@NotNull final MatrixStack stack, final int x, final int y)
    { if (func_230449_g_()) { field_238487_u_.onTooltip(this, stack, x, y); } }
}