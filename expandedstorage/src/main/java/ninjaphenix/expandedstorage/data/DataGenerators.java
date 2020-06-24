package ninjaphenix.expandedstorage.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import ninjaphenix.expandedstorage.ExpandedStorage;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators
{
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        final DataGenerator generator = event.getGenerator();
        generator.addProvider(new BlockTags(generator));
        generator.addProvider(new ItemTags(generator));
        generator.addProvider(new Recipes(generator));
        generator.addProvider(new LootTables(generator));
        generator.addProvider(new Language(generator, ExpandedStorage.MOD_ID, "en_us"));
    }
}
