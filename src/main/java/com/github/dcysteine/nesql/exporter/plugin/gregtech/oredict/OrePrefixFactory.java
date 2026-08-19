package com.github.dcysteine.nesql.exporter.plugin.gregtech.oredict;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.gregtech.oredict.GregTechOrePrefix;
import gregtech.api.enums.OrePrefixes;

public class OrePrefixFactory extends EntityFactory<GregTechOrePrefix, String> {
    public OrePrefixFactory(PluginExporter exporter) {
        super(exporter);
    }

    public GregTechOrePrefix get(OrePrefixes prefix) {
        String id = IdPrefixUtil.GREG_TECH_ORE_PREFIX.applyPrefix(prefix.getName());

        GregTechOrePrefix entity =
                new GregTechOrePrefix(
                        id, prefix.getName(), prefix.isUnifiable(), prefix.isSelfReferencing(),
                        prefix.isMaterialBased(), prefix.isContainer(), prefix.isRecyclable(),
                        prefix.getMaterialAmount());

        return findOrPersist(GregTechOrePrefix.class, entity);
    }
}
