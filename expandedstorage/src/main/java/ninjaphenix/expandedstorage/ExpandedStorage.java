package ninjaphenix.expandedstorage;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import ninjaphenix.expandedstorage.client.ExpandedStorageClient;

@Mod("expandedstorage")
public class ExpandedStorage
{
	public static final String MOD_ID = "expandedstorage";
	public static final ItemGroup group = new ItemGroup(MOD_ID)
	{
		@Override
		public ItemStack createIcon() { return new ItemStack(ModContent.DIAMOND_CHEST.getSecond()); }
	};

	public static ResourceLocation getRl(final String path) { return new ResourceLocation(MOD_ID, path); }

	public ExpandedStorage()
	{
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			FMLJavaModLoadingContext.get().getModEventBus().register(ExpandedStorageClient.class);
			MinecraftForge.EVENT_BUS.register(ExpandedStorageClient.class);
		});
	}
}