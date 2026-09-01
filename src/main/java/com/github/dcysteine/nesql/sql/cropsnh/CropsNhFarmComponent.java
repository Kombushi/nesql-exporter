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

/** One tiered Industrial Farm component block variant (e.g. a seed bed) with its tier. */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class CropsNhFarmComponent implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @ManyToOne
    private Item item;

    /** Fully-qualified Java class of the component block. */
    @Column(nullable = false)
    private String componentClass;

    private int tier;

    /** Needed by Hibernate. */
    protected CropsNhFarmComponent() {}

    public CropsNhFarmComponent(String id, Item item, String componentClass, int tier) {
        this.id = id;
        this.item = item;
        this.componentClass = componentClass;
        this.tier = tier;
    }
}
