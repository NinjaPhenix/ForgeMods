package ninjaphenix.refinement.impl.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public final class Language extends LanguageProvider
{
    public Language(final DataGenerator generator, final String modId, final String locale) { super(generator, modId, locale); }

    @Override
    protected void addTranslations()
    {
        add("container.refinement.upgrade.upgrade", "Upgrade");
        add("container.refinement.upgrade.downgrade", "Downgrade");
        add("container.refinement.upgrade.information", "Information");
    }

    @Override
    public String getName()
    {
        return "Refinement - Language";
    }
}