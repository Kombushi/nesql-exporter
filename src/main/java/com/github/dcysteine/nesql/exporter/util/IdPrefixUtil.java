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
    GREG_TECH_ORE_PREFIX("gtop"),
    GREG_TECH_ORE_DICT_UNIFICATION("gtodu"),
    GREG_TECH_UNIFICATION_BLACKLIST("gtub"),
    GREG_TECH_ITEM_DATA("gtid"),
    GREG_TECH_CATALYST("gtcat"),
    GREG_TECH_GENERATOR("gtgen"),
    GREG_TECH_DYNAMO("gtdyn"),
    GREG_TECH_TURBINE_ROTOR("gtrot"),
    GREG_TECH_MULTIBLOCK("gtmb"),
    GREG_TECH_LARGE_BOILER("gtlb"),
    GREG_TECH_COIL("gtcoil"),
    ITEM_CONTAINER("ic"),

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
