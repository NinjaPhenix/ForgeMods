package ninjaphenix.expandedstorage.common.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import ninjaphenix.expandedstorage.ModContent;
import ninjaphenix.expandedstorage.Registries;
import ninjaphenix.expandedstorage.common.block.BaseChestBlock;
import ninjaphenix.expandedstorage.common.block.CursedChestBlock;
import ninjaphenix.expandedstorage.common.inventory.AbstractContainer;
import ninjaphenix.expandedstorage.common.inventory.DoubleSidedInventory;
import javax.annotation.Nullable;

@OnlyIn(value = Dist.CLIENT, _interface = IChestLid.class)
public final class CursedChestTileEntity extends AbstractChestTileEntity implements IChestLid, ITickableTileEntity
{
    private float animationAngle, lastAnimationAngle;
    private int viewerCount, ticksOpen;

    public CursedChestTileEntity(final @Nullable ResourceLocation block) { super(ModContent.CURSED_CHEST_TE, block); }

    private static int tickViewerCount(final World world, final CursedChestTileEntity instance, final int ticksOpen, final int x,
                                       final int y, final int z, final int viewCount)
    {
        if (!world.isRemote && viewCount != 0 && (ticksOpen + x + y + z) % 200 == 0)
        {
            return world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(x - 5, y - 5, z - 5, x + 6, y + 6, z + 6)).stream()
                    .filter(player -> player.openContainer instanceof AbstractContainer)
                    .map(player -> ((AbstractContainer<?>) player.openContainer).getInv())
                    .filter(inventory -> inventory == instance ||
                            inventory instanceof DoubleSidedInventory && ((DoubleSidedInventory) inventory).isPart(instance))
                    .mapToInt(inv -> 1).sum();
        }
        return viewCount;
    }

    @Override
    protected void initialize(final ResourceLocation block)
    {
        this.block = block;
        final Registries.ModeledTierData data = Registries.MODELED.getOrDefault(block);
        defaultContainerName = data.getContainerName();
        inventorySize = data.getSlotCount();
        inventory = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
        SLOTS = new int[inventorySize];
        for (int i = 0; i < inventorySize; i++) { SLOTS[i] = i; }
    }

    @Override
    public boolean receiveClientEvent(final int actionId, final int value)
    {
        if (actionId == 1)
        {
            viewerCount = value;
            return true;
        }
        else { return super.receiveClientEvent(actionId, value); }
    }

    @Override
    public float getLidAngle(final float f) { return MathHelper.lerp(f, lastAnimationAngle, animationAngle); }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void tick()
    {
        viewerCount = tickViewerCount(world, this, ++ticksOpen, pos.getX(), pos.getY(), pos.getZ(), viewerCount);
        lastAnimationAngle = animationAngle;
        if (viewerCount > 0 && animationAngle == 0.0F) { playSound(SoundEvents.BLOCK_CHEST_OPEN); }
        if (viewerCount == 0 && animationAngle > 0.0F || viewerCount > 0 && animationAngle < 1.0F)
        {
            animationAngle = MathHelper.clamp(animationAngle + (viewerCount > 0 ? 0.1F : -0.1F), 0, 1);
            if (animationAngle < 0.5F && lastAnimationAngle >= 0.5F) { playSound(SoundEvents.BLOCK_CHEST_CLOSE); }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void playSound(final SoundEvent soundEvent)
    {
        final BlockState state = getBlockState();
        if (BaseChestBlock.getMergeType(state) == TileEntityMerger.Type.SECOND) { return; }
        final Vector3i offset = BaseChestBlock.getDirectionToAttached(state).getDirectionVec();
        final Vector3d soundPos = Vector3d.copyCentered(pos).add(offset.getX() * 0.5D, offset.getY() * 0.5D, offset.getZ() * 0.5D);
        world.playSound(null, soundPos.getX(), soundPos.getY(), soundPos.getZ(), soundEvent, SoundCategory.BLOCKS, 0.5F,
                        world.rand.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public void openInventory(final PlayerEntity player)
    {
        if (player.isSpectator()) { return; }
        if (viewerCount < 0) { viewerCount = 0; }
        viewerCount++;
        onInvOpenOrClose();
    }

    @Override
    public void closeInventory(final PlayerEntity player)
    {
        if (player.isSpectator()) { return; }
        viewerCount--;
        onInvOpenOrClose();
    }

    @SuppressWarnings("ConstantConditions")
    private void onInvOpenOrClose()
    {
        final Block block = getBlockState().getBlock();
        if (block instanceof CursedChestBlock)
        {
            world.addBlockEvent(pos, block, 1, viewerCount);
            world.notifyNeighborsOfStateChange(pos, block);
        }
    }
}