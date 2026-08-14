package com.github.dcysteine.nesql.exporter.plugin.minecraft.blockdrops;

import com.github.dcysteine.nesql.exporter.plugin.ExporterState;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.sql.Plugin;

/** Plugin which exports what each block drops when broken. */
public class BlockDropsPluginExporter extends PluginExporter {
    public BlockDropsPluginExporter(Plugin plugin, ExporterState exporterState) {
        super(plugin, exporterState);
    }

    @Override
    public void process() {
        new BlockDropsProcessor(this).process();
    }
}
