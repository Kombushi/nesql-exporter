package com.github.dcysteine.nesql.exporter.plugin.gregtech.catalyst;

import com.github.dcysteine.nesql.exporter.plugin.ExporterState;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.sql.Plugin;

/** Plugin which exports items GregTech machines use up by durability instead of consuming. */
public class GregTechCatalystPluginExporter extends PluginExporter {

    public GregTechCatalystPluginExporter(Plugin plugin, ExporterState exporterState) {
        super(plugin, exporterState);
    }

    @Override
    public void process() {
        new GregTechCatalystProcessor(this).process();
    }
}
