package com.github.dcysteine.nesql.sql.gregtech.machineprops;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/** A single-block fuel-burning generator and its conversion stats. */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class GregTechGenerator implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @ManyToOne
    private Item item;

    /** Fuel-to-EU conversion efficiency in percent; may exceed 100 (naquadah reactors). */
    private double efficiency;

    /** Output voltage per amp, in EU/t. */
    private long maxEuOutput;

    private long amperesOut;

    /** Needed by Hibernate. */
    protected GregTechGenerator() {}

    public GregTechGenerator(
            String id, Item item, double efficiency, long maxEuOutput, long amperesOut) {
        this.id = id;
        this.item = item;
        this.efficiency = efficiency;
        this.maxEuOutput = maxEuOutput;
        this.amperesOut = amperesOut;
    }
}
