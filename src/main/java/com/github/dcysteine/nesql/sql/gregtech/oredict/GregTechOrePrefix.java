package com.github.dcysteine.nesql.sql.gregtech.oredict;

import com.github.dcysteine.nesql.sql.Identifiable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/** One GregTech ore prefix and its unification-relevant properties. */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class GregTechOrePrefix implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    /** Prefix key as used in oredict names, e.g. {@code "ingot"}. */
    @Column(nullable = false)
    private String name;

    /** True if GT unifies items registered under this prefix. */
    private boolean unifiable;

    /** True if the prefix is its own material, e.g. cobblestone. */
    private boolean selfReferencing;

    /** True if items under this prefix are made of their material. */
    private boolean materialBased;

    /** True if the item merely contains its material, e.g. cells. */
    private boolean container;

    /** True if GT recycling recovers this prefix's material. */
    private boolean recyclable;

    /** Material units contained in one item; 3628800 units equal one ingot. */
    private long materialAmount;

    /** Needed by Hibernate. */
    protected GregTechOrePrefix() {}

    public GregTechOrePrefix(
            String id, String name, boolean unifiable, boolean selfReferencing,
            boolean materialBased, boolean container, boolean recyclable, long materialAmount) {
        this.id = id;
        this.name = name;
        this.unifiable = unifiable;
        this.selfReferencing = selfReferencing;
        this.materialBased = materialBased;
        this.container = container;
        this.recyclable = recyclable;
        this.materialAmount = materialAmount;
    }
}
