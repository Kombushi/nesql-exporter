package com.github.dcysteine.nesql.exporter.plugin.gregtech.oredict;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.gregtech.oredict.GregTechUnificationBlacklist;

public class UnificationBlacklistFactory extends EntityFactory<GregTechUnificationBlacklist, String> {
    public UnificationBlacklistFactory(PluginExporter exporter) {
        super(exporter);
    }

    public GregTechUnificationBlacklist get(Item item) {
        String id = IdPrefixUtil.GREG_TECH_UNIFICATION_BLACKLIST.applyPrefix(item.getId());
        return findOrPersist(
                GregTechUnificationBlacklist.class, new GregTechUnificationBlacklist(id, item));
    }
}
