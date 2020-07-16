package ninjaphenix.expandedstorage.common.inventory;

import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;

public interface IDataNamedContainerProvider extends INamedContainerProvider
{
    void writeExtraData(final PacketBuffer buffer);
}