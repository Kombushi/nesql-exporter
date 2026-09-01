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

/**
 * Every registered machine with its Java class — including machines that serve no recipe
 * map (steam turbines, farm controllers) and so never appear on a recipe map's machine list.
 */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class GregTechMachine implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @ManyToOne
    private Item item;

    /** Fully-qualified Java class of the machine. */
    @Column(nullable = false)
    private String machineClass;

    /** Voltage tier; null for machines without a tier. */
    @Column
    private Integer tier;

    private boolean multiblock;

    /** True for steam-powered machines. */
    private boolean steam;

    /** Needed by Hibernate. */
    protected GregTechMachine() {}

    public GregTechMachine(
            String id, Item item, String machineClass, Integer tier, boolean multiblock,
            boolean steam) {
        this.id = id;
        this.item = item;
        this.machineClass = machineClass;
        this.tier = tier;
        this.multiblock = multiblock;
        this.steam = steam;
    }
}
