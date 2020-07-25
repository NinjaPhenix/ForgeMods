package torcherino.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public final class Language extends LanguageProvider
{
    public Language(final DataGenerator generator, final String modId, final String locale)
    {
        super(generator, modId, locale);
    }

    @Override
    protected void addTranslations()
    {
        // Block Translations
        add(ModContentHolder.TORCHERINO, "Torcherino");
        add(ModContentHolder.COMPRESSED_TORCHERINO, "Compressed Torcherino");
        add(ModContentHolder.DOUBLE_COMPRESSED_TORCHERINO, "Double Compressed Torcherino");
        add(ModContentHolder.LANTERINO, "Jack o'Lanterino");
        add(ModContentHolder.COMPRESSED_LANTERINO, "Compressed Jack o'Lanterino");
        add(ModContentHolder.DOUBLE_COMPRESSED_LANTERINO, "Double Compressed Jack o'Lanterino");
        add(ModContentHolder.LANTERN, "Lanterino");
        add(ModContentHolder.COMPRESSED_LANTERN, "Compressed Lanterino");
        add(ModContentHolder.DOUBLE_COMPRESSED_LANTERN, "Double Compressed Lanterino");
        // Screen Translations
        add("gui.torcherino.speed", "Speed, %s%%");
        add("gui.torcherino.x_range", "X Range, %s Blocks");
        add("gui.torcherino.z_range", "Z Range, %s Blocks");
        add("gui.torcherino.y_range", "Y Range, %s Blocks");
        add("gui.torcherino.mode", "Mode, %s");
        add("gui.torcherino.mode.normal", "Redstone Normal");
        add("gui.torcherino.mode.inverted", "Redstone Inverted");
        add("gui.torcherino.mode.ignored", "Redstone Ignored");
        add("gui.torcherino.mode.off", "Off");
    }

    @Override
    public String getName()
    {
        return "Torcherino - Language";
    }
}