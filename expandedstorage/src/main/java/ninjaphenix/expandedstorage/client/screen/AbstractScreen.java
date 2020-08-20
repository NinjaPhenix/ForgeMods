package ninjaphenix.expandedstorage.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import ninjaphenix.expandedstorage.common.inventory.AbstractContainer;
import ninjaphenix.expandedstorage.common.network.Networker;
import ninjaphenix.expandedstorage.common.screen.ScreenMeta;

import java.util.function.Function;

public abstract class AbstractScreen<T extends AbstractContainer<R>, R extends ScreenMeta> extends ContainerScreen<T>
{
    protected final R SCREEN_META;
    private final Integer INVENTORY_LABEL_LEFT;

    protected AbstractScreen(final T container, final PlayerInventory playerInventory, final ITextComponent title,
            final Function<R, Integer> inventoryLabelLeftFunction)
    {
        super(container, playerInventory, title);
        SCREEN_META = container.SCREEN_META;
        INVENTORY_LABEL_LEFT = inventoryLabelLeftFunction.apply(SCREEN_META);
    }

    @Override @SuppressWarnings({ "ConstantConditions", "deprecation" })
    protected void drawGuiContainerBackgroundLayer(final MatrixStack stack, final float partialTicks, final int mouseX, final int mouseY)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(SCREEN_META.TEXTURE);
        blit(stack, guiLeft, guiTop, 0, 0, xSize, ySize, SCREEN_META.TEXTURE_WIDTH, SCREEN_META.TEXTURE_HEIGHT);
    }

    @Override
    public void render(final MatrixStack stack, final int mouseX, final int mouseY, final float partialTicks)
    {
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        renderHoveredTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(final MatrixStack stack, final int mouseX, final int mouseY)
    {
        font.func_243248_b(stack, title, 8, 6, 0x404040);
        font.func_243248_b(stack, playerInventory.getDisplayName(), INVENTORY_LABEL_LEFT, ySize - 96 + 2, 0x404040);
    }

    @Override @SuppressWarnings("ConstantConditions")
    public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers)
    {
        if (keyCode == 256 || minecraft.gameSettings.keyBindInventory.matchesKey(keyCode, scanCode))
        {
            Networker.INSTANCE.sendRemovePreferenceCallbackToServer();
            minecraft.player.closeScreen();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    protected final void renderButtonTooltip(final Widget widget, final MatrixStack stack, final int x, final int y)
    {
        renderTooltip(stack, widget.getMessage(), x, y);
    }

    protected static class Rectangle
    {
        public final int X, Y, WIDTH, HEIGHT, TEXTURE_X, TEXTURE_Y, TEXTURE_WIDTH, TEXTURE_HEIGHT;

        public Rectangle(final int x, final int y, final int width, final int height, final int textureX, final int textureY,
                         final int textureWidth, final int textureHeight)
        {
            X = x;
            Y = y;
            WIDTH = width;
            HEIGHT = height;
            TEXTURE_X = textureX;
            TEXTURE_Y = textureY;
            TEXTURE_WIDTH = textureWidth;
            TEXTURE_HEIGHT = textureHeight;
        }

        public void render(final MatrixStack stack)
        {
            blit(stack, X, Y, TEXTURE_X, TEXTURE_Y, WIDTH, HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        }
    }
}
