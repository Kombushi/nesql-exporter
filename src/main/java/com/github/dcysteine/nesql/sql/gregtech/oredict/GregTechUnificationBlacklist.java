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

/** An item GT excludes from unification even where it is a member of a unified oredict. */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class GregTechUnificationBlacklist implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @ManyToOne
    private Item item;

    /** Needed by Hibernate. */
    protected GregTechUnificationBlacklist() {}

    public GregTechUnificationBlacklist(String id, Item item) {
        this.id = id;
        this.item = item;
    }
}
