package ninjaphenix.expandedstorage.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.generators.*;
import ninjaphenix.expandedstorage.ModContent;
import org.jetbrains.annotations.NotNull;

public class ItemModels extends ItemModelProvider
{
    public ItemModels(final DataGenerator generator, final String modId, final ExistingFileHelper fileHelper) { super(generator, modId, fileHelper); }

    @Override
    protected void registerModels()
    {
        simple(ModContent.CHEST_MUTATOR);
        simple(ModContent.CONVERSION_KIT_WOOD_IRON);
        simple(ModContent.CONVERSION_KIT_WOOD_GOLD);
        simple(ModContent.CONVERSION_KIT_WOOD_DIAMOND);
        simple(ModContent.CONVERSION_KIT_WOOD_OBSIDIAN);
        simple(ModContent.CONVERSION_KIT_IRON_GOLD);
        simple(ModContent.CONVERSION_KIT_IRON_DIAMOND);
        simple(ModContent.CONVERSION_KIT_IRON_OBSIDIAN);
        simple(ModContent.CONVERSION_KIT_GOLD_DIAMOND);
        simple(ModContent.CONVERSION_KIT_GOLD_OBSIDIAN);
        simple(ModContent.CONVERSION_KIT_DIAMOND_OBSIDIAN);

        chest(ModContent.WOOD_CHEST.getSecond());
        chest(ModContent.PUMPKIN_CHEST.getSecond());
        chest(ModContent.CHRISTMAS_CHEST.getSecond());
        chest(ModContent.IRON_CHEST.getSecond());
        chest(ModContent.GOLD_CHEST.getSecond());
        chest(ModContent.DIAMOND_CHEST.getSecond());
        chest(ModContent.OBSIDIAN_CHEST.getSecond());

        oldChest(ModContent.OLD_WOOD_CHEST.getSecond());
        oldChest(ModContent.OLD_IRON_CHEST.getSecond());
        oldChest(ModContent.OLD_GOLD_CHEST.getSecond());
        oldChest(ModContent.OLD_DIAMOND_CHEST.getSecond());
        oldChest(ModContent.OLD_OBSIDIAN_CHEST.getSecond());
    }

    private ItemModelBuilder oldChest(final Item item)
    {
        final ModelFile parent = BlockStatesAndModels.SINGLE_OLD_MODELS.get(item);
        final String itemId = item.getRegistryName().getPath();
        return getBuilder(itemId).parent(parent);
    }

    private ItemModelBuilder simple(final Item item) {
        final String itemId = item.getRegistryName().getPath();
        return withExistingParent(itemId, mcLoc("item/generated")).texture("layer0", "item/"+itemId);
    }

    private ItemModelBuilder chest(final Item item) { return withExistingParent(item.getRegistryName().getPath(), mcLoc("item/chest")); }

    @Override
    public @NotNull String getName() { return "Expanded Storage - Item Models"; }
}
