package ninjaphenix.expandedstorage.common.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import ninjaphenix.expandedstorage.common.block.enums.CursedChestType;

public class CursedChestBlock extends BaseChestBlock
{
    public static final IProperty<CursedChestType> TYPE = PropertyEnum.create("type", CursedChestType.class);

    protected CursedChestBlock(final Material material, final MapColor color)
    {
        super(material, color);
    }
}