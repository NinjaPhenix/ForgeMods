package ninjaphenix.expandedstorage.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninjaphenix.expandedstorage.ExpandedStorage;
import ninjaphenix.expandedstorage.Registries;
import ninjaphenix.expandedstorage.client.render.CursedChestTileEntityItemStackRenderer;
import ninjaphenix.expandedstorage.common.block.CursedChestBlock;
import ninjaphenix.expandedstorage.common.block.OldChestBlock;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = ExpandedStorage.MOD_ID)
public class ModContent
{
    public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(ExpandedStorage.MOD_ID)
    {
        @Override
        public ItemStack createIcon() { return new ItemStack(DIAMOND_CHEST.getRight()); }
    };

    public static final Pair<OldChestBlock, ItemBlock> OLD_WOOD_CHEST;
    public static final Pair<OldChestBlock, ItemBlock> OLD_IRON_CHEST;
    public static final Pair<OldChestBlock, ItemBlock> OLD_GOLD_CHEST;
    public static final Pair<OldChestBlock, ItemBlock> OLD_DIAMOND_CHEST;
    public static final Pair<OldChestBlock, ItemBlock> OLD_OBSIDIAN_CHEST;
    public static final Pair<CursedChestBlock, ItemBlock> WOOD_CHEST;
    public static final Pair<CursedChestBlock, ItemBlock> PUMPKIN_CHEST;
    public static final Pair<CursedChestBlock, ItemBlock> CHRISTMAS_CHEST;
    public static final Pair<CursedChestBlock, ItemBlock> IRON_CHEST;
    public static final Pair<CursedChestBlock, ItemBlock> GOLD_CHEST;
    public static final Pair<CursedChestBlock, ItemBlock> DIAMOND_CHEST;
    public static final Pair<CursedChestBlock, ItemBlock> OBSIDIAN_CHEST;

    static
    {
        OLD_WOOD_CHEST = old("wood_chest", 3, Blocks.PLANKS);
        OLD_IRON_CHEST = old("iron_chest", 6, Blocks.IRON_BLOCK);
        OLD_GOLD_CHEST = old("gold_chest", 9, Blocks.GOLD_BLOCK);
        OLD_DIAMOND_CHEST = old("diamond_chest", 12, Blocks.DIAMOND_BLOCK);
        OLD_OBSIDIAN_CHEST = old("obsidian_chest", 12, Blocks.OBSIDIAN);
        WOOD_CHEST = cursed("wood_chest", 3, Blocks.PLANKS);
        PUMPKIN_CHEST = cursed("pumpkin_chest", 3, Blocks.PUMPKIN);
        CHRISTMAS_CHEST = cursed("christmas_chest", 3, Blocks.PLANKS);
        IRON_CHEST = cursed("iron_chest", 6, Blocks.IRON_BLOCK);
        GOLD_CHEST = cursed("gold_chest", 9, Blocks.GOLD_BLOCK);
        DIAMOND_CHEST = cursed("diamond_chest", 12, Blocks.DIAMOND_BLOCK);
        OBSIDIAN_CHEST = cursed("obsidian_chest", 12, Blocks.OBSIDIAN);
    }

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event)
    {
        event.getRegistry().registerAll(OLD_WOOD_CHEST.getLeft(),
                                        OLD_IRON_CHEST.getLeft(),
                                        OLD_GOLD_CHEST.getLeft(),
                                        OLD_DIAMOND_CHEST.getLeft(),
                                        OLD_OBSIDIAN_CHEST.getLeft(),
                                        WOOD_CHEST.getLeft(),
                                        PUMPKIN_CHEST.getLeft(),
                                        CHRISTMAS_CHEST.getLeft(),
                                        IRON_CHEST.getLeft(),
                                        GOLD_CHEST.getLeft(),
                                        DIAMOND_CHEST.getLeft(),
                                        OBSIDIAN_CHEST.getLeft());
    }

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(OLD_WOOD_CHEST.getRight(),
                                        OLD_IRON_CHEST.getRight(),
                                        OLD_GOLD_CHEST.getRight(),
                                        OLD_DIAMOND_CHEST.getRight(),
                                        OLD_OBSIDIAN_CHEST.getRight(),
                                        WOOD_CHEST.getRight(),
                                        PUMPKIN_CHEST.getRight(),
                                        CHRISTMAS_CHEST.getRight(),
                                        IRON_CHEST.getRight(),
                                        GOLD_CHEST.getRight(),
                                        DIAMOND_CHEST.getRight(),
                                        OBSIDIAN_CHEST.getRight());
    }

    private static Pair<OldChestBlock, ItemBlock> old(final String name, final int rows, final Block copy)
    {
        final ResourceLocation registryRl = ExpandedStorage.getRl("old_" + name);
        final IBlockState state = copy.getDefaultState();
        final OldChestBlock block = new OldChestBlock(registryRl, copy.getMaterial(state), copy.getMapColor(state, null, null));
        final ItemBlock item = new ItemBlock(block);
        item.setRegistryName(registryRl).setCreativeTab(CREATIVE_TAB);
        Registries.OLD.putObject(ExpandedStorage.getRl(name), new Registries.TierData(rows * 9, registryRl, new TextComponentTranslation("container.expandedstorage." + name)));
        return Pair.of(block, item);
    }

    private static Pair<CursedChestBlock, ItemBlock> cursed(final String name, final int rows, final Block copy)
    {
        final ResourceLocation registryRl = ExpandedStorage.getRl(name);
        final IBlockState state = copy.getDefaultState();
        final CursedChestBlock block = new CursedChestBlock(registryRl, copy.getMaterial(state), copy.getMapColor(state, null, null));
        block.setHarvestLevel(copy.getHarvestTool(state), copy.getHarvestLevel(state));
        final ItemBlock item = new ItemBlock(block);
        item.setRegistryName(registryRl).setCreativeTab(CREATIVE_TAB).setTileEntityItemStackRenderer(CursedChestTileEntityItemStackRenderer.INSTANCE);
        Registries.MODELED.putObject(registryRl, new Registries.ModeledTierData(rows * 9, registryRl, new TextComponentTranslation("container.expandedstorage." + name), type -> ExpandedStorage.getRl(String.format("entity/%s/%s", name, type.getName()))));
        return Pair.of(block, item);
    }
}