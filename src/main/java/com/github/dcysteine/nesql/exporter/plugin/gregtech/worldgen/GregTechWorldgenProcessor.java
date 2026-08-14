package com.github.dcysteine.nesql.exporter.plugin.gregtech.worldgen;

import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import gregtech.api.enums.OreMixes;
import gregtech.api.enums.SmallOres;
import gtneioreplugin.util.DimensionHelper;
import gtneioreplugin.util.GT5UndergroundFluidHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GregTechWorldgenProcessor extends PluginHelper {
    public GregTechWorldgenProcessor(PluginExporter exporter) {
        super(exporter);
    }

    public void process() {
        processDimensions();
        processOreVeins();
        processSmallOres();
        processUndergroundFluids();

        exporterState.flushEntityManager();
        logger.info("Finished processing GregTech worldgen!");
    }

    private void processDimensions() {
        DimensionFactory factory = new DimensionFactory(exporter);
        logger.info("Processing {} dimensions...", DimensionHelper.getAllDim().size());
        DimensionHelper.getAllDim().forEach(factory::get);
    }

    private void processOreVeins() {
        Map<String, Integer> totalWeightByDim = new HashMap<>();
        for (OreMixes mix : OreMixes.VALUES) {
            for (String internalName : mix.oreMixBuilder.dimsEnabled) {
                String abbr = DimensionHelper.INTERNAL_TO_ABBR.get(internalName);
                if (abbr != null) {
                    totalWeightByDim.merge(abbr, mix.oreMixBuilder.weight, Integer::sum);
                }
            }
        }

        OreVeinFactory factory = new OreVeinFactory(exporter);
        logger.info("Processing {} ore veins...", OreMixes.VALUES.length);
        for (OreMixes mix : OreMixes.VALUES) {
            factory.get(mix.oreMixBuilder, totalWeightByDim);
        }
    }

    private void processSmallOres() {
        Map<String, Integer> totalAmountByDim = new HashMap<>();
        for (SmallOres ore : SmallOres.values()) {
            for (String internalName : ore.smallOreBuilder.dimsEnabled) {
                String abbr = DimensionHelper.INTERNAL_TO_ABBR.get(internalName);
                if (abbr != null) {
                    totalAmountByDim.merge(abbr, ore.smallOreBuilder.amount, Integer::sum);
                }
            }
        }

        SmallOreFactory factory = new SmallOreFactory(exporter);
        logger.info("Processing {} small ores...", SmallOres.values().length);
        for (SmallOres ore : SmallOres.values()) {
            factory.get(ore.smallOreBuilder, totalAmountByDim);
        }
    }

    private void processUndergroundFluids() {
        // Normally initialized on FMLLoadCompleteEvent; init here if that somehow didn't run.
        if (GT5UndergroundFluidHelper.getAllEntries().isEmpty()) {
            GT5UndergroundFluidHelper.init();
        }

        Map<String, List<GT5UndergroundFluidHelper.UndergroundFluidWrapper>> entries =
                GT5UndergroundFluidHelper.getAllEntries();
        UndergroundFluidFactory factory = new UndergroundFluidFactory(exporter);
        logger.info("Processing {} underground fluids...", entries.size());
        entries.forEach(factory::get);
    }
}