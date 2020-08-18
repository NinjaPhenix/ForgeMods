//package ninjaphenix.expandedstorage.common.network;
//
//import com.google.common.collect.ImmutableMap;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.entity.player.ServerPlayerEntity;
//import net.minecraft.inventory.IInventory;
//import net.minecraft.inventory.container.Container;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.text.ITextComponent;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
//import net.minecraftforge.event.entity.player.PlayerEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.network.NetworkHooks;
//import net.minecraftforge.fml.network.NetworkRegistry;
//import net.minecraftforge.fml.network.PacketDistributor;
//import net.minecraftforge.fml.network.simple.SimpleChannel;
//import ninjaphenix.expandedstorage.ExpandedStorage;
//import ninjaphenix.expandedstorage.common.OLD_ExpandedStorageConfig;
//import ninjaphenix.expandedstorage.common.inventory.*;
//
//import javax.annotation.Nullable;
//import java.util.HashMap;
//import java.util.UUID;
//import java.util.function.Consumer;
//
//public final class OLD_Networker
//{
//    public static final OLD_Networker INSTANCE = new OLD_Networker();
//    private final SimpleChannel channel;
//    private final ImmutableMap<ResourceLocation, OLD_ServerContainerFactory<?>> containerFactories =
//            new ImmutableMap.Builder<ResourceLocation, OLD_ServerContainerFactory<?>>()
//                    .put(ExpandedStorage.getRl("single"), OLD_SingleContainer::new)
//                    .put(ExpandedStorage.getRl("scrollable"), OLD_ScrollableContainer::new)
//                    .put(ExpandedStorage.getRl("paged"), OLD_PagedContainer::new)
//                    .build();
//    private final HashMap<UUID, Consumer<ResourceLocation>> preferenceCallbacks = new HashMap<>();
//    private final HashMap<UUID, ResourceLocation> playerPreferences = new HashMap<>();
//
//    private OLD_Networker()
//    {
//        final String channelVersion = "1";
//        channel = NetworkRegistry.newSimpleChannel(ExpandedStorage.getRl("channel"), () -> channelVersion, channelVersion::equals, channelVersion::equals);
//    }
//
//    public void registerMessages()
//    {
//        // @formatter:off
//        channel.registerMessage(0, OLD_PreferenceUpdateMessage.class, OLD_PreferenceUpdateMessage::encode, OLD_PreferenceUpdateMessage::decode,
//                OLD_PreferenceUpdateMessage::handle);
//        channel.registerMessage(1, OLD_OpenSelectScreenMessage.class, OLD_OpenSelectScreenMessage::encode, OLD_OpenSelectScreenMessage::decode,
//                OLD_OpenSelectScreenMessage::handle);
//        channel.registerMessage(2, OLD_RemovePreferenceCallbackMessage.class, OLD_RemovePreferenceCallbackMessage::encode, OLD_RemovePreferenceCallbackMessage::decode,
//                OLD_RemovePreferenceCallbackMessage::handle);
//        // @formatter:on
//    }
//
//    public void sendPreferenceToServer()
//    { channel.sendToServer(new OLD_PreferenceUpdateMessage(new ResourceLocation(OLD_ExpandedStorageConfig.CLIENT.preferredContainerType.get()))); }
//
//    @SuppressWarnings("InstantiationOfUtilityClass")
//    public void sendRemovePreferenceCallbackToServer() { channel.sendToServer(new OLD_RemovePreferenceCallbackMessage()); }
//
//    @SuppressWarnings("InstantiationOfUtilityClass")
//    public void requestOpenSelectionScreen() { channel.sendToServer(new OLD_OpenSelectScreenMessage()); }
//
//    @SuppressWarnings("InstantiationOfUtilityClass")
//    public void openSelectionScreen(final ServerPlayerEntity player)
//    { channel.send(PacketDistributor.PLAYER.with(() -> player), new OLD_OpenSelectScreenMessage()); }
//
//    public void setPlayerPreference(final PlayerEntity player, @Nullable final ResourceLocation containerType)
//    {
//        final UUID uuid = player.getUniqueID();
//        if (containerFactories.containsKey(containerType))
//        {
//            playerPreferences.put(uuid, containerType);
//            if (preferenceCallbacks.containsKey(uuid))
//            {
//                preferenceCallbacks.get(uuid).accept(containerType);
//                preferenceCallbacks.remove(uuid);
//            }
//        }
//        else
//        {
//            playerPreferences.remove(uuid);
//            preferenceCallbacks.remove(uuid);
//        }
//    }
//
//    void removePlayerPreferenceCallback(final PlayerEntity player) { preferenceCallbacks.remove(player.getUniqueID()); }
//
//    public void openContainer(final ServerPlayerEntity player, final IDataNamedContainerProvider containerProvider)
//    {
//        final UUID uuid = player.getUniqueID();
//        if (playerPreferences.containsKey(uuid) && containerFactories.containsKey(playerPreferences.get(uuid)))
//        { NetworkHooks.openGui(player, containerProvider, containerProvider::writeExtraData); }
//        else { openSelectScreen(player, (type) -> openContainer(player, containerProvider)); }
//    }
//
//    void openSelectScreen(final ServerPlayerEntity player, @Nullable final Consumer<ResourceLocation> preferenceSetCallback)
//    {
//        if (preferenceSetCallback != null) { preferenceCallbacks.put(player.getUniqueID(), preferenceSetCallback); }
//        OLD_Networker.INSTANCE.openSelectionScreen(player);
//    }
//
//    public Container getContainer(final int windowId, final BlockPos pos, final IInventory inventory, final PlayerEntity player,
//            final ITextComponent displayName)
//    {
//        final UUID uuid = player.getUniqueID();
//        final ResourceLocation playerPreference;
//        if (playerPreferences.containsKey(uuid) && containerFactories.containsKey(playerPreference = playerPreferences.get(uuid)))
//        { return containerFactories.get(playerPreference).create(windowId, pos, inventory, player, displayName); }
//        return null;
//    }
//
//    @SubscribeEvent @OnlyIn(Dist.CLIENT)
//    public void onPlayerConnected(final ClientPlayerNetworkEvent.LoggedInEvent event) { OLD_Networker.INSTANCE.sendPreferenceToServer(); }
//
//    @SubscribeEvent
//    public void onPlayerDisconnected(final PlayerEvent.PlayerLoggedOutEvent event) { setPlayerPreference(event.getPlayer(), null); }
//}