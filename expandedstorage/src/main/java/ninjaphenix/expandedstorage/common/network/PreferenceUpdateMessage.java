package ninjaphenix.expandedstorage.common.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class PreferenceUpdateMessage
{
    private final ResourceLocation preference;

    public PreferenceUpdateMessage(@NotNull final ResourceLocation preference) { this.preference = preference; }

    static void encode(@NotNull final PreferenceUpdateMessage message, @NotNull final PacketBuffer buffer) { buffer.writeResourceLocation(message.preference); }

    static PreferenceUpdateMessage decode(@NotNull final PacketBuffer buffer) { return new PreferenceUpdateMessage(buffer.readResourceLocation()); }

    @SuppressWarnings("ConstantConditions")
    static void handle(@NotNull final PreferenceUpdateMessage message, @NotNull final Supplier<NetworkEvent.Context> contextSupplier)
    {
        final NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getOriginationSide() == LogicalSide.CLIENT)
        { Networker.INSTANCE.setPlayerPreference(context.getSender(), message.preference); }
        context.setPacketHandled(true);
    }
}