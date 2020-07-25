package torcherino.data;

import java.util.function.Consumer;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;

public final class Recipes extends RecipeProvider
{
    public Recipes(final DataGenerator generator)
    {
        super(generator);
    }

    @Override
    protected void registerRecipes(final Consumer<IFinishedRecipe> consumer)
    {

    }

    @Override
    public String getName()
    {
        return "Torcherino - Recipes";
    }
}