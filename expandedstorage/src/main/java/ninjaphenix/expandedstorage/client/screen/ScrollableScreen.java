package ninjaphenix.expandedstorage.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.ModList;
import ninjaphenix.expandedstorage.common.ExpandedStorageConfig;
import ninjaphenix.expandedstorage.common.inventory.ScrollableContainer;
import ninjaphenix.expandedstorage.common.screen.ScrollableScreenMeta;

import java.util.Collections;
import java.util.List;

public final class ScrollableScreen extends AbstractScreen<ScrollableContainer, ScrollableScreenMeta>
{
    protected final boolean hasScrollbar;
    private Rectangle blankArea = null;
    private boolean isDragging;
    private int topRow;
    private ScreenTypeSelectionScreenButton screenSelectButton;

    public ScrollableScreen(final ScrollableContainer container, final PlayerInventory playerInventory, final ITextComponent title)
    {
        super(container, playerInventory, title, (screenMeta) -> (screenMeta.WIDTH * 18 + 14) / 2 - 80);
        xSize = 14 + 18 * SCREEN_META.WIDTH;
        ySize = 17 + 97 + 18 * SCREEN_META.HEIGHT;
        hasScrollbar = SCREEN_META.TOTAL_ROWS != SCREEN_META.HEIGHT;
    }

    public List<Rectangle2d> getJeiRectangle()
    {
        if (!hasScrollbar) { return Collections.emptyList(); }
        final int height = SCREEN_META.HEIGHT * 18 + (SCREEN_META.WIDTH > 9 ? 34 : 24);
        return Collections.singletonList(new Rectangle2d(guiLeft + xSize - 4, guiTop, 22, height));
    }

    @Override
    protected void init()
    {
        super.init();
        int settingsXOffset = -(hasScrollbar ? (ExpandedStorageConfig.CLIENT.centerSettingsButtonOnScrollbar.get() ? 2 : 1) : ModList.get().isLoaded("quark") ?
                43 : 19);
        screenSelectButton = addButton(new ScreenTypeSelectionScreenButton(guiLeft + xSize + settingsXOffset, guiTop + 4, this::renderButtonTooltip));
        if (hasScrollbar)
        {
            isDragging = false;
            topRow = 0;
        }
        else
        {
            final int blanked = SCREEN_META.BLANK_SLOTS;
            if (blanked > 0)
            {
                final int xOffset = 7 + (SCREEN_META.WIDTH - blanked) * 18;
                blankArea = new Rectangle(guiLeft + xOffset, guiTop + ySize - 115, blanked * 18, 18, xOffset, ySize, SCREEN_META.TEXTURE_WIDTH,
                        SCREEN_META.TEXTURE_HEIGHT);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final MatrixStack stack, final float partialTicks, final int mouseX, final int mouseY)
    {
        super.drawGuiContainerBackgroundLayer(stack, partialTicks, mouseX, mouseY);
        if (hasScrollbar)
        {
            final int slotsHeight = SCREEN_META.HEIGHT * 18;
            final int scrollbarHeight = slotsHeight + (SCREEN_META.WIDTH > 9 ? 34 : 24);
            blit(stack, guiLeft + xSize - 4, guiTop, xSize, 0, 22, scrollbarHeight, SCREEN_META.TEXTURE_WIDTH, SCREEN_META.TEXTURE_HEIGHT);
            int yOffset = MathHelper.floor((slotsHeight - 17) * (((double) topRow) / (SCREEN_META.TOTAL_ROWS - SCREEN_META.HEIGHT)));
            blit(stack, guiLeft + xSize - 2, guiTop + yOffset + 18, xSize, scrollbarHeight, 12, 15, SCREEN_META.TEXTURE_WIDTH, SCREEN_META.TEXTURE_HEIGHT);
        }
        if (blankArea != null) { blankArea.render(stack); }
    }

    @Override
    public void render(final MatrixStack stack, final int mouseX, final int mouseY, final float partialTicks)
    {
        super.render(stack, mouseX, mouseY, partialTicks);
        screenSelectButton.renderTooltip(stack, mouseX, mouseY);
    }

    private boolean isMouseOverScrollbar(final double mouseX, final double mouseY)
    {
        final int top = guiTop + 18;
        final int left = guiLeft + xSize - 2;
        return mouseX >= left && mouseY >= top && mouseX < left + 12 && mouseY < top + SCREEN_META.HEIGHT * 18;
    }

    @Override
    protected boolean hasClickedOutside(final double mouseX, final double mouseY, final int left, final int top, final int button)
    { return super.hasClickedOutside(mouseX, mouseY, left, top, button) && !isMouseOverScrollbar(mouseX, mouseY); }

    @Override
    public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers)
    {
        if (hasScrollbar)
        {
            if (keyCode == 264 || keyCode == 267) // Down Arrow, Page Down
            {
                if (topRow != SCREEN_META.TOTAL_ROWS - SCREEN_META.HEIGHT)
                {
                    if (hasShiftDown()) { setTopRow(topRow, Math.min(topRow + SCREEN_META.HEIGHT, SCREEN_META.TOTAL_ROWS - SCREEN_META.HEIGHT)); }
                    else {setTopRow(topRow, topRow + 1);}
                }
                return true;
            }
            else if (keyCode == 265 || keyCode == 266) // Up Arrow, Page Up
            {
                if (topRow != 0)
                {
                    if (hasShiftDown()) { setTopRow(topRow, Math.max(topRow - SCREEN_META.HEIGHT, 0)); }
                    else {setTopRow(topRow, topRow - 1);}
                }
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(final double mouseX, final double mouseY, final int button)
    {
        if (hasScrollbar && isMouseOverScrollbar(mouseX, mouseY) && button == 0)
        {
            isDragging = true;
            updateTopRow(mouseY);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(final double mouseX, final double mouseY, final int button, final double deltaX, final double deltaY)
    {
        if (hasScrollbar && isDragging) { updateTopRow(mouseY); }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    private void updateTopRow(final double mouseY)
    {
        int top = guiTop + 18;
        int height = SCREEN_META.HEIGHT * 18;
        int newTopRow = MathHelper.floor(MathHelper.clampedLerp(0, SCREEN_META.TOTAL_ROWS - SCREEN_META.HEIGHT, (mouseY - top) / height));
        setTopRow(topRow, newTopRow);
    }

    @Override
    public boolean mouseScrolled(final double mouseX, final double mouseY, final double delta)
    {
        if (hasScrollbar && (!ExpandedStorageConfig.CLIENT.restrictiveScrolling.get() || isMouseOverScrollbar(mouseX, mouseY)))
        {
            int newTop;
            if (delta < 0) { newTop = Math.min(topRow + (hasShiftDown() ? SCREEN_META.HEIGHT : 1), SCREEN_META.TOTAL_ROWS - SCREEN_META.HEIGHT); }
            else { newTop = Math.max(topRow - (hasShiftDown() ? SCREEN_META.HEIGHT : 1), 0); }
            setTopRow(topRow, newTop);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void setTopRow(final int oldTopRow, final int newTopRow)
    {
        if (oldTopRow == newTopRow) { return; }
        topRow = newTopRow;
        final int delta = newTopRow - oldTopRow;
        final int rows = Math.abs(delta);
        if (rows < SCREEN_META.HEIGHT)
        {
            final int setAmount = rows * SCREEN_META.WIDTH;
            final int movableAmount = (SCREEN_META.HEIGHT - rows) * SCREEN_META.WIDTH;
            if (delta > 0)
            {
                final int setOutBegin = oldTopRow * SCREEN_META.WIDTH;
                final int movableBegin = newTopRow * SCREEN_META.WIDTH;
                final int setInBegin = movableBegin + movableAmount;
                container.setSlotRange(setOutBegin, setOutBegin + setAmount, index -> -2000);
                container.moveSlotRange(movableBegin, setInBegin, -18 * rows);
                container.setSlotRange(setInBegin, Math.min(setInBegin + setAmount, SCREEN_META.TOTAL_SLOTS), index -> 18 *
                        MathHelper.intFloorDiv(index - movableBegin + SCREEN_META.WIDTH, SCREEN_META.WIDTH));
            }
            else
            {
                final int setInBegin = newTopRow * SCREEN_META.WIDTH;
                final int movableBegin = oldTopRow * SCREEN_META.WIDTH;
                final int setOutBegin = movableBegin + movableAmount;
                container.setSlotRange(setInBegin, setInBegin + setAmount, index -> 18 * MathHelper.intFloorDiv(index - setInBegin + SCREEN_META.WIDTH,
                        SCREEN_META.WIDTH));
                container.moveSlotRange(movableBegin, setOutBegin, 18 * rows);
                container.setSlotRange(setOutBegin, Math.min(setOutBegin + setAmount, SCREEN_META.TOTAL_SLOTS), index -> -2000);
            }
        }
        else
        {
            final int oldMin = oldTopRow * SCREEN_META.WIDTH;
            container.setSlotRange(oldMin, Math.min(oldMin + SCREEN_META.WIDTH * SCREEN_META.HEIGHT, SCREEN_META.TOTAL_SLOTS), index -> -2000);
            final int newMin = newTopRow * SCREEN_META.WIDTH;
            container.setSlotRange(newMin, newMin + SCREEN_META.WIDTH * SCREEN_META.HEIGHT, index -> 18 + 18 *
                    MathHelper.intFloorDiv(index - newMin, SCREEN_META.WIDTH));
        }

        if (newTopRow == SCREEN_META.TOTAL_ROWS - SCREEN_META.HEIGHT)
        {
            int blanked = SCREEN_META.BLANK_SLOTS;
            if (blanked > 0)
            {
                final int xOffset = 7 + (SCREEN_META.WIDTH - blanked) * 18;
                blankArea = new Rectangle(guiLeft + xOffset, guiTop + ySize - 115, blanked * 18, 18, xOffset, ySize, SCREEN_META.TEXTURE_WIDTH,
                        SCREEN_META.TEXTURE_HEIGHT);
            }
        }
        else { blankArea = null; }
    }

    @Override
    public void resize(final Minecraft minecraft, final int width, final int height)
    {
        super.resize(minecraft, width, height);
        if (hasScrollbar)
        {
            int row = topRow;
            super.resize(minecraft, width, height);
            setTopRow(topRow, row);
        }
        else { super.resize(minecraft, width, height); }
    }

    @Override
    public boolean mouseReleased(final double mouseX, final double mouseY, final int button)
    {
        if (hasScrollbar && isDragging)
        {
            isDragging = false;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }
}