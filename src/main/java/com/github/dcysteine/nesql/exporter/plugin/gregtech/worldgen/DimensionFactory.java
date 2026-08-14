package com.github.dcysteine.nesql.exporter.plugin.gregtech.worldgen;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.gregtech.worldgen.GregTechDimension;
import gtneioreplugin.util.DimensionHelper;

import java.util.List;
import java.util.stream.Collectors;

public class DimensionFactory extends EntityFactory<GregTechDimension, String> {
    public DimensionFactory(PluginExporter exporter) {
        super(exporter);
    }

    public GregTechDimension get(DimensionHelper.Dimension dimension) {
        String id = IdPrefixUtil.GREG_TECH_DIMENSION.applyPrefix(dimension.abbr());

        List<String> stoneTypes =
                dimension.stoneTypes().stream().map(Enum::name).collect(Collectors.toList());

        GregTechDimension entity =
                new GregTechDimension(
                        id,
                        dimension.abbr(),
                        dimension.fullName(),
                        dimension.internalName(),
                        parseRocketTier(dimension.tierKey()),
                        stoneTypes);
        return findOrPersist(GregTechDimension.class, entity);
    }

    /** Tier keys look like {@code "gtnop.tier.4"}. */
    private static int parseRocketTier(String tierKey) {
        return Integer.parseInt(tierKey.substring(tierKey.lastIndexOf('.') + 1));
    }
}
