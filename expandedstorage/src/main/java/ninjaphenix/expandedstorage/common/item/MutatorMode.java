package ninjaphenix.expandedstorage.common.item;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.KeybindTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public enum MutatorMode
{
	MERGE(new TranslationTextComponent("tooltip.expandedstorage.chest_mutator.merge"),
            src -> new TranslationTextComponent("tooltip.expandedstorage.chest_mutator.merge_desc", src), 1),
	UNMERGE(new TranslationTextComponent("tooltip.expandedstorage.chest_mutator.unmerge"),
            src -> new TranslationTextComponent("tooltip.expandedstorage.chest_mutator.unmerge_desc", src), 2),
	ROTATE(new TranslationTextComponent("tooltip.expandedstorage.chest_mutator.rotate"),
            src -> new TranslationTextComponent("tooltip.expandedstorage.chest_mutator.rotate_desc", src), 0);

	public final ITextComponent title;
	public final ITextComponent description;
	public final byte next;

	MutatorMode(@NotNull final ITextComponent title, @NotNull final Function<ITextComponent, ITextComponent> description, final int next)
	{
		this.title = title;
		this.description = description.apply(new KeybindTextComponent("key.sneak")
                .appendText(" + ").appendSibling(new KeybindTextComponent("key.use")).applyTextStyle(TextFormatting.GOLD));
		this.next = (byte) next;
	}
}