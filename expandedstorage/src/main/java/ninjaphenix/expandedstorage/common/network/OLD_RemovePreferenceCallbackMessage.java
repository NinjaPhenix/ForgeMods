//package ninjaphenix.expandedstorage.common.network;
//
//import net.minecraft.network.PacketBuffer;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.network.NetworkEvent;
//
//import java.util.function.Supplier;
//
//public final class OLD_RemovePreferenceCallbackMessage
//{
//    public static void encode(final OLD_RemovePreferenceCallbackMessage message, final PacketBuffer buffer) {}
//
//    @SuppressWarnings("InstantiationOfUtilityClass")
//    public static OLD_RemovePreferenceCallbackMessage decode(final PacketBuffer buffer) { return new OLD_RemovePreferenceCallbackMessage(); }
//
//    @SuppressWarnings("ConstantConditions")
//    public static void handle(final OLD_RemovePreferenceCallbackMessage message, final Supplier<NetworkEvent.Context> contextSupplier)
//    {
//        final NetworkEvent.Context context = contextSupplier.get();
//        if (context.getDirection().getOriginationSide() == LogicalSide.CLIENT)
//        { OLD_Networker.INSTANCE.removePlayerPreferenceCallback(context.getSender()); }
//        context.setPacketHandled(true);
//    }
//}