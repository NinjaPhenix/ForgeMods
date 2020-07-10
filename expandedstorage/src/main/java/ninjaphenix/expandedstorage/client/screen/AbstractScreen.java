package ninjaphenix.expandedstorage.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import ninjaphenix.expandedstorage.common.inventory.AbstractContainer;
import ninjaphenix.expandedstorage.common.screen.ScreenMeta;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public abstract class AbstractScreen<T extends AbstractContainer<R>, R extends ScreenMeta> extends ContainerScreen<T>
{
    protected final R SCREEN_META;
    private final Integer INVENTORY_LABEL_LEFT;

    protected AbstractScreen(@NotNull final T container, @NotNull final PlayerInventory playerInventory, @NotNull final ITextComponent title,
            @NotNull final Function<R, Integer> inventoryLabelLeftFunction)
    {
        super(container, playerInventory, title);
        SCREEN_META = container.SCREEN_META; INVENTORY_LABEL_LEFT = inventoryLabelLeftFunction.apply(SCREEN_META);
    }

    @Override @SuppressWarnings("ConstantConditions")
    protected void func_230450_a_(@NotNull final MatrixStack stack, final float partialTicks, final int mouseX, final int mouseY)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        field_230706_i_.getTextureManager().bindTexture(SCREEN_META.TEXTURE);
        func_238463_a_(stack, guiLeft, guiTop, 0, 0, xSize, ySize, SCREEN_META.TEXTURE_WIDTH, SCREEN_META.TEXTURE_HEIGHT);
    }

    @Override
    public void func_230430_a_(@NotNull final MatrixStack stack, final int mouseX, final int mouseY, final float partialTicks)
    {
        func_230446_a_(stack);
        super.func_230430_a_(stack, mouseX, mouseY, partialTicks);
        this.func_230459_a_(stack, mouseX, mouseY);
    }

    @Override
    protected void func_230451_b_(@NotNull final MatrixStack stack, final int mouseX, final int mouseY)
    {
        field_230712_o_.func_238407_a_(stack, field_230704_d_, 8, 6, 4210752);
        field_230712_o_.func_238407_a_(stack, playerInventory.getDisplayName(), INVENTORY_LABEL_LEFT, this.ySize - 96 + 2, 4210752);
    }

    @Override @SuppressWarnings("ConstantConditions")
    public void func_231175_as__() { field_230706_i_.player.closeScreen(); }

    @Override @SuppressWarnings("ConstantConditions")
    public boolean func_231046_a_(final int keyCode, final int scanCode, final int modifiers)
    {
        if (keyCode == 256 || field_230706_i_.gameSettings.keyBindInventory.matchesKey(keyCode, scanCode)) { func_231175_as__(); return true; }
        return super.func_231046_a_(keyCode, scanCode, modifiers);
    }

    protected static class Rectangle
    {
        public final int X, Y, WIDTH, HEIGHT, TEXTURE_X, TEXTURE_Y, TEXTURE_WIDTH, TEXTURE_HEIGHT;

        public Rectangle(final int x, final int y, final int width, final int height, final int textureX, final int textureY, final int textureWidth,
                final int textureHeight)
        {
            X = x; Y = y; WIDTH = width; HEIGHT = height;
            TEXTURE_X = textureX; TEXTURE_Y = textureY; TEXTURE_WIDTH = textureWidth; TEXTURE_HEIGHT = textureHeight;
        }

        public void render(@NotNull final MatrixStack stack)
        { func_238463_a_(stack, X, Y, TEXTURE_X, TEXTURE_Y, WIDTH, HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT); }
    }
}