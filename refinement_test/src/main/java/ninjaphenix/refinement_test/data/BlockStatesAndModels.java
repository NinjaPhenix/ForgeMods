package ninjaphenix.refinement_test.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;

public final class BlockStatesAndModels extends BlockStateProvider
{
    public BlockStatesAndModels(final DataGenerator generator, final String modId, final ExistingFileHelper fileHelper) { super(generator, modId, fileHelper); }

    @Override
    protected void registerStatesAndModels()
    {

    }

    @Override
    public String getName() { return "Refinement Test - BlockStates / Models"; }
}