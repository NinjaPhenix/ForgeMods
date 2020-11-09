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

public final class ExpandedStorageClient
{
    @SubscribeEvent
    public static void setup(final FMLClientSetupEvent event) { ClientRegistry.bindTileEntityRenderer(ModContent.CURSED_CHEST_TE, CursedChestRenderer::new); }

    @SubscribeEvent
    public static void preStitchTextures(final TextureStitchEvent.Pre event)
    {
        if (!event.getMap().location().equals(Atlases.CHEST_SHEET)) { return; }
        for (final ResourceLocation entry : Registries.MODELED.keySet())
        {
            final Registries.ModeledTierData data = Registries.MODELED.get(entry);
            for (final CursedChestType value : CursedChestType.values())
            {
                event.addSprite(new ResourceLocation(ExpandedStorage.MOD_ID, data.getChestTexture(value).getPath()));
            }
        }
    }
}