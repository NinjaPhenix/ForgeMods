package ninjaphenix.expandedstorage.data;

import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import ninjaphenix.expandedstorage.ExpandedStorage;
import ninjaphenix.expandedstorage.ModContent;
import ninjaphenix.expandedstorage.common.block.CursedChestBlock;
import ninjaphenix.expandedstorage.common.block.OldChestBlock;

import java.util.function.Consumer;

public final class Recipes extends RecipeProvider
{
    public Recipes(final DataGenerator generator) { super(generator); }

    @Override
    protected void buildShapelessRecipes(final Consumer<IFinishedRecipe> consumer)
    {
        // <editor-fold desc="local copy of blocks cause they're stored as Pair<Block, Item> in my class">
        final CursedChestBlock WOOD_CHEST = ModContent.WOOD_CHEST.getFirst();
        final CursedChestBlock PUMPKIN_CHEST = ModContent.PUMPKIN_CHEST.getFirst();
        final CursedChestBlock CHRISTMAS_CHEST = ModContent.CHRISTMAS_CHEST.getFirst();
        final CursedChestBlock IRON_CHEST = ModContent.IRON_CHEST.getFirst();
        final CursedChestBlock GOLD_CHEST = ModContent.GOLD_CHEST.getFirst();
        final CursedChestBlock DIAMOND_CHEST = ModContent.DIAMOND_CHEST.getFirst();
        final CursedChestBlock OBSIDIAN_CHEST = ModContent.OBSIDIAN_CHEST.getFirst();
        final CursedChestBlock NETHERITE_CHEST = ModContent.NETHERITE_CHEST.getFirst();
        final OldChestBlock OLD_WOOD_CHEST = ModContent.OLD_WOOD_CHEST.getFirst();
        final OldChestBlock OLD_IRON_CHEST = ModContent.OLD_IRON_CHEST.getFirst();
        final OldChestBlock OLD_GOLD_CHEST = ModContent.OLD_GOLD_CHEST.getFirst();
        final OldChestBlock OLD_DIAMOND_CHEST = ModContent.OLD_DIAMOND_CHEST.getFirst();
        final OldChestBlock OLD_OBSIDIAN_CHEST = ModContent.OLD_OBSIDIAN_CHEST.getFirst();
        final OldChestBlock OLD_NETHERITE_CHEST = ModContent.OLD_NETHERITE_CHEST.getFirst();
        // </editor-fold>
        // <editor-fold desc="Mutator Item Recipe">
        ShapedRecipeBuilder.shaped(ModContent.CHEST_MUTATOR)
                           .pattern("  C")
                           .pattern(" S ")
                           .pattern("S  ")
                           .define('S', Items.STICK)
                           .define('C', WOOD_CHEST)
                           .unlockedBy("has_chest", has(WOOD_CHEST))
                           .save(consumer);
        // </editor-fold>
        // <editor-fold desc="Regular Chest Recipes">
        ShapelessRecipeBuilder.shapeless(WOOD_CHEST)
                              .requires(Items.CHEST)
                              .unlockedBy("has_chest", has(Items.CHEST))
                              .save(consumer);
        ShapedRecipeBuilder.shaped(PUMPKIN_CHEST)
                           .pattern("SSS")
                           .pattern("SCS")
                           .pattern("SSS")
                           .define('S', Items.PUMPKIN_SEEDS)
                           .define('C', Tags.Items.CHESTS_WOODEN)
                           .unlockedBy("has_chest", has(Tags.Items.CHESTS_WOODEN))
                           .save(consumer);
        ShapedRecipeBuilder.shaped(CHRISTMAS_CHEST)
                           .pattern(" B ")
                           .pattern("RCW")
                           .pattern(" S ")
                           .define('S', Items.SPRUCE_SAPLING)
                           .define('B', Items.SWEET_BERRIES)
                           .define('R', Tags.Items.DYES_RED)
                           .define('W', Tags.Items.DYES_WHITE)
                           .define('C', Tags.Items.CHESTS_WOODEN)
                           .unlockedBy("has_chest", has(Tags.Items.CHESTS_WOODEN))
                           .save(consumer);
        ShapedRecipeBuilder.shaped(IRON_CHEST)
                           .pattern("III")
                           .pattern("ICI")
                           .pattern("III")
                           .define('I', Tags.Items.INGOTS_IRON)
                           .define('C', Tags.Items.CHESTS_WOODEN)
                           .unlockedBy("has_chest", has(Tags.Items.CHESTS_WOODEN))
                           .save(consumer);
        ShapedRecipeBuilder.shaped(GOLD_CHEST)
                           .pattern("III")
                           .pattern("ICI")
                           .pattern("III")
                           .define('I', Tags.Items.INGOTS_GOLD)
                           .define('C', IRON_CHEST)
                           .unlockedBy("has_chest", has(IRON_CHEST))
                           .save(consumer);
        ShapedRecipeBuilder.shaped(DIAMOND_CHEST)
                           .pattern("GGG")
                           .pattern("DCD")
                           .pattern("GGG")
                           .define('G', Tags.Items.GLASS)
                           .define('D', Tags.Items.GEMS_DIAMOND)
                           .define('C', GOLD_CHEST)
                           .unlockedBy("has_chest", has(GOLD_CHEST))
                           .save(consumer);
        ShapedRecipeBuilder.shaped(OBSIDIAN_CHEST)
                           .pattern("OOO")
                           .pattern("OCO")
                           .pattern("OOO")
                           .define('O', Tags.Items.OBSIDIAN)
                           .define('C', DIAMOND_CHEST)
                           .unlockedBy("has_chest", has(DIAMOND_CHEST))
                           .save(consumer);
        ShapelessRecipeBuilder.shapeless(NETHERITE_CHEST)
                              .requires(OBSIDIAN_CHEST)
                              .requires(Items.NETHERITE_INGOT)
                              .unlockedBy("has_chest", has(OBSIDIAN_CHEST))
                              .save(consumer);
        // </editor-fold>
        // <editor-fold desc="Regular <-> Old Converison Recipes">
        ShapelessRecipeBuilder.shapeless(OLD_WOOD_CHEST).requires(WOOD_CHEST)
                              .unlockedBy("has_chest", has(WOOD_CHEST)).save(consumer, ExpandedStorage.getRl("wood_to_old_wood"));
        ShapelessRecipeBuilder.shapeless(WOOD_CHEST).requires(OLD_WOOD_CHEST)
                              .unlockedBy("has_chest", has(OLD_WOOD_CHEST)).save(consumer, ExpandedStorage.getRl("old_wood_to_wood"));
        ShapelessRecipeBuilder.shapeless(OLD_IRON_CHEST).requires(IRON_CHEST)
                              .unlockedBy("has_chest", has(IRON_CHEST)).save(consumer, ExpandedStorage.getRl("iron_to_old_iron"));
        ShapelessRecipeBuilder.shapeless(IRON_CHEST).requires(OLD_IRON_CHEST)
                              .unlockedBy("has_chest", has(OLD_IRON_CHEST)).save(consumer, ExpandedStorage.getRl("old_iron_to_iron"));
        ShapelessRecipeBuilder.shapeless(OLD_GOLD_CHEST).requires(GOLD_CHEST)
                              .unlockedBy("has_chest", has(GOLD_CHEST)).save(consumer, ExpandedStorage.getRl("gold_to_old_gold"));
        ShapelessRecipeBuilder.shapeless(GOLD_CHEST).requires(OLD_GOLD_CHEST)
                              .unlockedBy("has_chest", has(OLD_GOLD_CHEST)).save(consumer, ExpandedStorage.getRl("old_gold_to_gold"));
        ShapelessRecipeBuilder.shapeless(OLD_DIAMOND_CHEST).requires(DIAMOND_CHEST)
                              .unlockedBy("has_chest", has(DIAMOND_CHEST)).save(consumer, ExpandedStorage.getRl("diamond_to_old_diamond"));
        ShapelessRecipeBuilder.shapeless(DIAMOND_CHEST).requires(OLD_DIAMOND_CHEST)
                              .unlockedBy("has_chest", has(OLD_DIAMOND_CHEST)).save(consumer, ExpandedStorage.getRl("old_diamond_to_diamond"));
        ShapelessRecipeBuilder.shapeless(OLD_OBSIDIAN_CHEST).requires(OBSIDIAN_CHEST)
                              .unlockedBy("has_chest", has(OBSIDIAN_CHEST)).save(consumer, ExpandedStorage.getRl("obsidian_to_old_obsidian"));
        ShapelessRecipeBuilder.shapeless(OBSIDIAN_CHEST).requires(OLD_OBSIDIAN_CHEST)
                              .unlockedBy("has_chest", has(OLD_OBSIDIAN_CHEST)).save(consumer, ExpandedStorage.getRl("old_obsidian_to_obsidian"));
        ShapelessRecipeBuilder.shapeless(OLD_NETHERITE_CHEST).requires(NETHERITE_CHEST)
                              .unlockedBy("has_chest", has(NETHERITE_CHEST)).save(consumer, ExpandedStorage.getRl("netherite_to_old_netherite"));
        ShapelessRecipeBuilder.shapeless(NETHERITE_CHEST).requires(OLD_NETHERITE_CHEST)
                              .unlockedBy("has_chest", has(OLD_NETHERITE_CHEST)).save(consumer, ExpandedStorage.getRl("old_netherite_to_netherite"));
        // </editor-fold>
        // <editor-fold desc="Wood Conversion Kits">
        ShapedRecipeBuilder.shaped(ModContent.CONVERSION_KIT_WOOD_IRON)
                           .pattern("III")
                           .pattern("IWI")
                           .pattern("III")
                           .define('I', Tags.Items.INGOTS_IRON)
                           .define('W', ItemTags.PLANKS)
                           .unlockedBy("has_planks", has(ItemTags.PLANKS))
                           .save(consumer);
        ShapedRecipeBuilder.shaped(ModContent.CONVERSION_KIT_WOOD_GOLD)
                           .pattern("III")
                           .pattern("IUI")
                           .pattern("III")
                           .define('I', Tags.Items.INGOTS_GOLD)
                           .define('U', ModContent.CONVERSION_KIT_WOOD_IRON)
                           .unlockedBy("has_kit", has(ModContent.CONVERSION_KIT_WOOD_IRON))
                           .save(consumer);
        ShapedRecipeBuilder.shaped(ModContent.CONVERSION_KIT_WOOD_DIAMOND)
                           .pattern("GGG")
                           .pattern("DUD")
                           .pattern("GGG")
                           .define('G', Tags.Items.GLASS)
                           .define('D', Tags.Items.GEMS_DIAMOND)
                           .define('U', ModContent.CONVERSION_KIT_WOOD_GOLD)
                           .unlockedBy("has_kit", has(ModContent.CONVERSION_KIT_WOOD_GOLD))
                           .save(consumer);
        ShapedRecipeBuilder.shaped(ModContent.CONVERSION_KIT_WOOD_OBSIDIAN)
                           .pattern("OOO")
                           .pattern("OUO")
                           .pattern("OOO")
                           .define('O', Tags.Items.OBSIDIAN)
                           .define('U', ModContent.CONVERSION_KIT_WOOD_DIAMOND)
                           .unlockedBy("has_kit", has(ModContent.CONVERSION_KIT_WOOD_DIAMOND))
                           .save(consumer);
        ShapelessRecipeBuilder.shapeless(ModContent.CONVERSION_KIT_WOOD_NETHERITE)
                              .requires(ModContent.CONVERSION_KIT_WOOD_OBSIDIAN)
                              .requires(Items.NETHERITE_INGOT)
                              .unlockedBy("has_kit", has(ModContent.CONVERSION_KIT_WOOD_OBSIDIAN))
                              .save(consumer);
        // </editor-fold>
        // <editor-fold desc="Iron Converison Kits">
        ShapedRecipeBuilder.shaped(ModContent.CONVERSION_KIT_IRON_GOLD)
                           .pattern("GGG")
                           .pattern("GIG")
                           .pattern("GGG")
                           .define('I', Tags.Items.INGOTS_IRON)
                           .define('G', Tags.Items.INGOTS_GOLD)
                           .unlockedBy("has_ingot", has(Tags.Items.INGOTS_IRON))
                           .save(consumer);
        ShapedRecipeBuilder.shaped(ModContent.CONVERSION_KIT_IRON_DIAMOND)
                           .pattern("GGG")
                           .pattern("DUD")
                           .pattern("GGG")
                           .define('G', Tags.Items.GLASS)
                           .define('D', Tags.Items.GEMS_DIAMOND)
                           .define('U', ModContent.CONVERSION_KIT_IRON_GOLD)
                           .unlockedBy("has_kit", has(ModContent.CONVERSION_KIT_IRON_GOLD))
                           .save(consumer);
        ShapedRecipeBuilder.shaped(ModContent.CONVERSION_KIT_IRON_OBSIDIAN)
                           .pattern("OOO")
                           .pattern("OUO")
                           .pattern("OOO")
                           .define('O', Tags.Items.OBSIDIAN)
                           .define('U', ModContent.CONVERSION_KIT_IRON_DIAMOND)
                           .unlockedBy("has_kit", has(ModContent.CONVERSION_KIT_IRON_DIAMOND))
                           .save(consumer);
        ShapelessRecipeBuilder.shapeless(ModContent.CONVERSION_KIT_IRON_NETHERITE)
                              .requires(ModContent.CONVERSION_KIT_IRON_OBSIDIAN)
                              .requires(Items.NETHERITE_INGOT)
                              .unlockedBy("has_kit", has(ModContent.CONVERSION_KIT_IRON_OBSIDIAN))
                              .save(consumer);
        // </editor-fold>
        // <editor-fold desc="Gold Conversion Kits">
        ShapedRecipeBuilder.shaped(ModContent.CONVERSION_KIT_GOLD_DIAMOND)
                           .pattern("GGG")
                           .pattern("DID")
                           .pattern("GGG")
                           .define('G', Tags.Items.GLASS)
                           .define('D', Tags.Items.GEMS_DIAMOND)
                           .define('I', Tags.Items.INGOTS_GOLD)
                           .unlockedBy("has_ingot", has(Tags.Items.INGOTS_GOLD))
                           .save(consumer);
        ShapedRecipeBuilder.shaped(ModContent.CONVERSION_KIT_GOLD_OBSIDIAN)
                           .pattern("OOO")
                           .pattern("OUO")
                           .pattern("OOO")
                           .define('O', Tags.Items.OBSIDIAN)
                           .define('U', ModContent.CONVERSION_KIT_GOLD_DIAMOND)
                           .unlockedBy("has_kit", has(ModContent.CONVERSION_KIT_GOLD_DIAMOND))
                           .save(consumer);
        ShapelessRecipeBuilder.shapeless(ModContent.CONVERSION_KIT_GOLD_NETHERITE)
                              .requires(ModContent.CONVERSION_KIT_GOLD_OBSIDIAN)
                              .requires(Items.NETHERITE_INGOT)
                              .unlockedBy("has_kit", has(ModContent.CONVERSION_KIT_GOLD_OBSIDIAN))
                              .save(consumer);
        // </editor-fold>
        // <editor-fold desc="Diamond Conversion Kits">
        ShapedRecipeBuilder.shaped(ModContent.CONVERSION_KIT_DIAMOND_OBSIDIAN)
                           .pattern("OOO")
                           .pattern("ODO")
                           .pattern("OOO")
                           .define('O', Tags.Items.OBSIDIAN)
                           .define('D', Tags.Items.GEMS_DIAMOND)
                           .unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND))
                           .save(consumer);
        ShapelessRecipeBuilder.shapeless(ModContent.CONVERSION_KIT_DIAMOND_NETHERITE)
                              .requires(ModContent.CONVERSION_KIT_DIAMOND_OBSIDIAN)
                              .requires(Items.NETHERITE_INGOT)
                              .unlockedBy("has_kit", has(ModContent.CONVERSION_KIT_DIAMOND_OBSIDIAN))
                              .save(consumer);
        // </editor-fold>
        // <editor-fold desc="Obsidian Conversion Kits">
        ShapelessRecipeBuilder.shapeless(ModContent.CONVERSION_KIT_OBSIDIAN_NETHERITE)
                              .requires(Tags.Items.OBSIDIAN)
                              .requires(Items.NETHERITE_INGOT)
                              .unlockedBy("has_obsidian", has(Tags.Items.OBSIDIAN))
                              .save(consumer);
        // </editor-fold>
    }

    @Override
    public String getName() { return "Expanded Storage - Recipes"; }
}