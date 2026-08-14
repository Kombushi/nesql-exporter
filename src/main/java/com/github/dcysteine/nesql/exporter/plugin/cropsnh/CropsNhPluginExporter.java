package com.github.dcysteine.nesql.exporter.plugin.cropsnh;

import com.github.dcysteine.nesql.exporter.plugin.ExporterState;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.sql.Plugin;

/** Plugin which exports CropsNH crops: seeds, drops, tiers, and growth conditions. */
public class CropsNhPluginExporter extends PluginExporter {
    public CropsNhPluginExporter(Plugin plugin, ExporterState exporterState) {
        super(plugin, exporterState);
    }

    @Override
    public void process() {
        new CropsNhProcessor(this).process();
    }
}
