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
        this.add("container.expandedstorage.wood_chest", "Chest");
        this.add("container.expandedstorage.pumpkin_chest", "Pumpkin Chest");
        this.add("container.expandedstorage.christmas_chest", "Christmas Chest");
        this.add("container.expandedstorage.iron_chest", "Iron Chest");
        this.add("container.expandedstorage.gold_chest", "Gold Chest");
        this.add("container.expandedstorage.diamond_chest", "Diamond Chest");
        this.add("container.expandedstorage.obsidian_chest", "Obsidian Chest");
        // Container Title Misc
        this.add("container.expandedstorage.generic_double", "Large %s");
        this.add("container.expandedstorage.error", "Error");
        // Old Chest Blocks
        this.add(ModContent.OLD_WOOD_CHEST.getFirst(), "Old Chest");
        this.add(ModContent.OLD_IRON_CHEST.getFirst(), "Old Iron Chest");
        this.add(ModContent.OLD_GOLD_CHEST.getFirst(), "Old Gold Chest");
        this.add(ModContent.OLD_DIAMOND_CHEST.getFirst(), "Old Diamond Chest");
        this.add(ModContent.OLD_OBSIDIAN_CHEST.getFirst(), "Old Obsidian Chest");
        // Chest Blocks
        this.add(ModContent.WOOD_CHEST.getFirst(), "Chest");
        this.add(ModContent.PUMPKIN_CHEST.getFirst(), "Pumpkin Chest");
        this.add(ModContent.CHRISTMAS_CHEST.getFirst(), "Christmas Chest");
        this.add(ModContent.IRON_CHEST.getFirst(), "Iron Chest");
        this.add(ModContent.GOLD_CHEST.getFirst(), "Gold Chest");
        this.add(ModContent.DIAMOND_CHEST.getFirst(), "Diamond Chest");
        this.add(ModContent.OBSIDIAN_CHEST.getFirst(), "Obsidian Chest");
        // Wood Conversion Kits
        this.add(ModContent.CONVERSION_KIT_WOOD_IRON, "Wood to Iron upgrade");
        this.add(ModContent.CONVERSION_KIT_WOOD_GOLD, "Wood to Gold upgrade");
        this.add(ModContent.CONVERSION_KIT_WOOD_DIAMOND, "Wood to Diamond upgrade");
        this.add(ModContent.CONVERSION_KIT_WOOD_OBSIDIAN, "Wood to Obsidian upgrade");
        // Iron Conversion Kits
        this.add(ModContent.CONVERSION_KIT_IRON_GOLD, "Iron to Gold upgrade");
        this.add(ModContent.CONVERSION_KIT_IRON_DIAMOND, "Iron to Diamond upgrade");
        this.add(ModContent.CONVERSION_KIT_IRON_OBSIDIAN, "Iron to Obsidian upgrade");
        // Gold Conversion Kits
        this.add(ModContent.CONVERSION_KIT_GOLD_DIAMOND, "Gold to Diamond upgrade");
        this.add(ModContent.CONVERSION_KIT_GOLD_OBSIDIAN, "Gold to Obsidian upgrade");
        // Diamond Conversion Kits
        this.add(ModContent.CONVERSION_KIT_DIAMOND_OBSIDIAN, "Diamond to Obsidian upgrade");
        // Chest Mutator
        this.add(ModContent.CHEST_MUTATOR, "Chest Mutator");
        // Tooltips
        this.add("tooltip.expandedstorage.chest_mutator.merge", "Merge");
        this.add("tooltip.expandedstorage.chest_mutator.merge_desc", "%s on two adjacent chests to merge them.");
        this.add("tooltip.expandedstorage.chest_mutator.unmerge", "Unmerge");
        this.add("tooltip.expandedstorage.chest_mutator.unmerge_desc", "%s on a chest to split it into two single chests.");
        this.add("tooltip.expandedstorage.chest_mutator.rotate", "Rotate");
        this.add("tooltip.expandedstorage.chest_mutator.rotate_desc", "%s on a chest to rotate it.");
        this.add("tooltip.expandedstorage.chest_mutator.merge_start", "Merging started, now %s the other chest.");
        this.add("tooltip.expandedstorage.chest_mutator.merge_end", "Merging finished.");
        this.add("tooltip.expandedstorage.tool_mode", "Tool Mode: %s");
        // Creative Group
        this.add(ExpandedStorage.group.getTranslationKey(), "Expanded Storage");
        // Screen
        this.add("screen.expandedstorage.screen_picker_title", "Select Screen Type");
        this.add("screen.expandedstorage.change_screen_button", "Change Screen Type");
        this.add("screen.expandedstorage.single_screen", "Single Page Screen");
        this.add("screen.expandedstorage.paged_screen", "Paginated Screen");
        this.add("screen.expandedstorage.scrollable_screen", "Scrollable Screen");
        this.add("screen.expandedstorage.page_x_y", "%d/%d");
        this.add("screen.expandedstorage.next_page", "Next Page");
        this.add("screen.expandedstorage.prev_page", "Previous Page");
    }

    @NotNull @Override
    public String getName() { return "Expanded Storage - Language"; }
}