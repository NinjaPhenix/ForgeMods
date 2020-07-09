package ninjaphenix.expandedstorage.client.screen;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import ninjaphenix.expandedstorage.ExpandedStorage;
import ninjaphenix.expandedstorage.common.ExpandedStorageConfig;
import ninjaphenix.expandedstorage.common.network.Networker;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class SelectContainerScreen extends Screen
{
    private static final ImmutableMap<ResourceLocation, Pair<ResourceLocation, ITextComponent>> OPTIONS = new
            ImmutableMap.Builder<ResourceLocation, Pair<ResourceLocation, ITextComponent>>()
            .put(ExpandedStorage.getRl("single"), new Pair<>(ExpandedStorage.getRl("textures/gui/single_button.png"),
                    new TranslationTextComponent("screen.expandedstorage.single_screen")))
            .put(ExpandedStorage.getRl("scrollable"), new Pair<>(ExpandedStorage.getRl("textures/gui/scrollable_button.png"),
                    new TranslationTextComponent("screen.expandedstorage.scrollable_screen")))
            .put(ExpandedStorage.getRl("paged"), new Pair<>(ExpandedStorage.getRl("textures/gui/paged_button.png"),
                    new TranslationTextComponent("screen.expandedstorage.paged_screen")))
            .build();
    private final int PADDING = 24;
    private int TOP;

    public SelectContainerScreen() { super(new TranslationTextComponent("screen.expandedstorage.screen_picker_title")); }

    @Override
    protected void init()
    {
        super.init();
        final int choices = OPTIONS.size();
        final int maxColumns = Math.min(MathHelper.intFloorDiv(width - PADDING, 96 + PADDING), choices);
        final int totalRows = MathHelper.ceil((double) choices / maxColumns);
        int x = 0; int y = 0;
        int leftPadding = MathHelper.ceil((width - 96 * maxColumns - PADDING * (maxColumns - 1)) / 2D);
        TOP = MathHelper.ceil((height - 96 * totalRows - PADDING * (totalRows - 1)) / 2D);
        for (@NotNull final HashMap.Entry<ResourceLocation, Pair<ResourceLocation, ITextComponent>> entry : OPTIONS.entrySet())
        {
            final ResourceLocation id = entry.getKey();
            final Pair<ResourceLocation, ITextComponent> settings = entry.getValue();
            addButton(new ScreenTypeButton(leftPadding + (PADDING + 96) * x++, TOP + (PADDING + 96) * y, 96, 96, settings.getFirst(),
                    settings.getSecond().getFormattedText(), button -> updatePlayerPreference(id)));
            if (x == maxColumns)
            {
                x = 0;
                if (y++ == totalRows - 1)
                {
                    final int remaining = choices - (maxColumns * (totalRows - 1));
                    leftPadding = MathHelper.ceil((width - 96 * remaining - PADDING * (remaining - 1)) / 2D);
                }
            }
        }
    }

    @Override
    public void onClose()
    {
        Networker.INSTANCE.sendRemovePreferenceCallbackToServer();
        super.onClose();
    }

    private void updatePlayerPreference(@NotNull final ResourceLocation selection)
    {
        ExpandedStorageConfig.CLIENT.preferredContainerType.set(selection.toString());
        Networker.INSTANCE.sendPreferenceToServer();
        super.onClose();
    }

    @Override
    public boolean shouldCloseOnEsc() { return false; }

    @Override
    public boolean isPauseScreen() { return false; }

    @Override
    public void render(final int mouseX, final int mouseY, final float partialTicks)
    {
        setBlitOffset(0);
        renderBackground();
        buttons.forEach(button -> button.render(mouseX, mouseY, partialTicks));
        buttons.stream().filter(w -> w instanceof ScreenTypeButton).map(w -> (ScreenTypeButton) w)
               .forEachOrdered(w -> w.renderTooltip(mouseX, mouseY, partialTicks));
        drawCenteredString(font, title.getFormattedText(), width / 2, Math.max(TOP - 2 * PADDING, 0), 0xFFFFFFFF);
    }

    private class ScreenTypeButton extends Button
    {
        private final ResourceLocation TEXTURE;

        public ScreenTypeButton(final int x, final int y, final int width, final int height, @NotNull final ResourceLocation texture,
                @NotNull final String message, @NotNull final Button.IPressable press)
        { super(x, y, width, height, message, press); TEXTURE = texture; }

        @Override
        public void renderButton(final int mouseX, final int mouseY, final float partialTicks)
        {
            Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
            blit(x, y, 0, isHovered() ? height : 0, width, height, width, height * 2);
            if (isHovered()) { renderToolTip(x, y); }
        }

        public void renderTooltip(final int mouseX, final int mouseY, final float partialTicks)
        {
            if (active)
            {
                if (isHovered) { SelectContainerScreen.this.renderTooltip(getMessage(), mouseX, mouseY); }
                else if (isHovered()) { SelectContainerScreen.this.renderTooltip(getMessage(), x, y); }
            }
        }
    }
}