package ninjaphenix.expandedstorage;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.KeybindTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import ninjaphenix.expandedstorage.client.ExpandedStorageClient;
import ninjaphenix.expandedstorage.common.ExpandedStorageConfig;
import ninjaphenix.expandedstorage.common.network.Networker;

@Mod("expandedstorage")
public final class ExpandedStorage
{
    public static final String MOD_ID = "expandedstorage";
    public static final ITextComponent leftShiftRightClick =
            new TranslationTextComponent("tooltip.expandedstorage.left_shift_right_click",
                                         new KeybindTextComponent("key.sneak"),
                                         new KeybindTextComponent("key.use")).withStyle(TextFormatting.GOLD);
    public static final ItemGroup group = new ItemGroup(MOD_ID)
    {
        @Override
        public ItemStack makeIcon() { return new ItemStack(ModContent.DIAMOND_CHEST.getSecond()); }
    };

    public ExpandedStorage()
    {
        Networker.INSTANCE.registerMessages();
        ExpandedStorageConfig.register();
        MinecraftForge.EVENT_BUS.register(Networker.INSTANCE);
        if (FMLLoader.getDist() == Dist.CLIENT)
        {
            FMLJavaModLoadingContext.get().getModEventBus().register(ExpandedStorageClient.class);
            MinecraftForge.EVENT_BUS.register(ExpandedStorageClient.class);
        }
    }

    public static ResourceLocation getRl(final String path) { return new ResourceLocation(MOD_ID, path); }
}