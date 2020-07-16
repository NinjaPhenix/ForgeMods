package ninjaphenix.expandedstorage.common.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ninjaphenix.expandedstorage.common.block.BaseChestBlock;
import ninjaphenix.expandedstorage.common.block.enums.CursedChestType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ChestModifierItem extends Item
{
    private static final DirectionProperty FACING = BaseChestBlock.FACING;
    private static final EnumProperty<CursedChestType> TYPE = BaseChestBlock.TYPE;

    public ChestModifierItem(@NotNull final Item.Properties properties) { super(properties); }

    @NotNull @Override
    public ActionResultType onItemUse(@NotNull final ItemUseContext context)
    {
        final World world = context.getWorld();
        final BlockPos pos = context.getPos();
        final BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BaseChestBlock)
        {
            ActionResultType result = ActionResultType.FAIL;
            final CursedChestType type = state.get(TYPE);
            final Direction facing = state.get(FACING);
            if (type == CursedChestType.SINGLE) { result = useModifierOnChestBlock(context, state, pos, null, null); }
            else if (type == CursedChestType.BOTTOM)
            {
                BlockPos otherPos = pos.offset(Direction.UP);
                result = useModifierOnChestBlock(context, state, pos, world.getBlockState(otherPos), otherPos);
            }
            else if (type == CursedChestType.TOP)
            {
                BlockPos otherPos = pos.offset(Direction.DOWN);
                result = useModifierOnChestBlock(context, world.getBlockState(otherPos), otherPos, state, pos);
            }
            else if (type == CursedChestType.LEFT)
            {
                BlockPos otherPos = pos.offset(facing.rotateYCCW());
                result = useModifierOnChestBlock(context, state, pos, world.getBlockState(otherPos), otherPos);
            }
            else if (type == CursedChestType.RIGHT)
            {
                BlockPos otherPos = pos.offset(facing.rotateY());
                result = useModifierOnChestBlock(context, world.getBlockState(otherPos), otherPos, state, pos);
            }
            else if (type == CursedChestType.FRONT)
            {
                BlockPos otherPos = pos.offset(facing.getOpposite());
                result = useModifierOnChestBlock(context, state, pos, world.getBlockState(otherPos), otherPos);
            }
            else if (type == CursedChestType.BACK)
            {
                BlockPos otherPos = pos.offset(facing);
                result = useModifierOnChestBlock(context, world.getBlockState(otherPos), otherPos, state, pos);
            }
            return result;
        }
        else { return useModifierOnBlock(context, state); }
    }

    @NotNull @Override
    public ActionResultType itemInteractionForEntity(@NotNull final ItemStack stack, @NotNull final PlayerEntity player, @NotNull final LivingEntity entity,
            @NotNull final Hand hand)
    { return useModifierOnEntity(stack, player, entity, hand); }

    @NotNull @Override
    public ActionResult<ItemStack> onItemRightClick(@NotNull final World world, @NotNull final PlayerEntity player, @NotNull final Hand hand)
    {
        final ActionResult<ItemStack> result = useModifierInAir(world, player, hand);
        if (result.getType() == ActionResultType.SUCCESS) { player.getCooldownTracker().setCooldown(this, 5); }
        return result;
    }

    @NotNull
    protected ActionResultType useModifierOnChestBlock(@NotNull final ItemUseContext context, @NotNull final BlockState mainState,
            @NotNull final BlockPos mainBlockPos, @Nullable final BlockState otherState, @Nullable final BlockPos otherBlockPos)
    { return ActionResultType.PASS; }

    @NotNull
    protected ActionResultType useModifierOnBlock(@NotNull final ItemUseContext context, @NotNull final BlockState state) { return ActionResultType.PASS; }

    @SuppressWarnings("unused")
    protected ActionResultType useModifierOnEntity(final ItemStack stack, final PlayerEntity player, final LivingEntity entity, final Hand hand)
    { return ActionResultType.PASS; }

    protected ActionResult<ItemStack> useModifierInAir(final World world, final PlayerEntity player, final Hand hand)
    { return ActionResult.resultPass(player.getHeldItem(hand)); }
}