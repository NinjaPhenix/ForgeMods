package ninjaphenix.expandedstorage.client.render;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.BlockPos;
import ninjaphenix.expandedstorage.client.model.*;
import ninjaphenix.expandedstorage.common.block.CursedChestBlock;
import ninjaphenix.expandedstorage.common.block.entity.CursedChestTileEntity;
import ninjaphenix.expandedstorage.common.block.enums.CursedChestType;

public final class CursedChestRenderer extends TileEntitySpecialRenderer<CursedChestTileEntity>
{
    //private static final IBlockState defaultState = OLD_ModContent.WOOD_CHEST.getFirst().getDefaultState().with(OLD_CursedChestBlock.FACING, Direction.SOUTH)
    //                                                                        .with(OLD_CursedChestBlock.TYPE, CursedChestType.SINGLE);

    // todo: default block state
    private static final IBlockState defaultState = null;

    private static final ImmutableMap<CursedChestType, SingleChestModel> MODELS = new ImmutableMap.Builder<CursedChestType, SingleChestModel>()
            .put(CursedChestType.SINGLE, new SingleChestModel())
            .put(CursedChestType.FRONT, new FrontChestModel())
            .put(CursedChestType.BACK, new BackChestModel())
            .put(CursedChestType.TOP, new TopChestModel())
            .put(CursedChestType.BOTTOM, new BottomChestModel())
            .put(CursedChestType.LEFT, new LeftChestModel())
            .put(CursedChestType.RIGHT, new RightChestModel())
            .build();

    public static final CursedChestRenderer INSTANCE = new CursedChestRenderer();

    private CursedChestRenderer() {}

    @Override
    public void render(final CursedChestTileEntity tile, final double x, final double y, final double z, final float partialTicks,
            final int destroyStage, final float alpha)
    {
        super.render(tile, x, y, z, partialTicks, destroyStage, alpha);
        final IBlockState state = tile.hasWorld() ? tile.getWorld().getBlockState(new BlockPos(x, y, z)) : defaultState;
        if (state.getBlock() instanceof CursedChestBlock)
        {
            final CursedChestBlock block = (CursedChestBlock) state.getBlock();
            final SingleChestModel model = getModel(state.getValue(CursedChestBlock.TYPE));
            model.renderAll();
            // todo: combined light and destroy progress.
        }
    }

    //@Override @SuppressWarnings("ConstantConditions")
    //public void render(final OLD_CursedChestTileEntity te, final float v, final MatrixStack stack, final IRenderTypeBuffer buffer,
    //        final int light, final int overlay)
    //{
    //    final BlockState state = te.hasWorld() ? te.getBlockState() : defaultState;
    //    if (state.getBlock() instanceof CursedChestBlock)
    //    {
    //        final OLD_CursedChestBlock block = (OLD_CursedChestBlock) state.getBlock();
    //        final CursedChestType chestType = state.get(OLD_CursedChestBlock.TYPE);
    //        final SingleChestModel model = getModel(chestType);
    //        stack.push();
    //        stack.translate(0.5D, 0.5D, 0.5D);
    //        stack.rotate(Vector3f.YP.rotationDegrees(-state.get(BlockStateProperties.HORIZONTAL_FACING).getHorizontalAngle()));
    //        stack.translate(-0.5D, -0.5D, -0.5D);
    //        model.setLidPitch(te.getLidAngle(v));
    //        final TileEntityMerger.ICallbackWrapper<? extends OLD_CursedChestTileEntity> wrapper = te.hasWorld() ?
    //                block.combine(state, te.getWorld(), te.getPos(), true) : TileEntityMerger.ICallback::func_225537_b_;
    //        final int combinedLight = wrapper.apply(new DualBrightnessCallback<>()).applyAsInt(light);
    //        final RenderMaterial material = new RenderMaterial(Atlases.CHEST_ATLAS, Registries.MODELED.getOrDefault(te.getBlock()).getChestTexture(chestType));
    //        model.render(stack, material.getBuffer(buffer, RenderType::getEntityCutout), combinedLight, overlay);
    //        stack.pop();
    //    }
    //}

    public SingleChestModel getModel(final CursedChestType type) { return MODELS.get(type); }
}