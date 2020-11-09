package ninjaphenix.expandedstorage.common.block.enums;

import net.minecraft.state.properties.ChestType;
import net.minecraft.util.IStringSerializable;

public enum CursedChestType implements IStringSerializable
{
    SINGLE("single", -1), TOP("top", -1), BACK("back", 2), RIGHT("right", 3), BOTTOM("bottom", -1), FRONT("front", 0), LEFT("left", 1);

    private final String name;
    private final int offset;

    CursedChestType(final String string, final int outlineOffset)
    {
        name = string;
        offset = outlineOffset;
    }

    // todo: dead code?
    public static CursedChestType valueOf(final ChestType type)
    {
        if (type == ChestType.SINGLE) { return SINGLE; }
        else if (type == ChestType.RIGHT) { return LEFT; }
        else if (type == ChestType.LEFT) { return RIGHT; }
        throw new IllegalArgumentException("Unexpected chest type passed to CursedChestType#valueOf.");
    }

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
    public String getSerializedName() { return name; }

    public int getOffset() { return offset; }
}