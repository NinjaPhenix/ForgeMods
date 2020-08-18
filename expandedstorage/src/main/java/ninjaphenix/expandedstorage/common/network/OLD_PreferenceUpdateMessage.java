//package ninjaphenix.expandedstorage.common.network;
//
//import net.minecraft.network.PacketBuffer;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.network.NetworkEvent;
//
//import java.util.function.Supplier;
//
//public final class OLD_PreferenceUpdateMessage
//{
//    private final ResourceLocation preference;
//
//    public OLD_PreferenceUpdateMessage(final ResourceLocation preference) { this.preference = preference; }
//
//    static void encode(final OLD_PreferenceUpdateMessage message, final PacketBuffer buffer) { buffer.writeResourceLocation(message.preference); }
//
//    static OLD_PreferenceUpdateMessage decode(final PacketBuffer buffer) { return new OLD_PreferenceUpdateMessage(buffer.readResourceLocation()); }
//
//    @SuppressWarnings("ConstantConditions")
//    static void handle(final OLD_PreferenceUpdateMessage message, final Supplier<NetworkEvent.Context> contextSupplier)
//    {
//        final NetworkEvent.Context context = contextSupplier.get();
//        if (context.getDirection().getOriginationSide() == LogicalSide.CLIENT)
//        {
//            OLD_Networker.INSTANCE.setPlayerPreference(context.getSender(), message.preference);
//        }
//        context.setPacketHandled(true);
//    }
//}