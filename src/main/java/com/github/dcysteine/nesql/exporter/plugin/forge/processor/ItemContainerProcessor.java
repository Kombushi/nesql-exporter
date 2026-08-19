package com.github.dcysteine.nesql.exporter.plugin.forge.processor;

import codechicken.nei.ItemList;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.plugin.forge.factory.ItemContainerFactory;
import net.minecraft.item.ItemStack;

public class ItemContainerProcessor extends PluginHelper {
    public ItemContainerProcessor(PluginExporter exporter) {
        super(exporter);
    }

    public void process() {
        ItemContainerFactory factory = new ItemContainerFactory(exporter);
        logger.info("Scanning {} items for container items...", ItemList.items.size());
        int count = 0;
        for (ItemStack stack : ItemList.items) {
            try {
                if (stack == null || stack.getItem() == null
                        || !stack.getItem().hasContainerItem(stack)) {
                    continue;
                }
                // Tools damage the stack they are asked about, so they get a copy.
                ItemStack container = stack.getItem().getContainerItem(stack.copy());
                if (container == null || container.getItem() == null) {
                    continue;
                }
                factory.get(stack, container);
                count++;
            } catch (Exception e) {
                logger.warn("Skipping container item check for an item", e);
            }
        }

        exporterState.flushEntityManager();
        logger.info("Finished processing item containers: {} items", count);
    }
}
