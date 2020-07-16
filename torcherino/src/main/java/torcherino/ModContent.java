package torcherino;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.WallOrFloorItem;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import torcherino.api.TierSupplier;
import torcherino.api.TorcherinoAPI;
import torcherino.block.JackoLanterinoBlock;
import torcherino.block.LanterinoBlock;
import torcherino.block.TorcherinoBlock;
import torcherino.block.TorcherinoWallBlock;
import torcherino.block.tile.CustomTileEntityType;
import torcherino.block.tile.TorcherinoTileEntity;

@Mod.EventBusSubscriber(modid = Torcherino.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModContent
{
    @SuppressWarnings("ConstantConditions")
    public static final TileEntityType<TorcherinoTileEntity> TORCHERINO_TILE_ENTITY = new CustomTileEntityType<>(TorcherinoTileEntity::new,
            block -> block instanceof TierSupplier, null);
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Torcherino.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Torcherino.MOD_ID);
    private static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Torcherino.MOD_ID);
    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Torcherino.MOD_ID);

    public static void initialise(final IEventBus bus)
    {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        PARTICLE_TYPES.register(bus);
        TILE_ENTITIES.register(bus);
        TILE_ENTITIES.register("torcherino", TORCHERINO_TILE_ENTITY.delegate);
        TorcherinoAPI.INSTANCE.blacklistTileEntity(TORCHERINO_TILE_ENTITY);
        TorcherinoAPI.INSTANCE.getTiers().keySet().forEach(ModContent::register);
    }

    private static String getPath(final ResourceLocation tierID, final String type)
    { return (tierID.getPath().equals("normal") ? "" : tierID.getPath() + "_") + type; }

    private static void register(final ResourceLocation tierID)
    {
        if (tierID.getNamespace().equals(Torcherino.MOD_ID))
        {
            final BasicParticleType particleType = new BasicParticleType(false);
            PARTICLE_TYPES.register(getPath(tierID, "flame"), particleType.delegate);
            final TorcherinoBlock standingBlock = new TorcherinoBlock(tierID, particleType);
            final TorcherinoWallBlock wallBlock = new TorcherinoWallBlock(standingBlock, particleType);
            final Item torcherinoItem = new WallOrFloorItem(standingBlock, wallBlock, new Item.Properties().group(ItemGroup.DECORATIONS));
            final JackoLanterinoBlock jackoLanterinoBlock = new JackoLanterinoBlock(tierID);
            final Item jackoLanterinoItem = new BlockItem(jackoLanterinoBlock, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS));
            final LanterinoBlock lanterinoBlock = new LanterinoBlock(tierID);
            final Item lanterinoItem = new BlockItem(lanterinoBlock, new Item.Properties().group(ItemGroup.DECORATIONS));
            final String torcherinoPath = getPath(tierID, "torcherino");
            final String jackoLanterinoPath = getPath(tierID, "lanterino");
            final String lanterinoPath = getPath(tierID, "lantern");
            BLOCKS.register(torcherinoPath, standingBlock.delegate);
            BLOCKS.register("wall_" + torcherinoPath, wallBlock.delegate);
            BLOCKS.register(jackoLanterinoPath, jackoLanterinoBlock.delegate);
            BLOCKS.register(lanterinoPath, lanterinoBlock.delegate);
            TorcherinoAPI.INSTANCE.blacklistBlock(standingBlock);
            TorcherinoAPI.INSTANCE.blacklistBlock(wallBlock);
            TorcherinoAPI.INSTANCE.blacklistBlock(jackoLanterinoBlock);
            TorcherinoAPI.INSTANCE.blacklistBlock(lanterinoBlock);
            if (FMLLoader.getDist().isClient())
            {
                Minecraft.getInstance().deferTask(() ->
                {
                    RenderTypeLookup.setRenderLayer(standingBlock, RenderType.getCutout());
                    RenderTypeLookup.setRenderLayer(wallBlock, RenderType.getCutout());
                    RenderTypeLookup.setRenderLayer(lanterinoBlock, RenderType.getCutout());
                });
            }
            ITEMS.register(torcherinoPath, torcherinoItem.delegate);
            ITEMS.register(jackoLanterinoPath, jackoLanterinoItem.delegate);
            ITEMS.register(lanterinoPath, lanterinoItem.delegate);
        }
    }

    @SubscribeEvent
    public static void registerParticleFactories(final ParticleFactoryRegisterEvent event)
    {
        PARTICLE_TYPES.getEntries().forEach(registryObject -> Minecraft.getInstance().particles.registerFactory((BasicParticleType) registryObject.get(),
                FlameParticle.Factory::new));
    }
}