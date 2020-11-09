package torcherino.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import torcherino.Torcherino;
import torcherino.api.TorcherinoAPI;
import torcherino.block.tile.TorcherinoTileEntity;

public final class Networker
{
    public static final Networker INSTANCE = new Networker();

    public SimpleChannel torcherinoChannel;

    public void initialise()
    {
        final String version = "2";
        torcherinoChannel = NetworkRegistry.newSimpleChannel(Torcherino.getRl("channel"), () -> version, version::equals, version::equals);
        torcherinoChannel.registerMessage(0, ValueUpdateMessage.class, ValueUpdateMessage::encode, ValueUpdateMessage::decode, ValueUpdateMessage::handle);
        torcherinoChannel.registerMessage(1, OpenScreenMessage.class, OpenScreenMessage::encode, OpenScreenMessage::decode, OpenScreenMessage::handle);
        torcherinoChannel.registerMessage(2, S2CTierSyncMessage.class, S2CTierSyncMessage::encode, S2CTierSyncMessage::decode, S2CTierSyncMessage::handle);
    }

    public void openScreenServer(final World world, final ServerPlayerEntity player, final BlockPos pos)
    {
        if (world.isClientSide) { return; }
        final TileEntity tile = world.getBlockEntity(pos);
        if (tile instanceof TorcherinoTileEntity)
        { torcherinoChannel.sendTo(((TorcherinoTileEntity) tile).createOpenMessage(), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT); }
    }

    public void sendServerTiers(final ServerPlayerEntity player)
    {
        final S2CTierSyncMessage message = new S2CTierSyncMessage(TorcherinoAPI.INSTANCE.getTiers());
        torcherinoChannel.sendTo(message, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public void processPlayerJoin(final PlayerEvent.PlayerLoggedInEvent event) { sendServerTiers((ServerPlayerEntity) event.getPlayer()); }
}