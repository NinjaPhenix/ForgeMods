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
import org.jetbrains.annotations.NotNull;
import torcherino.api.Tier;
import torcherino.api.TierSupplier;
import torcherino.api.TorcherinoAPI;
import torcherino.blocks.JackoLanterinoBlock;
import torcherino.blocks.LanterinoBlock;
import torcherino.blocks.TorcherinoBlock;
import torcherino.blocks.TorcherinoWallBlock;
import torcherino.blocks.tile.CustomTileEntityType;
import torcherino.blocks.tile.TorcherinoTileEntity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ModContent
{
    public static final ModContent INSTANCE = new ModContent();
    private HashSet<Block> blocks;
    private HashSet<Item> items;
    private HashMap<ResourceLocation, BasicParticleType> particles;
    public static final TileEntityType<TorcherinoTileEntity> TORCHERINO_TILE_ENTITY;

    static
    {
        TORCHERINO_TILE_ENTITY = new CustomTileEntityType<>(TorcherinoTileEntity::new, (b) -> b instanceof TierSupplier, null);
        TORCHERINO_TILE_ENTITY.setRegistryName(Torcherino.resloc("torcherino"));
        TorcherinoAPI.INSTANCE.blacklistTileEntity(TORCHERINO_TILE_ENTITY);
    }

    public void initialise()
    {
        blocks = new HashSet<>();
        items = new HashSet<>();
        particles = new HashMap<>();
        Map<ResourceLocation, Tier> tiers = TorcherinoAPI.INSTANCE.getTiers();
        tiers.keySet().forEach(this::register);
    }

    private ResourceLocation getRl(@NotNull final ResourceLocation tierID, @NotNull final String type)
    {
        if (tierID.getPath().equals("normal")) return new ResourceLocation(Torcherino.MOD_ID, type);
        return new ResourceLocation(Torcherino.MOD_ID, tierID.getPath() + "_" + type);
    }

    private void register(@NotNull final ResourceLocation tierID)
    {
        if (tierID.getNamespace().equals(Torcherino.MOD_ID))
        {
            final BasicParticleType particleType = new BasicParticleType(false); particleType.setRegistryName(getRl(tierID, "flame"));
            particles.put(particleType.getRegistryName(), particleType);
            TorcherinoBlock standingBlock = new TorcherinoBlock(tierID, getRl(tierID, "torcherino"), particleType);
            TorcherinoWallBlock wallBlock = new TorcherinoWallBlock(standingBlock, Torcherino.resloc("wall_" + standingBlock.getRegistryName().getPath()), particleType);
            Item torcherinoItem = new WallOrFloorItem(standingBlock, wallBlock, new Item.Properties().group(ItemGroup.DECORATIONS)).setRegistryName(standingBlock.getRegistryName());
            JackoLanterinoBlock jackoLanterinoBlock = new JackoLanterinoBlock(tierID, getRl(tierID, "lanterino"));
            Item jackoLanterinoItem = new BlockItem(jackoLanterinoBlock, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(jackoLanterinoBlock.getRegistryName());
            LanterinoBlock lanterinoBlock = new LanterinoBlock(tierID, getRl(tierID, "lantern"));
            Item lanterinoItem = new BlockItem(lanterinoBlock, new Item.Properties().group(ItemGroup.DECORATIONS)).setRegistryName(lanterinoBlock.getRegistryName());
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
    public void registerBlocks(@NotNull final RegistryEvent.Register<Block> registryEvent)
    {
        IForgeRegistry<Block> registry = registryEvent.getRegistry();
        blocks.forEach(registry::register);
        blocks = null;
    }

    @SubscribeEvent
    public void registerItems(@NotNull final RegistryEvent.Register<Item> registryEvent)
    {
        IForgeRegistry<Item> registry = registryEvent.getRegistry();
        items.forEach(registry::register);
        items = null;
    }

    @SubscribeEvent
    public void registerTileEntityTypes(@NotNull final RegistryEvent.Register<TileEntityType<?>> registryEvent)
    { registryEvent.getRegistry().register(TORCHERINO_TILE_ENTITY); }

    @SubscribeEvent
    public void registerParticleTypes(@NotNull final RegistryEvent.Register<ParticleType<?>> registryEvent)
    {
        final IForgeRegistry<ParticleType<?>> registry = registryEvent.getRegistry();
        particles.forEach((resourceLocation, particleType) -> registry.register(particleType));
    }

    @SubscribeEvent
    public void registerParticleFactories(@NotNull final ParticleFactoryRegisterEvent event)
    { particles.forEach((resourceLocation, particleType) -> Minecraft.getInstance().particles.registerFactory(particleType, FlameParticle.Factory::new)); }
}