package ninjaphenix.expandedstorage.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.ModList;
import ninjaphenix.expandedstorage.ExpandedStorage;
import ninjaphenix.expandedstorage.common.inventory.PagedContainer;
import ninjaphenix.expandedstorage.common.screen.PagedScreenMeta;
import java.util.Collections;
import java.util.List;

public final class PagedScreen extends AbstractScreen<PagedContainer, PagedScreenMeta>
{
    private Rectangle blankArea;
    private PageButtonWidget leftPageButton, rightPageButton;
    private int page;
    private TranslationTextComponent currentPageText;
    private float pageTextX;
    private ScreenTypeSelectionScreenButton screenSelectButton;

    public PagedScreen(final PagedContainer container, final PlayerInventory playerInventory, final ITextComponent title)
    {
        super(container, playerInventory, title, (screenMeta) -> (screenMeta.WIDTH * 18 + 14) / 2 - 80);
        imageWidth = 14 + 18 * SCREEN_META.WIDTH;
        imageHeight = 17 + 97 + 18 * SCREEN_META.HEIGHT;
    }

    private void setPage(final int oldPage, final int newPage)
    {
        page = newPage;
        if (newPage > oldPage)
        {
            if (page == SCREEN_META.PAGES)
            {
                rightPageButton.setActive(false);
                final int blanked = SCREEN_META.BLANK_SLOTS;
                if (blanked > 0)
                {
                    final int xOffset = 7 + (SCREEN_META.WIDTH - blanked) * 18;
                    blankArea = new Rectangle(leftPos + xOffset, topPos + imageHeight - 115, blanked * 18, 18, xOffset, imageHeight,
                                              SCREEN_META.TEXTURE_WIDTH, SCREEN_META.TEXTURE_HEIGHT);
                }
            }
            if (!leftPageButton.active) { leftPageButton.setActive(true); }
        }
        else if (newPage < oldPage)
        {
            if (page == 1) { leftPageButton.setActive(false); }
            if (blankArea != null) {blankArea = null; }
            if (!rightPageButton.active) { rightPageButton.setActive(true); }
        }
        final int slotsPerPage = SCREEN_META.WIDTH * SCREEN_META.HEIGHT;
        final int oldMin = slotsPerPage * (oldPage - 1);
        final int oldMax = Math.min(oldMin + slotsPerPage, SCREEN_META.TOTAL_SLOTS);
        menu.moveSlotRange(oldMin, oldMax, -2000);
        final int newMin = slotsPerPage * (newPage - 1);
        final int newMax = Math.min(newMin + slotsPerPage, SCREEN_META.TOTAL_SLOTS);
        menu.moveSlotRange(newMin, newMax, 2000);
        setPageText();
    }

    private void setPageText() { currentPageText = new TranslationTextComponent("screen.expandedstorage.page_x_y", page, SCREEN_META.PAGES); }

    @Override
    public void render(final MatrixStack stack, final int mouseX, final int mouseY, final float partialTicks)
    {
        super.render(stack, mouseX, mouseY, partialTicks);
        if (SCREEN_META.PAGES != 1)
        {
            leftPageButton.renderTooltip(stack, mouseX, mouseY);
            rightPageButton.renderTooltip(stack, mouseX, mouseY);
        }
        screenSelectButton.renderTooltip(stack, mouseX, mouseY);
    }

    public List<Rectangle2d> getJeiRectangles() { return Collections.singletonList(new Rectangle2d(leftPos + imageWidth + 4, topPos, 22, 22)); }

    @Override
    protected void init()
    {
        super.init();
        final int settingsButtonX = leftPos + imageWidth + 4;
        screenSelectButton = addButton(new ScreenTypeSelectionScreenButton(settingsButtonX, topPos, this::renderButtonTooltip));
        if (SCREEN_META.PAGES != 1)
        {
            final int pageButtonsXOffset = ModList.get().isLoaded("quark") ? 36 : 0;
            page = 1;
            setPageText();
            leftPageButton = new PageButtonWidget(leftPos + imageWidth - 61 - pageButtonsXOffset, topPos + imageHeight - 96, 0,
                                                  new TranslationTextComponent("screen.expandedstorage.prev_page"),
                                                  button -> setPage(page, page - 1), this::renderButtonTooltip);
            leftPageButton.setActive(false);
            addButton(leftPageButton);
            rightPageButton = new PageButtonWidget(leftPos + imageWidth - 19 - pageButtonsXOffset, topPos + imageHeight - 96, 1,
                                                   new TranslationTextComponent("screen.expandedstorage.next_page"),
                                                   button -> setPage(page, page + 1), this::renderButtonTooltip);
            addButton(rightPageButton);
            pageTextX = (1 + leftPageButton.x + rightPageButton.x - rightPageButton.getWidth() / 2F) / 2F;
        }
    }

    @Override
    protected void renderBg(final MatrixStack stack, final float partialTicks, final int mouseX, final int mouseY)
    {
        super.renderBg(stack, partialTicks, mouseX, mouseY);
        if (blankArea != null) { blankArea.render(stack); }
    }

    @Override
    public void resize(final Minecraft minecraft, final int width, final int height)
    {
        if (SCREEN_META.PAGES != 1)
        {
            final int currentPage = page;
            if (currentPage != 1)
            {
                menu.resetSlotPositions(false);
                super.resize(minecraft, width, height);
                setPage(1, currentPage);
                return;
            }
        }
        super.resize(minecraft, width, height);
    }

    @Override
    protected void renderLabels(final MatrixStack stack, final int mouseX, final int mouseY)
    {
        super.renderLabels(stack, mouseX, mouseY);
        if (currentPageText != null) { font.draw(stack, currentPageText, pageTextX - leftPos, imageHeight - 94, 0x404040); }
    }

    @Override
    public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers)
    {
        if (keyCode == 262 || keyCode == 267) // Right Arrow, Page Down
        {
            if (SCREEN_META.PAGES != 1)
            {
                if (hasShiftDown()) { setPage(page, SCREEN_META.PAGES); }
                else { if (page != SCREEN_META.PAGES) { setPage(page, page + 1); } }
                return true;
            }
        }
        else if (keyCode == 263 || keyCode == 266) // Left Arrow, Page Up
        {
            if (SCREEN_META.PAGES != 1)
            {
                if (hasShiftDown()) { setPage(page, 1); }
                else { if (page != 1) { setPage(page, page - 1); } }
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private static class PageButtonWidget extends Button
    {
        private final int TEXTURE_OFFSET;
        private final ResourceLocation TEXTURE = ExpandedStorage.getRl("textures/gui/page_buttons.png");

        public PageButtonWidget(final int x, final int y, final int textureOffset, final ITextComponent message, final IPressable onPress,
                                final ITooltip onTooltip)
        {
            super(x, y, 12, 12, message, onPress, onTooltip);
            TEXTURE_OFFSET = textureOffset;
        }

        public void setActive(final boolean active)
        {
            this.active = active;
            if (!active) { setFocused(false); }
        }

        @Override
        @SuppressWarnings("deprecation")
        public void renderButton(final MatrixStack stack, final int mouseX, final int mouseY, final float partialTicks)
        {
            Minecraft.getInstance().getTextureManager().bind(TEXTURE);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, alpha);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            blit(stack, x, y, TEXTURE_OFFSET * 12, getYImage(isHovered()) * 12, width, height, 32, 48);
        }

        public void renderTooltip(final MatrixStack stack, final int mouseX, final int mouseY)
        {
            if (active)
            {
                if (isHovered) { onTooltip.onTooltip(this, stack, mouseX, mouseY); }
                else if (isHovered()) { onTooltip.onTooltip(this, stack, x, y); }
            }
        }
    }
}