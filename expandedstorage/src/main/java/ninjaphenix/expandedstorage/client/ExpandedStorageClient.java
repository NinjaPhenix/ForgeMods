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
import ninjaphenix.expandedstorage.common.block.enums.CursedChestType;
import ninjaphenix.expandedstorage.client.render.CursedChestRenderer;

public class ExpandedStorageClient
{
    @SubscribeEvent
    public static void setup(final FMLClientSetupEvent event)
    {
        ClientRegistry.bindTileEntityRenderer(ModContent.CURSED_CHEST_TE, CursedChestRenderer::new);
    }

    @SubscribeEvent
    public static void preStitchTextures(final TextureStitchEvent.Pre event)
    {
        if (!event.getMap().getTextureLocation().equals(Atlases.CHEST_ATLAS)) { return; }
        for (ResourceLocation entry : Registries.MODELED.keySet())
        {
            if (entry.getNamespace().equals(ExpandedStorage.MOD_ID))
            {
                final Registries.ModeledTierData data = Registries.MODELED.getValue(entry).get();
                for (CursedChestType value : CursedChestType.values())
                {
                    event.addSprite(new ResourceLocation(ExpandedStorage.MOD_ID, data.getChestTexture(value).getPath()));
                }
            }
        }
    }
}
