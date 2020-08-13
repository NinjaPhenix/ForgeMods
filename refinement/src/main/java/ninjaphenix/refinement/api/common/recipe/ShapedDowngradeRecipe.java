package ninjaphenix.refinement.api.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import ninjaphenix.refinement.api.common.ITierRecipe;
import ninjaphenix.refinement.api.common.ITierRecipeSerializer;
import ninjaphenix.refinement.impl.RefinementContent;
import ninjaphenix.refinement.impl.common.container.UpgradeContainer;
import org.jetbrains.annotations.Nullable;

public class ShapedDowngradeRecipe implements ITierRecipe
{
    @Override
    public boolean matches(final UpgradeContainer container)
    {
        return false;
    }

    @Override
    public boolean canFit(final int width, final int height)
    {
        return false;
    }

    @Override
    public ResourceLocation getResult(final UpgradeContainer container)
    {
        return null;
    }

    @Override
    public ResourceLocation getId()
    {
        return null;
    }

    @Override
    public ITierRecipeSerializer<ShapedDowngradeRecipe> getSerializer()
    {
        return RefinementContent.SHAPED_DOWNGRADE_RECIPE.get();
    }

    public static class Serializer extends ForgeRegistryEntry<ITierRecipeSerializer<?>> implements ITierRecipeSerializer<ShapedDowngradeRecipe>
    {
        @Override
        public ShapedDowngradeRecipe read(final ResourceLocation recipeId, final JsonObject json)
        {
            return null;
        }

        @Nullable
        @Override
        public ShapedDowngradeRecipe read(final ResourceLocation recipeId, final PacketBuffer buffer)
        {
            return null;
        }

        @Override
        public void write(final PacketBuffer buffer, final ShapedDowngradeRecipe recipe)
        {

        }
    }
}
