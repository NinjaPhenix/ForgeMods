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
import torcherino.api.TierSupplier;
import torcherino.api.TorcherinoAPI;
import torcherino.config.Config;
import torcherino.network.OpenScreenMessage;

import javax.annotation.Nullable;

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
    { return hasCustomName() ? customName : new TranslationTextComponent(level.getBlockState(worldPosition).getBlock().getDescriptionId()); }

    @Override
    public boolean hasCustomName() { return customName != null; }

    @Nullable @Override
    public ITextComponent getCustomName() { return customName; }

    public void setCustomName(@Nullable final ITextComponent name) { customName = name; }

    @SuppressWarnings("ConstantConditions")
    public ResourceLocation getTierName()
    {
        if (tierName == null)
        {
            final Block block = level.getBlockState(worldPosition).getBlock();
            if (block instanceof TierSupplier) { tierName = ((TierSupplier) block).getTierName(); }
        }
        return tierName;
    }

    public OpenScreenMessage createOpenMessage() { return new OpenScreenMessage(worldPosition, getName(), xRange, zRange, yRange, speed, redstoneMode); }

    @Override
    public void load(final BlockState state, final CompoundNBT tag)
    {
        super.load(state, tag);
        if (tag.contains("CustomName", 8)) { setCustomName(ITextComponent.Serializer.fromJson(tag.getString("CustomName"))); }
        this.xRange = tag.getInt("XRange");
        this.zRange = tag.getInt("ZRange");
        this.yRange = tag.getInt("YRange");
        this.speed = tag.getInt("Speed");
        this.redstoneMode = tag.getInt("RedstoneMode");
    }

    @Override
    public CompoundNBT save(final CompoundNBT tag)
    {
        super.save(tag);
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
        area = BlockPos.betweenClosed(worldPosition.getX() - xRange, worldPosition.getY() - yRange, worldPosition.getZ() - zRange, worldPosition.getX() + xRange, worldPosition.getY() + yRange,
                                      worldPosition.getZ() + zRange);
        this.speed = speed;
        this.redstoneMode = redstoneMode;
        final BlockState state = level.getBlockState(worldPosition);
        if (state.hasProperty(BlockStateProperties.POWERED)) { setPoweredByRedstone(state.getValue(BlockStateProperties.POWERED)); }
        this.setChanged();
    }

    @Override @SuppressWarnings("ConstantConditions")
    public void tick()
    {
        if (level.isClientSide) { return; }
        if (!active || speed == 0 || (xRange == 0 && yRange == 0 && zRange == 0)) { return; }
        randomTicks = level.getGameRules().getInt(GameRules.RULE_RANDOMTICKING);
        area.forEach(this::tickBlock);
    }

    @SuppressWarnings({ "ConstantConditions", "deprecation" })
    private void tickBlock(final BlockPos blockPos)
    {
        final BlockState blockState = level.getBlockState(blockPos);
        final Block block = blockState.getBlock();
        if (TorcherinoAPI.INSTANCE.isBlockBlacklisted(block)) { return; }
        if (block.isRandomlyTicking(blockState) &&
                level.getRandom().nextInt(MathHelper.clamp(4096 / (speed * Config.INSTANCE.random_tick_rate), 1, 4096)) < randomTicks)
        { block.randomTick(blockState, (ServerWorld) level, blockPos, level.getRandom()); }
        if (!block.hasTileEntity(blockState)) { return; }
        final TileEntity tileEntity = level.getBlockEntity(blockPos);
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
        if (level.isClientSide) { return; }
        area = BlockPos.betweenClosed(worldPosition.getX() - xRange, worldPosition.getY() - yRange, worldPosition.getZ() - zRange, worldPosition.getX() + xRange, worldPosition.getY() + yRange,
                worldPosition.getZ() + zRange);
        level.getServer().tell(new TickDelayedTask(level.getServer().getTickCount(),
                () -> setPoweredByRedstone(level.getBlockState(worldPosition).getValue(BlockStateProperties.POWERED))));
    }
}