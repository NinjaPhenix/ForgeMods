package ninjaphenix.expandedstorage;

import com.mojang.serialization.Lifecycle;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.util.text.ITextComponent;
import ninjaphenix.expandedstorage.common.block.enums.CursedChestType;

import java.util.function.Function;

public final class Registries
{
    public static final SimpleRegistry<ModeledTierData> MODELED = new SimpleRegistry<>(RegistryKey.func_240905_a_(ExpandedStorage.getRl("root"),
            ExpandedStorage.getRl("modeled")), Lifecycle.experimental());
    public static final SimpleRegistry<TierData> OLD = new SimpleRegistry<>(RegistryKey.func_240905_a_(ExpandedStorage.getRl("root"),
            ExpandedStorage.getRl("old")), Lifecycle.experimental());

    public static class ModeledTierData extends TierData
    {
        private final ResourceLocation singleTexture, topTexture, backTexture, rightTexture, bottomTexture, frontTexture, leftTexture;

        public ModeledTierData(final int slots, final ResourceLocation blockId, final ITextComponent containerName,
                final Function<CursedChestType, ResourceLocation> textureFunction)
        {
            super(slots, blockId, containerName);
            singleTexture = textureFunction.apply(CursedChestType.SINGLE);
            topTexture = textureFunction.apply(CursedChestType.TOP);
            backTexture = textureFunction.apply(CursedChestType.BACK);
            rightTexture = textureFunction.apply(CursedChestType.RIGHT);
            bottomTexture = textureFunction.apply(CursedChestType.BOTTOM);
            frontTexture = textureFunction.apply(CursedChestType.FRONT);
            leftTexture = textureFunction.apply(CursedChestType.LEFT);
        }

        public ResourceLocation getChestTexture(final CursedChestType type)
        {
            switch (type)
            {
                case TOP: return topTexture;
                case BACK: return backTexture;
                case RIGHT: return rightTexture;
                case BOTTOM: return bottomTexture;
                case FRONT: return frontTexture;
                case LEFT: return leftTexture;
                case SINGLE: return singleTexture;
            }
            throw new IllegalArgumentException("Unexpected CursedChestType provided.");
        }
    }

    public static class TierData
    {
        private final int slots;
        private final ITextComponent containerName;
        private final ResourceLocation blockId;

        public TierData(final int slots, final ResourceLocation blockId, final ITextComponent containerName)
        {
            this.slots = slots;
            this.containerName = containerName;
            this.blockId = blockId;
        }

        public int getSlotCount() { return slots; }

        public ITextComponent getContainerName() { return containerName; }

        public ResourceLocation getBlockId() { return blockId; }
    }
}