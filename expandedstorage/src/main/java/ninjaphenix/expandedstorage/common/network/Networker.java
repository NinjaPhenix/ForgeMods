package ninjaphenix.expandedstorage.common.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import ninjaphenix.expandedstorage.ExpandedStorage;

public final class Networker
{
    public Networker INSTANCE = new Networker();
    private final SimpleNetworkWrapper channel;

    private Networker() {
        channel = NetworkRegistry.INSTANCE.newSimpleChannel(ExpandedStorage.MOD_ID);
    }
}
