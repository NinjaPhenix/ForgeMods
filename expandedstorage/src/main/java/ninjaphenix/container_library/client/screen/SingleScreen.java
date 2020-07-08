package ninjaphenix.container_library.client.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import ninjaphenix.container_library.common.inventory.SingleContainer;
import ninjaphenix.container_library.common.screen.SingleScreenMeta;
import org.jetbrains.annotations.NotNull;

public class SingleScreen extends AbstractScreen<SingleContainer, SingleScreenMeta>
{
    private Rectangle blankArea = null;
    private ScreenTypeSelectionScreenButton screenSelectButton;

    public SingleScreen(@NotNull final SingleContainer container, @NotNull final PlayerInventory playerInventory, @NotNull final ITextComponent title)
    {
        super(container, playerInventory, title, (screenMeta) -> (screenMeta.WIDTH * 18 + 14) / 2 - 80);
        xSize = 14 + 18 * SCREEN_META.WIDTH; ySize = 17 + 97 + 18 * SCREEN_META.HEIGHT;
    }

    @Override
    protected void init()
    {
        super.init();
        screenSelectButton = addButton(new ScreenTypeSelectionScreenButton(guiLeft + xSize - 19, guiTop + 4));
        final int blanked = SCREEN_META.BLANK_SLOTS;
        if (blanked > 0)
        {
            final int xOffset = 7 + (SCREEN_META.WIDTH - blanked) * 18;
            blankArea = new Rectangle(guiLeft + xOffset, guiTop + ySize - 115, blanked * 18, 18, xOffset, ySize, SCREEN_META.TEXTURE_WIDTH,
                    SCREEN_META.TEXTURE_HEIGHT);
        }
    }

    @Override
    public void render(final int mouseX, final int mouseY, final float partialTicks)
    { super.render(mouseX, mouseY, partialTicks); screenSelectButton.renderTooltip(mouseX, mouseY, this::renderTooltip); }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY)
    { super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY); if (blankArea != null) { blankArea.render(); } }
}