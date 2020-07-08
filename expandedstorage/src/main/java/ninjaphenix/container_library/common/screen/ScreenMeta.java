package ninjaphenix.container_library.common.screen;

import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public abstract class ScreenMeta
{
    public final int WIDTH, HEIGHT, TOTAL_SLOTS, TEXTURE_WIDTH, TEXTURE_HEIGHT;
    public final ResourceLocation TEXTURE;

    protected ScreenMeta(final int width, final int height, final int totalSlots, @NotNull final ResourceLocation texture, final int textureWidth,
            final int textureHeight)
    { WIDTH = width; HEIGHT = height; TOTAL_SLOTS = totalSlots; TEXTURE = texture; TEXTURE_WIDTH = textureWidth; TEXTURE_HEIGHT = textureHeight; }
}