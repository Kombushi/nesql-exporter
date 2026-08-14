package com.github.dcysteine.nesql.sql.gregtech.worldgen;

import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/** One placeable ore block of a GregTech ore vein: a vein layer in a specific stone type. */
@Embeddable
@EqualsAndHashCode
@Getter
@ToString
public class GregTechOreVeinOre implements Comparable<GregTechOreVeinOre> {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GregTechOreVeinLayer veinLayer;

    @Column(nullable = false)
    private String materialName;

    @Column(nullable = false)
    private String stoneType;

    @ManyToOne
    private Item item;

    /** Needed by Hibernate. */
    protected GregTechOreVeinOre() {}

    public GregTechOreVeinOre(
            GregTechOreVeinLayer veinLayer, String materialName, String stoneType, Item item) {
        this.veinLayer = veinLayer;
        this.materialName = materialName;
        this.stoneType = stoneType;
        this.item = item;
    }

    @Override
    public int compareTo(@NotNull GregTechOreVeinOre other) {
        return Comparator.comparing(GregTechOreVeinOre::getVeinLayer)
                .thenComparing(GregTechOreVeinOre::getStoneType)
                .thenComparing(GregTechOreVeinOre::getItem)
                .compare(this, other);
    }
}