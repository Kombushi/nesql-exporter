package com.github.dcysteine.nesql.exporter.plugin.gregtech.oredict;

import com.github.dcysteine.nesql.exporter.plugin.ExporterState;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.sql.Plugin;

/** Plugin which exports GT5's ore prefixes and oredict unification verdicts. */
public class GregTechOreDictPluginExporter extends PluginExporter {
    public GregTechOreDictPluginExporter(Plugin plugin, ExporterState exporterState) {
        super(plugin, exporterState);
    }

    @Override
    public void process() {
        new GregTechOreDictProcessor(this).process();
    }
}
