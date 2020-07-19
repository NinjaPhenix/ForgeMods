package ninjaphenix.refinement.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import ninjaphenix.refinement.common.container.UpgradeContainer;

public abstract class UpgradableBlock extends Block implements INamedContainerProvider
{
    protected UpgradableBlock(Properties properties) { super(properties); }

    @Override
    public final boolean hasTileEntity(final BlockState state) { return true; }

    @Override
    public abstract TileEntity createTileEntity(final BlockState state, final IBlockReader world);

    @Override
    public final ITextComponent getDisplayName()
    {
        return new TranslationTextComponent("container.refinement.upgrade");
    }

    @Override
    public Container createMenu(final int windowId, final PlayerInventory playerInventory, final PlayerEntity player)
    {
        return new UpgradeContainer(windowId, playerInventory);
    }

    @Override @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand,
            final BlockRayTraceResult hit)
    {
        if (player.isSneaking())
        {
            if (!world.isRemote())
            {
                NetworkHooks.openGui((ServerPlayerEntity) player, this);
            }
            return ActionResultType.func_233537_a_(world.isRemote);
        }
        return super.onBlockActivated(state, world, pos, player, hand, hit);
    }
}