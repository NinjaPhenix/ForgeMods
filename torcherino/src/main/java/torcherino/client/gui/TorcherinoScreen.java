package torcherino.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import torcherino.Torcherino;
import torcherino.api.Tier;
import torcherino.api.TorcherinoAPI;
import torcherino.block.tile.TorcherinoTileEntity;
import torcherino.client.gui.widget.GradatedSlider;
import torcherino.client.gui.widget.StateButton;
import torcherino.network.Networker;
import torcherino.network.OpenScreenMessage;
import torcherino.network.ValueUpdateMessage;

@OnlyIn(Dist.CLIENT)
public class TorcherinoScreen extends Screen
{
    private static final ResourceLocation BACKGROUND_TEXTURE = Torcherino.getRl("textures/gui/container/torcherino.png");
    private static final int xSize = 245;
    private static final int ySize = 123;
    private final TorcherinoTileEntity tileEntity;
    private final Tier tier;
    private final ITextComponent title;
    private int guiLeft, guiTop, xRange, zRange, yRange, speed, redstoneMode;

    public TorcherinoScreen(final TorcherinoTileEntity tileEntity, final ITextComponent title, final int xRange, final int zRange, final int yRange,
            final int speed, final int redstoneMode)
    {
        super(tileEntity.getName());
        this.tileEntity = tileEntity;
        tier = TorcherinoAPI.INSTANCE.getTier(tileEntity.getTierName());
        this.title = title;
        this.xRange = xRange;
        this.zRange = zRange;
        this.yRange = yRange;
        this.speed = speed;
        this.redstoneMode = redstoneMode;
    }

    @SuppressWarnings("ConstantConditions")
    public static void open(final OpenScreenMessage msg)
    {
        final Minecraft minecraft = Minecraft.getInstance();
        minecraft.deferTask(() ->
        {
            TileEntity tileEntity = minecraft.player.world.getTileEntity(msg.pos);
            if (tileEntity instanceof TorcherinoTileEntity)
            {
                final TorcherinoScreen screen = new TorcherinoScreen((TorcherinoTileEntity) tileEntity, msg.title, msg.xRange, msg.zRange, msg.yRange,
                        msg.speed, msg.redstoneMode);
                minecraft.displayGuiScreen(screen);
            }
        });
    }

    @Override
    protected void func_231160_c_()
    {
        super.func_231160_c_();
        guiLeft = (field_230708_k_ - xSize) / 2;
        guiTop = (field_230709_l_ - ySize) / 2;
        if (speed == 0) { speed = 1; }
        func_230480_a_(new GradatedSlider(guiLeft + 8, guiTop + 20, 205, (double) (speed - 1) / (tier.MAX_SPEED - 1), tier.MAX_SPEED - 1)
        {
            @Override
            protected void func_230979_b_() { func_238482_a_(new TranslationTextComponent("gui.torcherino.speed", 100 * TorcherinoScreen.this.speed)); }

            @Override
            protected void func_230972_a_()
            {
                TorcherinoScreen.this.speed = 1 + (int) Math.round(field_230683_b_ * (TorcherinoScreen.this.tier.MAX_SPEED - 1));
                field_230683_b_ = (double) (speed - 1) / (tier.MAX_SPEED - 1);
            }
        });
        func_230480_a_(new GradatedSlider(guiLeft + 8, guiTop + 45, 205, (double) xRange / tier.XZ_RANGE, tier.XZ_RANGE)
        {
            @Override
            protected void func_230979_b_() { func_238482_a_(new TranslationTextComponent("gui.torcherino.x_range", TorcherinoScreen.this.xRange * 2 + 1)); }

            @Override
            protected void func_230972_a_()
            {
                TorcherinoScreen.this.xRange = (int) Math.round(field_230683_b_ * TorcherinoScreen.this.tier.XZ_RANGE);
                field_230683_b_ = (double) xRange / tier.XZ_RANGE;
            }
        });
        func_230480_a_(new GradatedSlider(guiLeft + 8, guiTop + 70, 205, (double) zRange / tier.XZ_RANGE, tier.XZ_RANGE)
        {
            @Override
            protected void func_230979_b_() { func_238482_a_(new TranslationTextComponent("gui.torcherino.z_range", TorcherinoScreen.this.zRange * 2 + 1)); }

            @Override
            protected void func_230972_a_()
            {
                TorcherinoScreen.this.zRange = (int) Math.round(field_230683_b_ * TorcherinoScreen.this.tier.XZ_RANGE);
                field_230683_b_ = (double) zRange / tier.XZ_RANGE;
            }
        });
        func_230480_a_(new GradatedSlider(guiLeft + 8, guiTop + 95, 205, (double) yRange / tier.Y_RANGE, tier.Y_RANGE)
        {
            @Override
            protected void func_230979_b_() { func_238482_a_(new TranslationTextComponent("gui.torcherino.y_range", TorcherinoScreen.this.yRange * 2 + 1)); }

            @Override
            protected void func_230972_a_()
            {
                TorcherinoScreen.this.yRange = (int) Math.round(field_230683_b_ * TorcherinoScreen.this.tier.Y_RANGE);
                field_230683_b_ = (double) yRange / tier.Y_RANGE;
            }
        });
        func_230480_a_(new StateButton(guiLeft + 217, guiTop + 20, field_230708_k_, field_230709_l_, redstoneMode)
        {
            private ItemStack renderStack;

            @Override
            protected void setState(final int state)
            {
                final TranslationTextComponent modeText;
                switch (state)
                {
                    case 0:
                        renderStack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft", "redstone")));
                        modeText = new TranslationTextComponent("gui.torcherino.mode.normal");
                        break;
                    case 1:
                        renderStack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft", "redstone_torch")));
                        modeText = new TranslationTextComponent("gui.torcherino.mode.inverted");
                        break;
                    case 2:
                        renderStack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft", "gunpowder")));
                        modeText = new TranslationTextComponent("gui.torcherino.mode.ignored");
                        break;
                    case 3:
                        renderStack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft", "redstone_lamp")));
                        modeText = new TranslationTextComponent("gui.torcherino.mode.off");
                        break;
                    default:
                        renderStack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft", "furnace")));
                        modeText = new TranslationTextComponent("gui.torcherino.mode.error");
                        break;
                }
                setNarrationMessage(new TranslationTextComponent("gui.torcherino.mode", modeText));
                field_230696_r_ = Util.milliTime() + 250L;
                TorcherinoScreen.this.redstoneMode = state;
            }

            @Override
            protected int getMaxStates() { return 4; }

            @Override
            protected ItemStack getButtonIcon() { return renderStack; }
        });
    }

    @SuppressWarnings({ "ConstantConditions", "deprecation" })
    public void func_230430_a_(final MatrixStack stack, final int mouseX, final int mouseY, final float partialTicks)
    {
        func_230446_a_(stack);
        RenderSystem.color4f(1, 1, 1, 1);
        field_230706_i_.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
        func_238474_b_(stack, guiLeft, guiTop, 0, 0, xSize, ySize);
        field_230712_o_.func_238422_b_(stack, title, guiLeft + (xSize - field_230712_o_.func_238414_a_(title)) / 2.0F, guiTop + 6, 4210752);
        super.func_230430_a_(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void func_231175_as__()
    {
        Networker.INSTANCE.torcherinoChannel.sendToServer(new ValueUpdateMessage(tileEntity.getPos(), xRange, zRange, yRange, speed, redstoneMode));
        super.func_231175_as__();
    }

    @Override
    public boolean func_231177_au__() { return false; }

    @SuppressWarnings("ConstantConditions")
    public boolean func_231046_a_(final int keyCode, final int scanCode, final int modifiers)
    {
        if (keyCode == 256 || field_230706_i_.gameSettings.keyBindInventory.isActiveAndMatches(InputMappings.getInputByCode(keyCode, scanCode)))
        {
            func_231175_as__();
            return true;
        }
        return super.func_231046_a_(keyCode, scanCode, modifiers);
    }
}