package ninjaphenix.expandedstorage.client.model;

public class BottomChestModel extends SingleChestModel
{
    public BottomChestModel()
    {
        super(64, 32);
        base.setTextureOffset(0, 0);
        base.addBox(0, 0, 0, 14, 16, 14, 0);
        base.setRotationPoint(1, 0, 1);
    }
}