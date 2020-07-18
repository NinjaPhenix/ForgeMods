package ninjaphenix.refinement_test.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;

public final class ItemTags extends ItemTagsProvider
{
    public ItemTags(final DataGenerator generator, final BlockTagsProvider blockTagsProvider) { super(generator, blockTagsProvider); }

    @Override
    protected void registerTags()
    {

    }

    @Override
    public String getName() { return "Refinement Test - Item Tags"; }
}