package com.github.dcysteine.nesql.sql.gregtech.machineprops;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.SortNatural;

import java.util.SortedSet;
import java.util.TreeSet;

/** A multiblock controller with its parallel/speed/EU bonuses parsed from typed tooltips. */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class GregTechMultiblockMachine implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @ManyToOne
    private Item item;

    /** Prototype {@code getMaxParallelRecipes()}; null when the lookup needs a live structure. */
    @Column
    private Integer maxParallelRecipes;

    @ElementCollection
    @SortNatural
    private SortedSet<GregTechMultiblockBonus> bonuses;

    /** Needed by Hibernate. */
    protected GregTechMultiblockMachine() {}

    public GregTechMultiblockMachine(String id, Item item, Integer maxParallelRecipes) {
        this.id = id;
        this.item = item;
        this.maxParallelRecipes = maxParallelRecipes;

        bonuses = new TreeSet<>();
    }

    public void addBonus(GregTechMultiblockBonus bonus) {
        bonuses.add(bonus);
    }
}
