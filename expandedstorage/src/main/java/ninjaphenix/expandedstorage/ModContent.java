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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import ninjaphenix.expandedstorage.api.Registries;
import ninjaphenix.expandedstorage.api.Registries.ModeledTierData;
import ninjaphenix.expandedstorage.api.block.CursedChestBlock;
import ninjaphenix.expandedstorage.api.block.OldChestBlock;
import ninjaphenix.expandedstorage.api.block.entity.CursedChestTileEntity;
import ninjaphenix.expandedstorage.api.block.entity.CustomTileEntityType;
import ninjaphenix.expandedstorage.api.block.entity.OldChestTileEntity;
import ninjaphenix.expandedstorage.api.client.gui.screen.ingame.ScrollableScreen;
import ninjaphenix.expandedstorage.api.container.ScrollableContainer;
import ninjaphenix.expandedstorage.api.item.ChestConversionItem;
import ninjaphenix.expandedstorage.client.render.CursedChestTileEntityItemStackRenderer;
import ninjaphenix.expandedstorage.item.ChestMutatorItem;

import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;

@Mod.EventBusSubscriber(modid = ExpandedStorage.MOD_ID, bus = MOD)
public class ModContent
{
	public static final ContainerType<ScrollableContainer> SCROLLABLE_CONTAINER_TYPE;
	public static final Pair<OldChestBlock, BlockItem> OLD_WOOD_CHEST;
	public static final Pair<OldChestBlock, BlockItem> OLD_IRON_CHEST;
	public static final Pair<OldChestBlock, BlockItem> OLD_GOLD_CHEST;
	public static final Pair<OldChestBlock, BlockItem> OLD_DIAMOND_CHEST;
	public static final Pair<OldChestBlock, BlockItem> OLD_OBSIDIAN_CHEST;

	public static final Pair<CursedChestBlock, BlockItem> WOOD_CHEST;
	public static final Pair<CursedChestBlock, BlockItem> PUMPKIN_CHEST;
	public static final Pair<CursedChestBlock, BlockItem> CHRISTMAS_CHEST;
	public static final Pair<CursedChestBlock, BlockItem> IRON_CHEST;
	public static final Pair<CursedChestBlock, BlockItem> GOLD_CHEST;
	public static final Pair<CursedChestBlock, BlockItem> DIAMOND_CHEST;
	public static final Pair<CursedChestBlock, BlockItem> OBSIDIAN_CHEST;

	public static final CustomTileEntityType<CursedChestTileEntity> CURSED_CHEST_TE;
	public static final CustomTileEntityType<OldChestTileEntity> OLD_CHEST_TE;

	public static final ChestMutatorItem CHEST_MUTATOR;

	public static final ChestConversionItem CONVERSION_KIT_WOOD_IRON;
	public static final ChestConversionItem CONVERSION_KIT_WOOD_GOLD;
	public static final ChestConversionItem CONVERSION_KIT_WOOD_DIAMOND;
	public static final ChestConversionItem CONVERSION_KIT_WOOD_OBSIDIAN;

	public static final ChestConversionItem CONVERSION_KIT_IRON_GOLD;
	public static final ChestConversionItem CONVERSION_KIT_IRON_DIAMOND;
	public static final ChestConversionItem CONVERSION_KIT_IRON_OBSIDIAN;

	public static final ChestConversionItem CONVERSION_KIT_GOLD_DIAMOND;
	public static final ChestConversionItem CONVERSION_KIT_GOLD_OBSIDIAN;

	public static final ChestConversionItem CONVERSION_KIT_DIAMOND_OBSIDIAN;

	static
	{
		OLD_WOOD_CHEST = registerOld(Blocks.OAK_PLANKS, "wood_chest", 3);
		OLD_IRON_CHEST = registerOld(Blocks.IRON_BLOCK, "iron_chest", 6);
		OLD_GOLD_CHEST = registerOld(Blocks.GOLD_BLOCK, "gold_chest", 9);
		OLD_DIAMOND_CHEST = registerOld(Blocks.DIAMOND_BLOCK, "diamond_chest", 12);
		OLD_OBSIDIAN_CHEST = registerOld(Blocks.OBSIDIAN, "obsidian_chest", 12);

		WOOD_CHEST = register(Blocks.OAK_PLANKS, "wood_chest", 3);
		PUMPKIN_CHEST = register(Blocks.CARVED_PUMPKIN, "pumpkin_chest", 3);
		CHRISTMAS_CHEST = register(Blocks.OAK_PLANKS, "christmas_chest", 3);
		IRON_CHEST = register(Blocks.IRON_BLOCK, "iron_chest", 6);
		GOLD_CHEST = register(Blocks.GOLD_BLOCK, "gold_chest", 9);
		DIAMOND_CHEST = register(Blocks.DIAMOND_BLOCK, "diamond_chest", 12);
		OBSIDIAN_CHEST = register(Blocks.OBSIDIAN, "obsidian_chest", 12);

		CURSED_CHEST_TE = new CustomTileEntityType<>(CursedChestTileEntity::new, (b) -> b instanceof CursedChestBlock, ExpandedStorage.getRl("cursed_chest"));
		OLD_CHEST_TE = new CustomTileEntityType<>(OldChestTileEntity::new, (b) -> b instanceof OldChestBlock, ExpandedStorage.getRl("old_cursed_chest"));

		SCROLLABLE_CONTAINER_TYPE = new ContainerType<>(new ScrollableContainer.Factory());
		SCROLLABLE_CONTAINER_TYPE.setRegistryName(ExpandedStorage.getRl("scrollable_container"));

		CHEST_MUTATOR = new ChestMutatorItem();
		CHEST_MUTATOR.setRegistryName(ExpandedStorage.getRl("chest_mutator"));

		Pair<ResourceLocation, String> wood = new Pair<>(ExpandedStorage.getRl("wood_chest"), "wood");
		Pair<ResourceLocation, String> iron = new Pair<>(ExpandedStorage.getRl("iron_chest"), "iron");
		Pair<ResourceLocation, String> gold = new Pair<>(ExpandedStorage.getRl("gold_chest"), "gold");
		Pair<ResourceLocation, String> diamond = new Pair<>(ExpandedStorage.getRl("diamond_chest"), "diamond");
		Pair<ResourceLocation, String> obsidian = new Pair<>(ExpandedStorage.getRl("obsidian_chest"), "obsidian");
		CONVERSION_KIT_WOOD_IRON = getConversionItem(wood, iron);
		CONVERSION_KIT_WOOD_GOLD = getConversionItem(wood, gold);
		CONVERSION_KIT_WOOD_DIAMOND = getConversionItem(wood, diamond);
		CONVERSION_KIT_WOOD_OBSIDIAN = getConversionItem(wood, obsidian);
		CONVERSION_KIT_IRON_GOLD = getConversionItem(iron, gold);
		CONVERSION_KIT_IRON_DIAMOND = getConversionItem(iron, diamond);
		CONVERSION_KIT_IRON_OBSIDIAN = getConversionItem(iron, obsidian);
		CONVERSION_KIT_GOLD_DIAMOND = getConversionItem(gold, diamond);
		CONVERSION_KIT_GOLD_OBSIDIAN = getConversionItem(gold, obsidian);
		CONVERSION_KIT_DIAMOND_OBSIDIAN = getConversionItem(diamond, obsidian);
	}

	private static Pair<CursedChestBlock, BlockItem> register(final Block copy, final String name, final int rows)
	{
		final ResourceLocation registryRl = ExpandedStorage.getRl(name);
		final CursedChestBlock block = new CursedChestBlock(Block.Properties.from(copy), registryRl);
		final BlockItem item = new BlockItem(block,
				new Item.Properties().setISTER(() -> CursedChestTileEntityItemStackRenderer::new).group(ExpandedStorage.group));
		item.setRegistryName(registryRl);
        Registry.register(Registries.MODELED, registryRl, new ModeledTierData(rows * 9, registryRl,
                new TranslationTextComponent("container.expandedstorage." + name),
                type -> ExpandedStorage.getRl(String.format("entity/%s/%s", name, type.getName()))));
		return new Pair<>(block, item);
	}

	private static Pair<OldChestBlock, BlockItem> registerOld(final Block copy, final String name, final int rows)
	{
		final ResourceLocation registryRl = ExpandedStorage.getRl("old_" + name);
		final OldChestBlock block = new OldChestBlock(Block.Properties.from(copy), registryRl);
		final BlockItem item = new BlockItem(block, new Item.Properties().group(ExpandedStorage.group));
		item.setRegistryName(registryRl);
		Registries.OLD.register(ExpandedStorage.getRl(name), new Registries.TierData(rows * 9, registryRl,
				new TranslationTextComponent("container.expandedstorage." + name)));
		return new Pair<>(block, item);
	}

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event)
	{
		final IForgeRegistry<Block> registry = event.getRegistry();
		registry.registerAll(
				WOOD_CHEST.getFirst(),
				PUMPKIN_CHEST.getFirst(),
				CHRISTMAS_CHEST.getFirst(),
				IRON_CHEST.getFirst(),
				GOLD_CHEST.getFirst(),
				DIAMOND_CHEST.getFirst(),
				OBSIDIAN_CHEST.getFirst(),
				OLD_WOOD_CHEST.getFirst(),
				OLD_IRON_CHEST.getFirst(),
				OLD_GOLD_CHEST.getFirst(),
				OLD_DIAMOND_CHEST.getFirst(),
				OLD_OBSIDIAN_CHEST.getFirst()
		);
	}

	@SubscribeEvent
	public static void registerBlockItems(final RegistryEvent.Register<Item> event)
	{
		final IForgeRegistry<Item> registry = event.getRegistry();
		registry.registerAll(
				WOOD_CHEST.getSecond(),
				PUMPKIN_CHEST.getSecond(),
				CHRISTMAS_CHEST.getSecond(),
				IRON_CHEST.getSecond(),
				GOLD_CHEST.getSecond(),
				DIAMOND_CHEST.getSecond(),
				OBSIDIAN_CHEST.getSecond(),
				OLD_WOOD_CHEST.getSecond(),
				OLD_IRON_CHEST.getSecond(),
				OLD_GOLD_CHEST.getSecond(),
				OLD_DIAMOND_CHEST.getSecond(),
				OLD_OBSIDIAN_CHEST.getSecond(),
				CONVERSION_KIT_WOOD_IRON,
				CONVERSION_KIT_WOOD_GOLD,
				CONVERSION_KIT_WOOD_DIAMOND,
				CONVERSION_KIT_WOOD_OBSIDIAN,
				CONVERSION_KIT_IRON_GOLD,
				CONVERSION_KIT_IRON_DIAMOND,
				CONVERSION_KIT_IRON_OBSIDIAN,
				CONVERSION_KIT_GOLD_DIAMOND,
				CONVERSION_KIT_GOLD_OBSIDIAN,
				CONVERSION_KIT_DIAMOND_OBSIDIAN,
				CHEST_MUTATOR
		);
	}

	@SubscribeEvent
	public static void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> event)
	{
		final IForgeRegistry<TileEntityType<?>> registry = event.getRegistry();
		registry.registerAll(CURSED_CHEST_TE, OLD_CHEST_TE);
	}

	@SubscribeEvent
	public static void registerContainerTypes(final RegistryEvent.Register<ContainerType<?>> event)
	{
		final IForgeRegistry<ContainerType<?>> registry = event.getRegistry();
		registry.register(SCROLLABLE_CONTAINER_TYPE);
		DistExecutor.runWhenOn(Dist.CLIENT, () -> ModContent::registerScreenFactory);
	}

	public static void registerScreenFactory() { ScreenManager.registerFactory(SCROLLABLE_CONTAINER_TYPE, ScrollableScreen::new); }

	private static ChestConversionItem getConversionItem(final Pair<ResourceLocation, String> from, final Pair<ResourceLocation, String> to)
	{
		final ChestConversionItem conversionItem = new ChestConversionItem(from.getFirst(), to.getFirst());
		conversionItem.setRegistryName(ExpandedStorage.getRl(from.getSecond() + "_to_" + to.getSecond() + "_conversion_kit"));
		return conversionItem;
	}
}
