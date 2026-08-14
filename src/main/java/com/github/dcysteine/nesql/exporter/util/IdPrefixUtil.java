package com.github.dcysteine.nesql.exporter.util;

import com.google.common.base.Joiner;

import java.util.HashSet;
import java.util.Set;

/** Helper class for applying a unique prefix to all persisted entity IDs. */
public enum IdPrefixUtil {
    ITEM("i"),
    FLUID("f"),
    ITEM_GROUP("ig"),
    FLUID_GROUP("fg"),
    MOB("m"),
    RECIPE("r"),
    RECIPE_TYPE("rt"),

    ORE_DICTIONARY("od"),
    FLUID_BLOCK("fb"),
    FLUID_CONTAINER("fc"),
    EMPTY_CONTAINER("ec"),

    MOB_INFO("mi"),

    GREG_TECH_RECIPE("gtr"),
    GREG_TECH_DIMENSION("gtdim"),
    GREG_TECH_ORE_VEIN("gtov"),
    GREG_TECH_SMALL_ORE("gtso"),
    GREG_TECH_UNDERGROUND_FLUID("gtuf"),
    GREG_TECH_RECIPE_MAP("gtrm"),

    BLOCK_DROP("bd"),

    CROPSNH_CROP("cnh"),

    ASPECT("tca"),
    ASPECT_ENTRY("tcae"),

    QUEST("q"),
    QUEST_LINE("ql"),
    QUEST_TASK("qt"),
    QUEST_REWARD("qr"),
    ;

    private final String prefix;

    IdPrefixUtil(String... prefixParts) {
        prefix = Joiner.on(IdUtil.ID_SEPARATOR).join(prefixParts) + IdUtil.ID_SEPARATOR;
    }

    public String getPrefix() {
        return prefix;
    }

    public String applyPrefix(String... idParts) {
        return prefix + Joiner.on(IdUtil.ID_SEPARATOR).join(idParts);
    }

    static {
        Set<String> prefixes = new HashSet<>();
        for (IdPrefixUtil prefix : IdPrefixUtil.values()) {
            if (!prefixes.add(prefix.getPrefix())) {
                throw new IllegalStateException("Duplicate prefix: " + prefix);
            }
        }
    }
}
