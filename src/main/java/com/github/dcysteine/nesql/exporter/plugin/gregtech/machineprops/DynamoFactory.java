package com.github.dcysteine.nesql.exporter.plugin.gregtech.machineprops;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.gregtech.machineprops.GregTechDynamo;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import net.minecraft.item.ItemStack;

public class DynamoFactory extends EntityFactory<GregTechDynamo, String> {
    private final ItemFactory itemFactory;

    public DynamoFactory(PluginExporter exporter) {
        super(exporter);
        itemFactory = new ItemFactory(exporter);
    }

    public GregTechDynamo get(int metaId, MTEHatchDynamo dynamo) {
        ItemStack stack = dynamo.getStackForm(1);
        Item item = itemFactory.get(stack);

        String id = IdPrefixUtil.GREG_TECH_DYNAMO.applyPrefix(String.valueOf(metaId));
        GregTechDynamo entity =
                new GregTechDynamo(
                        id, item, dynamo.maxEUOutput(), dynamo.maxAmperesOut(),
                        dynamo.maxEUStore());
        return findOrPersist(GregTechDynamo.class, entity);
    }
}
