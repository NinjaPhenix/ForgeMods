package ninjaphenix.refinement.api.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import ninjaphenix.refinement.api.ITierRecipe;
import ninjaphenix.refinement.api.ITierRecipeSerializer;
import ninjaphenix.refinement.impl.RefinementContent;
import ninjaphenix.refinement.impl.common.container.UpgradeContainer;
import org.jetbrains.annotations.Nullable;

public class ShapedUpgradeRecipe implements ITierRecipe
{
    @Override
    public boolean matches(UpgradeContainer container)
    {
        return false;
    }

    @Override
    public boolean canFit(int width, int height)
    {
        return false;
    }

    @Override
    public ResourceLocation getResult(UpgradeContainer container)
    {
        return null;
    }

    @Override
    public ResourceLocation getId()
    {
        return null;
    }

    @Override
    public ITierRecipeSerializer<ShapedUpgradeRecipe> getSerializer()
    {
        return RefinementContent.SHAPED_UPGRADE_SERIALIZER.get();
    }

    public static class Serializer extends ForgeRegistryEntry<ITierRecipeSerializer<?>> implements ITierRecipeSerializer<ShapedUpgradeRecipe>
    {
        @Override
        public ShapedUpgradeRecipe read(ResourceLocation recipeId, JsonObject json)
        {
            return null;
        }

        @Nullable @Override
        public ShapedUpgradeRecipe read(ResourceLocation recipeId, PacketBuffer buffer)
        {
            return null;
        }

        @Override
        public void write(PacketBuffer buffer, ShapedUpgradeRecipe recipe)
        {

        }
    }
}
