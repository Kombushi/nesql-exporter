package com.github.dcysteine.nesql.exporter.plugin.gregtech.machineprops;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.gregtech.machineprops.GregTechTreeFarmTool;
import gregtech.common.tileentities.machines.multi.MTETreeFarm;
import net.minecraft.item.ItemStack;

public class TreeFarmToolFactory extends EntityFactory<GregTechTreeFarmTool, String> {
    private final ItemFactory itemFactory;

    public TreeFarmToolFactory(PluginExporter exporter) {
        super(exporter);
        itemFactory = new ItemFactory(exporter);
    }

    public GregTechTreeFarmTool get(ItemStack stack, MTETreeFarm.Mode mode, int multiplier) {
        Item item = itemFactory.get(stack);

        String id = IdPrefixUtil.GREG_TECH_TREE_FARM_TOOL.applyPrefix(item.getId(), mode.name());
        GregTechTreeFarmTool entity = new GregTechTreeFarmTool(id, item, mode.name(), multiplier);
        return findOrPersist(GregTechTreeFarmTool.class, entity);
    }
}
