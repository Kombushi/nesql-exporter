package com.github.dcysteine.nesql.sql.gregtech.worldgen;

import com.github.dcysteine.nesql.sql.Identifiable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.SortNatural;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

/** A dimension that GregTech ore generation can place ores in. */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class GregTechDimension implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    /** Abbreviated dimension name, e.g. {@code "Ow"}. Used as the join key by worldgen tables. */
    @Column(nullable = false)
    private String abbreviation;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String internalName;

    /** Rocket tier needed to reach this dimension; 0 for pre-space dimensions. */
    private int rocketTier;

    /** Stone types whose ore variants generate in this dimension. */
    @ElementCollection
    @SortNatural
    private SortedSet<String> stoneTypes;

    /** Needed by Hibernate. */
    protected GregTechDimension() {}

    public GregTechDimension(
            String id, String abbreviation, String fullName, String internalName, int rocketTier,
            Collection<String> stoneTypes) {
        this.id = id;
        this.abbreviation = abbreviation;
        this.fullName = fullName;
        this.internalName = internalName;
        this.rocketTier = rocketTier;
        this.stoneTypes = new TreeSet<>(stoneTypes);
    }
}