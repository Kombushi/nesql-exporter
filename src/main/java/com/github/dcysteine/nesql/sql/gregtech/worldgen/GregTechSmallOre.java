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

import java.util.SortedSet;
import java.util.TreeSet;

/** A GregTech small ore definition, including the dimensions it generates in and its drops. */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class GregTechSmallOre implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    /** Internal small ore name, e.g. {@code "ore.small.copper"}. */
    @Column(nullable = false)
    private String smallOreName;

    @Column(nullable = false)
    private String materialName;

    /** False for small ores that ship disabled in the default worldgen config. */
    private boolean enabledByDefault;

    private int amountPerChunk;

    private int minY;

    private int maxY;

    @ElementCollection
    @SortNatural
    private SortedSet<GregTechSmallOreDimension> dimensions;

    @ElementCollection
    @SortNatural
    private SortedSet<GregTechSmallOreBlock> blocks;

    @ElementCollection
    @SortNatural
    private SortedSet<GregTechSmallOreDrop> drops;

    /** Needed by Hibernate. */
    protected GregTechSmallOre() {}

    public GregTechSmallOre(
            String id, String smallOreName, String materialName, boolean enabledByDefault,
            int amountPerChunk, int minY, int maxY) {
        this.id = id;
        this.smallOreName = smallOreName;
        this.materialName = materialName;
        this.enabledByDefault = enabledByDefault;
        this.amountPerChunk = amountPerChunk;
        this.minY = minY;
        this.maxY = maxY;

        dimensions = new TreeSet<>();
        blocks = new TreeSet<>();
        drops = new TreeSet<>();
    }

    public void addDimension(GregTechSmallOreDimension dimension) {
        dimensions.add(dimension);
    }

    public void addBlock(GregTechSmallOreBlock block) {
        blocks.add(block);
    }

    public void addDrop(GregTechSmallOreDrop drop) {
        drops.add(drop);
    }
}