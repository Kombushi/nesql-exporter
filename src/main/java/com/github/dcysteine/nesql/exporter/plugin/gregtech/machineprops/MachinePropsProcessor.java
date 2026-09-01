package com.github.dcysteine.nesql.exporter.plugin.gregtech.machineprops;

import codechicken.nei.ItemList;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import goodgenerator.main.GGConfigLoader;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.metatileentity.implementations.MTEBasicGenerator;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.util.TurbineStatCalculator;
import gregtech.common.blocks.BlockCasings5;
import gregtech.common.tileentities.machines.multi.MTEExtremeCombustionEngine;
import gregtech.common.tileentities.machines.multi.MTELargeBoiler;
import gregtech.common.tileentities.machines.multi.MTELargeBoilerBase;
import gregtech.common.tileentities.machines.multi.MTELargeCombustionEngine;
import gregtech.common.tileentities.machines.multi.MTELargeNaquadahReactor;
import gregtech.common.tileentities.machines.multi.MTETreeFarm;
import gregtech.common.items.IDMetaTool01;
import gregtech.common.items.MetaGeneratedTool01;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        processCoils();
        processTreeFarmTools();
        processEngines();
        processNaquadahReactors();
        exporterState.flushEntityManager();
        logger.info("Finished processing GregTech machine properties!");
    }

    // Deliberately without a per-machine catch: a reflection miss must fail the export.
    private void processEngines() {
        CombustionEngineFactory factory = new CombustionEngineFactory(exporter);

        int engines = 0;
        for (int metaId = 0; metaId < GregTechAPI.METATILEENTITIES.length; metaId++) {
            IMetaTileEntity mte = GregTechAPI.METATILEENTITIES[metaId];
            if (mte instanceof MTELargeCombustionEngine
                    || mte instanceof MTEExtremeCombustionEngine) {
                factory.get(metaId, (MTEMultiBlockBase) mte);
                engines++;
            }
        }

        logger.info("Processed {} combustion engines", engines);
    }

    // Deliberately without a per-machine catch: a reflection miss must fail the export.
    private void processNaquadahReactors() {
        ReactorModeFactory factory = new ReactorModeFactory(exporter);

        int reactors = 0;
        for (int metaId = 0; metaId < GregTechAPI.METATILEENTITIES.length; metaId++) {
            IMetaTileEntity mte = GregTechAPI.METATILEENTITIES[metaId];
            if (!(mte instanceof MTELargeNaquadahReactor)) {
                continue;
            }

            ItemStack machineStack = mte.getStackForm(1);
            for (Pair<FluidStack, Integer> pair : invokeFluidPairList("getExcitedLiquid")) {
                factory.get(metaId, machineStack, "EXCITED", pair.getKey(), pair.getValue());
            }
            for (Pair<FluidStack, Integer> pair : invokeFluidPairList("getCoolant")) {
                factory.get(metaId, machineStack, "COOLANT", pair.getKey(), pair.getValue());
            }
            if (GGConfigLoader.LiquidAirConsumptionPerSecond > 0) {
                factory.get(
                        metaId, machineStack, "UPKEEP",
                        Materials.LiquidAir.getFluid(GGConfigLoader.LiquidAirConsumptionPerSecond),
                        null);
            }
            reactors++;
        }

        logger.info("Processed {} naquadah reactors", reactors);
    }

    @SuppressWarnings("unchecked")
    private static List<Pair<FluidStack, Integer>> invokeFluidPairList(String methodName) {
        try {
            Method method = MTELargeNaquadahReactor.class.getDeclaredMethod(methodName);
            method.setAccessible(true);
            return (List<Pair<FluidStack, Integer>>) method.invoke(null);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Naquadah reactor reflection failed: " + methodName, e);
        }
    }

    private void processTreeFarmTools() {
        TreeFarmToolFactory factory = new TreeFarmToolFactory(exporter);

        int tools = 0;
        for (ItemStack stack : ItemList.items) {
            for (MTETreeFarm.Mode mode : MTETreeFarm.Mode.values()) {
                try {
                    int multiplier = MTETreeFarm.getToolMultiplier(stack, mode);
                    if (multiplier <= 0) {
                        continue;
                    }

                    factory.get(stack, mode, multiplier);
                    tools++;
                } catch (Exception e) {
                    // Probing an arbitrary item as a tool may throw; such items are not tools.
                    logger.debug("Skipping tree farm tool probe: " + stack, e);
                }
            }
        }

        logger.info("Processed {} tree farm tool modes", tools);
    }

    private void processCoils() {
        CoilFactory factory = new CoilFactory(exporter);

        int coils = 0;
        Set<HeatingCoilLevel> seen = EnumSet.of(HeatingCoilLevel.None);
        for (int meta = 0; meta < 16; meta++) {
            HeatingCoilLevel level = BlockCasings5.getCoilHeatFromDamage(meta);
            if (!seen.add(level)) {
                continue;
            }

            factory.get(meta, new ItemStack(GregTechAPI.sBlockCasings5, 1, meta), level);
            coils++;
        }

        logger.info("Processed {} heating coils", coils);
    }

    private void processMachines() {
        GeneratorFactory generatorFactory = new GeneratorFactory(exporter);
        DynamoFactory dynamoFactory = new DynamoFactory(exporter);
        LargeBoilerFactory boilerFactory = new LargeBoilerFactory(exporter);
        MultiblockMachineFactory multiblockFactory = new MultiblockMachineFactory(exporter);

        int generators = 0;
        int dynamos = 0;
        int boilers = 0;
        int multiblocks = 0;
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

                if (mte instanceof MTELargeBoilerBase) {
                    MTELargeBoilerBase boiler = (MTELargeBoilerBase) mte;
                    boilerFactory.get(
                            metaId, mte.getStackForm(1), boiler.getEUt(),
                            boiler.getEfficiencyIncrease());
                    boilers++;
                } else if (mte instanceof MTELargeBoiler) {
                    MTELargeBoiler boiler = (MTELargeBoiler) mte;
                    boilerFactory.get(
                            metaId, mte.getStackForm(1), boiler.getEUt(),
                            boiler.getEfficiencyIncrease());
                    boilers++;
                }

                if (mte instanceof MTEMultiBlockBase) {
                    multiblockFactory.get(metaId, (MTEMultiBlockBase) mte);
                    multiblocks++;
                }
            } catch (Exception e) {
                // Some prototype machines throw when queried outside a live world; skip them.
                logger.warn("Skipping machine that failed property lookup: " + metaId, e);
            }
        }

        logger.info(
                "Processed {} generators, {} dynamo hatches, {} large boilers, {} multiblocks",
                generators, dynamos, boilers, multiblocks);
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
