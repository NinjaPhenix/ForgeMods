package ninjaphenix.refinement.data;

import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;

import java.util.Collections;

public final class BlockLoot extends BlockLootTables
{
    @Override
    protected void addTables()
    {

    }

    @Override
    protected Iterable<Block> getKnownBlocks()
    {
        return Collections.emptySet();
    }
}