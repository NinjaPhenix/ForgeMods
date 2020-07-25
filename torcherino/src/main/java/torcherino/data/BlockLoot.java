package torcherino.data;

import java.util.stream.Collectors;
import net.minecraft.advancements.criterion.EntityFlagsPredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.EntityHasProperty;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.CopyName;
import net.minecraftforge.fml.RegistryObject;
import torcherino.ModContent;

import static torcherino.data.ModContentHolder.*;

public final class BlockLoot extends BlockLootTables
{
    private final ILootCondition.IBuilder SELF_IS_SNEAKING = EntityHasProperty.builder(LootContext.EntityTarget.THIS, EntityPredicate.Builder.create().flags(new EntityFlagsPredicate(null, true, null, null, null)));

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void addTables()
    {
        registerLootTable(TORCHERINO, BlockLootTables::droppingWithName);
        registerLootTable(COMPRESSED_TORCHERINO, BlockLootTables::droppingWithName);
        registerLootTable(DOUBLE_COMPRESSED_TORCHERINO, BlockLootTables::droppingWithName);

        registerLootTable(LANTERN, lanterinoDropFunction(LANTERN, TORCHERINO));
        registerLootTable(COMPRESSED_LANTERN, lanterinoDropFunction(COMPRESSED_LANTERN, COMPRESSED_TORCHERINO));
        registerLootTable(DOUBLE_COMPRESSED_LANTERN, lanterinoDropFunction(DOUBLE_COMPRESSED_LANTERN, DOUBLE_COMPRESSED_TORCHERINO));

        registerLootTable(LANTERN, jackOLanterinoDropFunction(LANTERINO, TORCHERINO));
        registerLootTable(COMPRESSED_LANTERINO, jackOLanterinoDropFunction(COMPRESSED_LANTERINO, COMPRESSED_TORCHERINO));
        registerLootTable(DOUBLE_COMPRESSED_LANTERINO, jackOLanterinoDropFunction(DOUBLE_COMPRESSED_LANTERINO, DOUBLE_COMPRESSED_TORCHERINO));
    }

    private LootTable.Builder lanterinoDropFunction(final Block lantern, final Block torcherino)
    {
        final LootPool.Builder dropTorcherino = LootPool.builder().rolls(ConstantRange.of(1)).addEntry(AlternativesLootEntry.builder().alternatively(ItemLootEntry.builder(Items.LANTERN).acceptCondition(SELF_IS_SNEAKING)).alternatively(withSurvivesExplosion(lantern, ItemLootEntry.builder(lantern).acceptFunction(CopyName.builder(CopyName.Source.BLOCK_ENTITY)))));
        final LootPool.Builder dropLanternOrLanterino = LootPool.builder().rolls(ConstantRange.of(1)).addEntry(withSurvivesExplosion(torcherino, ItemLootEntry.builder(torcherino).acceptFunction(CopyName.builder(CopyName.Source.BLOCK_ENTITY)).acceptCondition(SELF_IS_SNEAKING)));
        return LootTable.builder().addLootPool(dropLanternOrLanterino).addLootPool(dropTorcherino);
    }

    private LootTable.Builder jackOLanterinoDropFunction(final Block lanterino, final Block torcherino)
    {
        final LootPool.Builder dropTorcherino = LootPool.builder().rolls(ConstantRange.of(1)).addEntry(AlternativesLootEntry.builder().alternatively(ItemLootEntry.builder(Items.CARVED_PUMPKIN).acceptCondition(SELF_IS_SNEAKING)).alternatively(withSurvivesExplosion(lanterino, ItemLootEntry.builder(lanterino).acceptFunction(CopyName.builder(CopyName.Source.BLOCK_ENTITY)))));
        final LootPool.Builder dropLanternOrLanterino = LootPool.builder().rolls(ConstantRange.of(1)).addEntry(withSurvivesExplosion(torcherino, ItemLootEntry.builder(torcherino).acceptFunction(CopyName.builder(CopyName.Source.BLOCK_ENTITY)).acceptCondition(SELF_IS_SNEAKING)));
        return LootTable.builder().addLootPool(dropLanternOrLanterino).addLootPool(dropTorcherino);
    }

    @Override
    protected Iterable<Block> getKnownBlocks()
    {
        return ModContent.BLOCKS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toSet());
    }
}