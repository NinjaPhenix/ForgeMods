package torcherino.data;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;

public final class BlockStatesAndModels extends BlockStateProvider
{
    public BlockStatesAndModels(final DataGenerator generator, final String modId, final ExistingFileHelper fileHelper)
    {
        super(generator, modId, fileHelper);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void registerStatesAndModels()
    {
        simpleTorch(ModContentHolder.TORCHERINO);
        simpleTorch(ModContentHolder.COMPRESSED_TORCHERINO);
        simpleTorch(ModContentHolder.DOUBLE_COMPRESSED_TORCHERINO);

        lanterinoModel(ModContentHolder.LANTERINO, mcLoc("blocks/pumpkin_top"), mcLoc("blocks/pumpkin_side"));
        lanterinoModel(ModContentHolder.COMPRESSED_LANTERINO, modLoc("blocks/compressed_lanterino_top"), modLoc("blocks/compressed_lanterino_side"));
        lanterinoModel(ModContentHolder.DOUBLE_COMPRESSED_LANTERINO, modLoc("blocks/double_compressed_lanterino_top"), modLoc("blocks/double_compressed_lanterino_side"));

        simpleLantern(ModContentHolder.LANTERN);
        simpleLantern(ModContentHolder.COMPRESSED_LANTERN);
        simpleLantern(ModContentHolder.DOUBLE_COMPRESSED_LANTERN);
    }

    @SuppressWarnings("ConstantConditions")
    private void simpleTorch(final Block block)
    {
        final String path = block.getRegistryName().getPath();
        simpleBlock(block, models().torch(path, modLoc("blocks/" + path)));
        simpleBlock(block, models().torchWall("wall_" + path, modLoc("blocks/" + path)));
        itemModels().singleTexture(path, mcLoc("item/generated"), "layer0", modLoc("item/" + path));
    }

    @SuppressWarnings("ConstantConditions")
    private void simpleLantern(final Block block)
    {
        final String path = block.getRegistryName().getPath();
        simpleBlock(block, models().singleTexture(path, mcLoc("block/template_lantern"), "lantern", modLoc("blocks/" + path)));
        itemModels().singleTexture(path, mcLoc("item/generated"), "layer0", modLoc("item/" + path));
    }

    @SuppressWarnings("ConstantConditions")
    private void lanterinoModel(final Block block, final ResourceLocation top, final ResourceLocation side)
    {
        final String path = block.getRegistryName().getPath();
        final ModelFile lanterinoModel = models().orientable(path, side, modLoc("blocks/" + path), top);
        horizontalBlock(block, lanterinoModel);
        simpleBlockItem(block, lanterinoModel);
    }

    @Override
    public String getName()
    {
        return "Torcherino - BlockStates / Models";
    }
}