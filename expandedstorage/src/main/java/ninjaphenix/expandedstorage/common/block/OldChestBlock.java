package ninjaphenix.expandedstorage.common.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import ninjaphenix.expandedstorage.common.block.entity.OldChestTileEntity;

public class OldChestBlock extends BaseChestBlock
{
    public OldChestBlock(final ResourceLocation registryName, final Material material, final MapColor color)
    {
        super(registryName, material, color);
    }

    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) { return new OldChestTileEntity(); }
}