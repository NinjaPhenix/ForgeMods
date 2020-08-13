package ninjaphenix.expandedstorage.common.item;

import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import ninjaphenix.expandedstorage.ExpandedStorage;

import java.util.function.Function;

public enum MutatorMode
{
    MERGE(new TranslationTextComponent("tooltip.expandedstorage.chest_mutator.merge"),
            src -> new TranslationTextComponent("tooltip.expandedstorage.chest_mutator.merge_desc", src), 1),
    UNMERGE(new TranslationTextComponent("tooltip.expandedstorage.chest_mutator.unmerge"),
            src -> new TranslationTextComponent("tooltip.expandedstorage.chest_mutator.unmerge_desc", src), 2),
    ROTATE(new TranslationTextComponent("tooltip.expandedstorage.chest_mutator.rotate"),
            src -> new TranslationTextComponent("tooltip.expandedstorage.chest_mutator.rotate_desc", src), 0);

    public final ITextComponent title, description;
    public final byte next;

    MutatorMode(final ITextComponent title, final Function<ITextComponent, IFormattableTextComponent> description,
            final int next)
    {
        this.title = title;
        this.description = description.apply(ExpandedStorage.leftShiftRightClick).mergeStyle(TextFormatting.GRAY);
        this.next = (byte) next;
    }
}