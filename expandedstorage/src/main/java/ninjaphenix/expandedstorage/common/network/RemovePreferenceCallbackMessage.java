package ninjaphenix.expandedstorage.common.network;

import java.util.function.Supplier;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

public final class RemovePreferenceCallbackMessage
{
    @SuppressWarnings({"unused", "EmptyMethod"})
    public static void encode(final RemovePreferenceCallbackMessage message, final PacketBuffer buffer)
    {
    }

    @SuppressWarnings({"InstantiationOfUtilityClass", "unused"})
    public static RemovePreferenceCallbackMessage decode(final PacketBuffer buffer)
    {
        return new RemovePreferenceCallbackMessage();
    }

    @SuppressWarnings({"ConstantConditions", "unused"})
    public static void handle(final RemovePreferenceCallbackMessage message, final Supplier<NetworkEvent.Context> contextSupplier)
    {
        final NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getOriginationSide() == LogicalSide.CLIENT)
        {
            Networker.INSTANCE.removePlayerPreferenceCallback(context.getSender());
        }
        context.setPacketHandled(true);
    }
}