package com.github.dcysteine.nesql.sql.forge;

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
 * An item that leaves another item behind when used in a crafting recipe.
 *
 * <p>Sourced from Forge's container-item mechanism: tools leave their damaged selves, and
 * filled buckets leave the empty bucket.
 */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class ItemContainer implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @ManyToOne
    private Item item;

    @ManyToOne
    private Item containerItem;

    /** Needed by Hibernate. */
    protected ItemContainer() {}

    public ItemContainer(String id, Item item, Item containerItem) {
        this.id = id;
        this.item = item;
        this.containerItem = containerItem;
    }
}
