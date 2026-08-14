package com.github.dcysteine.nesql.exporter.plugin.gregtech.worldgen;

import com.github.dcysteine.nesql.exporter.plugin.ExporterState;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.sql.Plugin;

/** Plugin which exports GT5 worldgen: dimensions, ore veins, small ores, underground fluids. */
public class GregTechWorldgenPluginExporter extends PluginExporter {
    public GregTechWorldgenPluginExporter(Plugin plugin, ExporterState exporterState) {
        super(plugin, exporterState);
    }

    @Override
    public void process() {
        new GregTechWorldgenProcessor(this).process();
    }
}
