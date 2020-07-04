package ninjaphenix.expandedstorage.common.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import ninjaphenix.expandedstorage.ModContent;
import ninjaphenix.expandedstorage.Registries;
import ninjaphenix.expandedstorage.common.block.BaseChestBlock;
import ninjaphenix.expandedstorage.common.block.CursedChestBlock;
import ninjaphenix.expandedstorage.common.block.enums.CursedChestType;
import ninjaphenix.expandedstorage.common.inventory.ScrollableContainer;
import ninjaphenix.expandedstorage.common.inventory.DoubleSidedInventory;

import java.util.List;

public class CursedChestTileEntity extends AbstractChestTileEntity implements IChestLid, ITickableTileEntity
{
	private float animationAngle;
	private float lastAnimationAngle;
	private int viewerCount;
	private int ticksOpen;

	public CursedChestTileEntity() { this(null); }

	public CursedChestTileEntity(final ResourceLocation block) { super(ModContent.CURSED_CHEST_TE, block); }

	private static int tickViewerCount(final World world, final CursedChestTileEntity instance, final int ticksOpen, final int x, final int y, final int z,
			final int viewCount)
	{
		if (!world.isRemote && viewCount != 0 && (ticksOpen + x + y + z) % 200 == 0)
		{
		    // todo: replace with stream?
            int viewers = 0;
            final List<PlayerEntity> playersInRange = world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(x - 5, y - 5, z - 5, x + 6, y + 6, z + 6));
            for (PlayerEntity player : playersInRange)
            {
                if (player.openContainer instanceof ScrollableContainer)
                {
                    final IInventory inventory = ((ScrollableContainer) player.openContainer).getInv();
                    if (inventory == instance || inventory instanceof DoubleSidedInventory && ((DoubleSidedInventory) inventory).isPart(instance)) { viewers++; }
                }
            }
            return viewers;
		}
		return viewCount;
	}

	@Override
	protected void initialize(final ResourceLocation block)
	{
		this.block = block;
		Registries.ModeledTierData data = Registries.MODELED.getValue(block).get();
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
	public float getLidAngle(float float_1) { return MathHelper.lerp(float_1, lastAnimationAngle, animationAngle); }

    @Override
	@SuppressWarnings("ConstantConditions")
	public void tick()
	{
		viewerCount = tickViewerCount(world, this, ++ticksOpen, pos.getX(), pos.getY(), pos.getZ(), viewerCount);
		lastAnimationAngle = animationAngle;
		if (viewerCount > 0 && animationAngle == 0.0F) { playSound(SoundEvents.BLOCK_CHEST_OPEN); }
		if (viewerCount == 0 && animationAngle > 0.0F || viewerCount > 0 && animationAngle < 1.0F)
		{
			float float_2 = animationAngle;
			if (viewerCount > 0) { animationAngle += 0.1F; }
			else { animationAngle -= 0.1F; }
			animationAngle = MathHelper.clamp(animationAngle, 0, 1);
			if (animationAngle < 0.5F && float_2 >= 0.5F) { playSound(SoundEvents.BLOCK_CHEST_CLOSE); }
		}
	}

    @SuppressWarnings("ConstantConditions")
	private void playSound(final SoundEvent soundEvent)
	{
        final BlockState state = getBlockState();
        if(BaseChestBlock.getMergeType(state) == TileEntityMerger.Type.SECOND) { return; }
		final Vec3i offset = BaseChestBlock.getDirectionToAttached(getBlockState()).getDirectionVec();
        Vec3d soundPos = new Vec3d(pos).add(0.5, 0.5, 0.5).add(offset.getX() * 0.5D, offset.getY() * 0.5D, offset.getZ() * 0.5D);
		world.playSound(null, soundPos.getX(), soundPos.getY(), soundPos.getZ(), soundEvent, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
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
		Block block = getBlockState().getBlock();
		if (block instanceof CursedChestBlock)
		{
			world.addBlockEvent(pos, block, 1, viewerCount);
			world.notifyNeighbors(pos, block);
		}
	}
}