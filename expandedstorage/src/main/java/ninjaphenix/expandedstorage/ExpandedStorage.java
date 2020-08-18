package ninjaphenix.expandedstorage;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentKeybind;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ninjaphenix.expandedstorage.common.proxy.IProxy;

@Mod(modid = ExpandedStorage.MOD_ID)
public final class ExpandedStorage
{
    public static final String MOD_ID = "expandedstorage";
    private static final String SERVER_PROXY = "ninjaphenix.expandedstorage.common.proxy.ServerProxy";
    private static final String CLIENT_PROXY = "ninjaphenix.expandedstorage.client.proxy.ClientProxy";
    public static final TextComponentTranslation leftShiftRightClick;
    public static final CreativeTabs creativeTab = new CreativeTabs(MOD_ID)
    {
        @Override // todo: change to diamond chest.
        public ItemStack createIcon() { return new ItemStack(Blocks.IRON_BLOCK); }
    };

    @SidedProxy(serverSide = SERVER_PROXY, clientSide = CLIENT_PROXY)
    public static IProxy proxy;

    static {
        leftShiftRightClick = new TextComponentTranslation("tooltip.expandedstorage.left_shift_right_click",
                new TextComponentKeybind("key.sneak"), new TextComponentTranslation("key.use"));
        leftShiftRightClick.getStyle().setColor(TextFormatting.GOLD);
    }

    public ExpandedStorage()
    {
        //OLD_Networker.INSTANCE.registerMessages();
        //OLD_ExpandedStorageConfig.register();
        //MinecraftForge.EVENT_BUS.register(OLD_Networker.INSTANCE);
        //if (FMLLoader.getDist() == Dist.CLIENT)
        //{
        //    FMLJavaModLoadingContext.get().getModEventBus().register(ExpandedStorageClient.class);
        //    MinecraftForge.EVENT_BUS.register(ExpandedStorageClient.class);
        //}
    }

    public void preInit(final FMLPreInitializationEvent event) { proxy.preInit(event); }

    public static ResourceLocation getRl(final String path) { return new ResourceLocation(MOD_ID, path); }
}