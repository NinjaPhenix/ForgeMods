package ninjaphenix.refinement.api;

import com.google.gson.JsonObject;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;

public interface ITierRecipeSerializer<T extends ITierRecipe> extends IForgeRegistryEntry<ITierRecipeSerializer<?>>
{
    T read(ResourceLocation recipeId, JsonObject json);

    @Nullable
    T read(ResourceLocation recipeId, PacketBuffer buffer);

    void write(PacketBuffer buffer, T recipe);
}
