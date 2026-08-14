package com.github.dcysteine.nesql.sql.cropsnh;

import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/** One weighted entry of a crop's harvest drop table. */
@Embeddable
@EqualsAndHashCode
@Getter
@ToString
public class CropsNhCropDrop implements Comparable<CropsNhCropDrop> {
    @ManyToOne
    private Item item;

    /** Selection weight of this entry within the crop's drop table. */
    private int weight;

    /** Needed by Hibernate. */
    protected CropsNhCropDrop() {}

    public CropsNhCropDrop(Item item, int weight) {
        this.item = item;
        this.weight = weight;
    }

    @Override
    public int compareTo(@NotNull CropsNhCropDrop other) {
        return Comparator.comparing(CropsNhCropDrop::getItem)
                .thenComparing(CropsNhCropDrop::getWeight)
                .compare(this, other);
    }
}