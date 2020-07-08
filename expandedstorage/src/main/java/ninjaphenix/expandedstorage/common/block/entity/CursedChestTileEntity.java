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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import ninjaphenix.container_library.common.inventory.AbstractContainer;
import ninjaphenix.expandedstorage.ModContent;
import ninjaphenix.expandedstorage.Registries;
import ninjaphenix.expandedstorage.common.block.BaseChestBlock;
import ninjaphenix.expandedstorage.common.block.CursedChestBlock;
import ninjaphenix.expandedstorage.common.inventory.DoubleSidedInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CursedChestTileEntity extends AbstractChestTileEntity implements IChestLid, ITickableTileEntity
{
    private float animationAngle, lastAnimationAngle;
    private int viewerCount, ticksOpen;

    public CursedChestTileEntity() { this(null); }

    public CursedChestTileEntity(@Nullable final ResourceLocation block) { super(ModContent.CURSED_CHEST_TE, block); }

    private static int tickViewerCount(@NotNull final World world, @NotNull final CursedChestTileEntity instance, final int ticksOpen, final int x,
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

    @Override @SuppressWarnings("OptionalGetWithoutIsPresent")
    protected void initialize(@NotNull final ResourceLocation block)
    {
        this.block = block;
        final Registries.ModeledTierData data = Registries.MODELED.getValue(block).get();
        defaultContainerName = data.getContainerName();
        inventorySize = data.getSlotCount();
        inventory = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
        SLOTS = new int[inventorySize];
        for (int i = 0; i < inventorySize; i++) { SLOTS[i] = i; }
    }

    @Override
    public boolean receiveClientEvent(final int actionId, final int value)
    {
        if (actionId == 1) { viewerCount = value; return true; }
        else { return super.receiveClientEvent(actionId, value); }
    }

    @Override
    public float getLidAngle(float partialTicks) { return MathHelper.lerp(partialTicks, lastAnimationAngle, animationAngle); }

    @Override @SuppressWarnings("ConstantConditions")
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
    private void playSound(@NotNull final SoundEvent soundEvent)
    {
        final BlockState state = getBlockState();
        if (BaseChestBlock.getMergeType(state) == TileEntityMerger.Type.SECOND) { return; }
        final Vec3i offset = BaseChestBlock.getDirectionToAttached(getBlockState()).getDirectionVec();
        final Vec3d soundPos = new Vec3d(pos).add(0.5, 0.5, 0.5).add(offset.getX() * 0.5D, offset.getY() * 0.5D, offset.getZ() * 0.5D);
        world.playSound(null, soundPos.getX(), soundPos.getY(), soundPos.getZ(), soundEvent, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public void openInventory(@NotNull final PlayerEntity player)
    {
        if (player.isSpectator()) { return; }
        if (viewerCount < 0) { viewerCount = 0; } viewerCount++;
        onInvOpenOrClose();
    }

    @Override
    public void closeInventory(@NotNull final PlayerEntity player)
    {
        if (player.isSpectator()) { return; }
        viewerCount--;
        onInvOpenOrClose();
    }

    @SuppressWarnings("ConstantConditions")
    private void onInvOpenOrClose()
    {
        final Block block = getBlockState().getBlock();
        if (block instanceof CursedChestBlock) { world.addBlockEvent(pos, block, 1, viewerCount); world.notifyNeighbors(pos, block); }
    }
}