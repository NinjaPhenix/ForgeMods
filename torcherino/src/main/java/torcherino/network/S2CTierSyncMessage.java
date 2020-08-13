package torcherino.network;

import com.mojang.datafixers.util.Pair;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import torcherino.api.Tier;
import torcherino.api.TorcherinoAPI;
import torcherino.api.impl.TorcherinoImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class S2CTierSyncMessage
{
    private final Map<ResourceLocation, Tier> tiers;

    public S2CTierSyncMessage(final Map<ResourceLocation, Tier> tiers) { this.tiers = tiers; }

    static void encode(final S2CTierSyncMessage message, final PacketBuffer buffer)
    {
        buffer.writeInt(message.tiers.size());
        message.tiers.forEach((name, tier) -> writeTier(name, tier, buffer));
    }

    static S2CTierSyncMessage decode(final PacketBuffer buffer)
    {
        final Map<ResourceLocation, Tier> localTiers = new HashMap<>();
        final int count = buffer.readInt();
        for (int i = 0; i < count; i++)
        {
            final Pair<ResourceLocation, Tier> entry = readTier(buffer);
            localTiers.put(entry.getFirst(), entry.getSecond());
        }
        return new S2CTierSyncMessage(localTiers);
    }

    static void handle(final S2CTierSyncMessage message, final Supplier<NetworkEvent.Context> contextSupplier)
    {
        final NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getOriginationSide() == LogicalSide.SERVER)
        {
            context.enqueueWork(() -> ((TorcherinoImpl) TorcherinoAPI.INSTANCE).setRemoteTiers(message.tiers));
            context.setPacketHandled(true);
        }
    }

    private static Pair<ResourceLocation, Tier> readTier(final PacketBuffer buffer)
    { return new Pair<>(buffer.readResourceLocation(), new Tier(buffer.readInt(), buffer.readInt(), buffer.readInt())); }

    private static void writeTier(final ResourceLocation name, final Tier tier, final PacketBuffer buffer)
    { buffer.writeResourceLocation(name).writeInt(tier.MAX_SPEED).writeInt(tier.XZ_RANGE).writeInt(tier.Y_RANGE); }
}