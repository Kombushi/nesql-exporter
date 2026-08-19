package com.github.dcysteine.nesql.exporter.plugin.gregtech.oredict;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.gregtech.oredict.GregTechOreDictUnification;

public class UnificationFactory extends EntityFactory<GregTechOreDictUnification, String> {
    public UnificationFactory(PluginExporter exporter) {
        super(exporter);
    }

    public GregTechOreDictUnification get(String name, Item target) {
        String id = IdPrefixUtil.GREG_TECH_ORE_DICT_UNIFICATION.applyPrefix(name);
        return findOrPersist(
                GregTechOreDictUnification.class, new GregTechOreDictUnification(id, name, target));
    }
}
