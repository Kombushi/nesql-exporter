package com.github.dcysteine.nesql.sql.gregtech.worldgen;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/** A dimension that a GregTech small ore generates in. */
@Embeddable
@EqualsAndHashCode
@Getter
@ToString
public class GregTechSmallOreDimension implements Comparable<GregTechSmallOreDimension> {
    @Column(nullable = false)
    private String dimensionAbbreviation;

    /** Share of this small ore among all small ore placements in this dimension. */
    private double probability;

    /** Needed by Hibernate. */
    protected GregTechSmallOreDimension() {}

    public GregTechSmallOreDimension(String dimensionAbbreviation, double probability) {
        this.dimensionAbbreviation = dimensionAbbreviation;
        this.probability = probability;
    }

    @Override
    public int compareTo(@NotNull GregTechSmallOreDimension other) {
        return Comparator.comparing(GregTechSmallOreDimension::getDimensionAbbreviation)
                .compare(this, other);
    }
}