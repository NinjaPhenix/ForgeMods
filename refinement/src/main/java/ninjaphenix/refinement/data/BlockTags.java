package ninjaphenix.refinement.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;

public final class BlockTags extends BlockTagsProvider
{
    public BlockTags(final DataGenerator generator) { super(generator); }

    @Override
    protected void registerTags()
    {

    }

    @Override
    public String getName() { return "Refinement - Block Tags"; }
}