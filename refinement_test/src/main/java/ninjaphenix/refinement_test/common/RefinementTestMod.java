package ninjaphenix.refinement_test.common;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(RefinementTestMod.MOD_ID)
public final class RefinementTestMod
{
    public static final String MOD_ID = "refinement_test";

    public RefinementTestMod()
    {
        TestContent.registerRegisters(FMLJavaModLoadingContext.get().getModEventBus());
    }
}