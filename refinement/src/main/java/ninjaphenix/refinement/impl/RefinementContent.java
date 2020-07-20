package ninjaphenix.refinement.impl;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import ninjaphenix.refinement.impl.client.gui.UpgradeContainerScreen;
import ninjaphenix.refinement.impl.common.container.UpgradeContainer;

@Mod.EventBusSubscriber(modid = Refinement.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class RefinementContent
{
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Refinement.MOD_ID);

    public static final RegistryObject<ContainerType<UpgradeContainer>> UPGRADE_CONTAINER_TYPE;

    static
    {
        UPGRADE_CONTAINER_TYPE = CONTAINERS.register("upgrade", () -> new ContainerType<>(UpgradeContainer::new));
    }

    @SubscribeEvent @SuppressWarnings("unused")
    public static void onClientSetup(final FMLClientSetupEvent event)
    {
        ScreenManager.registerFactory(UPGRADE_CONTAINER_TYPE.get(), UpgradeContainerScreen::new);
    }

    public static void registerRegisters(final IEventBus modEventBus)
    {
        CONTAINERS.register(modEventBus);
    }
}