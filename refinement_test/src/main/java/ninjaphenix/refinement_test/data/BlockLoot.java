package ninjaphenix.refinement_test.data;

import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraftforge.fml.RegistryObject;
import ninjaphenix.refinement_test.common.TestContent;

import java.util.stream.Collectors;

public final class BlockLoot extends BlockLootTables
{
    @Override
    protected void addTables()
    {

    }

    @Override
    protected Iterable<Block> getKnownBlocks()
    {
        return TestContent.BLOCKS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toSet());
    }
}