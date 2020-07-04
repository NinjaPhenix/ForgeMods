package ninjaphenix.expandedstorage.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class SingleChestModel extends Model
{
	protected ModelRenderer lid;
	protected ModelRenderer base;

	public SingleChestModel(final int width, final int height)
	{
		super(RenderType::getEntityCutout);
		textureWidth = width;
		textureHeight = height;
		lid = new ModelRenderer(this, 0, 0);
		base = new ModelRenderer(this, 0, 19);
	}

	public SingleChestModel()
	{
		this(64, 48);
		lid.addBox(0, 0, 0, 14, 5, 14, 0);
		lid.addBox(6, -2, 14, 2, 4, 1, 0);
		lid.setRotationPoint(1, 9, 1);
		base.addBox(0, 0, 0, 14, 10, 14, 0);
		base.setRotationPoint(1, 0, 1);
	}

	public void setLidPitch(float pitch)
	{
		pitch = 1.0f - pitch;
		lid.rotateAngleX = -((1.0F - pitch * pitch * pitch) * 1.5707964F);
	}

	public void render(final MatrixStack stack, final IVertexBuilder builder, final int i, final int j) { render(stack, builder, i, j, 1, 1, 1, 1); }

	@Override
	public void render(final MatrixStack stack, final IVertexBuilder builder, final int i, final int j,
			final float r, final float g, final float b, final float f)
	{
		base.render(stack, builder, i, j);
		lid.render(stack, builder, i, j);
	}
}