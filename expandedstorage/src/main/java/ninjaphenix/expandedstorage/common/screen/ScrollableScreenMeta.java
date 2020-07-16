package ninjaphenix.expandedstorage.common.screen;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

public class ScrollableScreenMeta extends ScreenMeta
{
    public final int BLANK_SLOTS, TOTAL_ROWS;

    public ScrollableScreenMeta(final int width, final int height, final int totalSlots, @NotNull final ResourceLocation texture, final int textureWidth,
            final int textureHeight)
    {
        super(width, height, totalSlots, texture, textureWidth, textureHeight);
        TOTAL_ROWS = MathHelper.ceil((double) totalSlots / width);
        BLANK_SLOTS = TOTAL_ROWS * width - totalSlots;
    }
}
