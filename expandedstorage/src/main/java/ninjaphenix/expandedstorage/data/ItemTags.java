package ninjaphenix.expandedstorage.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import ninjaphenix.expandedstorage.ModContent;
import org.jetbrains.annotations.NotNull;

public class ItemTags extends ItemTagsProvider
{
    public ItemTags(final DataGenerator generator) { super(generator); }

    @Override
    protected void registerTags() { getBuilder(Tags.Items.CHESTS_WOODEN).add(ModContent.WOOD_CHEST.getSecond()).build(Tags.Items.CHESTS_WOODEN.getId()); }

    @Override
    public @NotNull String getName() { return "Expanded Storage - Item Tags"; }
}
