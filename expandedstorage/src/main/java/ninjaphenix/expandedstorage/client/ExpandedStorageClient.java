package ninjaphenix.expandedstorage.client;

import net.minecraft.client.renderer.Atlases;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import ninjaphenix.expandedstorage.ExpandedStorage;
import ninjaphenix.expandedstorage.ModContent;
import ninjaphenix.expandedstorage.Registries;
import ninjaphenix.expandedstorage.client.render.CursedChestRenderer;
import ninjaphenix.expandedstorage.common.block.enums.CursedChestType;
import org.jetbrains.annotations.NotNull;

public class ExpandedStorageClient
{
    @SubscribeEvent
    public static void setup(final FMLClientSetupEvent event) { ClientRegistry.bindTileEntityRenderer(ModContent.CURSED_CHEST_TE, CursedChestRenderer::new); }

    @SubscribeEvent @SuppressWarnings("OptionalGetWithoutIsPresent")
    public static void preStitchTextures(final TextureStitchEvent.Pre event)
    {
        if (!event.getMap().getTextureLocation().equals(Atlases.CHEST_ATLAS)) { return; }
        for (@NotNull final ResourceLocation entry : Registries.MODELED.keySet())
        {
            final Registries.ModeledTierData data = Registries.MODELED.getValue(entry).get();
            for (@NotNull final CursedChestType value : CursedChestType.values())
            {
                event.addSprite(new ResourceLocation(ExpandedStorage.MOD_ID, data.getChestTexture(value).getPath()));
            }
        }
    }
}