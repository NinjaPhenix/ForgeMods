package ninjaphenix.expandedstorage.client.render;

import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import ninjaphenix.expandedstorage.common.block.entity.CursedChestTileEntity;

public final class CursedChestTileEntityItemStackRenderer extends TileEntityItemStackRenderer
{
    public static final CursedChestTileEntityItemStackRenderer INSTANCE = new CursedChestTileEntityItemStackRenderer();
    private static final CursedChestTileEntity cursedChestRenderEntity = new CursedChestTileEntity();

    private CursedChestTileEntityItemStackRenderer() {}

    @Override
    public void renderByItem(final ItemStack stack, final float partialTicks)
    {
        cursedChestRenderEntity.setRenderBlock(stack.getItem().delegate.name());
        TileEntityRendererDispatcher.instance.render(cursedChestRenderEntity, 0, 0, 0, 0, partialTicks);
    }
}