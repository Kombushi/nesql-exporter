package com.github.dcysteine.nesql.sql.gregtech.machineprops;

import com.github.dcysteine.nesql.sql.Identifiable;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.item.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * A combustion engine controller with its per-class fuel constants.
 *
 * <p>The shared engine mechanics are not stored: the booster gas burns at 2 L/t times
 * the additive factor, and lubricant at 1 L per 72 ticks (doubled while boosted).
 */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class GregTechCombustionEngine implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @ManyToOne
    private Item item;

    /** Unboosted EU/t output; fuel draw per tick is this divided by the fuel value. */
    private int nominalOutput;

    @ManyToOne
    private Fluid boosterFluid;

    @ManyToOne
    private Fluid lubricantFluid;

    /** Fuel draw multiplier while boosted. */
    private int boostFuelFactor;

    /** Multiplier on booster and lubricant draw. */
    private int additiveFactor;

    /** Output efficiency in hundredths of a percent when running unboosted. */
    private int efficiencyUnboosted;

    /** Output efficiency in hundredths of a percent when running boosted. */
    private int efficiencyBoosted;

    /** Needed by Hibernate. */
    protected GregTechCombustionEngine() {}

    public GregTechCombustionEngine(
            String id, Item item, int nominalOutput, Fluid boosterFluid, Fluid lubricantFluid,
            int boostFuelFactor, int additiveFactor, int efficiencyUnboosted,
            int efficiencyBoosted) {
        this.id = id;
        this.item = item;
        this.nominalOutput = nominalOutput;
        this.boosterFluid = boosterFluid;
        this.lubricantFluid = lubricantFluid;
        this.boostFuelFactor = boostFuelFactor;
        this.additiveFactor = additiveFactor;
        this.efficiencyUnboosted = efficiencyUnboosted;
        this.efficiencyBoosted = efficiencyBoosted;
    }
}
