package ninjaphenix.expandedstorage.common;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import ninjaphenix.expandedstorage.ExpandedStorage;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = ExpandedStorage.MOD_ID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class ExpandedStorageConfig
{
    private static class Client
    {
        private final ForgeConfigSpec.ConfigValue<String> preferredContainerType;
        private final ForgeConfigSpec.BooleanValue restrictiveScrolling;
        private final ForgeConfigSpec.BooleanValue centerSettingsButtonOnScrollbar;

        Client(ForgeConfigSpec.Builder builder)
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

    private static final ForgeConfigSpec clientSpec;
    public static final ExpandedStorageConfig.Client CLIENT;
    static {
        final Pair<ExpandedStorageConfig.Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ExpandedStorageConfig.Client::new);
        clientSpec = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static ForgeConfigSpec getClientSpec() { return clientSpec; }
}
