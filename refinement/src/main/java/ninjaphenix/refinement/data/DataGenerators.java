package ninjaphenix.refinement.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import ninjaphenix.refinement.Refinement;

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
        generator.addProvider(new Language(generator, Refinement.MOD_ID, "en_us"));
        generator.addProvider(new BlockStatesAndModels(generator, Refinement.MOD_ID, fileHelper));
        generator.addProvider(new ItemModels(generator, Refinement.MOD_ID, fileHelper));
    }
}