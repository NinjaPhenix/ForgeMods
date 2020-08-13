package ninjaphenix.refinement.api.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import java.util.Map;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class TierManager extends JsonReloadListener
{
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    public TierManager()
    {
        super(GSON, "tiers");
        RecipeManager r;
    }

    @Override
    protected void apply(final Map<ResourceLocation, JsonElement> objectIn, final IResourceManager resourceManagerIn, final IProfiler profilerIn)
    {
        System.out.println("Tiers reloaded."); // todo: check why this isn't printing
    }
}
