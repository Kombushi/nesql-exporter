package com.github.dcysteine.nesql.exporter.plugin.gregtech.catalyst;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.gregtech.catalyst.GregTechCatalyst;
import net.minecraft.item.ItemStack;

public class CatalystFactory extends EntityFactory<GregTechCatalyst, String> {
    private final ItemFactory itemFactory;

    public CatalystFactory(PluginExporter exporter) {
        super(exporter);
        itemFactory = new ItemFactory(exporter);
    }

    public GregTechCatalyst get(ItemStack stack, String kind) {
        Item item = itemFactory.get(stack);
        String id = IdPrefixUtil.GREG_TECH_CATALYST.applyPrefix(kind, item.getId());

        GregTechCatalyst entity = new GregTechCatalyst(id, item, kind);
        return findOrPersist(GregTechCatalyst.class, entity);
    }
}
