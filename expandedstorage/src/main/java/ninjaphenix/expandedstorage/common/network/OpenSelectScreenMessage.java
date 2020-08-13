package ninjaphenix.expandedstorage.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import ninjaphenix.expandedstorage.client.screen.SelectContainerScreen;
import ninjaphenix.expandedstorage.common.inventory.AbstractContainer;
import ninjaphenix.expandedstorage.common.inventory.IDataNamedContainerProvider;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public final class OpenSelectScreenMessage
{
    static void encode(final OpenSelectScreenMessage message, final PacketBuffer buffer) { }

    @SuppressWarnings("InstantiationOfUtilityClass")
    static OpenSelectScreenMessage decode(final PacketBuffer buffer) { return new OpenSelectScreenMessage(); }

    @SuppressWarnings("ConstantConditions")
    static void handle(final OpenSelectScreenMessage message, final Supplier<NetworkEvent.Context> ctx)
    {
        final NetworkEvent.Context context = ctx.get();
        if (context.getDirection().getOriginationSide() == LogicalSide.SERVER)
        {
            handleClient();
            context.setPacketHandled(true);
            return;
        }
        final ServerPlayerEntity sender = context.getSender();
        final AbstractContainer<?> container = (AbstractContainer<?>) sender.openContainer;
        Networker.INSTANCE.openSelectScreen(sender, (type) -> Networker.INSTANCE.openContainer(sender, new IDataNamedContainerProvider()
        {
            @Override
            public void writeExtraData(final PacketBuffer buffer)
            {
                buffer.writeInt(container.getInv().getSizeInventory());
                buffer.writeBlockPos(container.ORIGIN);
            }

            @Nullable @Override
            public Container createMenu(final int windowId, final PlayerInventory playerInventory, final PlayerEntity player)
            { return Networker.INSTANCE.getContainer(windowId, container.ORIGIN, container.getInv(), player, container.DISPLAY_NAME); }

            @Override
            public ITextComponent getDisplayName() { return container.DISPLAY_NAME; }
        }));
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClient() { Minecraft.getInstance().displayGuiScreen(new SelectContainerScreen()); }
}