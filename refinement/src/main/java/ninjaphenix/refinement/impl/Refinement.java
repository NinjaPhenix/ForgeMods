package ninjaphenix.refinement.impl;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import ninjaphenix.refinement.api.common.TierManager;
import ninjaphenix.refinement.api.common.TierRecipeManager;

@Mod(Refinement.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class Refinement
{
    public static final TierManager tierManager = new TierManager();
    public static final TierRecipeManager recipeManager = new TierRecipeManager();
    public static final String MOD_ID = "refinement";

    public Refinement()
    {
        RefinementContent.registerRegisters(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static ResourceLocation getRl(final String path)
    {
        return new ResourceLocation(MOD_ID, path);
    }

    @SubscribeEvent
    public void registerResourceReloadListeners(final AddReloadListenerEvent event)
    {
        event.addListener(tierManager);
        event.addListener(recipeManager);
    }
}