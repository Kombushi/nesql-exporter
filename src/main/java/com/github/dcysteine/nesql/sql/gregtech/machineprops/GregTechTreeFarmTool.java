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

/** A tool the Tree Growth Simulator accepts for one output mode, with its output multiplier. */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class GregTechTreeFarmTool implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @ManyToOne
    private Item item;

    /** {@code MTETreeFarm.Mode} name: LOG, SAPLING, LEAVES or FRUIT. */
    @Column(nullable = false)
    private String mode;

    private int multiplier;

    /** Needed by Hibernate. */
    protected GregTechTreeFarmTool() {}

    public GregTechTreeFarmTool(String id, Item item, String mode, int multiplier) {
        this.id = id;
        this.item = item;
        this.mode = mode;
        this.multiplier = multiplier;
    }
}
