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

/** A dynamo hatch; a pure output-capacity constraint, GT dynamos have no conversion loss. */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class GregTechDynamo implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @ManyToOne
    private Item item;

    /** Output voltage per amp, in EU/t. */
    private long maxEuOutput;

    private long amperesOut;

    private long maxEuStore;

    /** Needed by Hibernate. */
    protected GregTechDynamo() {}

    public GregTechDynamo(
            String id, Item item, long maxEuOutput, long amperesOut, long maxEuStore) {
        this.id = id;
        this.item = item;
        this.maxEuOutput = maxEuOutput;
        this.amperesOut = amperesOut;
        this.maxEuStore = maxEuStore;
    }
}
