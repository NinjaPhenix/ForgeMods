package ninjaphenix.expandedstorage.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import ninjaphenix.expandedstorage.common.block.entity.StorageBlockEntity;
import ninjaphenix.expandedstorage.common.block.enums.CursedChestType;
import ninjaphenix.expandedstorage.common.inventory.DoubleSidedInventory;
import ninjaphenix.expandedstorage.common.inventory.IDataNamedContainerProvider;
import ninjaphenix.expandedstorage.common.network.Networker;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

import static net.minecraft.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public abstract class ChestBlock<T extends StorageBlockEntity> extends StorageBlock
{
    public static final EnumProperty<CursedChestType> TYPE = EnumProperty.create("type", CursedChestType.class);
    private final Supplier<TileEntityType<T>> blockEntityType;
    private final TileEntityMerger.ICallback<T, Optional<ISidedInventory>> INVENTORY_GETTER =
            new TileEntityMerger.ICallback<T, Optional<ISidedInventory>>()
            {
                @Override
                public Optional<ISidedInventory> acceptDouble(final T first, final T second)
                {
                    return Optional.of(new DoubleSidedInventory(first, second));
                }

                @Override
                public Optional<ISidedInventory> acceptSingle(final T single) { return Optional.of(single); }

                @Override
                public Optional<ISidedInventory> acceptNone() { return Optional.empty(); }
            };
    private final TileEntityMerger.ICallback<T, Optional<IDataNamedContainerProvider>> CONTAINER_GETTER =
            new TileEntityMerger.ICallback<T, Optional<IDataNamedContainerProvider>>()
            {
                @Override
                public Optional<IDataNamedContainerProvider> acceptDouble(final T first, final T second)
                {
                    return Optional.of(new IDataNamedContainerProvider()
                    {
                        private final DoubleSidedInventory inventory = new DoubleSidedInventory(first, second);

                        @Override
                        public void writeExtraData(final PacketBuffer buffer)
                        {
                            buffer.writeBlockPos(first.getBlockPos()).writeInt(inventory.getContainerSize());
                        }

                        @Override
                        public ITextComponent getDisplayName()
                        {
                            if (first.hasCustomName()) { return first.getDisplayName(); }
                            else if (second.hasCustomName()) { return second.getDisplayName(); }
                            return new TranslationTextComponent("container.expandedstorage.generic_double", first.getDisplayName());
                        }

                        @Nullable
                        @Override
                        public Container createMenu(final int syncId, final PlayerInventory playerInventory, final PlayerEntity player)
                        {
                            if (first.stillValid(player) && second.stillValid(player))
                            {
                                first.unpackLootTable(player);
                                second.unpackLootTable(player);
                                return Networker.INSTANCE.getContainer(syncId, first.getBlockPos(), inventory, player, getDisplayName());
                            }
                            return null;
                        }
                    });
                }

                @Override
                public Optional<IDataNamedContainerProvider> acceptSingle(final T single)
                {
                    return Optional.of(new IDataNamedContainerProvider()
                    {
                        @Override
                        public void writeExtraData(final PacketBuffer buffer)
                        {
                            buffer.writeBlockPos(single.getBlockPos()).writeInt(single.getContainerSize());
                        }

                        @Override
                        public ITextComponent getDisplayName() { return single.getDisplayName(); }

                        @Nullable
                        @Override
                        public Container createMenu(final int syncId, final PlayerInventory playerInventory, final PlayerEntity player)
                        {
                            if (single.stillValid(player))
                            {
                                single.unpackLootTable(player);
                                return Networker.INSTANCE.getContainer(syncId, single.getBlockPos(), single, player, getDisplayName());
                            }
                            return null;
                        }
                    });
                }

                @Override
                public Optional<IDataNamedContainerProvider> acceptNone() { return Optional.empty(); }
            };

    protected ChestBlock(final Properties builder, final Supplier<TileEntityType<T>> blockEntityType)
    {
        super(builder);
        this.blockEntityType = blockEntityType;
        registerDefaultState(defaultBlockState().setValue(HORIZONTAL_FACING, Direction.NORTH).setValue(TYPE, CursedChestType.SINGLE));
    }

    public static Direction getDirectionToAttached(final BlockState state)
    {
        switch (state.getValue(TYPE))
        {
            case TOP: return Direction.DOWN;
            case BACK: return state.getValue(HORIZONTAL_FACING);
            case RIGHT: return state.getValue(HORIZONTAL_FACING).getClockWise();
            case BOTTOM: return Direction.UP;
            case FRONT: return state.getValue(HORIZONTAL_FACING).getOpposite();
            case LEFT: return state.getValue(HORIZONTAL_FACING).getCounterClockWise();
            default: throw new IllegalArgumentException("BaseChestBlock#getDirectionToAttached received an unexpected state.");
        }
    }

    public static TileEntityMerger.Type getBlockType(final BlockState state)
    {
        switch (state.getValue(TYPE))
        {
            case TOP:
            case LEFT:
            case FRONT: return TileEntityMerger.Type.FIRST;
            case BACK:
            case RIGHT:
            case BOTTOM: return TileEntityMerger.Type.SECOND;
            default: return TileEntityMerger.Type.SINGLE;
        }
    }

    public static CursedChestType getChestType(final Direction facing, final Direction offset)
    {
        if (facing.getClockWise() == offset) { return CursedChestType.RIGHT; }
        else if (facing.getCounterClockWise() == offset) { return CursedChestType.LEFT; }
        else if (facing == offset) { return CursedChestType.BACK; }
        else if (facing == offset.getOpposite()) { return CursedChestType.FRONT; }
        else if (offset == Direction.DOWN) { return CursedChestType.TOP; }
        else if (offset == Direction.UP) { return CursedChestType.BOTTOM; }
        return CursedChestType.SINGLE;
    }

    @Override
    protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(HORIZONTAL_FACING, TYPE);
    }

    public final TileEntityMerger.ICallbackWrapper<? extends T> combine(final BlockState state, final IWorld world, final BlockPos pos,
                                                            final boolean alwaysOpen)
    {
        final BiPredicate<IWorld, BlockPos> isChestBlocked = alwaysOpen ? (_world, _pos) -> false : this::isBlocked;
        return TileEntityMerger.combineWithNeigbour(blockEntityType.get(), ChestBlock::getBlockType,
                                                       ChestBlock::getDirectionToAttached, HORIZONTAL_FACING, state, world, pos,
                                                       isChestBlocked);
    }

    protected boolean isBlocked(final IWorld world, final BlockPos pos) { return net.minecraft.block.ChestBlock.isChestBlockedAt(world, pos); }

    // todo: look at and see if it can be updated, specifically want to remove "BlockState state;", "Direction direction_3;" if possible
    // todo: add config to prevent automatic merging of chests.
    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockItemUseContext context)
    {
        final World world = context.getLevel();
        final BlockPos pos = context.getClickedPos();
        CursedChestType chestType = CursedChestType.SINGLE;
        final Direction direction_1 = context.getHorizontalDirection().getOpposite();
        final Direction direction_2 = context.getClickedFace();
        if (context.isSecondaryUseActive())
        {
            final BlockState state;
            final Direction direction_3;
            if (direction_2.getAxis().isVertical())
            {
                state = world.getBlockState(pos.relative(direction_2.getOpposite()));
                direction_3 = state.getBlock() == this && state.getValue(TYPE) == CursedChestType.SINGLE ? state.getValue(HORIZONTAL_FACING) : null;
                if (direction_3 != null && direction_3.getAxis() != direction_2.getAxis() && direction_3 == direction_1)
                {
                    chestType = direction_2 == Direction.UP ? CursedChestType.TOP : CursedChestType.BOTTOM;
                }
            }
            else
            {
                Direction offsetDir = direction_2.getOpposite();
                final BlockState clickedBlock = world.getBlockState(pos.relative(offsetDir));
                if (clickedBlock.getBlock() == this && clickedBlock.getValue(TYPE) == CursedChestType.SINGLE)
                {
                    if (clickedBlock.getValue(HORIZONTAL_FACING) == direction_2 && clickedBlock.getValue(HORIZONTAL_FACING) == direction_1)
                    {
                        chestType = CursedChestType.FRONT;
                    }
                    else
                    {
                        state = world.getBlockState(pos.relative(direction_2.getOpposite()));
                        if (state.getValue(HORIZONTAL_FACING).get2DDataValue() < 2) { offsetDir = offsetDir.getOpposite(); }
                        if (direction_1 == state.getValue(HORIZONTAL_FACING))
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
                final BlockState state = world.getBlockState(pos.relative(dir));
                if (state.getBlock() != this || state.getValue(TYPE) != CursedChestType.SINGLE || state.getValue(HORIZONTAL_FACING) != direction_1)
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
        return defaultBlockState().setValue(HORIZONTAL_FACING, direction_1).setValue(TYPE, chestType);
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(final BlockState state, final Direction offset, final BlockState offsetState,
                                  final IWorld world, final BlockPos pos, final BlockPos offsetPos)
    {
        final TileEntityMerger.Type mergeType = getBlockType(state);
        if (mergeType == TileEntityMerger.Type.SINGLE)
        {
            final Direction facing = state.getValue(HORIZONTAL_FACING);
            if (!offsetState.hasProperty(TYPE)) { return state.setValue(TYPE, CursedChestType.SINGLE); }
            final CursedChestType newType = getChestType(facing, offset);
            if (offsetState.getValue(TYPE) == newType.getOpposite() && facing == offsetState.getValue(HORIZONTAL_FACING))
            {
                return state.setValue(TYPE, newType);
            }
        }
        else if (world.getBlockState(pos.relative(getDirectionToAttached(state))).getBlock() != this)
        {
            return state.setValue(TYPE, CursedChestType.SINGLE);
        }
        return super.updateShape(state, offset, offsetState, world, pos, offsetPos);
    }

    @Override
    public int getAnalogOutputSignal(final BlockState state, final World world, final BlockPos pos)
    {
        return combine(state, world, pos, true).apply(INVENTORY_GETTER).map(Container::getRedstoneSignalFromContainer).orElse(0);
    }

    @Override
    protected ResourceLocation getOpenStat() { return Stats.OPEN_CHEST; }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState mirror(final BlockState state, final Mirror mirror)
    {
        return state.rotate(mirror.getRotation(state.getValue(HORIZONTAL_FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState rotate(final BlockState state, final Rotation rotation)
    {
        return state.setValue(HORIZONTAL_FACING, rotation.rotate(state.getValue(HORIZONTAL_FACING)));
    }

    @Override // keep for hoppers.
    public ISidedInventory getContainer(final BlockState state, final IWorld world, final BlockPos pos)
    {
        return combine(state, world, pos, true).apply(INVENTORY_GETTER).orElse(null);
    }

    @Override
    protected IDataNamedContainerProvider createContainerFactory(final BlockState state, final IWorld world, final BlockPos pos)
    {
        return combine(state, world, pos, true).apply(CONTAINER_GETTER).orElse(null);
    }
}