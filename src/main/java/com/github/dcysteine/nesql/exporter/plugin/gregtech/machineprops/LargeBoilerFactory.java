package com.github.dcysteine.nesql.exporter.plugin.gregtech.machineprops;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.gregtech.machineprops.GregTechLargeBoiler;
import net.minecraft.item.ItemStack;

public class LargeBoilerFactory extends EntityFactory<GregTechLargeBoiler, String> {
    private final ItemFactory itemFactory;

    public LargeBoilerFactory(PluginExporter exporter) {
        super(exporter);
        itemFactory = new ItemFactory(exporter);
    }

    public GregTechLargeBoiler get(
            int metaId, ItemStack stack, int euT, int efficiencyIncrease) {
        Item item = itemFactory.get(stack);

        String id = IdPrefixUtil.GREG_TECH_LARGE_BOILER.applyPrefix(String.valueOf(metaId));
        GregTechLargeBoiler entity = new GregTechLargeBoiler(id, item, euT, efficiencyIncrease);
        return findOrPersist(GregTechLargeBoiler.class, entity);
    }
}
