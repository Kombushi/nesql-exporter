package com.github.dcysteine.nesql.exporter.plugin.minecraft.blockdrops;

import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class BlockDropsProcessor extends PluginHelper {
    /** Metadata values 0-15 are the only ones storable in a placed block. */
    private static final int MAX_BLOCK_META = 16;

    public BlockDropsProcessor(PluginExporter exporter) {
        super(exporter);
    }

    public void process() {
        BlockDropFactory factory = new BlockDropFactory(exporter);

        int processed = 0;
        for (Object entry : Block.blockRegistry) {
            Block block = (Block) entry;
            String blockName = Block.blockRegistry.getNameForObject(block);
            if (blockName == null) {
                continue;
            }

            processBlock(factory, block, blockName);
            processed++;
        }

        exporterState.flushEntityManager();
        logger.info("Finished processing drops of {} blocks!", processed);
    }

    private void processBlock(BlockDropFactory factory, Block block, String blockName) {
        DropResult base = null;
        for (int meta = 0; meta < MAX_BLOCK_META; meta++) {
            DropResult result = computeDrop(block, meta);
            if (meta == 0) {
                base = result;
            } else if (result == null ? base == null : result.sameAs(base)) {
                // Collapse metas that drop exactly what meta 0 drops.
                continue;
            }
            if (result == null) {
                continue;
            }

            try {
                factory.get(block, blockName, meta, result.dropStack, result.quantity);
            } catch (Exception e) {
                logger.warn("Skipping drop of block {} meta {}", blockName, meta, e);
            }
        }
    }

    private DropResult computeDrop(Block block, int meta) {
        try {
            // Fixed seed keeps randomized drops reproducible across exports.
            Random random = new Random(31L * Block.getIdFromBlock(block) + meta);
            net.minecraft.item.Item dropped = block.getItemDropped(meta, random, 0);
            if (dropped == null) {
                return null;
            }

            int quantity = block.quantityDropped(random);
            if (quantity <= 0) {
                return null;
            }

            ItemStack dropStack = new ItemStack(dropped, 1, block.damageDropped(meta));
            if (dropStack.getItem() == null) {
                return null;
            }
            return new DropResult(dropStack, quantity);
        } catch (Exception e) {
            // Some modded blocks require a live world to compute drops; skip them.
            return null;
        }
    }

    private static final class DropResult {
        private final ItemStack dropStack;
        private final int quantity;

        private DropResult(ItemStack dropStack, int quantity) {
            this.dropStack = dropStack;
            this.quantity = quantity;
        }

        private boolean sameAs(DropResult other) {
            return other != null
                    && quantity == other.quantity
                    && dropStack.getItem() == other.dropStack.getItem()
                    && dropStack.getItemDamage() == other.dropStack.getItemDamage();
        }
    }
}
