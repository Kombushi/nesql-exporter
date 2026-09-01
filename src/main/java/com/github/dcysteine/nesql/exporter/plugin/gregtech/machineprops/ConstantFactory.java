package com.github.dcysteine.nesql.exporter.plugin.gregtech.machineprops;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.gregtech.machineprops.GregTechConstant;

public class ConstantFactory extends EntityFactory<GregTechConstant, String> {
    public ConstantFactory(PluginExporter exporter) {
        super(exporter);
    }

    public GregTechConstant get(String name, long value, String source) {
        String id = IdPrefixUtil.GREG_TECH_CONSTANT.applyPrefix(name);
        GregTechConstant entity = new GregTechConstant(id, name, value, source);
        return findOrPersist(GregTechConstant.class, entity);
    }
}
