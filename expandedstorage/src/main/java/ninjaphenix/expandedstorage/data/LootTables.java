package ninjaphenix.expandedstorage.data;

import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.ValidationTracker;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LootTables extends LootTableProvider
{
    public LootTables(@NotNull final DataGenerator generator) { super(generator); }

    @NotNull @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables()
    { return Collections.singletonList(new Pair<>(BlockLoot::new, LootParameterSets.BLOCK)); }

    @Override
    protected void validate(@NotNull final Map<ResourceLocation, LootTable> map, @NotNull final ValidationTracker validationtracker) {}

    @NotNull @Override
    public String getName() { return "Expanded Storage - Loot Tables"; }
}