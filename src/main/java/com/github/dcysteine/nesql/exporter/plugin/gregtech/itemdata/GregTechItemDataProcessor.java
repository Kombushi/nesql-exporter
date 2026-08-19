package com.github.dcysteine.nesql.exporter.plugin.gregtech.itemdata;

import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.lang.reflect.Field;
import java.util.Map;

public class GregTechItemDataProcessor extends PluginHelper {
    public GregTechItemDataProcessor(PluginExporter exporter) {
        super(exporter);
    }

    public void process() {
        Map<ItemStack, ItemData> itemDataMap = readItemDataMap();
        ItemDataFactory factory = new ItemDataFactory(exporter);
        logger.info("Processing {} GregTech item data entries...", itemDataMap.size());
        int exported = 0;
        for (Map.Entry<ItemStack, ItemData> entry : itemDataMap.entrySet()) {
            ItemStack stack = entry.getKey();
            ItemData data = entry.getValue();
            if (stack == null || stack.getItem() == null
                    || stack.getItemDamage() == OreDictionary.WILDCARD_VALUE
                    || data == null || !data.hasValidMaterialData()) {
                continue;
            }

            try {
                factory.get(stack, data);
                exported++;
            } catch (Exception e) {
                logger.warn("Skipping item data for " + stack.getDisplayName(), e);
            }
        }

        exporterState.flushEntityManager();
        logger.info("Finished processing GregTech item data: {} items", exported);
    }

    /** The item data map is private; this is the only way at the pinned GT version. */
    @SuppressWarnings("unchecked")
    private static Map<ItemStack, ItemData> readItemDataMap() {
        try {
            Field field = GTOreDictUnificator.class.getDeclaredField("sItemStack2DataMap");
            field.setAccessible(true);
            return (Map<ItemStack, ItemData>) field.get(null);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("GTOreDictUnificator.sItemStack2DataMap is unreadable", e);
        }
    }
}
