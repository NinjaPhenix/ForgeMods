package ninjaphenix.expandedstorage.data.loot;

import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraftforge.registries.ForgeRegistries;
import ninjaphenix.expandedstorage.ExpandedStorage;
import ninjaphenix.expandedstorage.ModContent;

import java.util.stream.Collectors;

public class Blocks extends BlockLootTables
{

    @Override
    protected void addTables()
    {
        chestLootTable(ModContent.WOOD_CHEST.getFirst());
        chestLootTable(ModContent.PUMPKIN_CHEST.getFirst());
        chestLootTable(ModContent.CHRISTMAS_CHEST.getFirst());
        chestLootTable(ModContent.IRON_CHEST.getFirst());
        chestLootTable(ModContent.GOLD_CHEST.getFirst());
        chestLootTable(ModContent.DIAMOND_CHEST.getFirst());
        chestLootTable(ModContent.OBSIDIAN_CHEST.getFirst());

        chestLootTable(ModContent.OLD_WOOD_CHEST.getFirst());
        chestLootTable(ModContent.OLD_IRON_CHEST.getFirst());
        chestLootTable(ModContent.OLD_GOLD_CHEST.getFirst());
        chestLootTable(ModContent.OLD_DIAMOND_CHEST.getFirst());
        chestLootTable(ModContent.OLD_OBSIDIAN_CHEST.getFirst());
    }

    private void chestLootTable(Block block) {
        registerLootTable(block, BlockLootTables::droppingWithName);
    }

    @Override
    protected Iterable<Block> getKnownBlocks()
    {
        return ForgeRegistries.BLOCKS.getValues().stream().filter(block ->
                ExpandedStorage.MOD_ID.equals(block.getRegistryName().getNamespace())).collect(Collectors.toSet());
    }
}
