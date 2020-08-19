package ninjaphenix.expandedstorage.common.network;

import java.util.function.Supplier;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

public final class PreferenceUpdateMessage
{
    private final ResourceLocation preference;

    public PreferenceUpdateMessage(final ResourceLocation preference) { this.preference = preference; }

    static void encode(final PreferenceUpdateMessage message, final PacketBuffer buffer)
    {
        buffer.writeResourceLocation(message.preference);
    }

    static PreferenceUpdateMessage decode(final PacketBuffer buffer) { return new PreferenceUpdateMessage(buffer.readResourceLocation()); }

    @SuppressWarnings("ConstantConditions")
    static void handle(final PreferenceUpdateMessage message, final Supplier<NetworkEvent.Context> contextSupplier)
    {
        final NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getOriginationSide() == LogicalSide.CLIENT)
        {
            Networker.INSTANCE.setPlayerPreference(context.getSender(), message.preference);
        }
        context.setPacketHandled(true);
    }
}