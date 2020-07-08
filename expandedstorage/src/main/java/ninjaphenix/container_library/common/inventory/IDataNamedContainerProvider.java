package ninjaphenix.container_library.common.inventory;

import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import org.jetbrains.annotations.NotNull;

public interface IDataNamedContainerProvider extends INamedContainerProvider
{
    void writeExtraData(@NotNull final PacketBuffer buffer);
}