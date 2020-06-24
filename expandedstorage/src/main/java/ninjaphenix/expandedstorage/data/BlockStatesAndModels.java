package ninjaphenix.expandedstorage.data;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;
import ninjaphenix.expandedstorage.ModContent;
import ninjaphenix.expandedstorage.api.block.CursedChestBlock;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class BlockStatesAndModels extends BlockStateProvider
{
    public BlockStatesAndModels(final DataGenerator generator, final String modId, final ExistingFileHelper fileHelper)
    {
        super(generator, modId, fileHelper);
    }

    @Override
    protected void registerStatesAndModels()
    {
        //chestBlock(ModContent.WOOD_CHEST.getFirst(), bs -> null);
    }

    @Override
    public String getName() { return "Expanded Storage - BlockStates / Models"; }

    private void chestBlock(final CursedChestBlock block, final Function<BlockState, ModelFile> modelFunc) {
        getVariantBuilder(block).forAllStates(state ->
                        ConfiguredModel.builder()
                                       .modelFile(modelFunc.apply(state))
                                       .build()
        );
    }
}
