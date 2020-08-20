package ninjaphenix.expandedstorage.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import ninjaphenix.expandedstorage.ModContent;

public final class ItemModels extends ItemModelProvider
{
    public ItemModels(final DataGenerator generator, final String modId, final ExistingFileHelper fileHelper)
    {
        super(generator, modId, fileHelper);
    }

    @Override
    protected void registerModels()
    {
        simple(ModContent.CHEST_MUTATOR);
        simple(ModContent.CONVERSION_KIT_WOOD_IRON);
        simple(ModContent.CONVERSION_KIT_WOOD_GOLD);
        simple(ModContent.CONVERSION_KIT_WOOD_DIAMOND);
        simple(ModContent.CONVERSION_KIT_WOOD_OBSIDIAN);
        simple(ModContent.CONVERSION_KIT_WOOD_NETHERITE);
        simple(ModContent.CONVERSION_KIT_IRON_GOLD);
        simple(ModContent.CONVERSION_KIT_IRON_DIAMOND);
        simple(ModContent.CONVERSION_KIT_IRON_OBSIDIAN);
        simple(ModContent.CONVERSION_KIT_IRON_NETHERITE);
        simple(ModContent.CONVERSION_KIT_GOLD_DIAMOND);
        simple(ModContent.CONVERSION_KIT_GOLD_OBSIDIAN);
        simple(ModContent.CONVERSION_KIT_GOLD_NETHERITE);
        simple(ModContent.CONVERSION_KIT_DIAMOND_OBSIDIAN);
        simple(ModContent.CONVERSION_KIT_DIAMOND_NETHERITE);
        simple(ModContent.CONVERSION_KIT_OBSIDIAN_NETHERITE);
        chest(ModContent.WOOD_CHEST.getSecond());
        chest(ModContent.PUMPKIN_CHEST.getSecond());
        chest(ModContent.CHRISTMAS_CHEST.getSecond());
        chest(ModContent.IRON_CHEST.getSecond());
        chest(ModContent.GOLD_CHEST.getSecond());
        chest(ModContent.DIAMOND_CHEST.getSecond());
        chest(ModContent.OBSIDIAN_CHEST.getSecond());
        chest(ModContent.NETHERITE_CHEST.getSecond());
        oldChest(ModContent.OLD_WOOD_CHEST.getSecond());
        oldChest(ModContent.OLD_IRON_CHEST.getSecond());
        oldChest(ModContent.OLD_GOLD_CHEST.getSecond());
        oldChest(ModContent.OLD_DIAMOND_CHEST.getSecond());
        oldChest(ModContent.OLD_OBSIDIAN_CHEST.getSecond());
        oldChest(ModContent.OLD_NETHERITE_CHEST.getSecond());
    }

    @SuppressWarnings("ConstantConditions")
    private void oldChest(final Item item) { getBuilder(item.getRegistryName().getPath()).parent(BlockStatesAndModels.SINGLE_OLD_MODELS.get(item)); }

    @SuppressWarnings("ConstantConditions")
    private void simple(final Item item)
    {
        final String itemId = item.getRegistryName().getPath();
        withExistingParent(itemId, mcLoc("item/generated")).texture("layer0", "item/" + itemId);
    }

    @SuppressWarnings("ConstantConditions")
    private void chest(final Item item) { withExistingParent(item.getRegistryName().getPath(), mcLoc("item/chest")); }

    @Override
    public String getName() { return "Expanded Storage - Item Models"; }
}