package ninjaphenix.refinement.impl;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Refinement.MOD_ID)
public final class Refinement
{
    public static final String MOD_ID = "refinement";

    public Refinement()
    {
        RefinementContent.registerRegisters(FMLJavaModLoadingContext.get().getModEventBus());
    }
}