package torcherino.client.gui.buttons;

import net.minecraft.client.gui.widget.AbstractSlider;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;

public abstract class FixedSliderButton extends AbstractSlider
{
    private final float nudgeAmount;

    public FixedSliderButton(final int x, final int y, final int width, final double progress, final int permutations)
    {
        super(x, y, width, 20, StringTextComponent.field_240750_d_, progress);
        this.nudgeAmount = 1.0F / permutations;
        this.func_230972_a_();
        this.func_230979_b_();
    }

    @Override
    public boolean func_231046_a_(final int keyCode, final int scanCode, final int modifiers)
    {
        final boolean pressedLeft = keyCode == 263;
        if (pressedLeft || keyCode == 262)
        {
            float offset = pressedLeft ? -nudgeAmount : nudgeAmount;
            this.setValue(this.field_230683_b_ + offset);
        }
        return false;
    }

    private void setValue(final double value)
    {
        double oldValue = this.field_230683_b_;
        this.field_230683_b_ = MathHelper.clamp(value, 0, 1);
        if (oldValue != this.field_230683_b_) this.func_230972_a_();
        this.func_230979_b_();
    }
}
