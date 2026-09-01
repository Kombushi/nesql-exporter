package com.github.dcysteine.nesql.exporter.plugin.cropsnh;

import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.gtnewhorizon.cropsnh.api.ICropCard;
import com.gtnewhorizon.cropsnh.blocks.abstracts.CropsNHBlockIndustrialFarmTiredComponent;
import com.gtnewhorizon.cropsnh.farming.registries.CropRegistry;
import com.gtnewhorizon.cropsnh.farming.registries.FertilizerRegistry;
import com.gtnewhorizon.cropsnh.farming.registries.FluidPotencyRegistry;
import com.gtnewhorizon.cropsnh.farming.registries.ItemPotencyRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import java.lang.reflect.Field;
import java.util.Map;

public class CropsNhProcessor extends PluginHelper {
    public CropsNhProcessor(PluginExporter exporter) {
        super(exporter);
    }

    public void process() {
        CropFactory factory = new CropFactory(exporter);

        logger.info(
                "Processing {} crops...", CropRegistry.instance.getAllInRegistrationOrder().size());
        for (ICropCard card : CropRegistry.instance.getAllInRegistrationOrder()) {
            try {
                factory.get(card);
            } catch (Exception e) {
                logger.warn("Skipping crop {} that failed to export", card.getId(), e);
            }
        }

        processFertilizers();
        processFarmComponents();

        exporterState.flushEntityManager();
        logger.info("Finished processing CropsNH crops!");
    }

    // Deliberately without a catch: a registry shape change must fail the export.
    private void processFertilizers() {
        FertilizerItemFactory itemFertilizerFactory = new FertilizerItemFactory(exporter);
        FertilizerFluidFactory fluidFertilizerFactory = new FertilizerFluidFactory(exporter);

        ItemPotencyRegistry items =
                (ItemPotencyRegistry) readField(FertilizerRegistry.instance, "itemRegistry");
        int[] itemCount = {0};
        items.registry.getStream().forEach(entry -> {
            int meta = entry.meta == null ? 0 : entry.meta;
            itemFertilizerFactory.get(new ItemStack(entry.key, 1, meta), entry.value);
            itemCount[0]++;
        });

        FluidPotencyRegistry fluids =
                (FluidPotencyRegistry) readField(FertilizerRegistry.instance, "fluidRegistry");
        int fluidCount = 0;
        for (Map.Entry<Fluid, Integer> entry : fluids.registry.entrySet()) {
            fluidFertilizerFactory.get(entry.getKey(), entry.getValue());
            fluidCount++;
        }

        logger.info("Processed {} item and {} fluid fertilizers", itemCount[0], fluidCount);
    }

    private void processFarmComponents() {
        FarmComponentFactory factory = new FarmComponentFactory(exporter);

        int components = 0;
        for (Object registered : Block.blockRegistry) {
            if (!(registered instanceof CropsNHBlockIndustrialFarmTiredComponent)) {
                continue;
            }

            CropsNHBlockIndustrialFarmTiredComponent block =
                    (CropsNHBlockIndustrialFarmTiredComponent) registered;
            if (Item.getItemFromBlock(block) == null) {
                continue;
            }
            for (int meta = 0; meta < 16; meta++) {
                Integer tier = block.getTier(meta);
                if (tier == null) {
                    continue;
                }

                factory.get(new ItemStack(block, 1, meta), block.getClass().getName(), tier);
                components++;
            }
        }

        logger.info("Processed {} farm component variants", components);
    }

    private static Object readField(Object target, String fieldName) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(
                    "Fertilizer registry reflection failed: " + fieldName, e);
        }
    }
}
