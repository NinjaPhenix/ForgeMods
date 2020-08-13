package ninjaphenix.expandedstorage;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
import ninjaphenix.expandedstorage.Registries.ModeledTierData;
import ninjaphenix.expandedstorage.client.render.CursedChestTileEntityItemStackRenderer;
import ninjaphenix.expandedstorage.client.screen.PagedScreen;
import ninjaphenix.expandedstorage.client.screen.ScrollableScreen;
import ninjaphenix.expandedstorage.client.screen.SingleScreen;
import ninjaphenix.expandedstorage.common.block.CursedChestBlock;
import ninjaphenix.expandedstorage.common.block.OldChestBlock;
import ninjaphenix.expandedstorage.common.block.entity.CursedChestTileEntity;
import ninjaphenix.expandedstorage.common.block.entity.CustomTileEntityType;
import ninjaphenix.expandedstorage.common.block.entity.OldChestTileEntity;
import ninjaphenix.expandedstorage.common.inventory.PagedContainer;
import ninjaphenix.expandedstorage.common.inventory.ScrollableContainer;
import ninjaphenix.expandedstorage.common.inventory.SingleContainer;
import ninjaphenix.expandedstorage.common.item.ChestConversionItem;
import ninjaphenix.expandedstorage.common.item.ChestMutatorItem;

import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;

@Mod.EventBusSubscriber(modid = ExpandedStorage.MOD_ID, bus = MOD)
public final class ModContent
{
    public static final ContainerType<PagedContainer> PAGED_CONTAINER_TYPE = new ContainerType<>(new PagedContainer.Factory());
    public static final ContainerType<SingleContainer> SINGLE_CONTAINER_TYPE = new ContainerType<>(new SingleContainer.Factory());
    public static final ContainerType<ScrollableContainer> SCROLLABLE_CONTAINER_TYPE = new ContainerType<>(new ScrollableContainer.Factory());
    public static final Pair<OldChestBlock, BlockItem> OLD_WOOD_CHEST = registerOld(Blocks.OAK_PLANKS, "wood_chest", 3);
    public static final Pair<OldChestBlock, BlockItem> OLD_IRON_CHEST = registerOld(Blocks.IRON_BLOCK, "iron_chest", 6);
    public static final Pair<OldChestBlock, BlockItem> OLD_GOLD_CHEST = registerOld(Blocks.GOLD_BLOCK, "gold_chest", 9);
    public static final Pair<OldChestBlock, BlockItem> OLD_DIAMOND_CHEST = registerOld(Blocks.DIAMOND_BLOCK, "diamond_chest", 12);
    public static final Pair<OldChestBlock, BlockItem> OLD_OBSIDIAN_CHEST = registerOld(Blocks.OBSIDIAN, "obsidian_chest", 12);
    public static final Pair<OldChestBlock, BlockItem> OLD_NETHERITE_CHEST = registerOld(Blocks.NETHERITE_BLOCK, "netherite_chest", 15);
    public static final Pair<CursedChestBlock, BlockItem> WOOD_CHEST = register(Blocks.OAK_PLANKS, "wood_chest", 3);
    public static final Pair<CursedChestBlock, BlockItem> PUMPKIN_CHEST = register(Blocks.CARVED_PUMPKIN, "pumpkin_chest", 3);
    public static final Pair<CursedChestBlock, BlockItem> CHRISTMAS_CHEST = register(Blocks.OAK_PLANKS, "christmas_chest", 3);
    public static final Pair<CursedChestBlock, BlockItem> IRON_CHEST = register(Blocks.IRON_BLOCK, "iron_chest", 6);
    public static final Pair<CursedChestBlock, BlockItem> GOLD_CHEST = register(Blocks.GOLD_BLOCK, "gold_chest", 9);
    public static final Pair<CursedChestBlock, BlockItem> DIAMOND_CHEST = register(Blocks.DIAMOND_BLOCK, "diamond_chest", 12);
    public static final Pair<CursedChestBlock, BlockItem> OBSIDIAN_CHEST = register(Blocks.OBSIDIAN, "obsidian_chest", 12);
    public static final Pair<CursedChestBlock, BlockItem> NETHERITE_CHEST = register(Blocks.NETHERITE_BLOCK, "netherite_chest", 15);
    public static final Item CHEST_MUTATOR = new ChestMutatorItem().setRegistryName(ExpandedStorage.getRl("chest_mutator"));
    public static final ChestConversionItem CONVERSION_KIT_WOOD_IRON;
    public static final ChestConversionItem CONVERSION_KIT_WOOD_GOLD;
    public static final ChestConversionItem CONVERSION_KIT_WOOD_DIAMOND;
    public static final ChestConversionItem CONVERSION_KIT_WOOD_OBSIDIAN;
    public static final ChestConversionItem CONVERSION_KIT_WOOD_NETHERITE;
    public static final ChestConversionItem CONVERSION_KIT_IRON_GOLD;
    public static final ChestConversionItem CONVERSION_KIT_IRON_DIAMOND;
    public static final ChestConversionItem CONVERSION_KIT_IRON_OBSIDIAN;
    public static final ChestConversionItem CONVERSION_KIT_IRON_NETHERITE;
    public static final ChestConversionItem CONVERSION_KIT_GOLD_DIAMOND;
    public static final ChestConversionItem CONVERSION_KIT_GOLD_OBSIDIAN;
    public static final ChestConversionItem CONVERSION_KIT_GOLD_NETHERITE;
    public static final ChestConversionItem CONVERSION_KIT_DIAMOND_OBSIDIAN;
    public static final ChestConversionItem CONVERSION_KIT_DIAMOND_NETHERITE;
    public static final ChestConversionItem CONVERSION_KIT_OBSIDIAN_NETHERITE;
    public static final CustomTileEntityType<CursedChestTileEntity> CURSED_CHEST_TE = new CustomTileEntityType<>(CursedChestTileEntity::new, (b) -> b instanceof CursedChestBlock, ExpandedStorage.getRl("cursed_chest"));
    public static final CustomTileEntityType<OldChestTileEntity> OLD_CHEST_TE = new CustomTileEntityType<>(OldChestTileEntity::new, (b) -> b instanceof OldChestBlock, ExpandedStorage.getRl("old_cursed_chest"));

    static
    {
        final Pair<ResourceLocation, String> wood = new Pair<>(ExpandedStorage.getRl("wood_chest"), "wood");
        final Pair<ResourceLocation, String> iron = new Pair<>(ExpandedStorage.getRl("iron_chest"), "iron");
        final Pair<ResourceLocation, String> gold = new Pair<>(ExpandedStorage.getRl("gold_chest"), "gold");
        final Pair<ResourceLocation, String> diamond = new Pair<>(ExpandedStorage.getRl("diamond_chest"), "diamond");
        final Pair<ResourceLocation, String> obsidian = new Pair<>(ExpandedStorage.getRl("obsidian_chest"), "obsidian");
        final Pair<ResourceLocation, String> netherite = new Pair<>(ExpandedStorage.getRl("netherite_chest"), "netherite");
        CONVERSION_KIT_WOOD_IRON = new ChestConversionItem(wood, iron);
        CONVERSION_KIT_WOOD_GOLD = new ChestConversionItem(wood, gold);
        CONVERSION_KIT_WOOD_DIAMOND = new ChestConversionItem(wood, diamond);
        CONVERSION_KIT_WOOD_OBSIDIAN = new ChestConversionItem(wood, obsidian);
        CONVERSION_KIT_WOOD_NETHERITE = new ChestConversionItem(wood, netherite);
        CONVERSION_KIT_IRON_GOLD = new ChestConversionItem(iron, gold);
        CONVERSION_KIT_IRON_DIAMOND = new ChestConversionItem(iron, diamond);
        CONVERSION_KIT_IRON_OBSIDIAN = new ChestConversionItem(iron, obsidian);
        CONVERSION_KIT_IRON_NETHERITE = new ChestConversionItem(iron, netherite);
        CONVERSION_KIT_GOLD_DIAMOND = new ChestConversionItem(gold, diamond);
        CONVERSION_KIT_GOLD_OBSIDIAN = new ChestConversionItem(gold, obsidian);
        CONVERSION_KIT_GOLD_NETHERITE = new ChestConversionItem(gold, netherite);
        CONVERSION_KIT_DIAMOND_OBSIDIAN = new ChestConversionItem(diamond, obsidian);
        CONVERSION_KIT_DIAMOND_NETHERITE = new ChestConversionItem(diamond, netherite);
        CONVERSION_KIT_OBSIDIAN_NETHERITE = new ChestConversionItem(obsidian, netherite);
    }

    private static Pair<CursedChestBlock, BlockItem> register(final Block copy, final String name, final int rows)
    {
        final ResourceLocation registryRl = ExpandedStorage.getRl(name);
        final CursedChestBlock block = new CursedChestBlock(Block.Properties.from(copy), registryRl);
        final BlockItem item = new BlockItem(block, new Item.Properties().setISTER(() -> CursedChestTileEntityItemStackRenderer::new).group(ExpandedStorage.group));
        item.setRegistryName(registryRl);
        Registry.register(Registries.MODELED, registryRl, new ModeledTierData(rows * 9, registryRl, new TranslationTextComponent("container.expandedstorage." + name), type -> ExpandedStorage.getRl(String.format("entity/%s/%s", name, type.getString()))));
        return new Pair<>(block, item);
    }

    private static Pair<OldChestBlock, BlockItem> registerOld(final Block copy, final String name, final int rows)
    {
        final ResourceLocation registryRl = ExpandedStorage.getRl("old_" + name);
        final OldChestBlock block = new OldChestBlock(Block.Properties.from(copy), registryRl);
        final BlockItem item = new BlockItem(block, new Item.Properties().group(ExpandedStorage.group));
        item.setRegistryName(registryRl);
        Registry.register(Registries.OLD, ExpandedStorage.getRl(name), new Registries.TierData(rows * 9, registryRl, new TranslationTextComponent("container.expandedstorage." + name)));
        return new Pair<>(block, item);
    }

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event)
    {
        event.getRegistry().registerAll(WOOD_CHEST.getFirst(), PUMPKIN_CHEST.getFirst(), CHRISTMAS_CHEST.getFirst(), IRON_CHEST.getFirst(), GOLD_CHEST.getFirst(), DIAMOND_CHEST.getFirst(), OBSIDIAN_CHEST.getFirst(), NETHERITE_CHEST.getFirst(), OLD_WOOD_CHEST.getFirst(), OLD_IRON_CHEST.getFirst(), OLD_GOLD_CHEST.getFirst(), OLD_DIAMOND_CHEST.getFirst(), OLD_OBSIDIAN_CHEST.getFirst(), OLD_NETHERITE_CHEST.getFirst());
    }

    @SubscribeEvent
    public static void registerBlockItems(final RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(WOOD_CHEST.getSecond(), PUMPKIN_CHEST.getSecond(), CHRISTMAS_CHEST.getSecond(), IRON_CHEST.getSecond(), GOLD_CHEST.getSecond(), DIAMOND_CHEST.getSecond(), OBSIDIAN_CHEST.getSecond(), NETHERITE_CHEST.getSecond(), OLD_WOOD_CHEST.getSecond(), OLD_IRON_CHEST.getSecond(), OLD_GOLD_CHEST.getSecond(), OLD_DIAMOND_CHEST.getSecond(), OLD_OBSIDIAN_CHEST.getSecond(), OLD_NETHERITE_CHEST.getSecond(), CONVERSION_KIT_WOOD_IRON, CONVERSION_KIT_WOOD_GOLD, CONVERSION_KIT_WOOD_DIAMOND, CONVERSION_KIT_WOOD_OBSIDIAN, CONVERSION_KIT_WOOD_NETHERITE, CONVERSION_KIT_IRON_GOLD, CONVERSION_KIT_IRON_DIAMOND, CONVERSION_KIT_IRON_OBSIDIAN, CONVERSION_KIT_IRON_NETHERITE, CONVERSION_KIT_GOLD_DIAMOND, CONVERSION_KIT_GOLD_OBSIDIAN, CONVERSION_KIT_GOLD_NETHERITE, CONVERSION_KIT_DIAMOND_OBSIDIAN, CONVERSION_KIT_DIAMOND_NETHERITE, CONVERSION_KIT_OBSIDIAN_NETHERITE, CHEST_MUTATOR);
    }

    @SubscribeEvent
    public static void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> event)
    {
        event.getRegistry().registerAll(CURSED_CHEST_TE, OLD_CHEST_TE);
    }

    @SubscribeEvent
    public static void registerContainerTypes(final RegistryEvent.Register<ContainerType<?>> event)
    {
        PAGED_CONTAINER_TYPE.setRegistryName(ExpandedStorage.getRl("paged_container"));
        SINGLE_CONTAINER_TYPE.setRegistryName(ExpandedStorage.getRl("single_container"));
        SCROLLABLE_CONTAINER_TYPE.setRegistryName(ExpandedStorage.getRl("scrollable_container"));
        event.getRegistry().register(SCROLLABLE_CONTAINER_TYPE);
        event.getRegistry().register(PAGED_CONTAINER_TYPE);
        event.getRegistry().register(SINGLE_CONTAINER_TYPE);
        if (FMLLoader.getDist().isClient())
        {
            registerScreenFactory();
        }
    }

    public static void registerScreenFactory()
    {
        ScreenManager.registerFactory(SCROLLABLE_CONTAINER_TYPE, ScrollableScreen::new);
        ScreenManager.registerFactory(PAGED_CONTAINER_TYPE, PagedScreen::new);
        ScreenManager.registerFactory(SINGLE_CONTAINER_TYPE, SingleScreen::new);
    }
}