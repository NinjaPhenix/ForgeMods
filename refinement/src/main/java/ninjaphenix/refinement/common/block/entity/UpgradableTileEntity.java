package ninjaphenix.refinement.common.block.entity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public abstract class UpgradableTileEntity extends TileEntity
{
    protected UpgradableTileEntity(final TileEntityType<?> type) { super(type); }
}