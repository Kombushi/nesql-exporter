package com.github.dcysteine.nesql.sql.cropsnh;

import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

/** An additional item that plants as this crop, e.g. the crop's berry. */
@Embeddable
@EqualsAndHashCode
@Getter
@ToString
public class CropsNhAlternateSeed implements Comparable<CropsNhAlternateSeed> {
    @ManyToOne
    private Item item;

    /** Needed by Hibernate. */
    protected CropsNhAlternateSeed() {}

    public CropsNhAlternateSeed(Item item) {
        this.item = item;
    }

    @Override
    public int compareTo(@NotNull CropsNhAlternateSeed other) {
        return item.compareTo(other.item);
    }
}