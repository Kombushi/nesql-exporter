package com.github.dcysteine.nesql.sql.gregtech.worldgen;

import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/** An item that mining a GregTech small ore block can drop. */
@Embeddable
@EqualsAndHashCode
@Getter
@ToString
public class GregTechSmallOreDrop implements Comparable<GregTechSmallOreDrop> {
    @ManyToOne
    private Item item;

    /** Needed by Hibernate. */
    protected GregTechSmallOreDrop() {}

    public GregTechSmallOreDrop(Item item) {
        this.item = item;
    }

    @Override
    public int compareTo(@NotNull GregTechSmallOreDrop other) {
        return Comparator.comparing(GregTechSmallOreDrop::getItem).compare(this, other);
    }
}