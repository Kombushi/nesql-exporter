package com.github.dcysteine.nesql.exporter.plugin.cropsnh;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.cropsnh.CropsNhFertilizerItem;
import net.minecraft.item.ItemStack;

public class FertilizerItemFactory extends EntityFactory<CropsNhFertilizerItem, String> {
    private final ItemFactory itemFactory;

    public FertilizerItemFactory(PluginExporter exporter) {
        super(exporter);
        itemFactory = new ItemFactory(exporter);
    }

    public CropsNhFertilizerItem get(ItemStack stack, int potency) {
        Item item = itemFactory.get(stack);

        String id = IdPrefixUtil.CROPSNH_FERTILIZER_ITEM.applyPrefix(item.getId());
        CropsNhFertilizerItem entity = new CropsNhFertilizerItem(id, item, potency);
        return findOrPersist(CropsNhFertilizerItem.class, entity);
    }
}
