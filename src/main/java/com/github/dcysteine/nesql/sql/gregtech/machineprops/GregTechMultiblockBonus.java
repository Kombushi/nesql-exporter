package com.github.dcysteine.nesql.sql.gregtech.machineprops;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 * One bonus line: {@code PARALLEL}, {@code PARALLEL_PER_TIER}, {@code SPEED},
 * {@code SPEED_BONUS_PER_TIER}, {@code SPEED_PER_TIER}, {@code EU_DISCOUNT},
 * {@code EU_DISCOUNT_PER_TIER}, or {@code STEAM_DISCOUNT}. The value is the displayed
 * number (percentages as shown, e.g. 220 for "220% Speed").
 */
@Embeddable
@EqualsAndHashCode
@Getter
@ToString
public class GregTechMultiblockBonus implements Comparable<GregTechMultiblockBonus> {
    @Column(nullable = false)
    private String kind;

    @Column(nullable = false)
    private double bonusValue;

    /** True for "Nx per tier" multiplicative parallel scaling. */
    private boolean multiplicative;

    /** Scaling axis for per-tier kinds ({@code VOLTAGE}, {@code COIL}, ...); null for static. */
    @Column
    private String tierAxis;

    /** The tooltip line the bonus was parsed from, color codes stripped. */
    @Column(nullable = false, length = 1000)
    private String sourceLine;

    /** Needed by Hibernate. */
    protected GregTechMultiblockBonus() {}

    public GregTechMultiblockBonus(
            String kind, double bonusValue, boolean multiplicative, String tierAxis,
            String sourceLine) {
        this.kind = kind;
        this.bonusValue = bonusValue;
        this.multiplicative = multiplicative;
        this.tierAxis = tierAxis;
        this.sourceLine = sourceLine;
    }

    @Override
    public int compareTo(@NotNull GregTechMultiblockBonus other) {
        return Comparator.comparing(GregTechMultiblockBonus::getKind)
                .thenComparing(GregTechMultiblockBonus::getSourceLine)
                .compare(this, other);
    }
}
