package ninjaphenix.expandedstorage.common.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import ninjaphenix.expandedstorage.ModContent;
import ninjaphenix.expandedstorage.Registries;
import ninjaphenix.expandedstorage.common.block.BaseChestBlock;
import ninjaphenix.expandedstorage.common.block.ChestBlock;
import ninjaphenix.expandedstorage.common.block.CursedChestBlock;
import ninjaphenix.expandedstorage.common.inventory.AbstractContainer;
import ninjaphenix.expandedstorage.common.inventory.DoubleSidedInventory;
import javax.annotation.Nullable;

@OnlyIn(value = Dist.CLIENT, _interface = IChestLid.class)
public final class CursedChestBlockEntity extends StorageBlockEntity implements IChestLid, ITickableTileEntity
{
    private float animationAngle, lastAnimationAngle;
    private int viewerCount, ticksOpen;

    public CursedChestBlockEntity(final ResourceLocation block) { super(ModContent.CURSED_CHEST_TE, block); }

    public static int countViewers(final World world, final ISidedInventory instance, final int x, final int y, final int z)
    {
        return world.getEntitiesOfClass(PlayerEntity.class, new AxisAlignedBB(x - 5, y - 5, z - 5, x + 6, y + 6, z + 6)).stream()
                .filter(player -> player.containerMenu instanceof AbstractContainer)
                .map(player -> ((AbstractContainer<?>) player.containerMenu).getInv())
                .filter(inventory -> inventory == instance ||
                        inventory instanceof DoubleSidedInventory && ((DoubleSidedInventory) inventory).isPart(instance))
                .mapToInt(inv -> 1).sum();
    }

    private static int tickViewerCount(final World world, final CursedChestBlockEntity instance, final int ticksOpen, final int x,
                                       final int y, final int z, final int viewCount)
    {
        if (!world.isClientSide && viewCount != 0 && (ticksOpen + x + y + z) % 200 == 0)
        {
            return countViewers(world, instance, x, y, z);
        }
        return viewCount;
    }

    public ResourceLocation getBlock() { return block; }

    // Client only!
    public void setBlock(final ResourceLocation block) { this.block = block; }

    @Override
    @SuppressWarnings({"ConstantConditions"})
    protected void initialize(final ResourceLocation block)
    {
        this.block = block;
        defaultContainerName = Registries.CHEST.get(block).CONTAINER_NAME;
        inventorySize = Registries.CHEST.get(block).SLOT_COUNT;
        inventory = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
        SLOTS = new int[inventorySize];
        for (int i = 0; i < inventorySize; i++) { SLOTS[i] = i; }
    }

    @Override
    public boolean triggerEvent(final int actionId, final int value)
    {
        if (actionId == 1)
        {
            viewerCount = value;
            return true;
        }
        else { return super.triggerEvent(actionId, value); }
    }

    @Override
    public float getOpenNess(final float f) { return MathHelper.lerp(f, lastAnimationAngle, animationAngle); }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void tick()
    {
        viewerCount = tickViewerCount(level, this, ++ticksOpen, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), viewerCount);
        lastAnimationAngle = animationAngle;
        if (viewerCount > 0 && animationAngle == 0.0F) { playSound(SoundEvents.CHEST_OPEN); }
        if (viewerCount == 0 && animationAngle > 0.0F || viewerCount > 0 && animationAngle < 1.0F)
        {
            animationAngle = MathHelper.clamp(animationAngle + (viewerCount > 0 ? 0.1F : -0.1F), 0, 1);
            if (animationAngle < 0.5F && lastAnimationAngle >= 0.5F) { playSound(SoundEvents.CHEST_CLOSE); }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void playSound(final SoundEvent soundEvent)
    {
        final BlockState state = getBlockState();
        final TileEntityMerger.Type mergeType = ChestBlock.getBlockType(state);
        final Vector3d soundPos;
        if (mergeType == TileEntityMerger.Type.SINGLE) { soundPos = Vector3d.atCenterOf(worldPosition); }
        else if (mergeType == TileEntityMerger.Type.FIRST)
        {
            soundPos = Vector3d.atCenterOf(worldPosition).add(Vector3d.atLowerCornerOf(ChestBlock.getDirectionToAttached(state).getNormal()).scale(0.5D));
        }
        else { return; }
        level.playSound(null, soundPos.x(), soundPos.y(), soundPos.z(), soundEvent, SoundCategory.BLOCKS, 0.5F,
                        level.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public void startOpen(final PlayerEntity player)
    {
        if (player.isSpectator()) { return; }
        if (viewerCount < 0) { viewerCount = 0; }
        viewerCount++;
        onInvOpenOrClose();
    }

    @Override
    public void stopOpen(final PlayerEntity player)
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
            level.blockEvent(worldPosition, block, 1, viewerCount);
            level.updateNeighborsAt(worldPosition, block);
        }
    }
}