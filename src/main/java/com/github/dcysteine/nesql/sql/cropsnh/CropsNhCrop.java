package com.github.dcysteine.nesql.sql.cropsnh;

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

/** A CropsNH crop: its seeds, harvest drops, tiers, and growth conditions. */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class CropsNhCrop implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    /** Crop registry id, e.g. {@code "aluminiumOreberry"}. */
    @Column(nullable = false)
    private String cropId;

    @Column(nullable = false)
    private String name;

    /** Crop progression (breeding) tier; not a voltage tier. */
    private int tier;

    /** Voltage tier of the machine breeding recipes for this crop. */
    private int machineBreedingRecipeTier;

    /** Minimum seed bed tier this crop needs to be planted. */
    private int minSeedBedTier;

    private int growthDuration;

    private double dropChance;

    /** True for internal crops hidden from NEI, e.g. weeds. */
    private boolean hidden;

    /** Id of the soil list this crop accepts, e.g. {@code "farmland"}; null if none. */
    @Column
    private String soilListId;

    /** Minimum light level required to grow; null if unrestricted. */
    @Column
    private Integer minLightLevel;

    /** Maximum light level allowed to grow; null if unrestricted. */
    @Column
    private Integer maxLightLevel;

    /** Canonical seed item; null for crops without seeds. */
    @ManyToOne
    private Item seed;

    @ElementCollection
    @SortNatural
    private SortedSet<CropsNhAlternateSeed> alternateSeeds;

    @ElementCollection
    @SortNatural
    private SortedSet<CropsNhCropDrop> drops;

    /** Blocks accepted under the crop; empty if the crop has no block-under requirement. */
    @ElementCollection
    @SortNatural
    private SortedSet<CropsNhUnderBlock> underBlocks;

    /** Needed by Hibernate. */
    protected CropsNhCrop() {}

    public CropsNhCrop(
            String id, String cropId, String name, int tier, int machineBreedingRecipeTier,
            int minSeedBedTier, int growthDuration, double dropChance, boolean hidden,
            String soilListId, Integer minLightLevel, Integer maxLightLevel, Item seed) {
        this.id = id;
        this.cropId = cropId;
        this.name = name;
        this.tier = tier;
        this.machineBreedingRecipeTier = machineBreedingRecipeTier;
        this.minSeedBedTier = minSeedBedTier;
        this.growthDuration = growthDuration;
        this.dropChance = dropChance;
        this.hidden = hidden;
        this.soilListId = soilListId;
        this.minLightLevel = minLightLevel;
        this.maxLightLevel = maxLightLevel;
        this.seed = seed;

        alternateSeeds = new TreeSet<>();
        drops = new TreeSet<>();
        underBlocks = new TreeSet<>();
    }

    public void addAlternateSeed(CropsNhAlternateSeed alternateSeed) {
        alternateSeeds.add(alternateSeed);
    }

    public void addDrop(CropsNhCropDrop drop) {
        drops.add(drop);
    }

    public void addUnderBlock(CropsNhUnderBlock underBlock) {
        underBlocks.add(underBlock);
    }
}