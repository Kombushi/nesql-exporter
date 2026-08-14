package com.github.dcysteine.nesql.sql.gregtech.recipemap;

import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/** One machine that can run a recipe map's recipes. */
@Embeddable
@EqualsAndHashCode
@Getter
@ToString
public class GregTechRecipeMapMachine implements Comparable<GregTechRecipeMapMachine> {
    @ManyToOne
    private Item item;

    /** Voltage tier of the machine; null for machines without a tier. */
    @Column
    private Integer tier;

    private boolean multiblock;

    /** Needed by Hibernate. */
    protected GregTechRecipeMapMachine() {}

    public GregTechRecipeMapMachine(Item item, Integer tier, boolean multiblock) {
        this.item = item;
        this.tier = tier;
        this.multiblock = multiblock;
    }

    @Override
    public int compareTo(@NotNull GregTechRecipeMapMachine other) {
        return Comparator.comparing(GregTechRecipeMapMachine::isMultiblock)
                .thenComparing(
                        GregTechRecipeMapMachine::getTier,
                        Comparator.nullsFirst(Comparator.naturalOrder()))
                .thenComparing(GregTechRecipeMapMachine::getItem)
                .compare(this, other);
    }
}