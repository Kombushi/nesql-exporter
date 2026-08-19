package com.github.dcysteine.nesql.exporter.plugin.gregtech.catalyst;

import codechicken.nei.ItemList;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import gregtech.api.util.GTUtility;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIsaMill;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.chemplant.MTEChemicalPlant;
import net.minecraft.item.ItemStack;

public class GregTechCatalystProcessor extends PluginHelper {
    public GregTechCatalystProcessor(PluginExporter exporter) {
        super(exporter);
    }

    public void process() {
        CatalystFactory factory = new CatalystFactory(exporter);
        logger.info("Scanning {} items for GregTech catalysts...", ItemList.items.size());
        int chemPlant = 0;
        int millingBalls = 0;
        for (ItemStack stack : ItemList.items) {
            try {
                if (stack == null || stack.getItem() == null) {
                    continue;
                }
                if (MTEChemicalPlant.CHEMPLANT_CATALYSTS.contains(
                        GTUtility.ItemId.createWithoutNBT(stack))) {
                    factory.get(stack, "chemical_plant");
                    chemPlant++;
                }
                if (MTEIsaMill.isMillingBall(stack)) {
                    factory.get(stack, "milling_ball");
                    millingBalls++;
                }
            } catch (Exception e) {
                logger.warn("Skipping catalyst check for an item", e);
            }
        }

        exporterState.flushEntityManager();
        logger.info(
                "Finished processing GregTech catalysts: {} chemical plant, {} milling balls",
                chemPlant, millingBalls);
    }
}
