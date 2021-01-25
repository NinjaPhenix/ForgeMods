package ninjaphenix.expandedstorage.client.render;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.DualBrightnessCallback;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import ninjaphenix.expandedstorage.ModContent;
import ninjaphenix.expandedstorage.Registries;
import ninjaphenix.expandedstorage.client.model.*;
import ninjaphenix.expandedstorage.common.block.CursedChestBlock;
import ninjaphenix.expandedstorage.common.block.entity.CursedChestBlockEntity;
import ninjaphenix.expandedstorage.common.block.enums.CursedChestType;

import static net.minecraft.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public final class CursedChestRenderer extends TileEntityRenderer<CursedChestBlockEntity>
{
    private static final BlockState defaultState = ModContent.WOOD_CHEST.getFirst().defaultBlockState().setValue(HORIZONTAL_FACING, Direction.SOUTH)
            .setValue(CursedChestBlock.TYPE, CursedChestType.SINGLE);

    private static final ImmutableMap<CursedChestType, SingleChestModel> MODELS = new ImmutableMap.Builder<CursedChestType, SingleChestModel>()
            .put(CursedChestType.SINGLE, new SingleChestModel())
            .put(CursedChestType.FRONT, new FrontChestModel())
            .put(CursedChestType.BACK, new BackChestModel())
            .put(CursedChestType.TOP, new TopChestModel())
            .put(CursedChestType.BOTTOM, new BottomChestModel())
            .put(CursedChestType.LEFT, new LeftChestModel())
            .put(CursedChestType.RIGHT, new RightChestModel())
            .build();

    public CursedChestRenderer(final TileEntityRendererDispatcher dispatcher) { super(dispatcher); }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void render(final CursedChestBlockEntity te, final float v, final MatrixStack stack, final IRenderTypeBuffer buffer,
                       final int light, final int overlay)
    {
        final BlockState state = te.hasLevel() ? te.getBlockState() : defaultState;
        final CursedChestType chestType = state.getValue(CursedChestBlock.TYPE);
        final SingleChestModel model = getModel(chestType);
        stack.pushPose();
        stack.translate(0.5D, 0.5D, 0.5D);
        stack.mulPose(Vector3f.YP.rotationDegrees(-state.getValue(HORIZONTAL_FACING).toYRot()));
        stack.translate(-0.5D, -0.5D, -0.5D);
        model.setLidPitch(te.getOpenNess(v));
        final TileEntityMerger.ICallbackWrapper<? extends CursedChestBlockEntity> wrapper = te.hasLevel() ?
                ((CursedChestBlock) state.getBlock()).combine(state, te.getLevel(), te.getBlockPos(), true) :
                TileEntityMerger.ICallback::acceptNone;
        model.render(stack, new RenderMaterial(Atlases.CHEST_SHEET,
                                         Registries.MODELED.get(te.getBlock()).getChestTexture(chestType))
                             .buffer(buffer, RenderType::entityCutout),
                     wrapper.apply(new DualBrightnessCallback<>()).applyAsInt(light), overlay);
        stack.popPose();
    }

    public SingleChestModel getModel(final CursedChestType type) { return MODELS.get(type); }
}