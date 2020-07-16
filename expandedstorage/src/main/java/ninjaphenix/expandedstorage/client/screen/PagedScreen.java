package ninjaphenix.expandedstorage.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.ModList;
import ninjaphenix.expandedstorage.ExpandedStorage;
import ninjaphenix.expandedstorage.common.inventory.PagedContainer;
import ninjaphenix.expandedstorage.common.screen.PagedScreenMeta;
import org.jetbrains.annotations.NotNull;

public class PagedScreen extends AbstractScreen<PagedContainer, PagedScreenMeta>
{
    private Rectangle blankArea = null;
    private PageButtonWidget leftPageButton, rightPageButton;
    private int page;
    private TranslationTextComponent currentPageText;
    private float pageTextX;
    private ScreenTypeSelectionScreenButton screenSelectButton;

    public PagedScreen(@NotNull final PagedContainer container, @NotNull final PlayerInventory playerInventory, @NotNull final ITextComponent title)
    {
        super(container, playerInventory, title, (screenMeta) -> (screenMeta.WIDTH * 18 + 14) / 2 - 80);
        xSize = 14 + 18 * SCREEN_META.WIDTH;
        ySize = 17 + 97 + 18 * SCREEN_META.HEIGHT;
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
                    blankArea = new Rectangle(guiLeft + xOffset, guiTop + ySize - 115, blanked * 18, 18, xOffset, ySize, SCREEN_META.TEXTURE_WIDTH,
                            SCREEN_META.TEXTURE_HEIGHT);
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
        int oldMin = slotsPerPage * (oldPage - 1);
        int oldMax = Math.min(oldMin + slotsPerPage, SCREEN_META.TOTAL_SLOTS);
        container.moveSlotRange(oldMin, oldMax, -2000);
        int newMin = slotsPerPage * (newPage - 1);
        int newMax = Math.min(newMin + slotsPerPage, SCREEN_META.TOTAL_SLOTS);
        container.moveSlotRange(newMin, newMax, 2000);
        setPageText();
    }

    private void setPageText() { currentPageText = new TranslationTextComponent("screen.expandedstorage.page_x_y", page, SCREEN_META.PAGES); }

    @Override
    public void render(@NotNull final MatrixStack stack, final int mouseX, final int mouseY, final float partialTicks)
    {
        super.render(stack, mouseX, mouseY, partialTicks);
        if (SCREEN_META.PAGES != 1)
        {
            leftPageButton.renderTooltip(stack, mouseX, mouseY);
            rightPageButton.renderTooltip(stack, mouseX, mouseY);
        }
        screenSelectButton.renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void init()
    {
        super.init();
        int settingsXOffset = -19;
        final boolean isQuarkLoaded = ModList.get().isLoaded("quark");
        if (isQuarkLoaded && SCREEN_META.WIDTH <= 9) { settingsXOffset -= 24; }
        screenSelectButton = addButton(new ScreenTypeSelectionScreenButton(guiLeft + xSize + settingsXOffset, guiTop + 4, this::renderButtonTooltip));
        if (SCREEN_META.PAGES != 1)
        {
            final int pageButtonsXOffset = isQuarkLoaded ? 36 : 0;
            page = 1;
            setPageText();
            leftPageButton = new PageButtonWidget(guiLeft + xSize - 61 - pageButtonsXOffset, guiTop + ySize - 96, 0,
                    new TranslationTextComponent("screen.expandedstorage.prev_page"), button -> setPage(page, page - 1), this::renderButtonTooltip);
            leftPageButton.setActive(false);
            addButton(leftPageButton);
            rightPageButton = new PageButtonWidget(guiLeft + xSize - 19 - pageButtonsXOffset, guiTop + ySize - 96, 1,
                    new TranslationTextComponent("screen.expandedstorage.next_page"), button -> setPage(page, page + 1), this::renderButtonTooltip);
            addButton(rightPageButton);
            pageTextX = (1 + leftPageButton.x + rightPageButton.x - rightPageButton.getWidth() / 2F) / 2F;
        }
    }

    @Override
    protected void func_230450_a_(@NotNull final MatrixStack stack, final float partialTicks, final int mouseX, final int mouseY)
    {
        super.func_230450_a_(stack, partialTicks, mouseX, mouseY);
        if (blankArea != null) { blankArea.render(stack); }
    }

    @Override
    public void resize(@NotNull final Minecraft minecraft, final int width, final int height)
    {
        if (SCREEN_META.PAGES != 1)
        {
            int currentPage = page;
            if (currentPage != 1)
            {
                container.resetSlotPositions(false);
                super.resize(minecraft, width, height);
                setPage(1, currentPage);
                return;
            }
        }
        super.resize(minecraft, width, height);
    }

    @Override
    protected void func_230451_b_(@NotNull final MatrixStack stack, final int mouseX, final int mouseY)
    {
        super.func_230451_b_(stack, mouseX, mouseY);
        if (currentPageText != null) { font.func_238422_b_(stack, currentPageText, pageTextX - guiLeft, ySize - 94, 0x404040); }
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

        public PageButtonWidget(final int x, final int y, final int textureOffset, @NotNull final ITextComponent message, @NotNull final IPressable onPress,
                @NotNull final ITooltip onTooltip)
        {
            super(x, y, 12, 12, message, onPress, onTooltip);
            TEXTURE_OFFSET = textureOffset;
        }

        public void setActive(final boolean active)
        {
            this.active = active;
            if (!active) { this.setFocused(false); }
        }

        @Override
        public void renderButton(@NotNull final MatrixStack stack, final int mouseX, final int mouseY, final float partialTicks)
        {
            Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            blit(stack, x, y, TEXTURE_OFFSET * 12, getYImage(isHovered()) * 12, width, height, 32, 48);
        }

        public void renderTooltip(@NotNull final MatrixStack stack, final int mouseX, final int mouseY)
        {
            if (active)
            {
                if (isHovered) { field_238487_u_.onTooltip(this, stack, mouseX, mouseY); }
                else if (isHovered()) { field_238487_u_.onTooltip(this, stack, x, y); }
            }
        }
    }
}