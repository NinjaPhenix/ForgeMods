package ninjaphenix.expandedstorage.common;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import ninjaphenix.expandedstorage.ExpandedStorage;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = ExpandedStorage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class OLD_ExpandedStorageConfig
{
    public static final OLD_ExpandedStorageConfig.Client CLIENT;
    private static final ForgeConfigSpec clientSpec;

    static
    {
        final Pair<OLD_ExpandedStorageConfig.Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(OLD_ExpandedStorageConfig.Client::new);
        clientSpec = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static void register() { ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, clientSpec); }

    public static class Client
    {
        public final ForgeConfigSpec.ConfigValue<String> preferredContainerType;
        public final ForgeConfigSpec.BooleanValue restrictiveScrolling;
        public final ForgeConfigSpec.BooleanValue centerSettingsButtonOnScrollbar;

        Client(final ForgeConfigSpec.Builder builder)
        {
            builder.push("client");
            preferredContainerType = builder.comment("Preferred Container Type, set to expandedstorage:auto to display selection screen.")
                                            .translation("expandedstorage.config_gui.preferred_container_type")
                                            .define("preferred_container_type", new ResourceLocation("expandedstorage", "auto").toString());
            restrictiveScrolling = builder.comment("Only allows scrolling with mouse-wheel whilst hovering over the scroll bar.")
                                          .translation("expandedstorage.config_gui.restrictive_scrolling")
                                          .define("restrictive_scrolling", false);
            centerSettingsButtonOnScrollbar = builder.comment("Centers the settings button on the scrollbar of the scrollable screen.")
                                                     .translation("expandedstorage.config_gui.settings_button_center_on_scrollbar")
                                                     .define("settings_button_center_on_scrollbar", true);
        }
    }
}