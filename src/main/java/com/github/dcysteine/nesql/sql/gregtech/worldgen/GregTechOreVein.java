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

/** A GregTech ore mix vein definition, including the dimensions it generates in. */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class GregTechOreVein implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    /** Internal vein name, e.g. {@code "ore.mix.chrome"}. */
    @Column(nullable = false)
    private String veinName;

    @Column(nullable = false)
    private String localizedName;

    /** False for veins that ship disabled in the default worldgen config. */
    private boolean enabledByDefault;

    /** Random selection weight of this vein during ore generation. */
    private int weight;

    private int size;

    private int density;

    /** Default height range; per-dimension overrides are on the dimension rows. */
    private int minY;

    private int maxY;

    @ElementCollection
    @SortNatural
    private SortedSet<GregTechOreVeinDimension> dimensions;

    @ElementCollection
    @SortNatural
    private SortedSet<GregTechOreVeinOre> ores;

    /** Needed by Hibernate. */
    protected GregTechOreVein() {}

    public GregTechOreVein(
            String id, String veinName, String localizedName, boolean enabledByDefault,
            int weight, int size, int density, int minY, int maxY) {
        this.id = id;
        this.veinName = veinName;
        this.localizedName = localizedName;
        this.enabledByDefault = enabledByDefault;
        this.weight = weight;
        this.size = size;
        this.density = density;
        this.minY = minY;
        this.maxY = maxY;

        dimensions = new TreeSet<>();
        ores = new TreeSet<>();
    }

    public void addDimension(GregTechOreVeinDimension dimension) {
        dimensions.add(dimension);
    }

    public void addOre(GregTechOreVeinOre ore) {
        ores.add(ore);
    }
}