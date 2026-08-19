package com.github.dcysteine.nesql.exporter.plugin.gregtech.recipemap;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.gregtech.recipemap.GregTechRecipeMap;
import com.github.dcysteine.nesql.sql.gregtech.recipemap.GregTechRecipeMapMachine;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.maps.FuelBackend;
import gregtech.api.recipe.maps.LargeBoilerFuelBackend;
import gregtech.api.util.GTLanguageManager;

import java.util.List;

public class RecipeMapFactory extends EntityFactory<GregTechRecipeMap, String> {
    public RecipeMapFactory(PluginExporter exporter) {
        super(exporter);
    }

    public GregTechRecipeMap get(RecipeMap<?> recipeMap, List<GregTechRecipeMapMachine> machines) {
        String id = IdPrefixUtil.GREG_TECH_RECIPE_MAP.applyPrefix(recipeMap.unlocalizedName);

        boolean isFuel = recipeMap.getBackend() instanceof FuelBackend
                || recipeMap.getBackend() instanceof LargeBoilerFuelBackend;
        GregTechRecipeMap entity =
                new GregTechRecipeMap(
                        id, recipeMap.unlocalizedName,
                        GTLanguageManager.getTranslation(recipeMap.unlocalizedName),
                        recipeMap.getAmperage(), isFuel);
        machines.forEach(entity::addMachine);

        return findOrPersist(GregTechRecipeMap.class, entity);
    }
}
