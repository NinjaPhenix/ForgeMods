package ninjaphenix.expandedstorage.client.render;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.tileentity.DualBrightnessCallback;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.util.Direction;
import ninjaphenix.expandedstorage.ModContent;
import ninjaphenix.expandedstorage.Registries;
import ninjaphenix.expandedstorage.client.model.*;
import ninjaphenix.expandedstorage.common.block.CursedChestBlock;
import ninjaphenix.expandedstorage.common.block.entity.CursedChestTileEntity;
import ninjaphenix.expandedstorage.common.block.enums.CursedChestType;
import org.jetbrains.annotations.NotNull;

public class CursedChestRenderer extends TileEntityRenderer<CursedChestTileEntity>
{
    private static final BlockState defaultState = ModContent.WOOD_CHEST.getFirst().getDefaultState().with(CursedChestBlock.FACING, Direction.SOUTH)
                                                                        .with(CursedChestBlock.TYPE, CursedChestType.SINGLE);

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

    @Override @SuppressWarnings({ "ConstantConditions", "OptionalGetWithoutIsPresent" })
    public void render(@NotNull final CursedChestTileEntity te, final float v, @NotNull final MatrixStack stack, @NotNull final IRenderTypeBuffer buffer,
            final int light, final int overlay)
    {
        final BlockState state = te.hasWorld() ? te.getBlockState() : defaultState;
        if (state.getBlock() instanceof CursedChestBlock)
        {
            final CursedChestBlock block = (CursedChestBlock) state.getBlock();
            final CursedChestType chestType = state.get(CursedChestBlock.TYPE);
            SingleChestModel model = getModel(chestType);
            stack.push();
            stack.translate(0.5D, 0.5D, 0.5D);
            stack.rotate(Vector3f.YP.rotationDegrees(-state.get(BlockStateProperties.HORIZONTAL_FACING).getHorizontalAngle()));
            stack.translate(-0.5D, -0.5D, -0.5D);
            model.setLidPitch(te.getLidAngle(v));
            TileEntityMerger.ICallbackWrapper<? extends CursedChestTileEntity> wrapper = te.hasWorld() ?
                    block.combine(state, te.getWorld(), te.getPos(), true) : TileEntityMerger.ICallback::func_225537_b_;
            int combinedLight = wrapper.apply(new DualBrightnessCallback<>()).applyAsInt(light);
            Material material = new Material(Atlases.CHEST_ATLAS, Registries.MODELED.getValue(te.getBlock()).get().getChestTexture(chestType));
            model.render(stack, material.getBuffer(buffer, RenderType::getEntityCutout), combinedLight, overlay);
            stack.pop();
        }
    }

    @NotNull
    public SingleChestModel getModel(@NotNull final CursedChestType type) { return MODELS.get(type); }
}