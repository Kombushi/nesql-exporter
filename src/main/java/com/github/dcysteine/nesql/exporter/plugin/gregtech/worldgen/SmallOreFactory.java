package com.github.dcysteine.nesql.exporter.plugin.gregtech.worldgen;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.gregtech.worldgen.GregTechSmallOre;
import com.github.dcysteine.nesql.sql.gregtech.worldgen.GregTechSmallOreBlock;
import com.github.dcysteine.nesql.sql.gregtech.worldgen.GregTechSmallOreDimension;
import com.github.dcysteine.nesql.sql.gregtech.worldgen.GregTechSmallOreDrop;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.interfaces.IStoneType;
import gregtech.common.SmallOreBuilder;
import gregtech.common.ores.OreInfo;
import gregtech.common.ores.OreManager;
import gtneioreplugin.util.DimensionHelper;
import net.minecraft.item.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SmallOreFactory extends EntityFactory<GregTechSmallOre, String> {
    private final ItemFactory itemFactory;

    public SmallOreFactory(PluginExporter exporter) {
        super(exporter);
        this.itemFactory = new ItemFactory(exporter);
    }

    /**
     * Persists one small ore.
     *
     * @param totalAmountByDim total small ore amount-per-chunk per dimension abbreviation
     */
    public GregTechSmallOre get(SmallOreBuilder ore, Map<String, Integer> totalAmountByDim) {
        String id = IdPrefixUtil.GREG_TECH_SMALL_ORE.applyPrefix(ore.smallOreName);

        GregTechSmallOre entity =
                new GregTechSmallOre(
                        id, ore.smallOreName, ore.ore.getInternalName(), ore.enabledByDefault,
                        ore.amount, ore.minY, ore.maxY);

        Set<IStoneType> dimStones = new HashSet<>();
        for (String internalName : ore.dimsEnabled) {
            String abbr = DimensionHelper.INTERNAL_TO_ABBR.get(internalName);
            if (abbr == null) {
                logger.warn(
                        "Skipping unknown dimension {} of small ore {}",
                        internalName, ore.smallOreName);
                continue;
            }
            dimStones.addAll(DimensionHelper.getStoneTypes(internalName));

            double probability = ((double) ore.amount) / totalAmountByDim.get(abbr);
            entity.addDimension(new GregTechSmallOreDimension(abbr, probability));
        }

        addBlocks(entity, ore.ore, dimStones);
        addDrops(entity, ore.ore);

        return findOrPersist(GregTechSmallOre.class, entity);
    }

    /** Adds one block row per stone type variant that can occur in the ore's dimensions. */
    private void addBlocks(GregTechSmallOre entity, IOreMaterial material, Set<IStoneType> dimStones) {
        List<IStoneType> validStones = material.getValidStones();

        boolean added = false;
        for (IStoneType stoneType : validStones) {
            if (dimStones.contains(stoneType)) {
                added |= addBlock(entity, material, stoneType);
            }
        }
        if (!added && !validStones.isEmpty()) {
            addBlock(entity, material, validStones.get(0));
        }
    }

    private boolean addBlock(GregTechSmallOre entity, IOreMaterial material, IStoneType stoneType) {
        try (OreInfo<IOreMaterial> info = OreInfo.getNewInfo()) {
            info.material = material;
            info.stoneType = stoneType;
            info.isSmall = true;

            ItemStack stack = OreManager.getStack(info, 1);
            if (stack == null) {
                logger.warn(
                        "Small ore stack was null: {}, stone {}",
                        entity.getSmallOreName(), stoneType);
                return false;
            }

            entity.addBlock(
                    new GregTechSmallOreBlock(
                            OreVeinFactory.stoneName(stoneType), itemFactory.get(stack)));
            return true;
        }
    }

    private void addDrops(GregTechSmallOre entity, IOreMaterial material) {
        try (OreInfo<IOreMaterial> info = OreInfo.getNewInfo()) {
            info.material = material;
            info.stoneType = null;
            info.isSmall = true;

            // Null for materials no ore adapter supports as a small ore, e.g. Infinity.
            List<ItemStack> drops = OreManager.getPotentialDrops(info);
            if (drops == null) {
                logger.warn("Small ore drops were null: {}", entity.getSmallOreName());
                return;
            }

            for (ItemStack stack : drops) {
                if (stack == null) {
                    continue;
                }
                entity.addDrop(new GregTechSmallOreDrop(itemFactory.get(stack)));
            }
        }
    }
}