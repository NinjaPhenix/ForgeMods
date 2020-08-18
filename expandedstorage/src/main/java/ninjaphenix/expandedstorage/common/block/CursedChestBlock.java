package ninjaphenix.expandedstorage.common.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import ninjaphenix.expandedstorage.common.block.entity.CursedChestTileEntity;

public class CursedChestBlock extends BaseChestBlock
{
    public CursedChestBlock(final ResourceLocation registryName, final Material material, final MapColor color)
    {
        super(registryName, material, color);
    }

    @Override
    public EnumBlockRenderType getRenderType(final IBlockState state) { return EnumBlockRenderType.ENTITYBLOCK_ANIMATED; }

    @Override
    public BlockRenderLayer getRenderLayer() { return BlockRenderLayer.CUTOUT; }

    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) { return new CursedChestTileEntity(); }

    @Override
    public boolean isOpaqueCube(final IBlockState state) { return false; }

    @Override
    public boolean isFullCube(final IBlockState state) { return false; }

    @Override
    public boolean isFullBlock(final IBlockState state) { return false; }
}