package com.github.dcysteine.nesql.sql.gregtech.machineprops;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 * Per-fuel-class rotor stats. Flow is L/t for {@code STEAM} and EU/t of fuel value for
 * {@code GAS} and {@code PLASMA}; efficiencies are fractions.
 */
@Embeddable
@EqualsAndHashCode
@Getter
@ToString
public class GregTechTurbineRotorFuelStats
        implements Comparable<GregTechTurbineRotorFuelStats> {
    /** Fuel class: {@code STEAM}, {@code GAS}, or {@code PLASMA}. */
    @Column(nullable = false)
    private String fuel;

    private double efficiency;

    private double looseEfficiency;

    private double optimalFlow;

    private double looseOptimalFlow;

    private double optimalEut;

    private double looseOptimalEut;

    /** Needed by Hibernate. */
    protected GregTechTurbineRotorFuelStats() {}

    public GregTechTurbineRotorFuelStats(
            String fuel, double efficiency, double looseEfficiency, double optimalFlow,
            double looseOptimalFlow, double optimalEut, double looseOptimalEut) {
        this.fuel = fuel;
        this.efficiency = efficiency;
        this.looseEfficiency = looseEfficiency;
        this.optimalFlow = optimalFlow;
        this.looseOptimalFlow = looseOptimalFlow;
        this.optimalEut = optimalEut;
        this.looseOptimalEut = looseOptimalEut;
    }

    @Override
    public int compareTo(@NotNull GregTechTurbineRotorFuelStats other) {
        return Comparator.comparing(GregTechTurbineRotorFuelStats::getFuel)
                .compare(this, other);
    }
}
