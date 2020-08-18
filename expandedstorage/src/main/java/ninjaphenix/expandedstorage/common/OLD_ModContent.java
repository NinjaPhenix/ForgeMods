//package ninjaphenix.expandedstorage;
//
//import net.minecraft.block.Block;
//import net.minecraft.init.Blocks;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemBlock;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.text.TextComponentTranslation;
//import net.minecraftforge.event.RegistryEvent;
//import net.minecraftforge.fml.common.Mod;
//import ninjaphenix.expandedstorage.Registries.ModeledTierData;
//import org.apache.commons.lang3.tuple.Pair;
//
//@Mod.EventBusSubscriber(modid = ExpandedStorage.MOD_ID)
//public final class OLD_ModContent
//{
//    public static final ContainerType<OLD_PagedContainer> PAGED_CONTAINER_TYPE;
//    public static final ContainerType<OLD_SingleContainer> SINGLE_CONTAINER_TYPE;
//    public static final ContainerType<OLD_ScrollableContainer> SCROLLABLE_CONTAINER_TYPE;
//    public static final Item CHEST_MUTATOR;
//    public static final OLD_ChestConversionItem CONVERSION_KIT_WOOD_IRON;
//    public static final OLD_ChestConversionItem CONVERSION_KIT_WOOD_GOLD;
//    public static final OLD_ChestConversionItem CONVERSION_KIT_WOOD_DIAMOND;
//    public static final OLD_ChestConversionItem CONVERSION_KIT_WOOD_OBSIDIAN;
//    public static final OLD_ChestConversionItem CONVERSION_KIT_WOOD_NETHERITE;
//    public static final OLD_ChestConversionItem CONVERSION_KIT_IRON_GOLD;
//    public static final OLD_ChestConversionItem CONVERSION_KIT_IRON_DIAMOND;
//    public static final OLD_ChestConversionItem CONVERSION_KIT_IRON_OBSIDIAN;
//    public static final OLD_ChestConversionItem CONVERSION_KIT_IRON_NETHERITE;
//    public static final OLD_ChestConversionItem CONVERSION_KIT_GOLD_DIAMOND;
//    public static final OLD_ChestConversionItem CONVERSION_KIT_GOLD_OBSIDIAN;
//    public static final OLD_ChestConversionItem CONVERSION_KIT_GOLD_NETHERITE;
//    public static final OLD_ChestConversionItem CONVERSION_KIT_DIAMOND_OBSIDIAN;
//    public static final OLD_ChestConversionItem CONVERSION_KIT_DIAMOND_NETHERITE;
//    public static final OLD_ChestConversionItem CONVERSION_KIT_OBSIDIAN_NETHERITE;
//
//    static
//    {
//        PAGED_CONTAINER_TYPE = new ContainerType<>(new PagedContainer.Factory());
//        SINGLE_CONTAINER_TYPE = new ContainerType<>(new SingleContainer.Factory());
//        SCROLLABLE_CONTAINER_TYPE = new ContainerType<>(new ScrollableContainer.Factory());
//        PAGED_CONTAINER_TYPE.setRegistryName(ExpandedStorage.getRl("paged_container"));
//        SINGLE_CONTAINER_TYPE.setRegistryName(ExpandedStorage.getRl("single_container"));
//        SCROLLABLE_CONTAINER_TYPE.setRegistryName(ExpandedStorage.getRl("scrollable_container"));
//        CHEST_MUTATOR = new OLD_ChestMutatorItem().setRegistryName(ExpandedStorage.getRl("chest_mutator"));
//    }
//}