package ninjaphenix.expandedstorage.common.item;

import net.minecraft.util.text.IFormattableTextComponent;
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

	public final IFormattableTextComponent title, description;
	public final byte next;

	MutatorMode(@NotNull final IFormattableTextComponent title, @NotNull final Function<IFormattableTextComponent, IFormattableTextComponent> description,
            final int next)
	{
		this.title = title;
		this.description = description.apply(new KeybindTextComponent("key.sneak").func_240702_b_(" + ").func_230529_a_(new KeybindTextComponent("key.use"))
                                                                                  .func_240699_a_(TextFormatting.GOLD)).func_240699_a_(TextFormatting.GRAY);
		this.next = (byte) next;
	}
}