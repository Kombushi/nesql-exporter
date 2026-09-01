package com.github.dcysteine.nesql.sql.gregtech.machineprops;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * One fluid a reactor drinks each second: an EXCITED liquid multiplies fuel draw and output
 * by its factor, a COOLANT scales output by its factor as a percentage, and the UPKEEP fluid
 * is mandatory with no factor.
 */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class GregTechReactorMode implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @ManyToOne
    private Item machine;

    @Column(nullable = false)
    private String kind;

    @ManyToOne
    private Fluid fluid;

    /** Liters consumed per second. */
    private int amount;

    /** Output multiplier (EXCITED) or output percentage (COOLANT); null for UPKEEP. */
    @Column
    private Integer factor;

    /** Needed by Hibernate. */
    protected GregTechReactorMode() {}

    public GregTechReactorMode(
            String id, Item machine, String kind, Fluid fluid, int amount, Integer factor) {
        this.id = id;
        this.machine = machine;
        this.kind = kind;
        this.fluid = fluid;
        this.amount = amount;
        this.factor = factor;
    }
}
