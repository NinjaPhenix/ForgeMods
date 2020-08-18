package ninjaphenix.expandedstorage.client.proxy;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import ninjaphenix.expandedstorage.ExpandedStorage;
import ninjaphenix.expandedstorage.Registries;
import ninjaphenix.expandedstorage.client.render.CursedChestRenderer;
import ninjaphenix.expandedstorage.common.block.entity.CursedChestTileEntity;
import ninjaphenix.expandedstorage.common.block.enums.CursedChestType;
import ninjaphenix.expandedstorage.common.proxy.IProxy;

@Mod.EventBusSubscriber(value = { Side.CLIENT })
public final class ClientProxy implements IProxy
{
    @SubscribeEvent
    public static void preStitchTextures(final TextureStitchEvent.Pre event)
    {
        final TextureMap map = event.getMap();
        if (!map.getBasePath().equals("textures")) { return; }
        for (final ResourceLocation entry : Registries.MODELED.getKeys())
        {
            final Registries.ModeledTierData data = Registries.MODELED.getObject(entry);
            for (final CursedChestType value : CursedChestType.values())
            {
                map.registerSprite(new ResourceLocation(ExpandedStorage.MOD_ID, data.getChestTexture(value).getPath()));
            }
        }
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        ClientRegistry.bindTileEntitySpecialRenderer(CursedChestTileEntity.class, CursedChestRenderer.INSTANCE);
    }
}