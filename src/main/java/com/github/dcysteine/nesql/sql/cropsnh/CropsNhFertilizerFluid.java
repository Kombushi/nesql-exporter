package com.github.dcysteine.nesql.sql.cropsnh;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/** A fluid fertilizer with its per-liter potency, from the CropsNH fertilizer registry. */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class CropsNhFertilizerFluid implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @ManyToOne
    private Fluid fluid;

    private int potency;

    /** Needed by Hibernate. */
    protected CropsNhFertilizerFluid() {}

    public CropsNhFertilizerFluid(String id, Fluid fluid, int potency) {
        this.id = id;
        this.fluid = fluid;
        this.potency = potency;
    }
}
