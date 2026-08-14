package com.github.dcysteine.nesql.exporter.plugin.gregtech.recipemap;

import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.gregtech.recipemap.GregTechRecipeMapMachine;
import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.RecipeMapWorkable;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.recipe.RecipeMap;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GregTechRecipeMapProcessor extends PluginHelper {
    public GregTechRecipeMapProcessor(PluginExporter exporter) {
        super(exporter);
    }

    public void process() {
        Map<String, List<GregTechRecipeMapMachine>> machinesByMap = collectMachines();

        RecipeMapFactory factory = new RecipeMapFactory(exporter);
        logger.info("Processing {} recipe maps...", RecipeMap.ALL_RECIPE_MAPS.size());
        for (RecipeMap<?> recipeMap : RecipeMap.ALL_RECIPE_MAPS.values()) {
            factory.get(
                    recipeMap,
                    machinesByMap.getOrDefault(
                            recipeMap.unlocalizedName, Collections.emptyList()));
        }

        exporterState.flushEntityManager();
        logger.info("Finished processing GregTech recipe maps!");
    }

    private Map<String, List<GregTechRecipeMapMachine>> collectMachines() {
        ItemFactory itemFactory = new ItemFactory(exporter);
        Map<String, List<GregTechRecipeMapMachine>> machinesByMap = new HashMap<>();

        for (IMetaTileEntity mte : GregTechAPI.METATILEENTITIES) {
            if (!(mte instanceof RecipeMapWorkable)) {
                continue;
            }

            try {
                Collection<RecipeMap<?>> recipeMaps =
                        ((RecipeMapWorkable) mte).getAvailableRecipeMaps();
                if (recipeMaps == null || recipeMaps.isEmpty()) {
                    continue;
                }

                ItemStack stack = mte.getStackForm(1);
                if (stack == null || stack.getItem() == null) {
                    continue;
                }
                Item item = itemFactory.get(stack);

                boolean multiblock = mte instanceof MTEMultiBlockBase;
                Integer tier = null;
                if (mte instanceof MTETieredMachineBlock) {
                    tier = (int) ((MTETieredMachineBlock) mte).mTier;
                }

                GregTechRecipeMapMachine machine =
                        new GregTechRecipeMapMachine(item, tier, multiblock);
                for (RecipeMap<?> recipeMap : recipeMaps) {
                    if (recipeMap == null) {
                        continue;
                    }
                    machinesByMap
                            .computeIfAbsent(recipeMap.unlocalizedName, key -> new ArrayList<>())
                            .add(machine);
                }
            } catch (Exception e) {
                // Some machines throw when queried outside a live world; skip them.
                logger.warn("Skipping machine that failed recipe map lookup", e);
            }
        }

        return machinesByMap;
    }
}
