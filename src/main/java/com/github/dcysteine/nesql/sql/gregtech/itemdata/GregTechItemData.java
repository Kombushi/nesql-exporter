package com.github.dcysteine.nesql.sql.gregtech.itemdata;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderColumn;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * GregTech's material composition for one item.
 *
 * <p>Sourced from {@code GTOreDictUnificator}'s item-data map: the same data GT's own
 * recycling uses to decide what an item smelts or grinds back into.
 */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class GregTechItemData implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @ManyToOne
    private Item item;

    /** Ore prefix name, e.g. {@code "ingot"}; null where GT records no prefix. */
    private String prefixName;

    /** Primary material name, e.g. {@code "Iron"}. */
    @Column(nullable = false)
    private String materialName;

    /** Primary material amount; {@code 3628800} is one ingot. */
    private long materialAmount;

    @ElementCollection
    @OrderColumn
    private List<GregTechItemDataByProduct> byProducts;

    /** Needed by Hibernate. */
    protected GregTechItemData() {}

    public GregTechItemData(
            String id, Item item, String prefixName, String materialName, long materialAmount,
            List<GregTechItemDataByProduct> byProducts) {
        this.id = id;
        this.item = item;
        this.prefixName = prefixName;
        this.materialName = materialName;
        this.materialAmount = materialAmount;
        this.byProducts = new ArrayList<>(byProducts);
    }
}
