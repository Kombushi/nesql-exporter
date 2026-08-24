package com.github.dcysteine.nesql.exporter.plugin.gregtech.machineprops;

import com.github.dcysteine.nesql.exporter.plugin.ExporterState;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.sql.Plugin;

/** Plugin which exports GT5 machine properties: generators, dynamos, and turbine rotors. */
public class GregTechMachinePropsPluginExporter extends PluginExporter {
    public GregTechMachinePropsPluginExporter(Plugin plugin, ExporterState exporterState) {
        super(plugin, exporterState);
    }

    @Override
    public void process() {
        new MachinePropsProcessor(this).process();
    }
}
