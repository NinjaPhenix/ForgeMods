package ninjaphenix.refinement_test.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public final class Language extends LanguageProvider
{
    public Language(final DataGenerator generator, final String modId, final String locale) { super(generator, modId, locale); }

    @Override
    protected void addTranslations()
    {

    }

    @Override
    public String getName() { return "Refinement Test - Language"; }
}