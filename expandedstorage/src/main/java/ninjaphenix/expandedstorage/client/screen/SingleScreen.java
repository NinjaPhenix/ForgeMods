package ninjaphenix.expandedstorage.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import ninjaphenix.expandedstorage.common.inventory.SingleContainer;
import ninjaphenix.expandedstorage.common.screen.SingleScreenMeta;
import java.util.Collections;
import java.util.List;

public final class SingleScreen extends AbstractScreen<SingleContainer, SingleScreenMeta>
{
    private Rectangle blankArea;
    private ScreenTypeSelectionScreenButton screenSelectButton;

    public SingleScreen(final SingleContainer container, final PlayerInventory playerInventory, final ITextComponent title)
    {
        super(container, playerInventory, title, (screenMeta) -> (screenMeta.WIDTH * 18 + 14) / 2 - 80);
        xSize = 14 + 18 * SCREEN_META.WIDTH;
        ySize = 17 + 97 + 18 * SCREEN_META.HEIGHT;
    }

    public List<Rectangle2d> getJeiRectangles()
    {
        return Collections.singletonList(new Rectangle2d(guiLeft + xSize + 4, guiTop, 22, 22));
    }

    @Override
    protected void init()
    {
        super.init();
        final int settingsButtonX = guiLeft + xSize + 4;
        screenSelectButton = addButton(new ScreenTypeSelectionScreenButton(settingsButtonX, guiTop, this::renderButtonTooltip));
        final int blanked = SCREEN_META.BLANK_SLOTS;
        if (blanked > 0)
        {
            final int xOffset = 7 + (SCREEN_META.WIDTH - blanked) * 18;
            blankArea = new Rectangle(guiLeft + xOffset, guiTop + ySize - 115, blanked * 18, 18, xOffset, ySize, SCREEN_META.TEXTURE_WIDTH,
                    SCREEN_META.TEXTURE_HEIGHT);
        }
    }

    @Override
    public void render(final MatrixStack stack, final int mouseX, final int mouseY, final float partialTicks)
    {
        super.render(stack, mouseX, mouseY, partialTicks);
        screenSelectButton.renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final MatrixStack stack, final float partialTicks, final int mouseX, final int mouseY)
    {
        super.drawGuiContainerBackgroundLayer(stack, partialTicks, mouseX, mouseY);
        if (blankArea != null) { blankArea.render(stack); }
    }
}