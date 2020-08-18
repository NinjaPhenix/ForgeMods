package ninjaphenix.expandedstorage.common.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BaseChestBlock extends BlockContainer
{
    protected BaseChestBlock(final Material material, final MapColor color)
    {
        super(material, color);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(final World world, final int meta)
    {
        return null;
    }
}
