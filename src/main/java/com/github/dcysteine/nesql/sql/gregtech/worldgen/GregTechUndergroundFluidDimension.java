package com.github.dcysteine.nesql.sql.gregtech.worldgen;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/** Presence of an underground fluid in one dimension. */
@Embeddable
@EqualsAndHashCode
@Getter
@ToString
public class GregTechUndergroundFluidDimension
        implements Comparable<GregTechUndergroundFluidDimension> {
    @Column(nullable = false)
    private String dimensionAbbreviation;

    /** Probability that a given chunk's underground fluid is this fluid. */
    private double probability;

    private int minAmount;

    private int maxAmount;

    /** Needed by Hibernate. */
    protected GregTechUndergroundFluidDimension() {}

    public GregTechUndergroundFluidDimension(
            String dimensionAbbreviation, double probability, int minAmount, int maxAmount) {
        this.dimensionAbbreviation = dimensionAbbreviation;
        this.probability = probability;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    @Override
    public int compareTo(@NotNull GregTechUndergroundFluidDimension other) {
        return Comparator.comparing(GregTechUndergroundFluidDimension::getDimensionAbbreviation)
                .compare(this, other);
    }
}