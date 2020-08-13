package ninjaphenix.expandedstorage.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.Tags;
import ninjaphenix.expandedstorage.ModContent;

public final class BlockTags extends BlockTagsProvider
{
    public BlockTags(final DataGenerator generator) { super(generator); }

    @Override
    protected void registerTags() { func_240522_a_(Tags.Blocks.CHESTS_WOODEN).func_240532_a_(ModContent.WOOD_CHEST.getFirst()); }

    @Override
    public String getName() { return "Expanded Storage - Block Tags"; }
}