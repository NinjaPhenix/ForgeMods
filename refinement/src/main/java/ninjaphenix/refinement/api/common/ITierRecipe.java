package ninjaphenix.refinement.api.common;

import net.minecraft.util.ResourceLocation;
import ninjaphenix.refinement.impl.common.container.UpgradeContainer;

public interface ITierRecipe
{
    boolean matches(final UpgradeContainer container);

    boolean canFit(final int width, final int height);

    ResourceLocation getResult(final UpgradeContainer container);

    ResourceLocation getId();

    ITierRecipeSerializer<? extends ITierRecipe> getSerializer();
}
