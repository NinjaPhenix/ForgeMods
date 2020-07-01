package ninjaphenix.expandedstorage.client.model;

public class LeftChestModel extends SingleChestModel
{

    public LeftChestModel()
    {
        super(64, 48);
        lid.addBox(0, 0, 0, 15, 5, 14, 0);
        lid.addBox(14, -2, 14, 1, 4, 1, 0);
        lid.setRotationPoint(1, 9, 1);
        base.addBox(0, 0, 0, 15, 10, 14, 0);
        base.setRotationPoint(1, 0, 1);
    }
}
