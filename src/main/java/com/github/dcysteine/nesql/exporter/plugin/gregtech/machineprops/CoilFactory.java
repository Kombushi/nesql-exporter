package com.github.dcysteine.nesql.exporter.plugin.gregtech.machineprops;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.gregtech.machineprops.GregTechCoil;
import gregtech.api.enums.HeatingCoilLevel;
import net.minecraft.item.ItemStack;

public class CoilFactory extends EntityFactory<GregTechCoil, String> {
    private final ItemFactory itemFactory;

    public CoilFactory(PluginExporter exporter) {
        super(exporter);
        itemFactory = new ItemFactory(exporter);
    }

    public GregTechCoil get(int meta, ItemStack stack, HeatingCoilLevel level) {
        Item item = itemFactory.get(stack);

        String id = IdPrefixUtil.GREG_TECH_COIL.applyPrefix(String.valueOf(meta));
        GregTechCoil entity =
                new GregTechCoil(id, item, level.getHeat(), level.getName(), level.getTier());
        return findOrPersist(GregTechCoil.class, entity);
    }
}
