package com.github.dcysteine.nesql.sql.gregtech.worldgen;

import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/** A placeable small ore block variant in a specific stone type. */
@Embeddable
@EqualsAndHashCode
@Getter
@ToString
public class GregTechSmallOreBlock implements Comparable<GregTechSmallOreBlock> {
    @Column(nullable = false)
    private String stoneType;

    @ManyToOne
    private Item item;

    /** Needed by Hibernate. */
    protected GregTechSmallOreBlock() {}

    public GregTechSmallOreBlock(String stoneType, Item item) {
        this.stoneType = stoneType;
        this.item = item;
    }

    @Override
    public int compareTo(@NotNull GregTechSmallOreBlock other) {
        return Comparator.comparing(GregTechSmallOreBlock::getStoneType)
                .thenComparing(GregTechSmallOreBlock::getItem)
                .compare(this, other);
    }
}