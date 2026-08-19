package com.github.dcysteine.nesql.exporter.plugin.gregtech.itemdata;

import com.github.dcysteine.nesql.exporter.plugin.ExporterState;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.sql.Plugin;

/** Plugin which exports GregTech's per-item material composition data. */
public class GregTechItemDataPluginExporter extends PluginExporter {

    public GregTechItemDataPluginExporter(Plugin plugin, ExporterState exporterState) {
        super(plugin, exporterState);
    }

    @Override
    public void process() {
        new GregTechItemDataProcessor(this).process();
    }
}
