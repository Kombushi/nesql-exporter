package com.github.dcysteine.nesql.exporter.plugin.gregtech.machineprops;

import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.metatileentity.implementations.MTEBasicGenerator;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.util.TurbineStatCalculator;
import gregtech.common.items.IDMetaTool01;
import gregtech.common.items.MetaGeneratedTool01;
import net.minecraft.item.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;

public class MachinePropsProcessor extends PluginHelper {
    private static final Map<IDMetaTool01, String> TURBINE_SIZES = new LinkedHashMap<>();

    static {
        TURBINE_SIZES.put(IDMetaTool01.TURBINE_SMALL, "SMALL");
        TURBINE_SIZES.put(IDMetaTool01.TURBINE, "NORMAL");
        TURBINE_SIZES.put(IDMetaTool01.TURBINE_LARGE, "LARGE");
        TURBINE_SIZES.put(IDMetaTool01.TURBINE_HUGE, "HUGE");
    }

    public MachinePropsProcessor(PluginExporter exporter) {
        super(exporter);
    }

    public void process() {
        processMachines();
        processTurbineRotors();
        exporterState.flushEntityManager();
        logger.info("Finished processing GregTech machine properties!");
    }

    private void processMachines() {
        GeneratorFactory generatorFactory = new GeneratorFactory(exporter);
        DynamoFactory dynamoFactory = new DynamoFactory(exporter);

        int generators = 0;
        int dynamos = 0;
        for (int metaId = 0; metaId < GregTechAPI.METATILEENTITIES.length; metaId++) {
            IMetaTileEntity mte = GregTechAPI.METATILEENTITIES[metaId];
            if (mte == null) {
                continue;
            }

            try {
                if (mte instanceof MTEHatchDynamo) {
                    dynamoFactory.get(metaId, (MTEHatchDynamo) mte);
                    dynamos++;
                } else if (mte instanceof MTEBasicGenerator) {
                    generatorFactory.get(metaId, (MTEBasicGenerator) mte);
                    generators++;
                }
            } catch (Exception e) {
                // Some prototype machines throw when queried outside a live world; skip them.
                logger.warn("Skipping machine that failed property lookup: " + metaId, e);
            }
        }

        logger.info("Processed {} generators and {} dynamo hatches", generators, dynamos);
    }

    private void processTurbineRotors() {
        TurbineRotorFactory factory = new TurbineRotorFactory(exporter);

        int rotors = 0;
        for (Map.Entry<IDMetaTool01, String> size : TURBINE_SIZES.entrySet()) {
            for (Materials material : Materials.values()) {
                try {
                    ItemStack stack = MetaGeneratedTool01.INSTANCE.getToolWithStats(
                            size.getKey().ID, 1, material, material, null);
                    if (stack == null || !(stack.getItem() instanceof MetaGeneratedTool)) {
                        continue;
                    }

                    TurbineStatCalculator calculator =
                            new TurbineStatCalculator(
                                    (MetaGeneratedTool) stack.getItem(), stack);
                    if (calculator.getMaxDurability() <= 0) {
                        continue;
                    }

                    factory.get(size.getKey().ID, size.getValue(), material, stack, calculator);
                    rotors++;
                } catch (Exception e) {
                    // Materials without tool stats cannot make rotors; skip them.
                    logger.debug("Skipping rotor material: " + material.mName, e);
                }
            }
        }

        logger.info("Processed {} turbine rotors", rotors);
    }
}
