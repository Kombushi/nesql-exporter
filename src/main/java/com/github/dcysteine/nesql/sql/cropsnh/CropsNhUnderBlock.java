package com.github.dcysteine.nesql.sql.cropsnh;

import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

/** One block accepted under the crop by its block-under growth requirement. */
@Embeddable
@EqualsAndHashCode
@Getter
@ToString
public class CropsNhUnderBlock implements Comparable<CropsNhUnderBlock> {
    @ManyToOne
    private Item item;

    /** Needed by Hibernate. */
    protected CropsNhUnderBlock() {}

    public CropsNhUnderBlock(Item item) {
        this.item = item;
    }

    @Override
    public int compareTo(@NotNull CropsNhUnderBlock other) {
        return item.compareTo(other.item);
    }
}
