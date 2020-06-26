package ninjaphenix.expandedstorage.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.*;
import ninjaphenix.expandedstorage.ModContent;
import ninjaphenix.expandedstorage.api.block.AbstractChestBlock;
import ninjaphenix.expandedstorage.api.block.CursedChestBlock;
import ninjaphenix.expandedstorage.api.block.OldChestBlock;
import ninjaphenix.expandedstorage.api.block.enums.CursedChestType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class BlockStatesAndModels extends BlockStateProvider
{
    private ModelFile OLD_CHEST_HORIZONTAL;
    private ModelFile OLD_CHEST_VERTICAL;
    public static HashMap<Item, ModelFile> SINGLE_OLD_MODELS = new HashMap<>();

    public BlockStatesAndModels(final DataGenerator generator, final String modId, final ExistingFileHelper fileHelper) { super(generator, modId, fileHelper); }

    @Override
    protected void registerStatesAndModels()
    {
        chestBlock(ModContent.WOOD_CHEST.getFirst());
        chestBlock(ModContent.PUMPKIN_CHEST.getFirst());
        chestBlock(ModContent.CHRISTMAS_CHEST.getFirst());
        chestBlock(ModContent.IRON_CHEST.getFirst());
        chestBlock(ModContent.GOLD_CHEST.getFirst());
        chestBlock(ModContent.DIAMOND_CHEST.getFirst());
        chestBlock(ModContent.OBSIDIAN_CHEST.getFirst());
        OLD_CHEST_HORIZONTAL = models().getBuilder("block/old_chest/horizontal")
                                       .parent(models().getExistingFile(mcLoc("cube")))
                                       .texture("particle", "#front")
                                       .texture("down", "#top")
                                       .texture("up", "#top")
                                       .texture("north", "#front")
                                       .texture("east", "#left")
                                       .texture("south", "#back")
                                       .texture("west", "#right");
        // todo: display first person transform
        OLD_CHEST_VERTICAL = models().getBuilder("block/old_chest/vertical")
                                     .parent(OLD_CHEST_HORIZONTAL)
                                     .texture("east", "#side")
                                     .texture("west", "#side")
                                     .texture("south", "#side");
        oldChestBlock(ModContent.OLD_WOOD_CHEST.getFirst());
        oldChestBlock(ModContent.OLD_IRON_CHEST.getFirst());
        oldChestBlock(ModContent.OLD_GOLD_CHEST.getFirst());
        oldChestBlock(ModContent.OLD_DIAMOND_CHEST.getFirst());
        oldChestBlock(ModContent.OLD_OBSIDIAN_CHEST.getFirst());
    }

    @Override
    public @NotNull String getName() { return "Expanded Storage - BlockStates / Models"; }

    private void oldChestBlock(final OldChestBlock block)
    {
        getVariantBuilder(block).forAllStatesExcept(state -> {
            final String blockPath = block.getRegistryName().getPath();
            final CursedChestType chestType = state.get(AbstractChestBlock.TYPE);
            final ConfiguredModel[] result = ConfiguredModel
                    .builder()
                    .rotationY(((state.get(BlockStateProperties.HORIZONTAL_FACING).getHorizontalIndex() + 2) % 4) * 90)
                    .modelFile(oldChestGetModel(blockPath, chestType))
                    .build();
            if(chestType == CursedChestType.SINGLE) {
                SINGLE_OLD_MODELS.put(block.asItem(), result[0].model);
            }
            return result;
        }, BlockStateProperties.WATERLOGGED);
    }

    private ModelFile oldChestGetModel(final String blockPath, final CursedChestType chestType)
    {
        final String chestTypeName = chestType.getName();
        final BlockModelBuilder builder = models().getBuilder(String.format("block/%s/%s", blockPath, chestTypeName));
        switch (chestType)
        {
            case TOP:
                builder.parent(OLD_CHEST_VERTICAL)
                       .texture("top", modLoc(String.format("block/%s/chest_top", blockPath)))
                       .texture("front", modLoc(String.format("block/%s/chest_front_%s", blockPath, chestTypeName)))
                       .texture("side", modLoc(String.format("block/%s/chest_side_top", blockPath)));
                break;
            case BOTTOM:
                builder.parent(OLD_CHEST_VERTICAL)
                       .texture("top", modLoc(String.format("block/%s/chest_top", blockPath)))
                       .texture("front", modLoc(String.format("block/%s/chest_front_%s", blockPath, chestTypeName)))
                       .texture("side", modLoc(String.format("block/%s/chest_front_bottom", blockPath)));
                break;
            case SINGLE:
                builder.parent(OLD_CHEST_VERTICAL)
                       .texture("top", modLoc(String.format("block/%s/chest_top", blockPath)))
                       .texture("front", modLoc(String.format("block/%s/chest_front_%s", blockPath, chestTypeName)))
                       .texture("side", modLoc(String.format("block/%s/chest_side", blockPath)));
                break;
            case FRONT:
                builder.parent(OLD_CHEST_HORIZONTAL)
                       .texture("top", modLoc(String.format("block/%s/chest_top_%s", blockPath, chestTypeName)))
                       .texture("front", modLoc(String.format("block/%s/chest_front_single", blockPath)))
                       .texture("back", modLoc(String.format("block/%s/chest_side", blockPath)))
                       .texture("left", modLoc(String.format("block/%s/chest_back_left", blockPath)))
                       .texture("right", modLoc(String.format("block/%s/chest_back_right", blockPath)));
                break;
            case BACK:
                builder.parent(OLD_CHEST_HORIZONTAL)
                       .texture("top", modLoc(String.format("block/%s/chest_top_%s", blockPath, chestTypeName)))
                       .texture("front", modLoc(String.format("block/%s/chest_front_single", blockPath)))
                       .texture("back", modLoc(String.format("block/%s/chest_side", blockPath)))
                       .texture("left", modLoc(String.format("block/%s/chest_back_right", blockPath)))
                       .texture("right", modLoc(String.format("block/%s/chest_back_left", blockPath)));
                break;
            case LEFT:
            case RIGHT:
                builder.parent(OLD_CHEST_HORIZONTAL)
                       .texture("top", modLoc(String.format("block/%s/chest_top_%s", blockPath, chestTypeName)))
                       .texture("front", modLoc(String.format("block/%s/chest_front_%s", blockPath, chestTypeName)))
                       .texture("back", modLoc(String.format("block/%s/chest_back_%s", blockPath, chestTypeName)))
                       .texture("left", modLoc(String.format("block/%s/chest_side", blockPath)))
                       .texture("right", modLoc(String.format("block/%s/chest_side", blockPath)));
                break;
        }
        return builder;
    }

    private void chestBlock(final CursedChestBlock block)
    {
        getVariantBuilder(block).forAllStatesExcept(state -> ConfiguredModel
                        .builder().modelFile(chestGetParticleTexture(state.get(AbstractChestBlock.TYPE), block.getRegistryName().getPath())).build(),
                BlockStateProperties.WATERLOGGED, BlockStateProperties.HORIZONTAL_FACING);
    }

    private ModelFile chestGetParticleTexture(final CursedChestType type, final String blockPath)
    {
        final String chestName = blockPath.substring(0, blockPath.indexOf('_'));
        if (blockPath.equals("pumpkin_chest"))
        {
            return models().getBuilder(blockPath).texture("particle", modLoc(String.format("block/%s_break", chestName)));
        }
        else if (blockPath.equals("christmas_chest"))
        {
            final String chestType = CursedChestType.TOP == type || CursedChestType.BOTTOM == type ? "tall" :
                    CursedChestType.LEFT == type || CursedChestType.RIGHT == type ? "vanilla" :
                            CursedChestType.FRONT == type || CursedChestType.BACK == type ? "long" : "single";
            return models().getBuilder(chestType + "_" + blockPath).texture("particle", modLoc(String.format("block/%s_%s_break", chestType, chestName)));
        }
        else
        {
            return models().getBuilder(blockPath).texture("particle", modLoc("block/old_" + blockPath + "/chest_front_single"));
        }
    }
}
