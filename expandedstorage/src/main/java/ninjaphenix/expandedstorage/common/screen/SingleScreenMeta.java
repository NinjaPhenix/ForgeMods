package ninjaphenix.expandedstorage.common.screen;

import net.minecraft.util.ResourceLocation;

public final class SingleScreenMeta extends ScreenMeta
{
    public final int BLANK_SLOTS;

    public SingleScreenMeta(final int width, final int height, final int totalSlots, final ResourceLocation texture, final int textureWidth, final int textureHeight)
    {
        super(width, height, totalSlots, texture, textureWidth, textureHeight);
        BLANK_SLOTS = width * height - totalSlots;
    }
}