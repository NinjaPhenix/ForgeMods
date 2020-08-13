package torcherino.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public abstract class StateButton extends AbstractButton
{
    private int state;
    private ITextComponent narrationMessage;

    public StateButton(final int x, final int y, final int state)
    {
        super(x, y, 20, 20, StringTextComponent.EMPTY);
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
    public void renderButton(final MatrixStack stack, final int mouseX, final int mouseY, final float partialTicks)
    {
        super.renderButton(stack, mouseX, mouseY, partialTicks);
        if (active)
        {
            Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(getButtonIcon(), x + 2, y + 2);
            if (isHovered())
            {
                drawHoveringText(stack, narrationMessage, mouseX, mouseY);
            }
        }
    }

    protected abstract void drawHoveringText(final MatrixStack stack, final ITextComponent text, final int width, final int height);

    @Override
    public void onPress() { setInternalState(++state); }

    @Override
    protected IFormattableTextComponent getNarrationMessage() { return new TranslationTextComponent("gui.narrate.button", narrationMessage); }

    public void setNarrationMessage(final ITextComponent narrationMessage) { this.narrationMessage = narrationMessage; }
}