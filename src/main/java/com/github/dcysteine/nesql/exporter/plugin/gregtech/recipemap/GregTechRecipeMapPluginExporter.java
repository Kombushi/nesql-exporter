package com.github.dcysteine.nesql.exporter.plugin.gregtech.recipemap;

import com.github.dcysteine.nesql.exporter.plugin.ExporterState;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.sql.Plugin;

/** Plugin which exports GT5 recipe maps and the machines that serve them. */
public class GregTechRecipeMapPluginExporter extends PluginExporter {
    public GregTechRecipeMapPluginExporter(Plugin plugin, ExporterState exporterState) {
        super(plugin, exporterState);
    }

    @Override
    public void process() {
        new GregTechRecipeMapProcessor(this).process();
    }
}