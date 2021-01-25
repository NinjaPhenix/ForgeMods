package ninjaphenix.expandedstorage.common.block;

import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.piglin.PiglinTasks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ISidedInventoryProvider;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import ninjaphenix.expandedstorage.Registries;
import ninjaphenix.expandedstorage.common.block.entity.StorageBlockEntity;
import ninjaphenix.expandedstorage.common.inventory.IDataNamedContainerProvider;
import ninjaphenix.expandedstorage.common.network.Networker;
import javax.annotation.Nullable;

public abstract class StorageBlock extends Block implements ISidedInventoryProvider
{
    protected StorageBlock(final AbstractBlock.Properties properties)
    {
        super(properties);
    }

    protected abstract ResourceLocation getOpenStat();

    public abstract <R extends Registries.TierData> MutableRegistry<R> getDataRegistry();

    @Nullable
    protected IDataNamedContainerProvider createContainerFactory(final BlockState state, final IWorld world, final BlockPos pos)
    {
        final TileEntity entity = world.getBlockEntity(pos);
        if (!(entity instanceof StorageBlockEntity)) { return null; }
        final StorageBlockEntity container = (StorageBlockEntity) entity;
        return new IDataNamedContainerProvider()
        {
            @Nullable
            @Override
            public Container createMenu(final int syncId, final PlayerInventory inventory, final PlayerEntity player)
            {
                if (container.stillValid(player))
                {
                    container.unpackLootTable(player);
                    Networker.INSTANCE.getContainer(syncId, container.getBlockPos(), container, player, getDisplayName());
                }
                return null;
            }

            @Override
            public ITextComponent getDisplayName()
            {
                return container.getDisplayName();
            }

            @Override
            public void writeExtraData(final PacketBuffer buffer)
            {
                buffer.writeBlockPos(pos).writeInt(container.getContainerSize());
            }
        };
    }

    @Override
    @SuppressWarnings("deprecation")
    public final boolean hasAnalogOutputSignal(final BlockState state) { return true; }

    @Override
    public BlockRenderType getRenderShape(final BlockState state) { return BlockRenderType.MODEL; }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(final BlockState state, final World world, final BlockPos pos, final BlockState newState,
                         final boolean moved)
    {
        if (state.getBlock() != newState.getBlock())
        {
            final TileEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof LockableLootTileEntity)
            {
                InventoryHelper.dropContents(world, pos, (LockableLootTileEntity) blockEntity);
                world.updateNeighborsAt(pos, this);
            }
            super.onRemove(state, world, pos, newState, moved);
        }
    }

    @Override
    public void setPlacedBy(final World world, final BlockPos pos, final BlockState state, @Nullable final LivingEntity placer,
                            final ItemStack stack)
    {
        if (stack.hasCustomHoverName())
        {
            final TileEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof StorageBlockEntity)
            {
                ((StorageBlockEntity) blockEntity).setCustomName(stack.getHoverName());
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getAnalogOutputSignal(final BlockState state, final World world, final BlockPos pos)
    {
        return Container.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType use(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand,
                                 final BlockRayTraceResult hit)
    {
        if (!world.isClientSide)
        {
            final IDataNamedContainerProvider factory = createContainerFactory(state, world, pos);
            if (factory != null && player instanceof ServerPlayerEntity)
            {
                Networker.INSTANCE.openContainer((ServerPlayerEntity) player, factory);
                player.awardStat(getOpenStat());
                PiglinTasks.angerNearbyPiglins(player, true);
            }
            return ActionResultType.CONSUME;
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public ISidedInventory getContainer(final BlockState state, final IWorld world, final BlockPos pos)
    {
        final TileEntity entity = world.getBlockEntity(pos);
        if (entity instanceof StorageBlockEntity) { return (StorageBlockEntity) entity; }
        //noinspection ConstantConditions
        return null;
    }

    @Override
    public boolean hasTileEntity(final BlockState state) { return true; }

    @Nullable
    @Override
    public abstract TileEntity createTileEntity(final BlockState state, final IBlockReader world);
}
