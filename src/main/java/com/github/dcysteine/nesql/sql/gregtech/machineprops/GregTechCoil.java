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

/** A heating coil casing with its heat capacity and coil level. */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class GregTechCoil implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @ManyToOne
    private Item item;

    /** Heat capacity used by heat-gated recipes (e.g. the Electric Blast Furnace). */
    private long heat;

    /** {@code HeatingCoilLevel} name, one distinct level per coil. */
    @Column(nullable = false)
    private String level;

    /** {@code HeatingCoilLevel} tier ordinal. */
    private int levelTier;

    /** Needed by Hibernate. */
    protected GregTechCoil() {}

    public GregTechCoil(String id, Item item, long heat, String level, int levelTier) {
        this.id = id;
        this.item = item;
        this.heat = heat;
        this.level = level;
        this.levelTier = levelTier;
    }
}
