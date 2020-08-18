package ninjaphenix.expandedstorage.common.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import ninjaphenix.expandedstorage.common.block.enums.CursedChestType;

public abstract class BaseChestBlock extends BlockContainer
{
    public static final IProperty<CursedChestType> TYPE = PropertyEnum.create("type", CursedChestType.class);
    public static final IProperty<EnumFacing> HORIZONTAL_FACING = BlockHorizontal.FACING;
    private final ResourceLocation tier;

    protected BaseChestBlock(final ResourceLocation registryName, final Material material, final MapColor color)
    {
        super(material, color);
        setRegistryName(registryName);
        setTranslationKey(registryName.getNamespace()+"."+registryName.getPath());
        tier = registryName;

        setDefaultState(getDefaultState().withProperty(TYPE, CursedChestType.SINGLE).withProperty(HORIZONTAL_FACING, EnumFacing.NORTH));
    }

    @Override
    public EnumBlockRenderType getRenderType(final IBlockState state) { return EnumBlockRenderType.MODEL; }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, TYPE, HORIZONTAL_FACING);
    }

    @Override
    public int getMetaFromState(final IBlockState state)
    {
        return state.getValue(HORIZONTAL_FACING).getHorizontalIndex() + (state.getValue(TYPE).getMetaOffset() >> 3);
    }

    @Override
    public IBlockState getStateFromMeta(final int meta)
    {
        final EnumFacing facing = EnumFacing.byHorizontalIndex(meta & 0b0_0000_0011);
        final CursedChestType chestType = CursedChestType.fromMeta((meta & 0b1_1111_1100) >> 2);
        return getDefaultState().withProperty(HORIZONTAL_FACING, facing).withProperty(TYPE, chestType);
    }
}
