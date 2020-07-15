package torcherino.client.gui.widget;

import net.minecraft.client.gui.widget.AbstractSlider;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;

public abstract class GradatedSlider extends AbstractSlider
{
    private final float nudgeAmount;

    public GradatedSlider(final int x, final int y, final int width, final double progress, final int permutations)
    {
        super(x, y, width, 20, StringTextComponent.EMPTY, progress);
        nudgeAmount = 1.0F / permutations;
        func_230972_a_();
        func_230979_b_();
    }

    @Override
    public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers)
    {
        final boolean leftArrowKeyDown = keyCode == 263;
        if (leftArrowKeyDown || keyCode == 262)
        {
            setValue(field_230683_b_ + (leftArrowKeyDown ? -nudgeAmount : nudgeAmount));
            return true;
        }
        return false;
    }

    private void setValue(final double value)
    {
        final double oldValue = field_230683_b_;
        field_230683_b_ = MathHelper.clamp(value, 0, 1);
        if (oldValue != field_230683_b_) { func_230972_a_(); }
        func_230979_b_();
    }
}