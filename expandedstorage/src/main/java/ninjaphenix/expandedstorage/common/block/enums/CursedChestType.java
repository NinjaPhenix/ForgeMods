package ninjaphenix.expandedstorage.common.block.enums;

import net.minecraft.util.IStringSerializable;

public enum CursedChestType implements IStringSerializable
{
    SINGLE("single", -1, 0),
    TOP("top", -1, 1),
    BACK("back", 2, 2),
    RIGHT("right", 3, 3),
    BOTTOM("bottom", -1, 4),
    FRONT("front", 0, 5),
    LEFT("left", 1, 6);

    private final String name;
    private final int outline;
    private final int meta;

    CursedChestType(final String string, final int outlineOffset, final int metaOffset)
    {
        name = string;
        outline = outlineOffset;
        meta = metaOffset;
    }

    public static CursedChestType fromMeta(final int meta)
    {
        switch (meta) {
            case 0: return SINGLE;
            case 1: return TOP;
            case 2: return BACK;
            case 3: return RIGHT;
            case 4: return BOTTOM;
            case 5: return FRONT;
            case 6: return LEFT;
        }
        throw new IllegalArgumentException("Unexpected meta index passed.");
    }

    // todo: add replacement
    //public static CursedChestType valueOf(final ChestT type)
    //{
    //    if (type == ChestType.SINGLE) { return SINGLE; }
    //    else if (type == ChestType.RIGHT) { return LEFT; }
    //    else if (type == ChestType.LEFT) { return RIGHT; }
    //    throw new IllegalArgumentException("Unexpected chest type passed.");
    //}

    public CursedChestType getOpposite()
    {
        if (this == FRONT) { return BACK; }
        else if (this == BACK) { return FRONT; }
        else if (this == BOTTOM) { return TOP; }
        else if (this == TOP) { return BOTTOM; }
        else if (this == LEFT) { return RIGHT; }
        else if (this == RIGHT) { return LEFT; }
        throw new IllegalArgumentException("CursedChestType#getOpposite is not supported for type SINGLE");
    }

    @Override
    public String getName() { return name; }

    public int getOutlineOffset() { return outline; }

    public int getMetaOffset() { return meta; }
}