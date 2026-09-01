package com.github.dcysteine.nesql.exporter.plugin.cropsnh;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.cropsnh.CropsNhFarmComponent;
import net.minecraft.item.ItemStack;

public class FarmComponentFactory extends EntityFactory<CropsNhFarmComponent, String> {
    private final ItemFactory itemFactory;

    public FarmComponentFactory(PluginExporter exporter) {
        super(exporter);
        itemFactory = new ItemFactory(exporter);
    }

    public CropsNhFarmComponent get(ItemStack stack, String componentClass, int tier) {
        Item item = itemFactory.get(stack);

        String id = IdPrefixUtil.CROPSNH_FARM_COMPONENT.applyPrefix(item.getId());
        CropsNhFarmComponent entity = new CropsNhFarmComponent(id, item, componentClass, tier);
        return findOrPersist(CropsNhFarmComponent.class, entity);
    }
}
