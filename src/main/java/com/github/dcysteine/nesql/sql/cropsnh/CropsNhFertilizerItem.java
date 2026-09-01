package com.github.dcysteine.nesql.sql.cropsnh;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/** An item fertilizer with its per-use potency, from the CropsNH fertilizer registry. */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class CropsNhFertilizerItem implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @ManyToOne
    private Item item;

    private int potency;

    /** Needed by Hibernate. */
    protected CropsNhFertilizerItem() {}

    public CropsNhFertilizerItem(String id, Item item, int potency) {
        this.id = id;
        this.item = item;
        this.potency = potency;
    }
}
