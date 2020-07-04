package ninjaphenix.expandedstorage.common.block;

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
import net.minecraftforge.fml.network.NetworkHooks;
import ninjaphenix.expandedstorage.Registries;
import ninjaphenix.expandedstorage.common.block.entity.AbstractChestTileEntity;
import ninjaphenix.expandedstorage.common.block.enums.CursedChestType;
import ninjaphenix.expandedstorage.common.inventory.DoubleSidedInventory;
import ninjaphenix.expandedstorage.common.inventory.ScrollableContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

public abstract class BaseChestBlock<T extends AbstractChestTileEntity> extends ContainerBlock
{
    private final Supplier<TileEntityType<? extends T>> tileEntityType;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<CursedChestType> TYPE = EnumProperty.create("type", CursedChestType.class);
    private final TileEntityMerger.ICallback<T, Optional<ISidedInventory>> INVENTORY_GETTER = new TileEntityMerger.ICallback<T, Optional<ISidedInventory>>()
    {
        @NotNull @Override
        public Optional<ISidedInventory> func_225539_a_(@NotNull final T first, @NotNull final T second)
        { return Optional.of(new DoubleSidedInventory(first, second)); }

        @NotNull @Override
        public Optional<ISidedInventory> func_225538_a_(@NotNull final T single)
        {
            return Optional.of(single);
        }

        @NotNull @Override
        public Optional<ISidedInventory> func_225537_b_() { return Optional.empty(); }
    };
    private final TileEntityMerger.ICallback<T, Optional<INamedContainerProvider>> CONTAINER_GETTER = new TileEntityMerger.ICallback<T, Optional<INamedContainerProvider>>()
    {
        @NotNull @Override
        public Optional<INamedContainerProvider> func_225539_a_(@NotNull final T first, @NotNull final T second)
        {
            return Optional.of(new INamedContainerProvider()
            {
                @NotNull @Override
                public ITextComponent getDisplayName()
                {
                    if (first.hasCustomName()) { return first.getDisplayName(); }
                    else if (second.hasCustomName()) { return second.getDisplayName(); }
                    return new TranslationTextComponent("container.expandedstorage.generic_double", first.getDisplayName());
                }

                @Nullable @Override
                public Container createMenu(final int windowId, @NotNull final PlayerInventory playerInventory, @NotNull final PlayerEntity player)
                {
                    if (first.canOpen(player) && second.canOpen(player))
                    {
                        first.fillWithLoot(player);
                        second.fillWithLoot(player);
                        final DoubleSidedInventory inventory = new DoubleSidedInventory(first, second);
                        return new ScrollableContainer(windowId, playerInventory, inventory); // todo: replace with new container code
                    }
                    return null;
                }
            });
        }

        @NotNull @Override
        public Optional<INamedContainerProvider> func_225538_a_(@NotNull final T single) { return Optional.of(single); }

        @NotNull @Override
        public Optional<INamedContainerProvider> func_225537_b_() { return Optional.empty(); }
    };

    protected BaseChestBlock(Properties builder, Supplier<TileEntityType<? extends T>> tileEntityType)
    {
        super(builder);
        this.tileEntityType = tileEntityType;
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH).with(TYPE, CursedChestType.SINGLE));
    }

    @Override
    protected void fillStateContainer(@NotNull final StateContainer.Builder<Block, BlockState> builder)
    {
        super.fillStateContainer(builder);
        builder.add(FACING, TYPE);
    }

    @NotNull
    public final TileEntityMerger.ICallbackWrapper<? extends T>
    combine(@NotNull final BlockState state, @NotNull final World world, @NotNull final BlockPos pos, final boolean alwaysOpen)
    {
        final BiPredicate<IWorld, BlockPos> isChestBlocked = alwaysOpen ? (_world, _pos) -> false : ChestBlock::isBlocked;
        return TileEntityMerger.func_226924_a_(tileEntityType.get(), BaseChestBlock::getMergeType,
                BaseChestBlock::getDirectionToAttached, FACING, state, world, pos, isChestBlocked);
    }

    @NotNull
    public static Direction getDirectionToAttached(@NotNull final BlockState state)
    {
        final CursedChestType type = state.get(TYPE);
        if (type == CursedChestType.BOTTOM) { return Direction.UP; }
        else if (type == CursedChestType.TOP) { return Direction.DOWN; }
        else if (type == CursedChestType.LEFT) { return state.get(FACING).rotateYCCW(); }
        else if (type == CursedChestType.RIGHT) { return state.get(FACING).rotateY(); }
        else if (type == CursedChestType.FRONT) { return state.get(FACING).getOpposite(); }
        else /* type == CursedChestType.BACK*/ { return state.get(FACING); }
    }

    @NotNull
    public static TileEntityMerger.Type getMergeType(@NotNull final BlockState state)
    {
        switch (state.get(TYPE))
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

    @Nullable @Override
    public final INamedContainerProvider getContainer(@NotNull final BlockState state, @NotNull final World world, @NotNull final BlockPos pos)
    {
        Optional<INamedContainerProvider> containerProvider = combine(state, world, pos, false).apply(CONTAINER_GETTER);
        return containerProvider.orElse(null);
    }

    @NotNull @Override @SuppressWarnings("deprecation")
    public final ActionResultType onBlockActivated(@NotNull final BlockState state, @NotNull final World world,
            @NotNull final BlockPos pos, @NotNull final PlayerEntity player, @NotNull final Hand handIn, @NotNull final BlockRayTraceResult hit)
    {
        if (!world.isRemote)
        {
            final INamedContainerProvider containerProvider = state.getContainer(world, pos);
            if (containerProvider != null)
            {
                final int invSize = combine(state, world, pos, true).apply(INVENTORY_GETTER).map(IInventory::getSizeInventory).orElse(0);
                NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, data -> data.writeInt(invSize));
                player.addStat(this.getOpenStat());
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public final void onBlockPlacedBy(@NotNull final World world, @NotNull final BlockPos pos,
            @NotNull final BlockState state, @Nullable final LivingEntity placer, @NotNull final ItemStack stack)
    {
        if (stack.hasDisplayName())
        {
            final TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof AbstractChestTileEntity) { ((AbstractChestTileEntity) tileEntity).setCustomName(stack.getDisplayName()); }
        }
    }

    @Override @SuppressWarnings("deprecation")
    public void onReplaced(@NotNull final BlockState state, @NotNull final World world,
            @NotNull final BlockPos pos, @NotNull final BlockState newState, final boolean isMoving)
    {
        if (state.getBlock() != newState.getBlock())
        {
            final TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof IInventory)
            {
                InventoryHelper.dropInventoryItems(world, pos, (IInventory) tileEntity);
                world.notifyNeighbors(pos, this);
            }
            super.onReplaced(state, world, pos, newState, isMoving);
        }
    }

    public static CursedChestType getChestType(final Direction facing, final Direction offset)
    {
        if (facing.rotateY() == offset) { return CursedChestType.RIGHT; }
        else if (facing.rotateYCCW() == offset) { return CursedChestType.LEFT; }
        else if (facing == offset) { return CursedChestType.BACK; }
        else if (facing == offset.getOpposite()) { return CursedChestType.FRONT; }
        else if (offset == Direction.DOWN) { return CursedChestType.TOP; }
        else if (offset == Direction.UP) { return CursedChestType.BOTTOM; }
        return CursedChestType.SINGLE;
    }

    // todo: look at and see if it can be updated.
    @NotNull @Override
    public BlockState getStateForPlacement(@NotNull final BlockItemUseContext context)
    {
        final World world = context.getWorld();
        final BlockPos pos = context.getPos();
        CursedChestType chestType = CursedChestType.SINGLE;
        final Direction direction_1 = context.getPlacementHorizontalFacing().getOpposite();
        final Direction direction_2 = context.getFace();
        boolean shouldCancelInteraction = context.func_225518_g_(); // Is sneaking
        if (shouldCancelInteraction)
        {
            BlockState state;
            Direction direction_3;
            if (direction_2.getAxis() == Direction.Axis.Y)
            {
                state = world.getBlockState(pos.offset(direction_2.getOpposite()));
                direction_3 = state.getBlock() == this && state.get(TYPE) == CursedChestType.SINGLE ? state.get(FACING) : null;
                if (direction_3 != null && direction_3.getAxis() != direction_2.getAxis() && direction_3 == direction_1)
                { chestType = direction_2 == Direction.UP ? CursedChestType.TOP : CursedChestType.BOTTOM; }
            }
            else
            {
                Direction offsetDir = direction_2.getOpposite();
                final BlockState clickedBlock = world.getBlockState(pos.offset(offsetDir));
                if (clickedBlock.getBlock() == this)
                {
                    if (clickedBlock.get(TYPE) == CursedChestType.SINGLE)
                    {
                        if (clickedBlock.get(FACING) == direction_2 && clickedBlock.get(FACING) == direction_1)
                        {
                            chestType = CursedChestType.FRONT;
                        }
                        else
                        {
                            state = world.getBlockState(pos.offset(direction_2.getOpposite()));
                            if (state.get(FACING).getHorizontalIndex() < 2) { offsetDir = offsetDir.getOpposite(); }
                            if (direction_1 == state.get(FACING))
                            {
                                if (offsetDir == Direction.WEST || offsetDir == Direction.NORTH) { chestType = CursedChestType.LEFT; }
                                else { chestType = CursedChestType.RIGHT; }
                            }
                        }
                    }
                }
            }
        }
        else
        {
            for (Direction dir : Direction.values())
            {
                final BlockState state = world.getBlockState(pos.offset(dir));
                if (state.getBlock() != this || state.get(TYPE) != CursedChestType.SINGLE || state.get(FACING) != direction_1) { continue; }
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

    @NotNull @Override @SuppressWarnings("deprecation")
    public BlockState updatePostPlacement(@NotNull final BlockState state, @NotNull final Direction offset,
            @NotNull final BlockState offsetState, @NotNull final IWorld world, @NotNull final BlockPos pos, @NotNull final BlockPos offsetPos)
    {
        final TileEntityMerger.Type mergeType = getMergeType(state);
        if (mergeType == TileEntityMerger.Type.SINGLE)
        {
            final Direction facing = state.get(FACING);
            if (!offsetState.has(TYPE)) { return state.with(TYPE, CursedChestType.SINGLE); }
            final CursedChestType newType = getChestType(facing, offset);
            if (offsetState.get(TYPE) == newType.getOpposite() && facing == offsetState.get(FACING)) { return state.with(TYPE, newType); }
        }
        else if (world.getBlockState(pos.offset(getDirectionToAttached(state))).getBlock() != this)
        {
            return state.with(TYPE, CursedChestType.SINGLE);
        }
        return super.updatePostPlacement(state, offset, offsetState, world, pos, offsetPos);
    }

    @Override @SuppressWarnings("deprecation")
    public int getComparatorInputOverride(@NotNull final BlockState state, @NotNull final World world, @NotNull final BlockPos pos)
    { return combine(state, world, pos, true).apply(INVENTORY_GETTER).map(Container::calcRedstoneFromInventory).orElse(0); }

    @NotNull
    private Stat<ResourceLocation> getOpenStat() { return Stats.CUSTOM.get(Stats.OPEN_CHEST); }

    @Nullable @Override
    public final TileEntity createNewTileEntity(@Nullable final IBlockReader world) { return null; }

    @Override
    public final boolean hasTileEntity(@Nullable final BlockState state) { return true; }

    @NotNull @Override
    public abstract TileEntity createTileEntity(@Nullable final BlockState state, @Nullable final IBlockReader world);

    @NotNull @Override @SuppressWarnings("deprecation")
    public final BlockState mirror(@NotNull final BlockState state, @NotNull final Mirror mirror)
    { return state.rotate(mirror.toRotation(state.get(FACING))); }

    @NotNull @Override @SuppressWarnings("deprecation")
    public final BlockState rotate(@NotNull final BlockState state, @NotNull final Rotation rotation)
    { return state.with(FACING, rotation.rotate(state.get(FACING))); }

    @Override @SuppressWarnings("deprecation")
    public final boolean hasComparatorInputOverride(@NotNull final BlockState state) { return true; }

    // todo: refactor out, replace with either client only registry thing or just a getClientExtraData method
    @NotNull
    public abstract <R extends Registries.TierData> SimpleRegistry<R> getDataRegistry();
}
