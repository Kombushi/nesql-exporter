package com.github.dcysteine.nesql.exporter.plugin.gregtech.machineprops;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.gregtech.machineprops.GregTechGenerator;
import gregtech.api.metatileentity.implementations.MTEBasicGenerator;
import gregtech.common.tileentities.generators.MTESteamTurbine;
import net.minecraft.item.ItemStack;

public class GeneratorFactory extends EntityFactory<GregTechGenerator, String> {
    private final ItemFactory itemFactory;

    public GeneratorFactory(PluginExporter exporter) {
        super(exporter);
        itemFactory = new ItemFactory(exporter);
    }

    public GregTechGenerator get(int metaId, MTEBasicGenerator generator) {
        ItemStack stack = generator.getStackForm(1);
        Item item = itemFactory.get(stack);

        // Steam turbines overload getEfficiency() as steam-per-EU (3 EU per 6+tier mB);
        // the true percentage against the 2 L/EU base rate is 600 / getEfficiency().
        double efficiency = generator instanceof MTESteamTurbine
                ? 600.0 / generator.getEfficiency()
                : generator.getEfficiency();

        String id = IdPrefixUtil.GREG_TECH_GENERATOR.applyPrefix(String.valueOf(metaId));
        GregTechGenerator entity =
                new GregTechGenerator(
                        id, item, efficiency, generator.maxEUOutput(),
                        generator.maxAmperesOut());
        return findOrPersist(GregTechGenerator.class, entity);
    }
}
