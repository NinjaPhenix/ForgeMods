package ninjaphenix.container_library.common.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import ninjaphenix.expandedstorage.ExpandedStorage;
import ninjaphenix.expandedstorage.common.ExpandedStorageConfig;
import org.jetbrains.annotations.NotNull;

public class Networker
{
    public static final Networker INSTANCE = new Networker();
    private final SimpleChannel channel;

    private Networker()
    {
        final String channelVersion = "1";
        channel = NetworkRegistry.newSimpleChannel(ExpandedStorage.getRl("channel"), () -> channelVersion, channelVersion::equals, channelVersion::equals);
    }

    public void registerMessages()
    {
        // @formatter:off
        channel.registerMessage(0, PreferenceUpdateMessage.class, PreferenceUpdateMessage::encode, PreferenceUpdateMessage::decode, PreferenceUpdateMessage::handle);
        channel.registerMessage(1, OpenSelectScreenMessage.class, OpenSelectScreenMessage::encode, OpenSelectScreenMessage::decode, OpenSelectScreenMessage::handle);
        channel.registerMessage(2, RemovePreferenceCallbackMessage.class, RemovePreferenceCallbackMessage::encode, RemovePreferenceCallbackMessage::decode, RemovePreferenceCallbackMessage::handle);
        // @formatter:on
    }

    public void sendPreferenceToServer()
    { channel.sendToServer(new PreferenceUpdateMessage(new ResourceLocation(ExpandedStorageConfig.CLIENT.preferredContainerType.get()))); }

    @SuppressWarnings("InstantiationOfUtilityClass")
    public void sendRemovePreferenceCallbackToServer() { channel.sendToServer(new RemovePreferenceCallbackMessage()); }

    @SuppressWarnings("InstantiationOfUtilityClass")
    public void requestOpenSelectionScreen() { channel.sendToServer(new OpenSelectScreenMessage()); }

    @SuppressWarnings("InstantiationOfUtilityClass")
    public void openSelectionScreen(@NotNull final ServerPlayerEntity player)
    { channel.send(PacketDistributor.PLAYER.with(() -> player), new OpenSelectScreenMessage()); }
}