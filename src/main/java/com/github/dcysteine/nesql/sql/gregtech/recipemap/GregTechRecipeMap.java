package com.github.dcysteine.nesql.sql.gregtech.recipemap;

import com.github.dcysteine.nesql.sql.Identifiable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.SortNatural;

import java.util.SortedSet;
import java.util.TreeSet;

/** A GregTech recipe map, including the machines that can run its recipes. */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class GregTechRecipeMap implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    /** Recipe map key, e.g. {@code "gt.recipe.blastfurnace"}. */
    @Column(nullable = false)
    private String unlocalizedName;

    @Column(nullable = false)
    private String localizedName;

    private int amperage;

    /** True if at least one single-block machine runs this map's recipes. */
    private boolean hasSingleBlock;

    /** True if at least one multiblock controller runs this map's recipes. */
    private boolean hasMultiBlock;

    /** True where the map's backend burns fuels for EU rather than crafting outputs. */
    private boolean isFuel;

    @ElementCollection
    @SortNatural
    private SortedSet<GregTechRecipeMapMachine> machines;

    /** Needed by Hibernate. */
    protected GregTechRecipeMap() {}

    public GregTechRecipeMap(
            String id, String unlocalizedName, String localizedName, int amperage,
            boolean isFuel) {
        this.id = id;
        this.unlocalizedName = unlocalizedName;
        this.localizedName = localizedName;
        this.amperage = amperage;
        this.isFuel = isFuel;

        machines = new TreeSet<>();
    }

    public void addMachine(GregTechRecipeMapMachine machine) {
        machines.add(machine);

        if (machine.isMultiblock()) {
            hasMultiBlock = true;
        } else {
            hasSingleBlock = true;
        }
    }
}