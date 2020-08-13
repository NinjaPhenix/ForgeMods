package ninjaphenix.refinement_test.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;

public final class ItemModels extends ItemModelProvider
{
    public ItemModels(final DataGenerator generator, final String modId, final ExistingFileHelper fileHelper) { super(generator, modId, fileHelper); }

    @Override
    protected void registerModels()
    {

    }

    @SuppressWarnings("ConstantConditions")
    private void simple(final Item item)
    {
        final String itemId = item.getRegistryName().getPath();
        singleTexture(itemId, mcLoc("item/generated"), "layer0", modLoc("item/" + itemId));
    }

    @Override
    public String getName() { return "Refinement Test - Item Models"; }
}