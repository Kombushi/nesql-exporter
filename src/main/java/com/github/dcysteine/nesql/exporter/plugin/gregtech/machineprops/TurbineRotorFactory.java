package com.github.dcysteine.nesql.exporter.plugin.gregtech.machineprops;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.gregtech.machineprops.GregTechTurbineRotor;
import com.github.dcysteine.nesql.sql.gregtech.machineprops.GregTechTurbineRotorFuelStats;
import gregtech.api.enums.Materials;
import gregtech.api.util.TurbineStatCalculator;
import net.minecraft.item.ItemStack;

public class TurbineRotorFactory extends EntityFactory<GregTechTurbineRotor, String> {
    private final ItemFactory itemFactory;

    public TurbineRotorFactory(PluginExporter exporter) {
        super(exporter);
        itemFactory = new ItemFactory(exporter);
    }

    public GregTechTurbineRotor get(
            int toolId, String size, Materials material, ItemStack stack,
            TurbineStatCalculator calculator) {
        Item item = itemFactory.get(stack);

        String id = IdPrefixUtil.GREG_TECH_TURBINE_ROTOR.applyPrefix(
                String.valueOf(toolId), material.mName);
        GregTechTurbineRotor entity =
                new GregTechTurbineRotor(
                        id, item, size, material.mName, calculator.getMaxDurability(),
                        calculator.getBaseEfficiency(), calculator.getOverflowEfficiency());
        entity.addFuelStats(
                new GregTechTurbineRotorFuelStats(
                        "STEAM",
                        calculator.getSteamEfficiency(),
                        calculator.getLooseSteamEfficiency(),
                        calculator.getOptimalSteamFlow(),
                        calculator.getOptimalLooseSteamFlow(),
                        calculator.getOptimalSteamEUt(),
                        calculator.getOptimalLooseSteamEUt()));
        entity.addFuelStats(
                new GregTechTurbineRotorFuelStats(
                        "GAS",
                        calculator.getGasEfficiency(),
                        calculator.getLooseGasEfficiency(),
                        calculator.getOptimalGasFlow(),
                        calculator.getOptimalLooseGasFlow(),
                        calculator.getOptimalGasEUt(),
                        calculator.getOptimalLooseGasEUt()));
        entity.addFuelStats(
                new GregTechTurbineRotorFuelStats(
                        "PLASMA",
                        calculator.getPlasmaEfficiency(),
                        calculator.getLoosePlasmaEfficiency(),
                        calculator.getOptimalPlasmaFlow(),
                        calculator.getOptimalLoosePlasmaFlow(),
                        calculator.getOptimalPlasmaEUt(),
                        calculator.getOptimalLoosePlasmaEUt()));
        return findOrPersist(GregTechTurbineRotor.class, entity);
    }
}
