package ninjaphenix.refinement_test.common;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

@Mod(RefinementTestMod.MOD_ID)
public final class RefinementTestMod
{
    public static final String MOD_ID = "refinement_test";

    public RefinementTestMod()
    {

    }

    public static ResourceLocation getRl(final String path) { return new ResourceLocation(MOD_ID, path); }
}