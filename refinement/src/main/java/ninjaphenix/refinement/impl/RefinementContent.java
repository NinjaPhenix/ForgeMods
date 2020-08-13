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
import net.minecraftforge.registries.RegistryBuilder;
import ninjaphenix.refinement.api.common.ITierRecipeSerializer;
import ninjaphenix.refinement.api.common.recipe.ShapedDowngradeRecipe;
import ninjaphenix.refinement.api.common.recipe.ShapedUpgradeRecipe;
import ninjaphenix.refinement.api.common.recipe.ShapelessDowngradeRecipe;
import ninjaphenix.refinement.api.common.recipe.ShapelessUpgradeRecipe;
import ninjaphenix.refinement.impl.client.gui.UpgradeContainerScreen;
import ninjaphenix.refinement.impl.common.container.UpgradeContainer;

@Mod.EventBusSubscriber(modid = Refinement.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class RefinementContent
{
    public static final Class<ITierRecipeSerializer<?>> x = (Class<ITierRecipeSerializer<?>>) ShapedUpgradeRecipe.Serializer.class.getInterfaces()[0];

    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Refinement.MOD_ID);
    private static final DeferredRegister<ITierRecipeSerializer<?>> TIER_RECIPE_SERIALIZER = DeferredRegister.create(x, Refinement.MOD_ID);

    public static final RegistryObject<ContainerType<UpgradeContainer>> UPGRADE_CONTAINER_TYPE;

    public static final RegistryObject<ITierRecipeSerializer<ShapedUpgradeRecipe>> SHAPED_UPGRADE_SERIALIZER = TIER_RECIPE_SERIALIZER
            .register("upgrade_shaped", ShapedUpgradeRecipe.Serializer::new);
    public static final RegistryObject<ITierRecipeSerializer<ShapedDowngradeRecipe>> SHAPED_DOWNGRADE_RECIPE = TIER_RECIPE_SERIALIZER
            .register("downgrade_shaped", ShapedDowngradeRecipe.Serializer::new);
    public static final RegistryObject<ITierRecipeSerializer<ShapelessUpgradeRecipe>> SHAPELESS_UPGRADE_SERIALIZER = TIER_RECIPE_SERIALIZER
            .register("upgrade_shapeless", ShapelessUpgradeRecipe.Serializer::new);
    public static final RegistryObject<ITierRecipeSerializer<ShapelessDowngradeRecipe>> SHAPELESS_DOWNGRADE_SERIALIZER = TIER_RECIPE_SERIALIZER
            .register("downgrade_shapeless", ShapelessDowngradeRecipe.Serializer::new);

    static
    {
        TIER_RECIPE_SERIALIZER.makeRegistry("tier_recipe_serializer", RegistryBuilder::new);
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
        TIER_RECIPE_SERIALIZER.register(modEventBus);
    }
}