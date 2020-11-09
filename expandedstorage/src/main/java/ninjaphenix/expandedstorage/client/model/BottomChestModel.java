package ninjaphenix.expandedstorage.client.model;

public final class BottomChestModel extends SingleChestModel
{
    public BottomChestModel()
    {
        super(64, 32);
        base.setTexSize(0, 0);
        base.addBox(0, 0, 0, 14, 16, 14, 0);
        base.setPos(1, 0, 1);
    }
}