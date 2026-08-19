package com.github.dcysteine.nesql.exporter.plugin.gregtech.itemdata;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.gregtech.itemdata.GregTechItemData;
import com.github.dcysteine.nesql.sql.gregtech.itemdata.GregTechItemDataByProduct;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.MaterialStack;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemDataFactory extends EntityFactory<GregTechItemData, String> {
    private final ItemFactory itemFactory;

    public ItemDataFactory(PluginExporter exporter) {
        super(exporter);
        itemFactory = new ItemFactory(exporter);
    }

    public GregTechItemData get(ItemStack stack, ItemData data) {
        Item item = itemFactory.get(stack);
        String id = IdPrefixUtil.GREG_TECH_ITEM_DATA.applyPrefix(item.getId());

        List<GregTechItemDataByProduct> byProducts = new ArrayList<>();
        if (data.mByProducts != null) {
            for (MaterialStack byProduct : data.mByProducts) {
                if (byProduct == null || byProduct.mMaterial == null) {
                    continue;
                }
                byProducts.add(
                        new GregTechItemDataByProduct(byProduct.mMaterial.mName, byProduct.mAmount));
            }
        }

        GregTechItemData entity =
                new GregTechItemData(
                        id, item,
                        data.mPrefix == null ? null : data.mPrefix.getName(),
                        data.mMaterial.mMaterial.mName,
                        data.mMaterial.mAmount,
                        byProducts);
        return findOrPersist(GregTechItemData.class, entity);
    }
}
