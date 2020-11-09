package ninjaphenix.expandedstorage.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import ninjaphenix.expandedstorage.common.block.entity.CursedChestTileEntity;

public final class CursedChestTileEntityItemStackRenderer extends ItemStackTileEntityRenderer
{
    private static final CursedChestTileEntity cursedChestRenderEntity = new CursedChestTileEntity(null);

    @Override
    @SuppressWarnings("ConstantConditions")
    public void renderByItem(final ItemStack stack, final ItemCameraTransforms.TransformType transformType, final MatrixStack matrix,
                             final IRenderTypeBuffer buffer, final int light, final int overlay)
    {
        cursedChestRenderEntity.setBlock(Block.byItem(stack.getItem()).getRegistryName());
        TileEntityRendererDispatcher.instance.renderItem(cursedChestRenderEntity, matrix, buffer, light, overlay);
    }
}