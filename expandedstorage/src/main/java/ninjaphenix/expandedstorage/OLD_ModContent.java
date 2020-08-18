package ninjaphenix.expandedstorage;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import ninjaphenix.expandedstorage.Registries.ModeledTierData;
import ninjaphenix.expandedstorage.common.block.OLD_CursedChestBlock;
import ninjaphenix.expandedstorage.common.block.OLD_OldChestBlock;
import ninjaphenix.expandedstorage.common.block.entity.OLD_CursedChestTileEntity;
import ninjaphenix.expandedstorage.common.block.entity.OLD_CustomTileEntityType;
import ninjaphenix.expandedstorage.common.block.entity.OLD_OldChestTileEntity;
import ninjaphenix.expandedstorage.common.inventory.OLD_PagedContainer;
import ninjaphenix.expandedstorage.common.inventory.OLD_ScrollableContainer;
import ninjaphenix.expandedstorage.common.inventory.OLD_SingleContainer;
import ninjaphenix.expandedstorage.common.item.OLD_ChestConversionItem;
import ninjaphenix.expandedstorage.common.item.OLD_ChestMutatorItem;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = ExpandedStorage.MOD_ID)
public final class OLD_ModContent
{
    public static final ContainerType<OLD_PagedContainer> PAGED_CONTAINER_TYPE;
    public static final ContainerType<OLD_SingleContainer> SINGLE_CONTAINER_TYPE;
    public static final ContainerType<OLD_ScrollableContainer> SCROLLABLE_CONTAINER_TYPE;
    public static final Pair<OLD_OldChestBlock, ItemBlock> OLD_WOOD_CHEST;
    public static final Pair<OLD_OldChestBlock, ItemBlock> OLD_IRON_CHEST;
    public static final Pair<OLD_OldChestBlock, ItemBlock> OLD_GOLD_CHEST;
    public static final Pair<OLD_OldChestBlock, ItemBlock> OLD_DIAMOND_CHEST;
    public static final Pair<OLD_OldChestBlock, ItemBlock> OLD_OBSIDIAN_CHEST;
    public static final Pair<OLD_CursedChestBlock, ItemBlock> WOOD_CHEST;
    public static final Pair<OLD_CursedChestBlock, ItemBlock> PUMPKIN_CHEST;
    public static final Pair<OLD_CursedChestBlock, ItemBlock> CHRISTMAS_CHEST;
    public static final Pair<OLD_CursedChestBlock, ItemBlock> IRON_CHEST;
    public static final Pair<OLD_CursedChestBlock, ItemBlock> GOLD_CHEST;
    public static final Pair<OLD_CursedChestBlock, ItemBlock> DIAMOND_CHEST;
    public static final Pair<OLD_CursedChestBlock, ItemBlock> OBSIDIAN_CHEST;
    public static final Item CHEST_MUTATOR;
    public static final OLD_ChestConversionItem CONVERSION_KIT_WOOD_IRON;
    public static final OLD_ChestConversionItem CONVERSION_KIT_WOOD_GOLD;
    public static final OLD_ChestConversionItem CONVERSION_KIT_WOOD_DIAMOND;
    public static final OLD_ChestConversionItem CONVERSION_KIT_WOOD_OBSIDIAN;
    public static final OLD_ChestConversionItem CONVERSION_KIT_WOOD_NETHERITE;
    public static final OLD_ChestConversionItem CONVERSION_KIT_IRON_GOLD;
    public static final OLD_ChestConversionItem CONVERSION_KIT_IRON_DIAMOND;
    public static final OLD_ChestConversionItem CONVERSION_KIT_IRON_OBSIDIAN;
    public static final OLD_ChestConversionItem CONVERSION_KIT_IRON_NETHERITE;
    public static final OLD_ChestConversionItem CONVERSION_KIT_GOLD_DIAMOND;
    public static final OLD_ChestConversionItem CONVERSION_KIT_GOLD_OBSIDIAN;
    public static final OLD_ChestConversionItem CONVERSION_KIT_GOLD_NETHERITE;
    public static final OLD_ChestConversionItem CONVERSION_KIT_DIAMOND_OBSIDIAN;
    public static final OLD_ChestConversionItem CONVERSION_KIT_DIAMOND_NETHERITE;
    public static final OLD_ChestConversionItem CONVERSION_KIT_OBSIDIAN_NETHERITE;
    // @formatter:off
    public static final OLD_CustomTileEntityType<OLD_CursedChestTileEntity> CURSED_CHEST_TE = new OLD_CustomTileEntityType<>(OLD_CursedChestTileEntity::new,
            (b) -> b instanceof OLD_CursedChestBlock, ExpandedStorage.getRl("cursed_chest"));
    public static final OLD_CustomTileEntityType<OLD_OldChestTileEntity> OLD_CHEST_TE = new OLD_CustomTileEntityType<>(OLD_OldChestTileEntity::new,
            (b) -> b instanceof OLD_OldChestBlock, ExpandedStorage.getRl("old_cursed_chest"));
    // @formatter:on

    static
    {
        OLD_WOOD_CHEST = registerOld(Blocks.PLANKS, "wood_chest", 3);
        OLD_IRON_CHEST = registerOld(Blocks.IRON_BLOCK, "iron_chest", 6);
        OLD_GOLD_CHEST = registerOld(Blocks.GOLD_BLOCK, "gold_chest", 9);
        OLD_DIAMOND_CHEST = registerOld(Blocks.DIAMOND_BLOCK, "diamond_chest", 12);
        OLD_OBSIDIAN_CHEST = registerOld(Blocks.OBSIDIAN, "obsidian_chest", 12);
        WOOD_CHEST = register(Blocks.PLANKS, "wood_chest", 3);
        PUMPKIN_CHEST = register(Blocks.PUMPKIN, "pumpkin_chest", 3);
        CHRISTMAS_CHEST = register(Blocks.PLANKS, "christmas_chest", 3);
        IRON_CHEST = register(Blocks.IRON_BLOCK, "iron_chest", 6);
        GOLD_CHEST = register(Blocks.GOLD_BLOCK, "gold_chest", 9);
        DIAMOND_CHEST = register(Blocks.DIAMOND_BLOCK, "diamond_chest", 12);
        OBSIDIAN_CHEST = register(Blocks.OBSIDIAN, "obsidian_chest", 12);
        //PAGED_CONTAINER_TYPE = new ContainerType<>(new PagedContainer.Factory());
        //SINGLE_CONTAINER_TYPE = new ContainerType<>(new SingleContainer.Factory());
        //SCROLLABLE_CONTAINER_TYPE = new ContainerType<>(new ScrollableContainer.Factory());
        //PAGED_CONTAINER_TYPE.setRegistryName(ExpandedStorage.getRl("paged_container"));
        //SINGLE_CONTAINER_TYPE.setRegistryName(ExpandedStorage.getRl("single_container"));
        //SCROLLABLE_CONTAINER_TYPE.setRegistryName(ExpandedStorage.getRl("scrollable_container"));
        CHEST_MUTATOR = new OLD_ChestMutatorItem().setRegistryName(ExpandedStorage.getRl("chest_mutator"));
        final Pair<ResourceLocation, String> wood = Pair.of(ExpandedStorage.getRl("wood_chest"), "wood");
        final Pair<ResourceLocation, String> iron = Pair.of(ExpandedStorage.getRl("iron_chest"), "iron");
        final Pair<ResourceLocation, String> gold = Pair.of(ExpandedStorage.getRl("gold_chest"), "gold");
        final Pair<ResourceLocation, String> diamond = Pair.of(ExpandedStorage.getRl("diamond_chest"), "diamond");
        final Pair<ResourceLocation, String> obsidian = Pair.of(ExpandedStorage.getRl("obsidian_chest"), "obsidian");
        final Pair<ResourceLocation, String> netherite = Pair.of(ExpandedStorage.getRl("netherite_chest"), "netherite");
        CONVERSION_KIT_WOOD_IRON = new OLD_ChestConversionItem(wood, iron);
        CONVERSION_KIT_WOOD_GOLD = new OLD_ChestConversionItem(wood, gold);
        CONVERSION_KIT_WOOD_DIAMOND = new OLD_ChestConversionItem(wood, diamond);
        CONVERSION_KIT_WOOD_OBSIDIAN = new OLD_ChestConversionItem(wood, obsidian);
        CONVERSION_KIT_WOOD_NETHERITE = new OLD_ChestConversionItem(wood, netherite);
        CONVERSION_KIT_IRON_GOLD = new OLD_ChestConversionItem(iron, gold);
        CONVERSION_KIT_IRON_DIAMOND = new OLD_ChestConversionItem(iron, diamond);
        CONVERSION_KIT_IRON_OBSIDIAN = new OLD_ChestConversionItem(iron, obsidian);
        CONVERSION_KIT_IRON_NETHERITE = new OLD_ChestConversionItem(iron, netherite);
        CONVERSION_KIT_GOLD_DIAMOND = new OLD_ChestConversionItem(gold, diamond);
        CONVERSION_KIT_GOLD_OBSIDIAN = new OLD_ChestConversionItem(gold, obsidian);
        CONVERSION_KIT_GOLD_NETHERITE = new OLD_ChestConversionItem(gold, netherite);
        CONVERSION_KIT_DIAMOND_OBSIDIAN = new OLD_ChestConversionItem(diamond, obsidian);
        CONVERSION_KIT_DIAMOND_NETHERITE = new OLD_ChestConversionItem(diamond, netherite);
        CONVERSION_KIT_OBSIDIAN_NETHERITE = new OLD_ChestConversionItem(obsidian, netherite);
    }

    private static Pair<OLD_CursedChestBlock, ItemBlock> register(final Block copy, final String name, final int rows)
    {
        final ResourceLocation registryRl = ExpandedStorage.getRl(name);
        final OLD_CursedChestBlock block = new OLD_CursedChestBlock(registryRl);
        //new Item.Properties().setISTER(() -> CursedChestTileEntityItemStackRenderer::new)
        //                     .group(ExpandedStorage.creativeTab)
        final ItemBlock item = new ItemBlock(block).setTileEntityItemStackRenderer()
                .setTileEntityItemStackRenderer();
        item.setRegistryName(registryRl);
        Registries.MODELED.putObject(registryRl, new ModeledTierData(rows * 9, registryRl, new TextComponentTranslation("container.expandedstorage." + name), type -> ExpandedStorage.getRl(String.format("entity/%s/%s", name, type.getString()))));
        return Pair.of(block, item);
    }

    private static Pair<OLD_OldChestBlock, ItemBlock> registerOld(final Block copy, final String name, final int rows)
    {
        final ResourceLocation registryRl = ExpandedStorage.getRl("old_" + name);
        final OLD_OldChestBlock block = new OLD_OldChestBlock(Block.Properties.from(copy), registryRl);
        final BlockItem item = new BlockItem(block, new Item.Properties().group(ExpandedStorage.creativeTab));
        item.setRegistryName(registryRl);
        Registry.register(Registries.OLD, ExpandedStorage.getRl(name), new Registries.TierData(rows * 9, registryRl,
                new TranslationTextComponent("container.expandedstorage." + name)));
        return Pair.of(block, item);
    }

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event)
    {
        event.getRegistry().registerAll(
                WOOD_CHEST.getLeft(),
                PUMPKIN_CHEST.getLeft(),
                CHRISTMAS_CHEST.getLeft(),
                IRON_CHEST.getLeft(),
                GOLD_CHEST.getLeft(),
                DIAMOND_CHEST.getLeft(),
                OBSIDIAN_CHEST.getLeft(),
                NETHERITE_CHEST.getLeft(),
                OLD_WOOD_CHEST.getLeft(),
                OLD_IRON_CHEST.getLeft(),
                OLD_GOLD_CHEST.getLeft(),
                OLD_DIAMOND_CHEST.getLeft(),
                OLD_OBSIDIAN_CHEST.getLeft(),
                OLD_NETHERITE_CHEST.getLeft()
        );
    }

    @SubscribeEvent
    public static void registerBlockItems(final RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(
                WOOD_CHEST.getRight(),
                PUMPKIN_CHEST.getRight(),
                CHRISTMAS_CHEST.getRight(),
                IRON_CHEST.getRight(),
                GOLD_CHEST.getRight(),
                DIAMOND_CHEST.getRight(),
                OBSIDIAN_CHEST.getRight(),
                NETHERITE_CHEST.getRight(),
                OLD_WOOD_CHEST.getRight(),
                OLD_IRON_CHEST.getRight(),
                OLD_GOLD_CHEST.getRight(),
                OLD_DIAMOND_CHEST.getRight(),
                OLD_OBSIDIAN_CHEST.getRight(),
                OLD_NETHERITE_CHEST.getRight(),
                CONVERSION_KIT_WOOD_IRON,
                CONVERSION_KIT_WOOD_GOLD,
                CONVERSION_KIT_WOOD_DIAMOND,
                CONVERSION_KIT_WOOD_OBSIDIAN,
                CONVERSION_KIT_WOOD_NETHERITE,
                CONVERSION_KIT_IRON_GOLD,
                CONVERSION_KIT_IRON_DIAMOND,
                CONVERSION_KIT_IRON_OBSIDIAN,
                CONVERSION_KIT_IRON_NETHERITE,
                CONVERSION_KIT_GOLD_DIAMOND,
                CONVERSION_KIT_GOLD_OBSIDIAN,
                CONVERSION_KIT_GOLD_NETHERITE,
                CONVERSION_KIT_DIAMOND_OBSIDIAN,
                CONVERSION_KIT_DIAMOND_NETHERITE,
                CONVERSION_KIT_OBSIDIAN_NETHERITE,
                CHEST_MUTATOR
        );
    }

    //@SubscribeEvent
    //public static void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> event)
    //{ event.getRegistry().registerAll(CURSED_CHEST_TE, OLD_CHEST_TE); }

    //@SubscribeEvent
    //public static void registerContainerTypes(final RegistryEvent.Register<ContainerType<?>> event)
    //{
    //    event.getRegistry().register(SCROLLABLE_CONTAINER_TYPE);
    //    event.getRegistry().register(PAGED_CONTAINER_TYPE);
    //    event.getRegistry().register(SINGLE_CONTAINER_TYPE);
    //    if (FMLLoader.getDist().isClient()) { registerScreenFactory(); }
    //}

    //public static void registerScreenFactory()
    //{
    //    ScreenManager.registerFactory(SCROLLABLE_CONTAINER_TYPE, ScrollableScreen::new);
    //    ScreenManager.registerFactory(PAGED_CONTAINER_TYPE, PagedScreen::new);
    //    ScreenManager.registerFactory(SINGLE_CONTAINER_TYPE, SingleScreen::new);
    //}
}