package ninjaphenix.expandedstorage.common.network;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import ninjaphenix.expandedstorage.ExpandedStorage;
import ninjaphenix.expandedstorage.common.ExpandedStorageConfig;
import ninjaphenix.expandedstorage.common.inventory.*;
import org.jetbrains.annotations.Nullable;

public final class Networker
{
    public static final Networker INSTANCE = new Networker();
    private final SimpleChannel channel;
    private final ImmutableMap<ResourceLocation, ServerContainerFactory<?>> containerFactories = new ImmutableMap.Builder<ResourceLocation, ServerContainerFactory<?>>().put(ExpandedStorage.getRl("single"), SingleContainer::new).put(ExpandedStorage.getRl("scrollable"), ScrollableContainer::new).put(ExpandedStorage.getRl("paged"), PagedContainer::new).build();
    private final HashMap<UUID, Consumer<ResourceLocation>> preferenceCallbacks = new HashMap<>();
    private final HashMap<UUID, ResourceLocation> playerPreferences = new HashMap<>();

    private Networker()
    {
        final String channelVersion = "2";
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
    {
        channel.sendToServer(new PreferenceUpdateMessage(new ResourceLocation(ExpandedStorageConfig.CLIENT.preferredContainerType.get())));
    }

    @SuppressWarnings("InstantiationOfUtilityClass")
    public void sendRemovePreferenceCallbackToServer()
    {
        channel.sendToServer(new RemovePreferenceCallbackMessage());
    }

    @SuppressWarnings("InstantiationOfUtilityClass")
    public void requestOpenSelectionScreen()
    {
        channel.sendToServer(new OpenSelectScreenMessage());
    }

    @SuppressWarnings("InstantiationOfUtilityClass")
    public void openSelectionScreen(final ServerPlayerEntity player)
    {
        channel.send(PacketDistributor.PLAYER.with(() -> player), new OpenSelectScreenMessage());
    }

    public void setPlayerPreference(final PlayerEntity player, final @Nullable ResourceLocation containerType)
    {
        final UUID uuid = player.getUniqueID();
        if (containerFactories.containsKey(containerType))
        {
            playerPreferences.put(uuid, containerType);
            if (preferenceCallbacks.containsKey(uuid))
            {
                preferenceCallbacks.get(uuid).accept(containerType);
                preferenceCallbacks.remove(uuid);
            }
        }
        else
        {
            playerPreferences.remove(uuid);
            preferenceCallbacks.remove(uuid);
        }
    }

    void removePlayerPreferenceCallback(final PlayerEntity player)
    {
        preferenceCallbacks.remove(player.getUniqueID());
    }

    public void openContainer(final ServerPlayerEntity player, final IDataNamedContainerProvider containerProvider)
    {
        final UUID uuid = player.getUniqueID();
        if (playerPreferences.containsKey(uuid) && containerFactories.containsKey(playerPreferences.get(uuid)))
        {
            NetworkHooks.openGui(player, containerProvider, containerProvider::writeExtraData);
        }
        else
        {
            openSelectScreen(player, (type) -> openContainer(player, containerProvider));
        }
    }

    void openSelectScreen(final ServerPlayerEntity player, final @Nullable Consumer<ResourceLocation> preferenceSetCallback)
    {
        if (preferenceSetCallback != null)
        {
            preferenceCallbacks.put(player.getUniqueID(), preferenceSetCallback);
        }
        Networker.INSTANCE.openSelectionScreen(player);
    }

    public @Nullable Container getContainer(final int windowId, final BlockPos pos, final IInventory inventory, final PlayerEntity player, final ITextComponent displayName)
    {
        final UUID uuid = player.getUniqueID();
        final ResourceLocation playerPreference;
        if (playerPreferences.containsKey(uuid) && containerFactories.containsKey(playerPreference = playerPreferences.get(uuid)))
        {
            return containerFactories.get(playerPreference).create(windowId, pos, inventory, player, displayName);
        }
        return null;
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onPlayerConnected(final ClientPlayerNetworkEvent.LoggedInEvent event)
    {
        Networker.INSTANCE.sendPreferenceToServer();
    }

    @SubscribeEvent
    public void onPlayerDisconnected(final PlayerEvent.PlayerLoggedOutEvent event)
    {
        setPlayerPreference(event.getPlayer(), null);
    }
}