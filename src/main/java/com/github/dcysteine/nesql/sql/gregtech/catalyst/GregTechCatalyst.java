package com.github.dcysteine.nesql.sql.gregtech.catalyst;

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
 * An item a GregTech machine uses up by durability rather than consuming per run.
 *
 * <p>Kinds: {@code "chemical_plant"} from the Chemical Plant's catalyst registry, and
 * {@code "milling_ball"} from the IsaMill's milling ball check.
 */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class GregTechCatalyst implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @ManyToOne
    private Item item;

    @Column(nullable = false)
    private String kind;

    /** Needed by Hibernate. */
    protected GregTechCatalyst() {}

    public GregTechCatalyst(String id, Item item, String kind) {
        this.id = id;
        this.item = item;
        this.kind = kind;
    }
}
