package ninjaphenix.container_library;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkHooks;
import ninjaphenix.container_library.common.inventory.*;
import ninjaphenix.container_library.common.network.Networker;
import ninjaphenix.expandedstorage.ExpandedStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = ExpandedStorage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ContainerLibraryImpl
{
    public static final ContainerLibraryImpl INSTANCE = new ContainerLibraryImpl();

    private final ImmutableMap<ResourceLocation, ServerContainerFactory<?>> containerFactories =
            new ImmutableMap.Builder<ResourceLocation, ServerContainerFactory<?>>()
            .put(ExpandedStorage.getRl("single"), SingleContainer::new)
            .put(ExpandedStorage.getRl("scrollable"), ScrollableContainer::new)
            .put(ExpandedStorage.getRl("paged"), PagedContainer::new)
            .build();
    private final HashMap<UUID, Consumer<ResourceLocation>> preferenceCallbacks = new HashMap<>();
    private final HashMap<UUID, ResourceLocation> playerPreferences = new HashMap<>();

    public void setPlayerPreference(@NotNull final PlayerEntity player, @Nullable final ResourceLocation containerType)
    {
        final UUID uuid = player.getUniqueID();
        if (containerFactories.containsKey(containerType))
        {
            playerPreferences.put(uuid, containerType);
            if (preferenceCallbacks.containsKey(uuid)) { preferenceCallbacks.get(uuid).accept(containerType); preferenceCallbacks.remove(uuid); }
        }
        else { playerPreferences.remove(uuid); preferenceCallbacks.remove(uuid); }
    }

    public void removePlayerPreferenceCallback(@NotNull final PlayerEntity player) { preferenceCallbacks.remove(player.getUniqueID()); }

    public void openContainer(@NotNull final ServerPlayerEntity player, @NotNull final IDataNamedContainerProvider containerProvider)
    {
        final UUID uuid = player.getUniqueID();
        if (playerPreferences.containsKey(uuid) && containerFactories.containsKey(playerPreferences.get(uuid)))
        { NetworkHooks.openGui(player, containerProvider, containerProvider::writeExtraData); }
        else { openSelectScreen(player, (type) -> openContainer(player, containerProvider)); }
    }

    public void openSelectScreen(@NotNull final ServerPlayerEntity player, @Nullable final Consumer<ResourceLocation> preferenceSetCallback)
    {
        if (preferenceSetCallback != null) { preferenceCallbacks.put(player.getUniqueID(), preferenceSetCallback); }
        Networker.INSTANCE.openSelectionScreen(player);
    }

    public Container getContainer(final int windowId, @NotNull final BlockPos pos, @NotNull final IInventory inventory, @NotNull final PlayerEntity player,
            @NotNull final ITextComponent displayName)
    {
        final UUID uuid = player.getUniqueID();
        final ResourceLocation playerPreference;
        if (playerPreferences.containsKey(uuid) && containerFactories.containsKey(playerPreference = playerPreferences.get(uuid)))
        { return containerFactories.get(playerPreference).create(windowId, pos, inventory, player, displayName); }
        return null;
    }

    @SubscribeEvent
    public void onPlayerConnected(@NotNull final ClientPlayerNetworkEvent.LoggedInEvent event) { Networker.INSTANCE.sendPreferenceToServer(); }

    @SubscribeEvent
    public void onPlayerDisconnected(@NotNull final PlayerEvent.PlayerLoggedOutEvent event) { setPlayerPreference(event.getPlayer(), null); }
}