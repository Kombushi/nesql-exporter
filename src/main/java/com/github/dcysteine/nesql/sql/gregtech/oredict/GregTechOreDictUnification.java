package com.github.dcysteine.nesql.sql.gregtech.oredict;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * One oredict name GT unifies, with the canonical item GT substitutes for it.
 *
 * <p>Sourced from {@code GTOreDictUnificator}'s name-to-stack map: a name appears here iff
 * GT treats its members as one material form.
 */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class GregTechOreDictUnification implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    /** Oredict name, e.g. {@code "ingotIron"}. */
    @Column(nullable = false)
    private String name;

    @ManyToOne
    private Item target;

    /** Needed by Hibernate. */
    protected GregTechOreDictUnification() {}

    public GregTechOreDictUnification(String id, String name, Item target) {
        this.id = id;
        this.name = name;
        this.target = target;
    }
}
