package torcherino.config;

import blue.endless.jankson.Comment;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.MarkerManager;
import torcherino.Torcherino;
import torcherino.api.TorcherinoAPI;

public final class Config
{
    public static Config INSTANCE;
    @Comment("\nAdd a block by resource location to the blacklist.\nExamples: \"minecraft:dirt\", \"minecraft:furnace\"")
    public final ResourceLocation[] blacklisted_blocks = new ResourceLocation[]{};
    @Comment("\nAdd a tile entity by resource location to the blacklist.\nExamples: \"minecraft:furnace\", \"minecraft:mob_spawner\"")
    public final ResourceLocation[] blacklisted_tiles = new ResourceLocation[]{};
    @Comment("\nAllows new custom torcherino tiers to be added.\nThis also allows for each tier to have their own max max_speed and ranges.")
    public final Tier[] tiers = new Tier[]{ new Tier("normal", 4, 4, 1), new Tier("compressed", 36, 4, 1), new Tier("double_compressed", 324, 4, 1) };
    @Comment("\nDefines how much faster randoms ticks are applied compared to what they should be.\nValid Range: 1 to 4096")
    private int random_tick_rate = 4;
    @Comment("Log torcherino placement (Intended for server use)")
    private boolean log_placement = FMLLoader.getDist().isDedicatedServer();

    @SuppressWarnings("ConstantConditions")
    public static void initialise()
    {
        INSTANCE = JanksonConfigParser.Builder
                .create()
                .deSerializer(JsonPrimitive.class, ResourceLocation.class, (it, marshaller) -> new ResourceLocation(it.asString()),
                        (identifier, marshaller) -> marshaller.serialize(identifier.toString()))
                .deSerializer(JsonObject.class, Tier.class, (it, marshaller) -> {
                    String name = it.get(String.class, "name");
                    int max_speed = it.getInt("max_speed", -1);
                    int xz_range = it.getInt("xz_range", -1);
                    int y_range = it.getInt("y_range", -1);
                    return new Tier(name, Math.max(max_speed, 1), Math.max(xz_range, 0), Math.max(y_range, 0));
                }, (tier, marshaller) -> {
                    final JsonObject rv = new JsonObject();
                    rv.put("name", new JsonPrimitive(tier.name));
                    rv.put("max_speed", new JsonPrimitive(tier.max_speed));
                    rv.put("xz_range", new JsonPrimitive(tier.xz_range));
                    rv.put("y_range", new JsonPrimitive(tier.y_range));
                    return rv;
                }).build()
                .load(Config.class, Config::new, FMLPaths.CONFIGDIR.get().resolve("sci4me/Torcherino.cfg"), new MarkerManager.Log4jMarker(Torcherino.MOD_ID));
        INSTANCE.onConfigLoaded();
    }

    private void onConfigLoaded()
    {
        for (Tier tier : tiers) { TorcherinoAPI.INSTANCE.registerTier(Torcherino.getRl(tier.name), tier.max_speed, tier.xz_range, tier.y_range); }
        for (ResourceLocation block : blacklisted_blocks) { TorcherinoAPI.INSTANCE.blacklistBlock(block); }
        for (ResourceLocation tile : blacklisted_tiles) { TorcherinoAPI.INSTANCE.blacklistTileEntity(tile); }
    }

    public int getRandomTickRate() { return random_tick_rate; }

    public boolean logPlacement() { return log_placement; }

    private static class Tier
    {
        final String name;
        final int max_speed, xz_range, y_range;

        Tier(final String name, final int max_speed, final int xz_range, final int y_range)
        {
            this.name = name;
            this.max_speed = max_speed;
            this.xz_range = xz_range;
            this.y_range = y_range;
        }
    }
}