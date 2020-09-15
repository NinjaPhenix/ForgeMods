package ninjaphenix.expandedstorage.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import ninjaphenix.expandedstorage.ExpandedStorage;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGenerators
{
    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event)
    {
        final DataGenerator generator = event.getGenerator();
        final ExistingFileHelper fileHelper = event.getExistingFileHelper();
        final BlockTagsProvider blockTagsProvider = new BlockTags(generator);
        generator.addProvider(blockTagsProvider);
        generator.addProvider(new ItemTags(generator, blockTagsProvider));
        generator.addProvider(new Recipes(generator));
        generator.addProvider(new LootTables(generator));
        generator.addProvider(new Language(generator, ExpandedStorage.MOD_ID, "en_us"));
        generator.addProvider(new BlockStatesAndModels(generator, ExpandedStorage.MOD_ID, fileHelper));
        generator.addProvider(new ItemModels(generator, ExpandedStorage.MOD_ID, fileHelper));
    }
}