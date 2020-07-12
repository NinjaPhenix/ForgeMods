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
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.IForgeRegistry;
import torcherino.api.Tier;
import torcherino.api.TierSupplier;
import torcherino.api.TorcherinoAPI;
import torcherino.block.JackoLanterinoBlock;
import torcherino.block.LanterinoBlock;
import torcherino.block.TorcherinoBlock;
import torcherino.block.TorcherinoWallBlock;
import torcherino.block.tile.CustomTileEntityType;
import torcherino.block.tile.TorcherinoTileEntity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ModContent
{
    public static final ModContent INSTANCE = new ModContent();
    public static final TileEntityType<TorcherinoTileEntity> TORCHERINO_TILE_ENTITY;

    static
    {
        TORCHERINO_TILE_ENTITY = new CustomTileEntityType<>(TorcherinoTileEntity::new, (b) -> b instanceof TierSupplier, null);
        TORCHERINO_TILE_ENTITY.setRegistryName(Torcherino.getRl("torcherino"));
        TorcherinoAPI.INSTANCE.blacklistTileEntity(TORCHERINO_TILE_ENTITY);
    }

    private HashSet<Block> blocks;
    private HashSet<Item> items;
    private HashMap<ResourceLocation, BasicParticleType> particles;

    public void initialise()
    {
        blocks = new HashSet<>();
        items = new HashSet<>();
        particles = new HashMap<>();
        Map<ResourceLocation, Tier> tiers = TorcherinoAPI.INSTANCE.getTiers();
        tiers.keySet().forEach(this::register);
    }

    private ResourceLocation getRl(final ResourceLocation tierID, final String type)
    {
        if (tierID.getPath().equals("normal")) { return new ResourceLocation(Torcherino.MOD_ID, type); }
        return new ResourceLocation(Torcherino.MOD_ID, tierID.getPath() + "_" + type);
    }

    @SuppressWarnings("ConstantConditions")
    private void register(final ResourceLocation tierID)
    {
        if (tierID.getNamespace().equals(Torcherino.MOD_ID))
        {
            final BasicParticleType particleType = new BasicParticleType(false);
            particleType.setRegistryName(getRl(tierID, "flame"));
            particles.put(particleType.getRegistryName(), particleType);
            final TorcherinoBlock standingBlock = new TorcherinoBlock(tierID, getRl(tierID, "torcherino"), particleType);
            final TorcherinoWallBlock wallBlock = new TorcherinoWallBlock(standingBlock,
                    Torcherino.getRl("wall_" + standingBlock.getRegistryName().getPath()), particleType);
            final Item torcherinoItem = new WallOrFloorItem(standingBlock, wallBlock, new Item.Properties().group(ItemGroup.DECORATIONS))
                    .setRegistryName(standingBlock.getRegistryName());
            final JackoLanterinoBlock jackoLanterinoBlock = new JackoLanterinoBlock(tierID, getRl(tierID, "lanterino"));
            final Item jackoLanterinoItem = new BlockItem(jackoLanterinoBlock, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS))
                    .setRegistryName(jackoLanterinoBlock.getRegistryName());
            final LanterinoBlock lanterinoBlock = new LanterinoBlock(tierID, getRl(tierID, "lantern"));
            final Item lanterinoItem = new BlockItem(lanterinoBlock, new Item.Properties().group(ItemGroup.DECORATIONS))
                    .setRegistryName(lanterinoBlock.getRegistryName());
            blocks.add(standingBlock);
            blocks.add(wallBlock);
            blocks.add(jackoLanterinoBlock);
            blocks.add(lanterinoBlock);
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
            items.add(torcherinoItem);
            items.add(jackoLanterinoItem);
            items.add(lanterinoItem);
        }
    }

    @SubscribeEvent
    public void registerBlocks(final RegistryEvent.Register<Block> registryEvent)
    {
        final IForgeRegistry<Block> registry = registryEvent.getRegistry();
        blocks.forEach(registry::register);
        blocks = null;
    }

    @SubscribeEvent
    public void registerItems(final RegistryEvent.Register<Item> registryEvent)
    {
        final IForgeRegistry<Item> registry = registryEvent.getRegistry();
        items.forEach(registry::register);
        items = null;
    }

    @SubscribeEvent
    public void registerTileEntityTypes(final RegistryEvent.Register<TileEntityType<?>> registryEvent)
    { registryEvent.getRegistry().register(TORCHERINO_TILE_ENTITY); }

    @SubscribeEvent
    public void registerParticleTypes(final RegistryEvent.Register<ParticleType<?>> registryEvent)
    {
        final IForgeRegistry<ParticleType<?>> registry = registryEvent.getRegistry();
        particles.forEach((resourceLocation, particleType) -> registry.register(particleType));
    }

    @SubscribeEvent
    public void registerParticleFactories(final ParticleFactoryRegisterEvent event)
    { particles.forEach((resourceLocation, particleType) -> Minecraft.getInstance().particles.registerFactory(particleType, FlameParticle.Factory::new)); }
}