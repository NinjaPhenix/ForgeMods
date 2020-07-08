package ninjaphenix.container_library.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import ninjaphenix.container_library.common.inventory.AbstractContainer;
import ninjaphenix.container_library.common.screen.ScreenMeta;
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
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(SCREEN_META.TEXTURE);
        blit(guiLeft, guiTop, 0, 0, xSize, ySize, SCREEN_META.TEXTURE_WIDTH, SCREEN_META.TEXTURE_HEIGHT);
    }

    @Override
    public void render(final int mouseX, final int mouseY, final float partialTicks)
    {
        renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY)
    {
        font.drawString(title.getUnformattedComponentText(), 8, 6, 4210752);
        font.drawString(playerInventory.getDisplayName().getUnformattedComponentText(), INVENTORY_LABEL_LEFT, this.ySize - 96 + 2, 4210752);
    }

    @Override @SuppressWarnings("ConstantConditions")
    public void onClose() { minecraft.player.closeScreen(); }

    @Override @SuppressWarnings("ConstantConditions")
    public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers)
    {
        if (keyCode == 256 || minecraft.gameSettings.keyBindInventory.matchesKey(keyCode, scanCode)) { onClose(); return true; }
        return super.keyPressed(keyCode, scanCode, modifiers);
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

        public void render() { blit(X, Y, TEXTURE_X, TEXTURE_Y, WIDTH, HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT); }
    }
}