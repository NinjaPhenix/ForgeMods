package ninjaphenix.expandedstorage.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import ninjaphenix.expandedstorage.common.block.entity.CursedChestTileEntity;
import org.jetbrains.annotations.NotNull;

public class CursedChestTileEntityItemStackRenderer extends ItemStackTileEntityRenderer
{
    private static final CursedChestTileEntity cursedChestRenderEntity = new CursedChestTileEntity();

    @Override @SuppressWarnings("ConstantConditions")
    public void render(@NotNull final ItemStack stack, @NotNull final MatrixStack matrix, @NotNull final IRenderTypeBuffer buffer, final int light,
            final int overlay)
    {
        cursedChestRenderEntity.setBlock(Block.getBlockFromItem(stack.getItem()).getRegistryName());
        TileEntityRendererDispatcher.instance.renderItem(cursedChestRenderEntity, matrix, buffer, light, overlay);
    }
}