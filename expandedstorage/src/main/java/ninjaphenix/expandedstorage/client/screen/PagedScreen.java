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
        xSize = 14 + 18 * SCREEN_META.WIDTH; ySize = 17 + 97 + 18 * SCREEN_META.HEIGHT;
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
            if (!leftPageButton.field_230693_o_) { leftPageButton.setActive(true); }
        }
        else if(newPage < oldPage)
        {
            if (page == 1) { leftPageButton.setActive(false); }
            if (blankArea != null) {blankArea = null; }
            if (!rightPageButton.field_230693_o_) { rightPageButton.setActive(true); }
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
    public void func_230430_a_(@NotNull final MatrixStack stack, final int mouseX, final int mouseY, final float partialTicks)
    {
        super.func_230430_a_(stack, mouseX, mouseY, partialTicks);
        if(SCREEN_META.PAGES != 1) { leftPageButton.renderTooltip(stack, mouseX, mouseY); rightPageButton.renderTooltip(stack, mouseX, mouseY); }
        screenSelectButton.renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void func_231160_c_()
    {
        super.func_231160_c_();
        int settingsXOffset = -19;
        final boolean isQuarkLoaded = ModList.get().isLoaded("quark");
        if(isQuarkLoaded && SCREEN_META.WIDTH <= 9) { settingsXOffset -= 24; }
        screenSelectButton = func_230480_a_(new ScreenTypeSelectionScreenButton(guiLeft + xSize + settingsXOffset, guiTop + 4,
                (button, stack, x, y) -> func_238652_a_(stack, button.func_230458_i_(), x, y)));
        if (SCREEN_META.PAGES != 1)
        {
            final int pageButtonsXOffset = isQuarkLoaded ? 36 : 0;
            page = 1;
            setPageText();
            leftPageButton = new PageButtonWidget(guiLeft + xSize - 61 - pageButtonsXOffset, guiTop + ySize - 96, 0,
                    new TranslationTextComponent("screen.expandedstorage.prev_page"), button -> setPage(page, page - 1),
                    (button, stack, x, y) -> func_238652_a_(stack, button.func_230458_i_(), x, y));
            leftPageButton.setActive(false);
            func_230480_a_(leftPageButton);
            rightPageButton = new PageButtonWidget(guiLeft + xSize - 19 - pageButtonsXOffset, guiTop + ySize - 96, 1,
                    new TranslationTextComponent("screen.expandedstorage.next_page"), button -> setPage(page, page + 1),
                    (button, stack, x, y) -> func_238652_a_(stack, button.func_230458_i_(), x, y));
            func_230480_a_(rightPageButton);
            pageTextX = (1 + leftPageButton.field_230690_l_ + rightPageButton.field_230690_l_ - rightPageButton.func_230998_h_() / 2F) / 2F;
        }
    }

    @Override
    protected void func_230450_a_(@NotNull final MatrixStack stack, final float partialTicks, final int mouseX, final int mouseY)
    {
        super.func_230450_a_(stack, partialTicks, mouseX, mouseY);
        if (blankArea != null) { blankArea.render(stack); }
    }

    @Override
    public void func_231152_a_(@NotNull final Minecraft minecraft, final int width, final int height)
    {
        if (SCREEN_META.PAGES != 1)
        {
            int currentPage = page;
            if (currentPage != 1)
            {
                container.resetSlotPositions(false);
                super.func_231152_a_(minecraft, width, height);
                setPage(1, currentPage);
                return;
            }
        }
        super.func_231152_a_(minecraft, width, height);
    }

    @Override
    protected void func_230451_b_(@NotNull final MatrixStack stack, final int mouseX, final int mouseY)
    {
        super.func_230451_b_(stack, mouseX, mouseY);
        if (currentPageText != null) { field_230712_o_.func_238422_b_(stack, currentPageText, pageTextX - guiLeft, ySize - 94, 0x404040); }
    }

    @Override
    public boolean func_231046_a_(final int keyCode, final int scanCode, final int modifiers)
    {
        if (keyCode == 262 || keyCode == 267) // Right Arrow, Page Down
        {
            if (SCREEN_META.PAGES != 1)
            {
                if (func_231173_s_()) { setPage(page, SCREEN_META.PAGES); }
                else { if (page != SCREEN_META.PAGES) { setPage(page, page + 1); } }
                return true;
            }
        }
        else if (keyCode == 263 || keyCode == 266) // Left Arrow, Page Up
        {
            if (SCREEN_META.PAGES != 1)
            {
                if (func_231173_s_()) { setPage(page, 1); }
                else { if (page != 1) { setPage(page, page - 1); } }
                return true;
            }
        }
        return super.func_231046_a_(keyCode, scanCode, modifiers);
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
            this.field_230693_o_ = active;
            if (!active) { this.func_230996_d_(false); }
        }

        @Override
        public void func_230431_b_(@NotNull final MatrixStack stack, final int mouseX, final int mouseY, final float partialTicks)
        {
            Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.field_230695_q_);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            func_238463_a_(stack, field_230690_l_, field_230691_m_, TEXTURE_OFFSET * 12, func_230989_a_(func_230449_g_()) * 12, field_230688_j_, field_230689_k_, 32, 48);
        }

        public void renderTooltip(@NotNull final MatrixStack stack, final int mouseX, final int mouseY)
        {
            if (field_230693_o_)
            {
                if (field_230692_n_) { field_238487_u_.onTooltip(this, stack, mouseX, mouseY); }
                else if (func_230449_g_()) { field_238487_u_.onTooltip(this, stack, field_230690_l_, field_230691_m_); }
            }
        }
    }
}