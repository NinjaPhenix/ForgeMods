package ninjaphenix.container_library.common.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import ninjaphenix.container_library.ContainerLibraryImpl;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class RemovePreferenceCallbackMessage
{
    public static void encode(@NotNull final RemovePreferenceCallbackMessage message, @NotNull final PacketBuffer buffer) {}

    @SuppressWarnings("InstantiationOfUtilityClass")
    public static RemovePreferenceCallbackMessage decode(@NotNull final PacketBuffer buffer) { return new RemovePreferenceCallbackMessage(); }

    @SuppressWarnings("ConstantConditions")
    public static void handle(@NotNull final RemovePreferenceCallbackMessage message, @NotNull final Supplier<NetworkEvent.Context> contextSupplier)
    {
        final NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getOriginationSide() == LogicalSide.CLIENT)
        { ContainerLibraryImpl.INSTANCE.removePlayerPreferenceCallback(context.getSender()); }
        context.setPacketHandled(true);
    }
}