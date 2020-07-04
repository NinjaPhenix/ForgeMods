package ninjaphenix.expandedstorage.common.block.enums;

import net.minecraft.state.properties.ChestType;
import net.minecraft.util.IStringSerializable;
import org.jetbrains.annotations.NotNull;

public enum CursedChestType implements IStringSerializable
{
    SINGLE("single"), TOP("top"), BACK("back"), RIGHT("right"), BOTTOM("bottom"), FRONT("front"), LEFT("left");

    private final String name;

    CursedChestType(String string) { name = string; }

    public static CursedChestType valueOf(ChestType type)
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
	public String getName() { return name; }
}