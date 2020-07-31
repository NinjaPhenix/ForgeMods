package torcherino;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import torcherino.api.TorcherinoAPI;
import torcherino.config.Config;
import torcherino.network.Networker;

@Mod(Torcherino.MOD_ID)
public final class Torcherino
{
    public static final Logger LOGGER = LogManager.getLogger(Torcherino.class);
    public static final String MOD_ID = "torcherino";

    @SuppressWarnings("deprecation")
    public Torcherino()
    {
        if (getClass().getClassLoader().getClass().getCanonicalName().startsWith("net.fabricmc"))
        {
            System.out.println("Running " + MOD_ID + " for forge under fabric is not supported, please download the fabric version of the mod.");
            System.exit(0);
            return;
        }
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Config.initialise();
        ModContent.initialise(eventBus);
        Networker.INSTANCE.initialise();
        eventBus.addListener(this::processIMC);
        MinecraftForge.EVENT_BUS.addListener(Networker.INSTANCE::processPlayerJoin);
        TorcherinoAPI.INSTANCE.blacklistBlock(Blocks.WATER);
        TorcherinoAPI.INSTANCE.blacklistBlock(Blocks.LAVA);
        TorcherinoAPI.INSTANCE.blacklistBlock(Blocks.AIR);
        TorcherinoAPI.INSTANCE.blacklistBlock(Blocks.CAVE_AIR);
        TorcherinoAPI.INSTANCE.blacklistBlock(Blocks.VOID_AIR);
    }

    public static ResourceLocation getRl(final String path)
    {
        return new ResourceLocation(MOD_ID, path);
    }

    @SubscribeEvent
    @SuppressWarnings("deprecation")
    public void processIMC(final InterModProcessEvent event)
    {
        // cannot use local for event.getIMCStream() as reusing stream will cause crash.
        if (event.getIMCStream().anyMatch(msg -> true))
        {
            LOGGER.warn("Torcherino IMC support is being removed in a future version in favour of data pack tags.");
        }
        event.getIMCStream().forEach((message) ->
        {
            final String method = message.getMethod();
            final Object value = message.getMessageSupplier().get();
            if (method.equals("blacklist_block"))
            {
                if (value instanceof ResourceLocation)
                {
                    TorcherinoAPI.INSTANCE.blacklistBlock((ResourceLocation) value);
                }
                else if (value instanceof Block)
                {
                    TorcherinoAPI.INSTANCE.blacklistBlock((Block) value);
                }
                else
                {
                    LOGGER.error("Received blacklist_block message with invalid value, must be either a Block or ResourceLocation.");
                }
            }
            else if (method.equals("blacklist_tile"))
            {
                if (value instanceof ResourceLocation)
                {
                    TorcherinoAPI.INSTANCE.blacklistTileEntity((ResourceLocation) value);
                }
                else if (value instanceof TileEntityType)
                {
                    TorcherinoAPI.INSTANCE.blacklistTileEntity((TileEntityType<?>) value);
                }
                else
                {
                    LOGGER.error("Received blacklist_tile message with invalid value, must be either a TileEntityType or ResourceLocation.");
                }
            }
            else
            {
                LOGGER.error("Received IMC message with invalid method, must be either: \"blacklist_block\" or \"blacklist_tile\".");
            }
        });
    }
}