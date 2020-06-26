package ninjaphenix.expandedstorage.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.Tags;
import ninjaphenix.expandedstorage.ModContent;
import org.jetbrains.annotations.NotNull;

public class BlockTags extends BlockTagsProvider
{
    public BlockTags(final DataGenerator generator) { super(generator); }

    @Override
    protected void registerTags()
    {
        getBuilder(Tags.Blocks.CHESTS_WOODEN).add(ModContent.WOOD_CHEST.getFirst()).build(Tags.Blocks.CHESTS_WOODEN.getId());
    }

    @Override
    public @NotNull String getName() { return "Expanded Storage - Block Tags"; }
}
