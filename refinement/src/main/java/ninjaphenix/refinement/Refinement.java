package ninjaphenix.refinement;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

@Mod("refinement")
public final class Refinement
{
    public static final String MOD_ID = "refinement";

    public Refinement()
    {

    }

    public static ResourceLocation getRl(final String path) { return new ResourceLocation(MOD_ID, path); }
}