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
    protected void registerRecipes(final Consumer<IFinishedRecipe> consumer)
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
        ShapedRecipeBuilder.shapedRecipe(ModContent.CHEST_MUTATOR)
                           .patternLine("  C")
                           .patternLine(" S ")
                           .patternLine("S  ")
                           .key('S', Items.STICK)
                           .key('C', WOOD_CHEST)
                           .addCriterion("has_chest", hasItem(WOOD_CHEST))
                           .build(consumer);
        // </editor-fold>
        // <editor-fold desc="Regular Chest Recipes">
        ShapelessRecipeBuilder.shapelessRecipe(WOOD_CHEST)
                              .addIngredient(Items.CHEST)
                              .addCriterion("has_chest", hasItem(Items.CHEST))
                              .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(PUMPKIN_CHEST)
                           .patternLine("SSS")
                           .patternLine("SCS")
                           .patternLine("SSS")
                           .key('S', Items.PUMPKIN_SEEDS)
                           .key('C', Tags.Items.CHESTS_WOODEN)
                           .addCriterion("has_chest", hasItem(Tags.Items.CHESTS_WOODEN))
                           .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(CHRISTMAS_CHEST)
                           .patternLine(" B ")
                           .patternLine("RCW")
                           .patternLine(" S ")
                           .key('S', Items.SPRUCE_SAPLING)
                           .key('B', Items.SWEET_BERRIES)
                           .key('R', Tags.Items.DYES_RED)
                           .key('W', Tags.Items.DYES_WHITE)
                           .key('C', Tags.Items.CHESTS_WOODEN)
                           .addCriterion("has_chest", hasItem(Tags.Items.CHESTS_WOODEN))
                           .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(IRON_CHEST)
                           .patternLine("III")
                           .patternLine("ICI")
                           .patternLine("III")
                           .key('I', Tags.Items.INGOTS_IRON)
                           .key('C', Tags.Items.CHESTS_WOODEN)
                           .addCriterion("has_chest", hasItem(Tags.Items.CHESTS_WOODEN))
                           .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(GOLD_CHEST)
                           .patternLine("III")
                           .patternLine("ICI")
                           .patternLine("III")
                           .key('I', Tags.Items.INGOTS_GOLD)
                           .key('C', IRON_CHEST)
                           .addCriterion("has_chest", hasItem(IRON_CHEST))
                           .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(DIAMOND_CHEST)
                           .patternLine("GGG")
                           .patternLine("DCD")
                           .patternLine("GGG")
                           .key('G', Tags.Items.GLASS)
                           .key('D', Tags.Items.GEMS_DIAMOND)
                           .key('C', GOLD_CHEST)
                           .addCriterion("has_chest", hasItem(GOLD_CHEST))
                           .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(OBSIDIAN_CHEST)
                           .patternLine("OOO")
                           .patternLine("OCO")
                           .patternLine("OOO")
                           .key('O', Tags.Items.OBSIDIAN)
                           .key('C', DIAMOND_CHEST)
                           .addCriterion("has_chest", hasItem(DIAMOND_CHEST))
                           .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(NETHERITE_CHEST)
                              .addIngredient(OBSIDIAN_CHEST)
                              .addIngredient(Items.NETHERITE_INGOT)
                              .addCriterion("has_chest", hasItem(OBSIDIAN_CHEST))
                              .build(consumer);
        // </editor-fold>
        // <editor-fold desc="Regular <-> Old Converison Recipes">
        ShapelessRecipeBuilder.shapelessRecipe(OLD_WOOD_CHEST).addIngredient(WOOD_CHEST)
                              .addCriterion("has_chest", hasItem(WOOD_CHEST)).build(consumer, ExpandedStorage.getRl("wood_to_old_wood"));
        ShapelessRecipeBuilder.shapelessRecipe(WOOD_CHEST).addIngredient(OLD_WOOD_CHEST)
                              .addCriterion("has_chest", hasItem(OLD_WOOD_CHEST)).build(consumer, ExpandedStorage.getRl("old_wood_to_wood"));
        ShapelessRecipeBuilder.shapelessRecipe(OLD_IRON_CHEST).addIngredient(IRON_CHEST)
                              .addCriterion("has_chest", hasItem(IRON_CHEST)).build(consumer, ExpandedStorage.getRl("iron_to_old_iron"));
        ShapelessRecipeBuilder.shapelessRecipe(IRON_CHEST).addIngredient(OLD_IRON_CHEST)
                              .addCriterion("has_chest", hasItem(OLD_IRON_CHEST)).build(consumer, ExpandedStorage.getRl("old_iron_to_iron"));
        ShapelessRecipeBuilder.shapelessRecipe(OLD_GOLD_CHEST).addIngredient(GOLD_CHEST)
                              .addCriterion("has_chest", hasItem(GOLD_CHEST)).build(consumer, ExpandedStorage.getRl("gold_to_old_gold"));
        ShapelessRecipeBuilder.shapelessRecipe(GOLD_CHEST).addIngredient(OLD_GOLD_CHEST)
                              .addCriterion("has_chest", hasItem(OLD_GOLD_CHEST)).build(consumer, ExpandedStorage.getRl("old_gold_to_gold"));
        ShapelessRecipeBuilder.shapelessRecipe(OLD_DIAMOND_CHEST).addIngredient(DIAMOND_CHEST)
                              .addCriterion("has_chest", hasItem(DIAMOND_CHEST)).build(consumer, ExpandedStorage.getRl("diamond_to_old_diamond"));
        ShapelessRecipeBuilder.shapelessRecipe(DIAMOND_CHEST).addIngredient(OLD_DIAMOND_CHEST)
                              .addCriterion("has_chest", hasItem(OLD_DIAMOND_CHEST)).build(consumer, ExpandedStorage.getRl("old_diamond_to_diamond"));
        ShapelessRecipeBuilder.shapelessRecipe(OLD_OBSIDIAN_CHEST).addIngredient(OBSIDIAN_CHEST)
                              .addCriterion("has_chest", hasItem(OBSIDIAN_CHEST)).build(consumer, ExpandedStorage.getRl("obsidian_to_old_obsidian"));
        ShapelessRecipeBuilder.shapelessRecipe(OBSIDIAN_CHEST).addIngredient(OLD_OBSIDIAN_CHEST)
                              .addCriterion("has_chest", hasItem(OLD_OBSIDIAN_CHEST)).build(consumer, ExpandedStorage.getRl("old_obsidian_to_obsidian"));
        ShapelessRecipeBuilder.shapelessRecipe(OLD_NETHERITE_CHEST).addIngredient(NETHERITE_CHEST)
                              .addCriterion("has_chest", hasItem(NETHERITE_CHEST)).build(consumer, ExpandedStorage.getRl("netherite_to_old_netherite"));
        ShapelessRecipeBuilder.shapelessRecipe(NETHERITE_CHEST).addIngredient(OLD_NETHERITE_CHEST)
                              .addCriterion("has_chest", hasItem(OLD_NETHERITE_CHEST)).build(consumer, ExpandedStorage.getRl("old_netherite_to_netherite"));
        // </editor-fold>
        // <editor-fold desc="Wood Conversion Kits">
        ShapedRecipeBuilder.shapedRecipe(ModContent.CONVERSION_KIT_WOOD_IRON)
                           .patternLine("III")
                           .patternLine("IWI")
                           .patternLine("III")
                           .key('I', Tags.Items.INGOTS_IRON)
                           .key('W', ItemTags.PLANKS)
                           .addCriterion("has_planks", hasItem(ItemTags.PLANKS))
                           .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModContent.CONVERSION_KIT_WOOD_GOLD)
                           .patternLine("III")
                           .patternLine("IUI")
                           .patternLine("III")
                           .key('I', Tags.Items.INGOTS_GOLD)
                           .key('U', ModContent.CONVERSION_KIT_WOOD_IRON)
                           .addCriterion("has_kit", hasItem(ModContent.CONVERSION_KIT_WOOD_IRON))
                           .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModContent.CONVERSION_KIT_WOOD_DIAMOND)
                           .patternLine("GGG")
                           .patternLine("DUD")
                           .patternLine("GGG")
                           .key('G', Tags.Items.GLASS)
                           .key('D', Tags.Items.GEMS_DIAMOND)
                           .key('U', ModContent.CONVERSION_KIT_WOOD_GOLD)
                           .addCriterion("has_kit", hasItem(ModContent.CONVERSION_KIT_WOOD_GOLD))
                           .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModContent.CONVERSION_KIT_WOOD_OBSIDIAN)
                           .patternLine("OOO")
                           .patternLine("OUO")
                           .patternLine("OOO")
                           .key('O', Tags.Items.OBSIDIAN)
                           .key('U', ModContent.CONVERSION_KIT_WOOD_DIAMOND)
                           .addCriterion("has_kit", hasItem(ModContent.CONVERSION_KIT_WOOD_DIAMOND))
                           .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModContent.CONVERSION_KIT_WOOD_NETHERITE)
                              .addIngredient(ModContent.CONVERSION_KIT_WOOD_OBSIDIAN)
                              .addIngredient(Items.NETHERITE_INGOT)
                              .addCriterion("has_kit", hasItem(ModContent.CONVERSION_KIT_WOOD_OBSIDIAN))
                              .build(consumer);
        // </editor-fold>
        // <editor-fold desc="Iron Converison Kits">
        ShapedRecipeBuilder.shapedRecipe(ModContent.CONVERSION_KIT_IRON_GOLD)
                           .patternLine("GGG")
                           .patternLine("GIG")
                           .patternLine("GGG")
                           .key('I', Tags.Items.INGOTS_IRON)
                           .key('G', Tags.Items.INGOTS_GOLD)
                           .addCriterion("has_ingot", hasItem(Tags.Items.INGOTS_IRON))
                           .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModContent.CONVERSION_KIT_IRON_DIAMOND)
                           .patternLine("GGG")
                           .patternLine("DUD")
                           .patternLine("GGG")
                           .key('G', Tags.Items.GLASS)
                           .key('D', Tags.Items.GEMS_DIAMOND)
                           .key('U', ModContent.CONVERSION_KIT_IRON_GOLD)
                           .addCriterion("has_kit", hasItem(ModContent.CONVERSION_KIT_IRON_GOLD))
                           .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModContent.CONVERSION_KIT_IRON_OBSIDIAN)
                           .patternLine("OOO")
                           .patternLine("OUO")
                           .patternLine("OOO")
                           .key('O', Tags.Items.OBSIDIAN)
                           .key('U', ModContent.CONVERSION_KIT_IRON_DIAMOND)
                           .addCriterion("has_kit", hasItem(ModContent.CONVERSION_KIT_IRON_DIAMOND))
                           .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModContent.CONVERSION_KIT_IRON_NETHERITE)
                              .addIngredient(ModContent.CONVERSION_KIT_IRON_OBSIDIAN)
                              .addIngredient(Items.NETHERITE_INGOT)
                              .addCriterion("has_kit", hasItem(ModContent.CONVERSION_KIT_IRON_OBSIDIAN))
                              .build(consumer);
        // </editor-fold>
        // <editor-fold desc="Gold Conversion Kits">
        ShapedRecipeBuilder.shapedRecipe(ModContent.CONVERSION_KIT_GOLD_DIAMOND)
                           .patternLine("GGG")
                           .patternLine("DID")
                           .patternLine("GGG")
                           .key('G', Tags.Items.GLASS)
                           .key('D', Tags.Items.GEMS_DIAMOND)
                           .key('I', Tags.Items.INGOTS_GOLD)
                           .addCriterion("has_ingot", hasItem(Tags.Items.INGOTS_GOLD))
                           .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModContent.CONVERSION_KIT_GOLD_OBSIDIAN)
                           .patternLine("OOO")
                           .patternLine("OUO")
                           .patternLine("OOO")
                           .key('O', Tags.Items.OBSIDIAN)
                           .key('U', ModContent.CONVERSION_KIT_GOLD_DIAMOND)
                           .addCriterion("has_kit", hasItem(ModContent.CONVERSION_KIT_GOLD_DIAMOND))
                           .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModContent.CONVERSION_KIT_GOLD_NETHERITE)
                              .addIngredient(ModContent.CONVERSION_KIT_GOLD_OBSIDIAN)
                              .addIngredient(Items.NETHERITE_INGOT)
                              .addCriterion("has_kit", hasItem(ModContent.CONVERSION_KIT_GOLD_OBSIDIAN))
                              .build(consumer);
        // </editor-fold>
        // <editor-fold desc="Diamond Conversion Kits">
        ShapedRecipeBuilder.shapedRecipe(ModContent.CONVERSION_KIT_DIAMOND_OBSIDIAN)
                           .patternLine("OOO")
                           .patternLine("ODO")
                           .patternLine("OOO")
                           .key('O', Tags.Items.OBSIDIAN)
                           .key('D', Tags.Items.GEMS_DIAMOND)
                           .addCriterion("has_diamond", hasItem(Tags.Items.GEMS_DIAMOND))
                           .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModContent.CONVERSION_KIT_DIAMOND_NETHERITE)
                              .addIngredient(ModContent.CONVERSION_KIT_DIAMOND_OBSIDIAN)
                              .addIngredient(Items.NETHERITE_INGOT)
                              .addCriterion("has_kit", hasItem(ModContent.CONVERSION_KIT_DIAMOND_OBSIDIAN))
                              .build(consumer);
        // </editor-fold>
        // <editor-fold desc="Obsidian Conversion Kits">
        ShapelessRecipeBuilder.shapelessRecipe(ModContent.CONVERSION_KIT_OBSIDIAN_NETHERITE)
                              .addIngredient(Tags.Items.OBSIDIAN)
                              .addIngredient(Items.NETHERITE_INGOT)
                              .addCriterion("has_obsidian", hasItem(Tags.Items.OBSIDIAN))
                              .build(consumer);
        // </editor-fold>
    }

    @Override
    public String getName() { return "Expanded Storage - Recipes"; }
}