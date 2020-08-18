package ninjaphenix.expandedstorage.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class SingleChestModel extends ModelBase
{
    protected final ModelRenderer lid, base;

    public SingleChestModel(final int width, final int height)
    {
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

    public void setLidPitch(final float pitch)
    {
        final float inversePitch = 1.0f - pitch;
        lid.rotateAngleX = -((1.0F - inversePitch * inversePitch * inversePitch) * 1.5707964F);
    }

    public void renderAll() {
        lid.render(0.0625F);
        base.render(0.0625F);
    }
}