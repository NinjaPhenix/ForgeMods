package ninjaphenix.expandedstorage.common.block.enums;

import net.minecraft.state.properties.ChestType;
import net.minecraft.util.IStringSerializable;
import org.jetbrains.annotations.NotNull;

public enum CursedChestType implements IStringSerializable
{
    SINGLE("single", -1), TOP("top", -1), BACK("back", 2), RIGHT("right", 3), BOTTOM("bottom", -1), FRONT("front", 0), LEFT("left", 1);

    private final String name;
    private final int offset;

    CursedChestType(@NotNull final String string, final int outlineOffset) { name = string; offset = outlineOffset; }

    public static CursedChestType valueOf(@NotNull final ChestType type)
    {
        if (type == ChestType.SINGLE) { return SINGLE; }
        else if (type == ChestType.RIGHT) { return LEFT; }
        else if (type == ChestType.LEFT) { return RIGHT; }
        throw new IllegalArgumentException("Unexpected chest type passed.");
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

    @NotNull @Override
    public String getString() { return name; }

    public int getOffset() { return offset; }
}