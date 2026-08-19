package com.github.dcysteine.nesql.sql;

import com.google.common.collect.ImmutableList;

import java.util.Arrays;

public enum Plugin {
    BASE("base"),
    MINECRAFT("minecraft"),
    NEI("nei"),
    FORGE("forge"),

    MOBS_INFO("mobsinfo"),

    AVARITIA("avaritia"),
    GREGTECH("gregtech"),
    GREGTECH_WORLDGEN("gregtech_worldgen"),
    GREGTECH_RECIPE_MAP("gregtech_recipe_map"),
    GREGTECH_ORE_DICT("gregtech_oredict"),
    THAUMCRAFT("thaumcraft"),
    QUEST("quest"),
    BLOCK_DROPS("block_drops"),
    CROPSNH("cropsnh"),
    ;

    public static final ImmutableList<String> NAMES;

    static {
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        Arrays.stream(values()).map(Plugin::getName).forEach(builder::add);
        NAMES = builder.build();
    }

    private final String name;

    Plugin(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
