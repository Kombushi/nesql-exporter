package com.github.dcysteine.nesql.exporter.plugin.gregtech.machineprops;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.FluidFactory;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.gregtech.machineprops.GregTechCombustionEngine;
import gregtech.api.enums.Materials;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import net.minecraftforge.fluids.FluidStack;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Reads each engine's constants off its class through the protected accessors, so a GT
 * update changes the export instead of silently drifting; a missing member fails the export.
 */
public class CombustionEngineFactory extends EntityFactory<GregTechCombustionEngine, String> {
    private final ItemFactory itemFactory;
    private final FluidFactory fluidFactory;

    public CombustionEngineFactory(PluginExporter exporter) {
        super(exporter);
        itemFactory = new ItemFactory(exporter);
        fluidFactory = new FluidFactory(exporter);
    }

    public GregTechCombustionEngine get(int metaId, MTEMultiBlockBase engine) {
        Item item = itemFactory.get(engine.getStackForm(1));

        try {
            int nominalOutput = (int) invoke(engine, "getNominalOutput");
            int boostFuelFactor = (int) invoke(engine, "getBoostFactor");
            int additiveFactor = (int) invoke(engine, "getAdditiveFactor");

            Materials booster = (Materials) invoke(engine, "getBooster");
            FluidStack boosterStack = booster.getGas(1);
            if (boosterStack == null) {
                throw new IllegalStateException("Booster material has no gas: " + booster.mName);
            }
            Fluid boosterFluid = fluidFactory.get(boosterStack);

            FluidStack lubricantStack = Materials.Lubricant.getFluid(1);
            if (lubricantStack == null) {
                throw new IllegalStateException("Lubricant material has no fluid");
            }
            Fluid lubricantFluid = fluidFactory.get(lubricantStack);

            Field boostEu = findField(engine.getClass(), "boostEu");
            boostEu.setBoolean(engine, false);
            int efficiencyUnboosted = engine.getMaxEfficiency(null);
            boostEu.setBoolean(engine, true);
            int efficiencyBoosted = engine.getMaxEfficiency(null);
            boostEu.setBoolean(engine, false);

            String id =
                    IdPrefixUtil.GREG_TECH_COMBUSTION_ENGINE.applyPrefix(String.valueOf(metaId));
            GregTechCombustionEngine entity =
                    new GregTechCombustionEngine(
                            id, item, nominalOutput, boosterFluid, lubricantFluid,
                            boostFuelFactor, additiveFactor, efficiencyUnboosted,
                            efficiencyBoosted);
            return findOrPersist(GregTechCombustionEngine.class, entity);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(
                    "Combustion engine reflection failed: " + engine.getClass().getName(), e);
        }
    }

    private static Object invoke(Object target, String methodName)
            throws ReflectiveOperationException {
        for (Class<?> cls = target.getClass(); cls != null; cls = cls.getSuperclass()) {
            try {
                Method method = cls.getDeclaredMethod(methodName);
                method.setAccessible(true);
                return method.invoke(target);
            } catch (NoSuchMethodException next) {
                // Keep walking up the hierarchy.
            }
        }
        throw new NoSuchMethodException(target.getClass().getName() + "." + methodName);
    }

    private static Field findField(Class<?> start, String fieldName) throws NoSuchFieldException {
        for (Class<?> cls = start; cls != null; cls = cls.getSuperclass()) {
            try {
                Field field = cls.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException next) {
                // Keep walking up the hierarchy.
            }
        }
        throw new NoSuchFieldException(start.getName() + "." + fieldName);
    }
}
