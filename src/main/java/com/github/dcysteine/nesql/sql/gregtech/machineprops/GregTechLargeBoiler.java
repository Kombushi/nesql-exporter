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

/** A large boiler controller; steam output derives from its EU/t rating. */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class GregTechLargeBoiler implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @ManyToOne
    private Item item;

    private int euT;

    /** Warm-up rate; ticks-to-full-efficiency behavior differs per boiler generation. */
    private int efficiencyIncrease;

    /** Needed by Hibernate. */
    protected GregTechLargeBoiler() {}

    public GregTechLargeBoiler(String id, Item item, int euT, int efficiencyIncrease) {
        this.id = id;
        this.item = item;
        this.euT = euT;
        this.efficiencyIncrease = efficiencyIncrease;
    }
}
