package ninjaphenix.expandedstorage;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import ninjaphenix.expandedstorage.client.ExpandedStorageClient;
import ninjaphenix.expandedstorage.common.ExpandedStorageConfig;
import ninjaphenix.expandedstorage.common.network.Networker;
import org.jetbrains.annotations.NotNull;

@Mod("expandedstorage")
public class ExpandedStorage
{
	public static final String MOD_ID = "expandedstorage";
	public static final ItemGroup group = new ItemGroup(MOD_ID)
	{
		@Override
		public ItemStack createIcon() { return new ItemStack(ModContent.DIAMOND_CHEST.getSecond()); }
	};

	public static ResourceLocation getRl(@NotNull final String path) { return new ResourceLocation(MOD_ID, path); }

	// todo: sided proxies
	public ExpandedStorage()
	{
        Networker.INSTANCE.registerMessages();
        ExpandedStorageConfig.register();
        MinecraftForge.EVENT_BUS.register(Networker.INSTANCE);
        if(FMLLoader.getDist() == Dist.CLIENT) {
            FMLJavaModLoadingContext.get().getModEventBus().register(ExpandedStorageClient.class);
            MinecraftForge.EVENT_BUS.register(ExpandedStorageClient.class);
        }
	}
}