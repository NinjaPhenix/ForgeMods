package torcherino.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.Collections;

public abstract class StateButton extends AbstractButton
{
    private final int screenWidth, screenHeight;
    private int state;
    private ITextComponent narrationMessage;

    public StateButton(final int x, final int y, final int screenWidth, final int screenHeight, final int state)
    {
        super(x, y, 20, 20, StringTextComponent.field_240750_d_);
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        setInternalState(state);
    }

    protected abstract ItemStack getButtonIcon();

    protected abstract void setState(final int state);

    protected abstract int getMaxStates();

    private void setInternalState(final int stateIn)
    {
        state = (stateIn >= getMaxStates() ? 0 : stateIn);
        setState(state);
    }

    @Override
    public void func_230430_a_(final MatrixStack stack, final int mouseX, final int mouseY, final float partialTicks)
    {
        super.func_230430_a_(stack, mouseX, mouseY, partialTicks);
        if (field_230694_p_)
        {
            Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(getButtonIcon(), field_230690_l_ + 2, field_230691_m_ + 2);
            if (func_230449_g_())
            {
                GuiUtils.drawHoveringText(getButtonIcon(), stack, Collections.singletonList(narrationMessage), field_230690_l_ + field_230688_j_ / 2,
                        field_230691_m_ + field_230689_k_ / 2, screenWidth, screenHeight, -1, Minecraft.getInstance().fontRenderer);
            }
        }
    }

    @Override
    public void func_230930_b_() { setInternalState(++state); }

    @Override
    public IFormattableTextComponent func_230442_c_() { return new TranslationTextComponent("gui.narrate.button", narrationMessage); }

    public void setNarrationMessage(final ITextComponent narrationMessage) { this.narrationMessage = narrationMessage; }
}