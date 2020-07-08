package ninjaphenix.container_library.common.network;

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
import ninjaphenix.container_library.ContainerLibraryImpl;
import ninjaphenix.container_library.common.inventory.AbstractContainer;
import ninjaphenix.container_library.common.inventory.IDataNamedContainerProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class OpenSelectScreenMessage
{
    static void encode(@NotNull final OpenSelectScreenMessage message, @NotNull final PacketBuffer buffer) { }

    @SuppressWarnings("InstantiationOfUtilityClass")
    static OpenSelectScreenMessage decode(@NotNull final PacketBuffer buffer) { return new OpenSelectScreenMessage(); }

    @SuppressWarnings("ConstantConditions")
    static void handle(@NotNull final OpenSelectScreenMessage message, @NotNull final Supplier<NetworkEvent.Context> ctx)
    {
        NetworkEvent.Context context = ctx.get();
        if (context.getDirection().getOriginationSide() == LogicalSide.SERVER) { handleClient(); context.setPacketHandled(true); return; }
        final ServerPlayerEntity sender = context.getSender();
        AbstractContainer<?> container = (AbstractContainer<?>) sender.openContainer;
        ContainerLibraryImpl.INSTANCE.openSelectScreen(sender, (type) -> ContainerLibraryImpl.INSTANCE.openContainer(sender, new IDataNamedContainerProvider()
        {
            @Override
            public void writeExtraData(@NotNull final PacketBuffer buffer)
            {
                buffer.writeInt(container.getInv().getSizeInventory());
                buffer.writeBlockPos(container.ORIGIN);
            }

            @Nullable @Override
            public Container createMenu(final int windowId, @NotNull final PlayerInventory playerInventory, @NotNull final PlayerEntity player)
            { return ContainerLibraryImpl.INSTANCE.getContainer(windowId, container.ORIGIN, container.getInv(), player, container.DISPLAY_NAME); }

            @NotNull @Override
            public ITextComponent getDisplayName() { return container.DISPLAY_NAME; }
        }));
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClient() { Minecraft.getInstance().displayGuiScreen(new ninjaphenix.container_library.client.screen.SelectContainerScreen()); }
}