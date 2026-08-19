package com.github.dcysteine.nesql.sql.gregtech.itemdata;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/** One byproduct material GT's recycling yields beside an item's primary material. */
@Embeddable
@EqualsAndHashCode
@Getter
@ToString
public class GregTechItemDataByProduct {
    @Column(nullable = false)
    private String materialName;

    private long amount;

    /** Needed by Hibernate. */
    protected GregTechItemDataByProduct() {}

    public GregTechItemDataByProduct(String materialName, long amount) {
        this.materialName = materialName;
        this.amount = amount;
    }
}
