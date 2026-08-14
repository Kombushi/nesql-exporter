package com.github.dcysteine.nesql.exporter.plugin.minecraft.blockdrops;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.minecraft.BlockDrop;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class BlockDropFactory extends EntityFactory<BlockDrop, String> {
    private final ItemFactory itemFactory;

    public BlockDropFactory(PluginExporter exporter) {
        super(exporter);
        this.itemFactory = new ItemFactory(exporter);
    }

    public BlockDrop get(
            Block block, String blockName, int meta, ItemStack dropStack, int quantity) {
        String id = IdPrefixUtil.BLOCK_DROP.applyPrefix(blockName, Integer.toString(meta));

        BlockDrop blockDrop =
                new BlockDrop(
                        id, blockName, meta, getBlockItem(block, meta),
                        itemFactory.get(dropStack), quantity);

        return findOrPersist(BlockDrop.class, blockDrop);
    }

    private Item getBlockItem(Block block, int meta) {
        net.minecraft.item.Item blockItem = net.minecraft.item.Item.getItemFromBlock(block);
        if (blockItem == null) {
            return null;
        }

        try {
            // Metadata only maps onto item damage for items that actually have subtypes.
            ItemStack stack = new ItemStack(blockItem, 1, blockItem.getHasSubtypes() ? meta : 0);
            return itemFactory.get(stack);
        } catch (Exception e) {
            logger.warn("Failed to export item form of block {} meta {}", blockName(block), meta);
            return null;
        }
    }

    private static String blockName(Block block) {
        return Block.blockRegistry.getNameForObject(block);
    }
}
