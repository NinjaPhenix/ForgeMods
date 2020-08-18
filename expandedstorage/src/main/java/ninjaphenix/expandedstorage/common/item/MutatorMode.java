package ninjaphenix.expandedstorage.common.item;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import ninjaphenix.expandedstorage.ExpandedStorage;

import java.util.function.Function;

public enum MutatorMode
{
    MERGE(new TextComponentTranslation("tooltip.expandedstorage.chest_mutator.merge"),
            src -> new TextComponentTranslation("tooltip.expandedstorage.chest_mutator.merge_desc", src), 1),
    UNMERGE(new TextComponentTranslation("tooltip.expandedstorage.chest_mutator.unmerge"),
            src -> new TextComponentTranslation("tooltip.expandedstorage.chest_mutator.unmerge_desc", src), 2),
    ROTATE(new TextComponentTranslation("tooltip.expandedstorage.chest_mutator.rotate"),
            src -> new TextComponentTranslation("tooltip.expandedstorage.chest_mutator.rotate_desc", src), 0);

    public final ITextComponent title, description;
    public final byte next;

    MutatorMode(final ITextComponent title, final Function<ITextComponent, TextComponentTranslation> description, final int next)
    {
        this.title = title;
        this.description = description.apply(ExpandedStorage.leftShiftRightClick);
        this.description.getStyle().setColor(TextFormatting.GRAY);
        this.next = (byte) next;
    }
}