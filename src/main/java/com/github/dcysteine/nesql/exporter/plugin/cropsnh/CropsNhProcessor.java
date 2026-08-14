package com.github.dcysteine.nesql.exporter.plugin.cropsnh;

import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.gtnewhorizon.cropsnh.api.ICropCard;
import com.gtnewhorizon.cropsnh.farming.registries.CropRegistry;

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

        exporterState.flushEntityManager();
        logger.info("Finished processing CropsNH crops!");
    }
}
