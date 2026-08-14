package com.github.dcysteine.nesql.sql.gregtech.worldgen;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
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

/** An underground fluid (drillable via the Ore Drilling Plant's fluid mechanic). */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class GregTechUndergroundFluid implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String fluidName;

    @ManyToOne
    private Fluid fluid;

    @ElementCollection
    @SortNatural
    private SortedSet<GregTechUndergroundFluidDimension> dimensions;

    /** Needed by Hibernate. */
    protected GregTechUndergroundFluid() {}

    public GregTechUndergroundFluid(String id, String fluidName, Fluid fluid) {
        this.id = id;
        this.fluidName = fluidName;
        this.fluid = fluid;

        dimensions = new TreeSet<>();
    }

    public void addDimension(GregTechUndergroundFluidDimension dimension) {
        dimensions.add(dimension);
    }
}