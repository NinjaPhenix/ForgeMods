package ninjaphenix.refinement_test.common;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import ninjaphenix.refinement_test.common.block.TestUpgradableBlock;
import ninjaphenix.refinement_test.common.block.entity.TestUpgradableTileEntity;
import ninjaphenix.refinement_test.common.container.TestContainer;

@Mod.EventBusSubscriber(modid = RefinementTestMod.MOD_ID)
public final class TestContent
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RefinementTestMod.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RefinementTestMod.MOD_ID);
    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, RefinementTestMod.MOD_ID);
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, RefinementTestMod.MOD_ID);

    public static final RegistryObject<ContainerType<TestContainer>> TEST_CONTAINER_TYPE;
    public static final RegistryObject<TileEntityType<TestUpgradableTileEntity>> TEST_UPGRADABLE_TILE_ENTITY;
    private static final RegistryObject<TestUpgradableBlock> TEST_BLOCK;
    private static final RegistryObject<BlockItem> TEST_ITEM;

    static
    {
        TEST_BLOCK = BLOCKS.register("test_block", () -> new TestUpgradableBlock(AbstractBlock.Properties.create(Material.EARTH)));
        TEST_ITEM = ITEMS.register("test_block", () -> new BlockItem(TEST_BLOCK.get(), new Item.Properties()));
        TEST_CONTAINER_TYPE = CONTAINERS.register("test_container", () -> new ContainerType<>(TestContainer::new));
        TEST_UPGRADABLE_TILE_ENTITY = TILE_ENTITIES.register("test_upgradable_tile_entity",
                () -> TileEntityType.Builder.create(TestUpgradableTileEntity::new, TEST_BLOCK.get()).build(null));
    }

    public static void registerRegisters(final IEventBus modEventBus)
    {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        TILE_ENTITIES.register(modEventBus);
        CONTAINERS.register(modEventBus);
    }
}