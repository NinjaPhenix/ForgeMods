package ninjaphenix.refinement.api.common;

import com.google.gson.JsonObject;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;

public interface ITierRecipeSerializer<T extends ITierRecipe> extends IForgeRegistryEntry<ITierRecipeSerializer<?>>
{
    T read(final ResourceLocation recipeId, final JsonObject json);

    @Nullable T read(final ResourceLocation recipeId, final PacketBuffer buffer);

    void write(final PacketBuffer buffer, final T recipe);
}
