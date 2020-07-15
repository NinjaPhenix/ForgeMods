package torcherino.block.tile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.INameable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.Nullable;
import torcherino.api.TierSupplier;
import torcherino.api.TorcherinoAPI;
import torcherino.config.Config;
import torcherino.network.OpenScreenMessage;

import static torcherino.ModContent.TORCHERINO_TILE_ENTITY;

public class TorcherinoTileEntity extends TileEntity implements INameable, ITickableTileEntity
{
    private ITextComponent customName;
    private int xRange, yRange, zRange, speed, redstoneMode, randomTicks;
    private boolean active;
    private Iterable<BlockPos> area;
    private ResourceLocation tierName;

    public TorcherinoTileEntity() { super(TORCHERINO_TILE_ENTITY); }

    @Override @SuppressWarnings("ConstantConditions")
    public ITextComponent getName()
    { return hasCustomName() ? customName : new TranslationTextComponent(world.getBlockState(pos).getBlock().getTranslationKey()); }

    @Override
    public boolean hasCustomName() { return customName != null; }

    @Nullable @Override
    public ITextComponent getCustomName() { return customName; }

    public void setCustomName(@Nullable ITextComponent name) { customName = name; }

    @SuppressWarnings("ConstantConditions")
    public ResourceLocation getTierName()
    {
        if (tierName == null)
        {
            final Block block = world.getBlockState(pos).getBlock();
            if (block instanceof TierSupplier) { tierName = ((TierSupplier) block).getTierName(); }
        }
        return tierName;
    }

    public OpenScreenMessage createOpenMessage() { return new OpenScreenMessage(pos, getName(), xRange, zRange, yRange, speed, redstoneMode); }

    @Override
    public void read(final BlockState state, final CompoundNBT tag)
    {
        super.read(state, tag);
        if (tag.contains("CustomName", 8)) { setCustomName(ITextComponent.Serializer.func_240643_a_(tag.getString("CustomName"))); }
        this.xRange = tag.getInt("XRange");
        this.zRange = tag.getInt("ZRange");
        this.yRange = tag.getInt("YRange");
        this.speed = tag.getInt("Speed");
        this.redstoneMode = tag.getInt("RedstoneMode");
    }

    @Override
    public CompoundNBT write(final CompoundNBT tag)
    {
        super.write(tag);
        if (hasCustomName()) { tag.putString("CustomName", ITextComponent.Serializer.toJson(getCustomName())); }
        tag.putInt("XRange", this.xRange);
        tag.putInt("ZRange", this.zRange);
        tag.putInt("YRange", this.yRange);
        tag.putInt("Speed", this.speed);
        tag.putInt("RedstoneMode", this.redstoneMode);
        return tag;
    }

    @SuppressWarnings("ConstantConditions")
    public void readClientData(final int xRange, final int zRange, final int yRange, final int speed, final int redstoneMode)
    {
        this.xRange = xRange;
        this.zRange = zRange;
        this.yRange = yRange;
        area = BlockPos.getAllInBoxMutable(pos.getX() - xRange, pos.getY() - yRange, pos.getZ() - zRange, pos.getX() + xRange, pos.getY() + yRange,
                pos.getZ() + zRange);
        this.speed = speed;
        this.redstoneMode = redstoneMode;
        BlockState state = world.getBlockState(pos);
        if (state.func_235901_b_(BlockStateProperties.POWERED)) { setPoweredByRedstone(state.get(BlockStateProperties.POWERED)); }
        this.markDirty();
    }

    @Override @SuppressWarnings("ConstantConditions")
    public void tick()
    {
        if (world.isRemote) { return; }
        if (!active || speed == 0 || (xRange == 0 && yRange == 0 && zRange == 0)) { return; }
        randomTicks = world.getGameRules().getInt(GameRules.RANDOM_TICK_SPEED);
        area.forEach(this::tickBlock);
    }

    @SuppressWarnings({ "ConstantConditions", "deprecation" })
    private void tickBlock(final BlockPos blockPos)
    {
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        if (TorcherinoAPI.INSTANCE.isBlockBlacklisted(block)) { return; }
        if (block.ticksRandomly(blockState) &&
                world.getRandom().nextInt(MathHelper.clamp(4096 / (speed * Config.INSTANCE.random_tick_rate), 1, 4096)) < randomTicks)
        { block.randomTick(blockState, (ServerWorld) world, blockPos, world.getRandom()); }
        if (!block.hasTileEntity(blockState)) { return; }
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (tileEntity == null || tileEntity.isRemoved() || TorcherinoAPI.INSTANCE.isTileEntityBlacklisted(tileEntity.getType()) ||
                !(tileEntity instanceof ITickableTileEntity)) { return; }
        for (int i = 0; i < speed; i++)
        {
            if (tileEntity.isRemoved()) { break; }
            ((ITickableTileEntity) tileEntity).tick();
        }
    }

    public void setPoweredByRedstone(final boolean powered)
    {
        if (redstoneMode == 0) { active = !powered; }
        else if (redstoneMode == 1) {active = powered; }
        else if (redstoneMode == 2) {active = true;}
        else if (redstoneMode == 3) { active = false; }
    }

    @Override @SuppressWarnings("ConstantConditions")
    public void onLoad()
    {
        super.onLoad();
        if (world.isRemote) { return; }
        area = BlockPos.getAllInBoxMutable(pos.getX() - xRange, pos.getY() - yRange, pos.getZ() - zRange, pos.getX() + xRange, pos.getY() + yRange,
                pos.getZ() + zRange);
        world.getServer().enqueue(new TickDelayedTask(world.getServer().getTickCounter(),
                () -> setPoweredByRedstone(world.getBlockState(pos).get(BlockStateProperties.POWERED))));
    }
}