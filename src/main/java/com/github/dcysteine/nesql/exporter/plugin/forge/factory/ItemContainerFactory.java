package com.github.dcysteine.nesql.exporter.plugin.forge.factory;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.forge.ItemContainer;
import net.minecraft.item.ItemStack;

public class ItemContainerFactory extends EntityFactory<ItemContainer, String> {
    private final ItemFactory itemFactory;

    public ItemContainerFactory(PluginExporter exporter) {
        super(exporter);
        itemFactory = new ItemFactory(exporter);
    }

    public ItemContainer get(ItemStack stack, ItemStack containerStack) {
        Item item = itemFactory.get(stack);
        Item containerItem = itemFactory.get(containerStack);

        String id = IdPrefixUtil.ITEM_CONTAINER.applyPrefix(item.getId());

        ItemContainer entity = new ItemContainer(id, item, containerItem);
        return findOrPersist(ItemContainer.class, entity);
    }
}
