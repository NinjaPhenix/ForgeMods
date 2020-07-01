package ninjaphenix.expandedstorage.api;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import ninjaphenix.expandedstorage.ExpandedStorage;
import ninjaphenix.expandedstorage.api.block.enums.CursedChestType;

import java.util.function.Function;

/**
 * This class provides data registries for adding new chests to already defined chest types. This will likely be refactored in the future as new features and
 * chest types are added.
 *
 * @author NinjaPhenix
 * @since 3.4.26
 */
public class Registries
{
	/**
	 * This registry for CursedChestBlock data storage.
	 */
	public static final SimpleRegistry<ModeledTierData> MODELED = new SimpleRegistry<>();

	/**
	 * This registry is for OldChestBlock data storage.
	 */
	public static final SimpleRegistry<TierData> OLD = new SimpleRegistry<>();

    public static class ModeledTierData extends TierData
    {
        private final ResourceLocation singleTexture;
        private final ResourceLocation topTexture;
        private final ResourceLocation backTexture;
        private final ResourceLocation rightTexture;
        private final ResourceLocation bottomTexture;
        private final ResourceLocation frontTexture;
        private final ResourceLocation leftTexture;

        /**
         * Data representing a vanilla looking chest block.
         *
         * @param slots The amount of itemstacks this chest tier can hold.
         * @param containerName The default container name for this chest tier.
         * @param blockId The block id that represents this data.
         * @param textureFunction The function which returns the chest texture for a supplied type.
         */
        public ModeledTierData(int slots, ResourceLocation blockId, ITextComponent containerName, Function<CursedChestType, ResourceLocation> textureFunction)
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

        /**
         * @param type The chest type to receive the texture for.
         * @return The texture relevant to the type.
         */
        public ResourceLocation getChestTexture(CursedChestType type)
        {
            switch(type) {
                case TOP: return topTexture;
                case BACK: return backTexture;
                case RIGHT: return rightTexture;
                case BOTTOM: return bottomTexture;
                case FRONT: return frontTexture;
                case LEFT: return leftTexture;
                default: return singleTexture;
            }
        }
    }

	public static class TierData
	{
		private final int slots;
		private final ITextComponent containerName;
		private final ResourceLocation blockId;

		/**
		 * Data representing minimalist chest blocks such as OldChestBlock's.
		 *
		 * @param slots The amount of itemstacks this chest tier can hold.
		 * @param containerName The default container name for this chest tier.
		 * @param blockId The block id that represents this data.
		 */
		public TierData(int slots, ResourceLocation blockId, ITextComponent containerName)
		{
			this.slots = slots;
			this.containerName = containerName;
			this.blockId = blockId;
		}

		/**
		 * @return The amount of slots the chest tier contains.
		 */
		public int getSlotCount() { return slots; }

		/**
		 * @return The default container name for the chest tier.
		 */
		public ITextComponent getContainerName() { return containerName; }

		/**
		 * @return The block that represents this data instance.
		 */
		public ResourceLocation getBlockId() { return blockId; }
	}
}
