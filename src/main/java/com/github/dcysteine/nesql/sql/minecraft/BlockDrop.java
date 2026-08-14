package com.github.dcysteine.nesql.sql.minecraft;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/** What one block, at one metadata value, drops when broken without silk touch or fortune. */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class BlockDrop implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    /** Block registry name, e.g. {@code "minecraft:clay"}. */
    @Column(nullable = false)
    private String blockName;

    private int blockMeta;

    /** Item form of the block itself; null for blocks without an item form. */
    @ManyToOne
    private Item blockItem;

    @ManyToOne
    private Item drop;

    private int quantity;

    /** Needed by Hibernate. */
    protected BlockDrop() {}

    public BlockDrop(
            String id, String blockName, int blockMeta, Item blockItem, Item drop, int quantity) {
        this.id = id;
        this.blockName = blockName;
        this.blockMeta = blockMeta;
        this.blockItem = blockItem;
        this.drop = drop;
        this.quantity = quantity;
    }
}