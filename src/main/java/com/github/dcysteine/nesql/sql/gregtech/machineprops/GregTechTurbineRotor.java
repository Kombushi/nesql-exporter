package com.github.dcysteine.nesql.sql.gregtech.machineprops;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.SortNatural;

import java.util.SortedSet;
import java.util.TreeSet;

/** One turbine rotor variant (size × material) with its computed large-turbine stats. */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class GregTechTurbineRotor implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @ManyToOne
    private Item item;

    /** Rotor size: {@code SMALL}, {@code NORMAL}, {@code LARGE}, or {@code HUGE}. */
    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    private String materialName;

    private long maxDurability;

    private double baseEfficiency;

    private int overflowEfficiency;

    @ElementCollection
    @SortNatural
    private SortedSet<GregTechTurbineRotorFuelStats> fuelStats;

    /** Needed by Hibernate. */
    protected GregTechTurbineRotor() {}

    public GregTechTurbineRotor(
            String id, Item item, String size, String materialName, long maxDurability,
            double baseEfficiency, int overflowEfficiency) {
        this.id = id;
        this.item = item;
        this.size = size;
        this.materialName = materialName;
        this.maxDurability = maxDurability;
        this.baseEfficiency = baseEfficiency;
        this.overflowEfficiency = overflowEfficiency;

        fuelStats = new TreeSet<>();
    }

    public void addFuelStats(GregTechTurbineRotorFuelStats stats) {
        fuelStats.add(stats);
    }
}
