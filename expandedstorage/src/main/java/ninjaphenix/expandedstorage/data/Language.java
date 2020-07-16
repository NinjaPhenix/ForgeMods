package ninjaphenix.expandedstorage.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import ninjaphenix.expandedstorage.ExpandedStorage;
import ninjaphenix.expandedstorage.ModContent;
import org.jetbrains.annotations.NotNull;

public class Language extends LanguageProvider
{
    public Language(@NotNull final DataGenerator generator, @NotNull final String modId, @NotNull final String locale) { super(generator, modId, locale); }

    @Override
    protected void addTranslations()
    {
        // Container Titles
        add("container.expandedstorage.wood_chest", "Chest");
        add("container.expandedstorage.pumpkin_chest", "Pumpkin Chest");
        add("container.expandedstorage.christmas_chest", "Christmas Chest");
        add("container.expandedstorage.iron_chest", "Iron Chest");
        add("container.expandedstorage.gold_chest", "Gold Chest");
        add("container.expandedstorage.diamond_chest", "Diamond Chest");
        add("container.expandedstorage.obsidian_chest", "Obsidian Chest");
        add("container.expandedstorage.netherite_chest", "Netherite Chest");
        // Container Title Misc
        add("container.expandedstorage.generic_double", "Large %s");
        add("container.expandedstorage.error", "Error");
        // Old Chest Blocks
        add(ModContent.OLD_WOOD_CHEST.getFirst(), "Old Chest");
        add(ModContent.OLD_IRON_CHEST.getFirst(), "Old Iron Chest");
        add(ModContent.OLD_GOLD_CHEST.getFirst(), "Old Gold Chest");
        add(ModContent.OLD_DIAMOND_CHEST.getFirst(), "Old Diamond Chest");
        add(ModContent.OLD_OBSIDIAN_CHEST.getFirst(), "Old Obsidian Chest");
        add(ModContent.OLD_NETHERITE_CHEST.getFirst(), "Old Netherite Chest");
        // Chest Blocks
        add(ModContent.WOOD_CHEST.getFirst(), "Chest");
        add(ModContent.PUMPKIN_CHEST.getFirst(), "Pumpkin Chest");
        add(ModContent.CHRISTMAS_CHEST.getFirst(), "Christmas Chest");
        add(ModContent.IRON_CHEST.getFirst(), "Iron Chest");
        add(ModContent.GOLD_CHEST.getFirst(), "Gold Chest");
        add(ModContent.DIAMOND_CHEST.getFirst(), "Diamond Chest");
        add(ModContent.OBSIDIAN_CHEST.getFirst(), "Obsidian Chest");
        add(ModContent.NETHERITE_CHEST.getFirst(), "Netherite Chest");
        // Wood Conversion Kits
        add(ModContent.CONVERSION_KIT_WOOD_IRON, "Wood to Iron upgrade");
        add(ModContent.CONVERSION_KIT_WOOD_GOLD, "Wood to Gold upgrade");
        add(ModContent.CONVERSION_KIT_WOOD_DIAMOND, "Wood to Diamond upgrade");
        add(ModContent.CONVERSION_KIT_WOOD_OBSIDIAN, "Wood to Obsidian upgrade");
        add(ModContent.CONVERSION_KIT_WOOD_NETHERITE, "Wood to Netherite upgrade");
        // Iron Conversion Kits
        add(ModContent.CONVERSION_KIT_IRON_GOLD, "Iron to Gold upgrade");
        add(ModContent.CONVERSION_KIT_IRON_DIAMOND, "Iron to Diamond upgrade");
        add(ModContent.CONVERSION_KIT_IRON_OBSIDIAN, "Iron to Obsidian upgrade");
        add(ModContent.CONVERSION_KIT_IRON_NETHERITE, "Iron to Netherite upgrade");
        // Gold Conversion Kits
        add(ModContent.CONVERSION_KIT_GOLD_DIAMOND, "Gold to Diamond upgrade");
        add(ModContent.CONVERSION_KIT_GOLD_OBSIDIAN, "Gold to Obsidian upgrade");
        add(ModContent.CONVERSION_KIT_GOLD_NETHERITE, "Gold to Netherite upgrade");
        // Diamond Conversion Kits
        add(ModContent.CONVERSION_KIT_DIAMOND_OBSIDIAN, "Diamond to Obsidian upgrade");
        add(ModContent.CONVERSION_KIT_DIAMOND_NETHERITE, "Diamond to Netherite upgrade");
        // Obsidian Conversion Kits
        add(ModContent.CONVERSION_KIT_OBSIDIAN_NETHERITE, "Obsidian to Netherite upgrade");
        // Chest Mutator
        add(ModContent.CHEST_MUTATOR, "Chest Mutator");
        // Tooltips
        add("tooltip.expandedstorage.chest_mutator.merge", "Merge");
        add("tooltip.expandedstorage.chest_mutator.merge_desc", "%s on two adjacent chests to merge them.");
        add("tooltip.expandedstorage.chest_mutator.unmerge", "Unmerge");
        add("tooltip.expandedstorage.chest_mutator.unmerge_desc", "%s on a chest to split it into two single chests.");
        add("tooltip.expandedstorage.chest_mutator.rotate", "Rotate");
        add("tooltip.expandedstorage.chest_mutator.rotate_desc", "%s on a chest to rotate it.");
        add("tooltip.expandedstorage.chest_mutator.merge_start", "Merging started, now %s the other chest.");
        add("tooltip.expandedstorage.chest_mutator.merge_end", "Merging finished.");
        add("tooltip.expandedstorage.tool_mode", "Tool Mode: %s");
        add("tooltip.expandedstorage.conversion_kit_wood_iron", "%1$s on a Wooden Chest to convert it to an Iron Chest%2$s");
        add("tooltip.expandedstorage.conversion_kit_wood_gold", "%1$s on a Wooden Chest to convert it to a Gold Chest%2$s");
        add("tooltip.expandedstorage.conversion_kit_wood_diamond", "%1$s on a Wooden Chest to convert it to a Diamond Chest%2$s");
        add("tooltip.expandedstorage.conversion_kit_wood_obsidian", "%1$s on a Wooden Chest to convert it to an Obsidian Chest%2$s");
        add("tooltip.expandedstorage.conversion_kit_wood_netherite", "%1$s on a Wooden Chest to convert it to a Netherite Chest%2$s");
        add("tooltip.expandedstorage.conversion_kit_iron_gold", "%1$s on an Iron Chest to convert it to a Gold Chest%2$s");
        add("tooltip.expandedstorage.conversion_kit_iron_diamond", "%1$s on an Iron Chest to convert it to a Diamond Chest%2$s");
        add("tooltip.expandedstorage.conversion_kit_iron_obsidian", "%1$s on an Iron Chest to convert it to an Obsidian Chest%2$s");
        add("tooltip.expandedstorage.conversion_kit_iron_netherite", "%1$s on an Iron Chest to convert it to a Netherite Chest%2$s");
        add("tooltip.expandedstorage.conversion_kit_gold_diamond", "%1$s on a Gold Chest to convert it to a Diamond Chest%2$s");
        add("tooltip.expandedstorage.conversion_kit_gold_obsidian", "%1$s on a Gold Chest to convert it to an Obsidian Chest%2$s");
        add("tooltip.expandedstorage.conversion_kit_gold_netherite", "%1$s on a Gold Chest to convert it to a Netherite Chest%2$s");
        add("tooltip.expandedstorage.conversion_kit_diamond_obsidian", "%1$s on a Diamond Chest to convert it to an Obsidian Chest%2$s");
        add("tooltip.expandedstorage.conversion_kit_diamond_netherite", "%1$s on a Diamond Chest to convert it to a Netherite Chest%2$s");
        add("tooltip.expandedstorage.conversion_kit_obsidian_netherite", "%1$s on an Obsidian Chest to convert it to a Netherite Chest%2$s");
        add("tooltip.expandedstorage.conversion_kit_double_requires_2", ", double chests require 2 upgrades.");
        add("tooltip.expandedstorage.left_shift_right_click", "%1$s + %2$s");
        // Creative Group
        add(ExpandedStorage.group.getTranslationKey(), "Expanded Storage");
        // Screen
        add("screen.expandedstorage.screen_picker_title", "Select Screen Type");
        add("screen.expandedstorage.change_screen_button", "Change Screen Type");
        add("screen.expandedstorage.single_screen", "Single Page Screen");
        add("screen.expandedstorage.paged_screen", "Paginated Screen");
        add("screen.expandedstorage.scrollable_screen", "Scrollable Screen");
        add("screen.expandedstorage.page_x_y", "%d/%d");
        add("screen.expandedstorage.next_page", "Next Page");
        add("screen.expandedstorage.prev_page", "Previous Page");
    }

    @NotNull @Override
    public String getName() { return "Expanded Storage - Language"; }
}