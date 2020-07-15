package torcherino.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import torcherino.client.gui.TorcherinoScreen;

import java.util.function.Supplier;

public class OpenScreenMessage
{
    public final BlockPos pos;
    public final ITextComponent title;
    public final int xRange, zRange, yRange, speed, redstoneMode;

    public OpenScreenMessage(final BlockPos pos, final ITextComponent title, final int xRange, final int zRange, final int yRange, final int speed,
            final int redstoneMode)
    {
        this.pos = pos;
        this.title = title;
        this.xRange = xRange;
        this.zRange = zRange;
        this.yRange = yRange;
        this.speed = speed;
        this.redstoneMode = redstoneMode;
    }

    static void encode(final OpenScreenMessage message, final PacketBuffer buffer)
    {
        buffer.writeBlockPos(message.pos).writeTextComponent(message.title).writeInt(message.xRange)
              .writeInt(message.zRange).writeInt(message.yRange).writeInt(message.speed).writeInt(message.redstoneMode);
    }

    static OpenScreenMessage decode(final PacketBuffer buffer)
    {
        return new OpenScreenMessage(buffer.readBlockPos(), buffer.readTextComponent(), buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt(),
                buffer.readInt());
    }

    static void handle(final OpenScreenMessage message, final Supplier<NetworkEvent.Context> contextSupplier)
    {
        final NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getOriginationSide() == LogicalSide.SERVER)
        {
            TorcherinoScreen.open(message);
            context.setPacketHandled(true);
        }
    }
}