package ninjaphenix.expandedstorage.data;

import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraftforge.registries.ForgeRegistries;
import ninjaphenix.expandedstorage.ExpandedStorage;
import ninjaphenix.expandedstorage.ModContent;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

public class BlockLoot extends BlockLootTables
{
    @Override
    protected void addTables()
    {
        registerLootTable(ModContent.WOOD_CHEST.getFirst(), BlockLootTables::droppingWithName);
        registerLootTable(ModContent.PUMPKIN_CHEST.getFirst(), BlockLootTables::droppingWithName);
        registerLootTable(ModContent.CHRISTMAS_CHEST.getFirst(), BlockLootTables::droppingWithName);
        registerLootTable(ModContent.IRON_CHEST.getFirst(), BlockLootTables::droppingWithName);
        registerLootTable(ModContent.GOLD_CHEST.getFirst(), BlockLootTables::droppingWithName);
        registerLootTable(ModContent.DIAMOND_CHEST.getFirst(), BlockLootTables::droppingWithName);
        registerLootTable(ModContent.OBSIDIAN_CHEST.getFirst(), BlockLootTables::droppingWithName);
        registerLootTable(ModContent.NETHERITE_CHEST.getFirst(), BlockLootTables::droppingWithName);
        registerLootTable(ModContent.OLD_WOOD_CHEST.getFirst(), BlockLootTables::droppingWithName);
        registerLootTable(ModContent.OLD_IRON_CHEST.getFirst(), BlockLootTables::droppingWithName);
        registerLootTable(ModContent.OLD_GOLD_CHEST.getFirst(), BlockLootTables::droppingWithName);
        registerLootTable(ModContent.OLD_DIAMOND_CHEST.getFirst(), BlockLootTables::droppingWithName);
        registerLootTable(ModContent.OLD_OBSIDIAN_CHEST.getFirst(), BlockLootTables::droppingWithName);
        registerLootTable(ModContent.OLD_NETHERITE_CHEST.getFirst(), BlockLootTables::droppingWithName);
    }

    @NotNull @Override @SuppressWarnings("ConstantConditions")
    protected Iterable<Block> getKnownBlocks()
    {
        return ForgeRegistries.BLOCKS.getValues().stream().filter(block -> ExpandedStorage.MOD_ID.equals(block.getRegistryName().getNamespace()))
                                     .collect(Collectors.toSet());
    }
}