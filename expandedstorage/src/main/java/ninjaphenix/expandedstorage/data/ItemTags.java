package ninjaphenix.expandedstorage.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import ninjaphenix.expandedstorage.ModContent;

public final class ItemTags extends ItemTagsProvider
{
    public ItemTags(final DataGenerator generator, final BlockTagsProvider blockTagsProvider) { super(generator, blockTagsProvider); }

    @Override
    protected void registerTags() { func_240522_a_(Tags.Items.CHESTS_WOODEN).func_240532_a_(ModContent.WOOD_CHEST.getSecond()); }

    @Override
    public String getName() { return "Expanded Storage - Item Tags"; }
}