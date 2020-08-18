//package ninjaphenix.expandedstorage.common.network;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.entity.player.PlayerInventory;
//import net.minecraft.entity.player.ServerPlayerEntity;
//import net.minecraft.inventory.container.Container;
//import net.minecraft.network.PacketBuffer;
//import net.minecraft.util.text.ITextComponent;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.network.NetworkEvent;
//import ninjaphenix.expandedstorage.client.screen.OLD_SelectContainerScreen;
//import ninjaphenix.expandedstorage.common.inventory.OLD_AbstractContainer;
//
//import javax.annotation.Nullable;
//import java.util.function.Supplier;
//
//public final class OLD_OpenSelectScreenMessage
//{
//    static void encode(final OLD_OpenSelectScreenMessage message, final PacketBuffer buffer) { }
//
//    @SuppressWarnings("InstantiationOfUtilityClass")
//    static OLD_OpenSelectScreenMessage decode(final PacketBuffer buffer) { return new OLD_OpenSelectScreenMessage(); }
//
//    @SuppressWarnings("ConstantConditions")
//    static void handle(final OLD_OpenSelectScreenMessage message, final Supplier<NetworkEvent.Context> ctx)
//    {
//        final NetworkEvent.Context context = ctx.get();
//        if (context.getDirection().getOriginationSide() == LogicalSide.SERVER)
//        {
//            handleClient();
//            context.setPacketHandled(true);
//            return;
//        }
//        final ServerPlayerEntity sender = context.getSender();
//        final OLD_AbstractContainer<?> container = (OLD_AbstractContainer<?>) sender.openContainer;
//        OLD_Networker.INSTANCE.openSelectScreen(sender, (type) -> OLD_Networker.INSTANCE.openContainer(sender, new IDataNamedContainerProvider()
//        {
//            @Override
//            public void writeExtraData(final PacketBuffer buffer)
//            {
//                buffer.writeInt(container.getInv().getSizeInventory());
//                buffer.writeBlockPos(container.ORIGIN);
//            }
//
//            @Nullable @Override
//            public Container createMenu(final int windowId, final PlayerInventory playerInventory, final PlayerEntity player)
//            { return OLD_Networker.INSTANCE.getContainer(windowId, container.ORIGIN, container.getInv(), player, container.DISPLAY_NAME); }
//
//            @Override
//            public ITextComponent getDisplayName() { return container.DISPLAY_NAME; }
//        }));
//        context.setPacketHandled(true);
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    private static void handleClient() { Minecraft.getInstance().displayGuiScreen(new OLD_SelectContainerScreen()); }
//}