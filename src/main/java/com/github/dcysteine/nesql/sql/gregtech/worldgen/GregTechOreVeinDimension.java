package com.github.dcysteine.nesql.sql.gregtech.worldgen;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/** A dimension that a GregTech ore vein generates in. */
@Embeddable
@EqualsAndHashCode
@Getter
@ToString
public class GregTechOreVeinDimension implements Comparable<GregTechOreVeinDimension> {
    @Column(nullable = false)
    private String dimensionAbbreviation;

    /** Probability of this vein being picked among all veins enabled in this dimension. */
    private double probability;

    /** Height range in this dimension, with any per-dimension override already applied. */
    private int minY;

    private int maxY;

    /** Needed by Hibernate. */
    protected GregTechOreVeinDimension() {}

    public GregTechOreVeinDimension(
            String dimensionAbbreviation, double probability, int minY, int maxY) {
        this.dimensionAbbreviation = dimensionAbbreviation;
        this.probability = probability;
        this.minY = minY;
        this.maxY = maxY;
    }

    @Override
    public int compareTo(@NotNull GregTechOreVeinDimension other) {
        return Comparator.comparing(GregTechOreVeinDimension::getDimensionAbbreviation)
                .compare(this, other);
    }
}