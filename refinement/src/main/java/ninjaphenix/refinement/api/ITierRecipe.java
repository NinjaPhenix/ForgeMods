package ninjaphenix.refinement.api;

import net.minecraft.util.ResourceLocation;
import ninjaphenix.refinement.impl.common.container.UpgradeContainer;

public interface ITierRecipe
{
    boolean matches(UpgradeContainer container);

    boolean canFit(final int width, final int height);

    ResourceLocation getResult(UpgradeContainer container);

    ResourceLocation getId();

    ITierRecipeSerializer<? extends ITierRecipe> getSerializer();
}
