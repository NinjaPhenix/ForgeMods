package ninjaphenix.expandedstorage.common.block;

import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import ninjaphenix.expandedstorage.Registries;
import ninjaphenix.expandedstorage.common.block.entity.BaseChestTileEntity;
import ninjaphenix.expandedstorage.common.block.enums.CursedChestType;
import ninjaphenix.expandedstorage.common.inventory.DoubleSidedInventory;
import ninjaphenix.expandedstorage.common.inventory.IDataNamedContainerProvider;
import ninjaphenix.expandedstorage.common.network.Networker;
import org.jetbrains.annotations.Nullable;

public abstract class BaseChestBlock<T extends BaseChestTileEntity> extends ContainerBlock
{
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<CursedChestType> TYPE = EnumProperty.create("type", CursedChestType.class);
    private final Supplier<TileEntityType<? extends T>> tileEntityType;
    private final TileEntityMerger.ICallback<T, Optional<ISidedInventory>> INVENTORY_GETTER = new TileEntityMerger.ICallback<T, Optional<ISidedInventory>>()
    {
        @Override
        public Optional<ISidedInventory> func_225539_a_(final T first, final T second)
        {
            return Optional.of(new DoubleSidedInventory(first, second));
        }

        @Override
        public Optional<ISidedInventory> func_225538_a_(final T single)
        {
            return Optional.of(single);
        }

        @Override
        public Optional<ISidedInventory> func_225537_b_()
        {
            return Optional.empty();
        }
    };
    private final TileEntityMerger.ICallback<T, Optional<IDataNamedContainerProvider>> CONTAINER_GETTER = new TileEntityMerger.ICallback<T, Optional<IDataNamedContainerProvider>>()
    {
        @Override
        public Optional<IDataNamedContainerProvider> func_225539_a_(final T first, final T second)
        {
            return Optional.of(new IDataNamedContainerProvider()
            {
                private final DoubleSidedInventory inventory = new DoubleSidedInventory(first, second);

                @Override
                public void writeExtraData(final PacketBuffer buffer)
                {
                    buffer.writeBlockPos(first.getPos()).writeInt(inventory.getSizeInventory());
                }

                @Override
                public ITextComponent getDisplayName()
                {
                    if (first.hasCustomName())
                    {
                        return first.getDisplayName();
                    }
                    else if (second.hasCustomName())
                    {
                        return second.getDisplayName();
                    }
                    return new TranslationTextComponent("container.expandedstorage.generic_double", first.getDisplayName());
                }

                @Override
                public @Nullable Container createMenu(final int windowId, final PlayerInventory playerInventory, final PlayerEntity player)
                {
                    if (first.canOpen(player) && second.canOpen(player))
                    {
                        first.fillWithLoot(player);
                        second.fillWithLoot(player);
                        return Networker.INSTANCE.getContainer(windowId, first.getPos(), inventory, player, getDisplayName());
                    }
                    return null;
                }
            });
        }

        @Override
        public Optional<IDataNamedContainerProvider> func_225538_a_(final T single)
        {
            return Optional.of(new IDataNamedContainerProvider()
            {
                @Override
                public void writeExtraData(final PacketBuffer buffer)
                {
                    buffer.writeBlockPos(single.getPos()).writeInt(single.getSizeInventory());
                }

                @Override
                public ITextComponent getDisplayName()
                {
                    return single.getDisplayName();
                }

                @Override
                public @Nullable Container createMenu(final int windowId, final PlayerInventory playerInventory, final PlayerEntity player)
                {
                    if (single.canOpen(player))
                    {
                        single.fillWithLoot(player);
                        return Networker.INSTANCE.getContainer(windowId, single.getPos(), single, player, getDisplayName());
                    }
                    return null;
                }
            });
        }

        @Override
        public Optional<IDataNamedContainerProvider> func_225537_b_()
        {
            return Optional.empty();
        }
    };

    protected BaseChestBlock(final Properties builder, final Supplier<TileEntityType<? extends T>> tileEntityType)
    {
        super(builder);
        this.tileEntityType = tileEntityType;
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH).with(TYPE, CursedChestType.SINGLE));
    }

    public static Direction getDirectionToAttached(final BlockState state)
    {
        final CursedChestType type = state.get(TYPE);
        if (type == CursedChestType.BOTTOM)
        {
            return Direction.UP;
        }
        else if (type == CursedChestType.TOP)
        {
            return Direction.DOWN;
        }
        else if (type == CursedChestType.LEFT)
        {
            return state.get(FACING).rotateYCCW();
        }
        else if (type == CursedChestType.RIGHT)
        {
            return state.get(FACING).rotateY();
        }
        else if (type == CursedChestType.FRONT)
        {
            return state.get(FACING).getOpposite();
        }
        else /* type == CursedChestType.BACK*/
        {
            return state.get(FACING);
        }
    }

    public static TileEntityMerger.Type getMergeType(final BlockState state)
    {
        switch (state.get(TYPE))
        {
            case TOP:
            case LEFT:
            case FRONT:
                return TileEntityMerger.Type.FIRST;
            case BACK:
            case RIGHT:
            case BOTTOM:
                return TileEntityMerger.Type.SECOND;
            default:
                return TileEntityMerger.Type.SINGLE;
        }
    }

    public static CursedChestType getChestType(final Direction facing, final Direction offset)
    {
        if (facing.rotateY() == offset)
        {
            return CursedChestType.RIGHT;
        }
        else if (facing.rotateYCCW() == offset)
        {
            return CursedChestType.LEFT;
        }
        else if (facing == offset)
        {
            return CursedChestType.BACK;
        }
        else if (facing == offset.getOpposite())
        {
            return CursedChestType.FRONT;
        }
        else if (offset == Direction.DOWN)
        {
            return CursedChestType.TOP;
        }
        else if (offset == Direction.UP)
        {
            return CursedChestType.BOTTOM;
        }
        return CursedChestType.SINGLE;
    }

    @Override
    protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder)
    {
        super.fillStateContainer(builder);
        builder.add(FACING, TYPE);
    }

    public final TileEntityMerger.ICallbackWrapper<? extends T> combine(final BlockState state, final World world, final BlockPos pos, final boolean alwaysOpen)
    {
        final BiPredicate<IWorld, BlockPos> isChestBlocked = alwaysOpen ? (_world, _pos) -> false : this::isBlocked;
        return TileEntityMerger.func_226924_a_(tileEntityType.get(), BaseChestBlock::getMergeType, BaseChestBlock::getDirectionToAttached, FACING, state, world, pos, isChestBlocked);
    }

    protected boolean isBlocked(final IWorld world, final BlockPos pos)
    {
        return ChestBlock.isBlocked(world, pos);
    }

    @Override
    public final @Nullable INamedContainerProvider getContainer(final BlockState state, final World world, final BlockPos pos)
    {
        return null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public final ActionResultType onBlockActivated(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand handIn, final BlockRayTraceResult hit)
    {
        if (!world.isRemote)
        {
            final Optional<IDataNamedContainerProvider> containerProvider = combine(state, world, pos, false).apply(CONTAINER_GETTER);
            containerProvider.ifPresent(provider ->
            {
                Networker.INSTANCE.openContainer((ServerPlayerEntity) player, provider);
                player.addStat(getOpenStat());
            });
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public final void onBlockPlacedBy(final World world, final BlockPos pos, final BlockState state, final @Nullable LivingEntity placer, final ItemStack stack)
    {
        if (stack.hasDisplayName())
        {
            final TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof BaseChestTileEntity)
            {
                ((BaseChestTileEntity) tileEntity).setCustomName(stack.getDisplayName());
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onReplaced(final BlockState state, final World world, final BlockPos pos, final BlockState newState, final boolean isMoving)
    {
        if (state.getBlock() != newState.getBlock())
        {
            final TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof IInventory)
            {
                InventoryHelper.dropInventoryItems(world, pos, (IInventory) tileEntity);
                world.notifyNeighborsOfStateChange(pos, this);
            }
            super.onReplaced(state, world, pos, newState, isMoving);
        }
    }

    // todo: look at and see if it can be updated, specifically want to remove "BlockState state;", "Direction direction_3;" if possible
    // todo: add config to prevent automatic merging of chests.
    @Override
    public BlockState getStateForPlacement(final BlockItemUseContext context)
    {
        final World world = context.getWorld();
        final BlockPos pos = context.getPos();
        CursedChestType chestType = CursedChestType.SINGLE;
        final Direction direction_1 = context.getPlacementHorizontalFacing().getOpposite();
        final Direction direction_2 = context.getFace();
        final boolean shouldCancelInteraction = context.func_225518_g_(); // Is sneaking
        if (shouldCancelInteraction)
        {
            final BlockState state;
            final Direction direction_3;
            if (direction_2.getAxis().isVertical())
            {
                state = world.getBlockState(pos.offset(direction_2.getOpposite()));
                direction_3 = state.getBlock() == this && state.get(TYPE) == CursedChestType.SINGLE ? state.get(FACING) : null;
                if (direction_3 != null && direction_3.getAxis() != direction_2.getAxis() && direction_3 == direction_1)
                {
                    chestType = direction_2 == Direction.UP ? CursedChestType.TOP : CursedChestType.BOTTOM;
                }
            }
            else
            {
                Direction offsetDir = direction_2.getOpposite();
                final BlockState clickedBlock = world.getBlockState(pos.offset(offsetDir));
                if (clickedBlock.getBlock() == this && clickedBlock.get(TYPE) == CursedChestType.SINGLE)
                {
                    if (clickedBlock.get(FACING) == direction_2 && clickedBlock.get(FACING) == direction_1)
                    {
                        chestType = CursedChestType.FRONT;
                    }
                    else
                    {
                        state = world.getBlockState(pos.offset(direction_2.getOpposite()));
                        if (state.get(FACING).getHorizontalIndex() < 2)
                        {
                            offsetDir = offsetDir.getOpposite();
                        }
                        if (direction_1 == state.get(FACING))
                        {
                            chestType = (offsetDir == Direction.WEST || offsetDir == Direction.NORTH) ? CursedChestType.LEFT : CursedChestType.RIGHT;
                        }
                    }
                }
            }
        }
        else
        {
            for (final Direction dir : Direction.values())
            {
                final BlockState state = world.getBlockState(pos.offset(dir));
                if (state.getBlock() != this || state.get(TYPE) != CursedChestType.SINGLE || state.get(FACING) != direction_1)
                {
                    continue;
                }
                final CursedChestType type = getChestType(direction_1, dir);
                if (type != CursedChestType.SINGLE)
                {
                    chestType = type;
                    break;
                }
            }
        }
        return getDefaultState().with(FACING, direction_1).with(TYPE, chestType);
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState updatePostPlacement(final BlockState state, final Direction offset, final BlockState offsetState, final IWorld world, final BlockPos pos, final BlockPos offsetPos)
    {
        final TileEntityMerger.Type mergeType = getMergeType(state);
        if (mergeType == TileEntityMerger.Type.SINGLE)
        {
            final Direction facing = state.get(FACING);
            if (!offsetState.hasProperty(TYPE))
            {
                return state.with(TYPE, CursedChestType.SINGLE);
            }
            final CursedChestType newType = getChestType(facing, offset);
            if (offsetState.get(TYPE) == newType.getOpposite() && facing == offsetState.get(FACING))
            {
                return state.with(TYPE, newType);
            }
        }
        else if (world.getBlockState(pos.offset(getDirectionToAttached(state))).getBlock() != this)
        {
            return state.with(TYPE, CursedChestType.SINGLE);
        }
        return super.updatePostPlacement(state, offset, offsetState, world, pos, offsetPos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getComparatorInputOverride(final BlockState state, final World world, final BlockPos pos)
    {
        return combine(state, world, pos, true).apply(INVENTORY_GETTER).map(Container::calcRedstoneFromInventory).orElse(0);
    }

    private Stat<ResourceLocation> getOpenStat()
    {
        return Stats.CUSTOM.get(Stats.OPEN_CHEST);
    }

    @Override
    public final @Nullable TileEntity createNewTileEntity(final @Nullable IBlockReader world)
    {
        return null;
    }

    @Override
    public final boolean hasTileEntity(final @Nullable BlockState state)
    {
        return true;
    }

    @Override
    public abstract TileEntity createTileEntity(final @Nullable BlockState state, final @Nullable IBlockReader world);

    @Override
    @SuppressWarnings("deprecation")
    public final BlockState mirror(final BlockState state, final Mirror mirror)
    {
        return state.rotate(mirror.toRotation(state.get(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public final BlockState rotate(final BlockState state, final Rotation rotation)
    {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public final boolean hasComparatorInputOverride(final BlockState state)
    {
        return true;
    }

    // todo: refactor to a "getExtraData" method, how to implement BlockEntity#fromTag in that case, not issue in 1.16 so perhaps just write a hack.
    public abstract <R extends Registries.TierData> SimpleRegistry<R> getDataRegistry();
}