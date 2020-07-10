package ninjaphenix.expandedstorage.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import ninjaphenix.expandedstorage.ExpandedStorage;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators
{
    @SubscribeEvent
    public static void gatherData(@NotNull final GatherDataEvent event)
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