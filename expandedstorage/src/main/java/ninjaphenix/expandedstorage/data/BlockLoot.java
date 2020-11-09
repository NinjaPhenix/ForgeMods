package ninjaphenix.expandedstorage.data;

import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraftforge.registries.ForgeRegistries;
import ninjaphenix.expandedstorage.ExpandedStorage;
import ninjaphenix.expandedstorage.ModContent;

import java.util.stream.Collectors;

public final class BlockLoot extends BlockLootTables
{
    @Override
    protected void addTables()
    {
        add(ModContent.WOOD_CHEST.getFirst(), BlockLootTables::createNameableBlockEntityTable);
        add(ModContent.PUMPKIN_CHEST.getFirst(), BlockLootTables::createNameableBlockEntityTable);
        add(ModContent.CHRISTMAS_CHEST.getFirst(), BlockLootTables::createNameableBlockEntityTable);
        add(ModContent.IRON_CHEST.getFirst(), BlockLootTables::createNameableBlockEntityTable);
        add(ModContent.GOLD_CHEST.getFirst(), BlockLootTables::createNameableBlockEntityTable);
        add(ModContent.DIAMOND_CHEST.getFirst(), BlockLootTables::createNameableBlockEntityTable);
        add(ModContent.OBSIDIAN_CHEST.getFirst(), BlockLootTables::createNameableBlockEntityTable);
        add(ModContent.NETHERITE_CHEST.getFirst(), BlockLootTables::createNameableBlockEntityTable);
        add(ModContent.OLD_WOOD_CHEST.getFirst(), BlockLootTables::createNameableBlockEntityTable);
        add(ModContent.OLD_IRON_CHEST.getFirst(), BlockLootTables::createNameableBlockEntityTable);
        add(ModContent.OLD_GOLD_CHEST.getFirst(), BlockLootTables::createNameableBlockEntityTable);
        add(ModContent.OLD_DIAMOND_CHEST.getFirst(), BlockLootTables::createNameableBlockEntityTable);
        add(ModContent.OLD_OBSIDIAN_CHEST.getFirst(), BlockLootTables::createNameableBlockEntityTable);
        add(ModContent.OLD_NETHERITE_CHEST.getFirst(), BlockLootTables::createNameableBlockEntityTable);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected Iterable<Block> getKnownBlocks()
    {
        return ForgeRegistries.BLOCKS.getValues().stream().filter(block -> ExpandedStorage.MOD_ID.equals(block.getRegistryName().getNamespace()))
                                     .collect(Collectors.toSet());
    }
}