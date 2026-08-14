package com.github.dcysteine.nesql.exporter.plugin.gregtech.worldgen;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.gregtech.worldgen.GregTechOreVein;
import com.github.dcysteine.nesql.sql.gregtech.worldgen.GregTechOreVeinDimension;
import com.github.dcysteine.nesql.sql.gregtech.worldgen.GregTechOreVeinLayer;
import com.github.dcysteine.nesql.sql.gregtech.worldgen.GregTechOreVeinOre;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.interfaces.IStoneType;
import gregtech.common.OreMixBuilder;
import gregtech.common.ores.OreInfo;
import gregtech.common.ores.OreManager;
import gtneioreplugin.util.DimensionHelper;
import it.unimi.dsi.fastutil.shorts.ShortShortPair;
import net.minecraft.item.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OreVeinFactory extends EntityFactory<GregTechOreVein, String> {
    private final ItemFactory itemFactory;

    public OreVeinFactory(PluginExporter exporter) {
        super(exporter);
        this.itemFactory = new ItemFactory(exporter);
    }

    /**
     * Persists one ore vein.
     *
     * @param totalWeightByDim total vein weight per dimension abbreviation, for probabilities
     */
    public GregTechOreVein get(OreMixBuilder mix, Map<String, Integer> totalWeightByDim) {
        String id = IdPrefixUtil.GREG_TECH_ORE_VEIN.applyPrefix(mix.oreMixName);

        GregTechOreVein vein =
                new GregTechOreVein(
                        id, mix.oreMixName, mix.getLocalizedName(), mix.enabledByDefault,
                        mix.weight, mix.size, mix.density, mix.minY, mix.maxY);

        Set<IStoneType> dimStones = new HashSet<>();
        for (String internalName : mix.dimsEnabled) {
            String abbr = DimensionHelper.INTERNAL_TO_ABBR.get(internalName);
            if (abbr == null) {
                logger.warn("Skipping unknown dimension {} of vein {}", internalName, mix.oreMixName);
                continue;
            }
            dimStones.addAll(DimensionHelper.getStoneTypes(internalName));

            int minY = mix.minY;
            int maxY = mix.maxY;
            ShortShortPair override = mix.dimVeinHeights.get(internalName);
            if (override != null) {
                minY = override.leftShort();
                maxY = override.rightShort();
            }

            double probability = ((double) mix.weight) / totalWeightByDim.get(abbr);
            vein.addDimension(new GregTechOreVeinDimension(abbr, probability, minY, maxY));
        }

        addLayer(vein, GregTechOreVeinLayer.PRIMARY, mix.primary, dimStones);
        addLayer(vein, GregTechOreVeinLayer.SECONDARY, mix.secondary, dimStones);
        addLayer(vein, GregTechOreVeinLayer.BETWEEN, mix.between, dimStones);
        addLayer(vein, GregTechOreVeinLayer.SPORADIC, mix.sporadic, dimStones);

        return findOrPersist(GregTechOreVein.class, vein);
    }

    /** Adds one ore row per stone type variant that can occur in the vein's dimensions. */
    private void addLayer(
            GregTechOreVein vein, GregTechOreVeinLayer layer, IOreMaterial material,
            Set<IStoneType> dimStones) {
        List<IStoneType> validStones = material.getValidStones();

        boolean added = false;
        for (IStoneType stoneType : validStones) {
            if (dimStones.contains(stoneType)) {
                added |= addOre(vein, layer, material, stoneType);
            }
        }
        // Same fallback as the NEI ore plugin, so we always export at least one variant.
        if (!added && !validStones.isEmpty()) {
            addOre(vein, layer, material, validStones.get(0));
        }
    }

    private boolean addOre(
            GregTechOreVein vein, GregTechOreVeinLayer layer, IOreMaterial material,
            IStoneType stoneType) {
        try (OreInfo<IOreMaterial> info = OreInfo.getNewInfo()) {
            info.material = material;
            info.stoneType = stoneType;

            ItemStack stack = OreManager.getStack(info, 1);
            if (stack == null) {
                logger.warn(
                        "Ore stack was null: vein {}, layer {}, stone {}",
                        vein.getVeinName(), layer, stoneType);
                return false;
            }

            Item item = itemFactory.get(stack);
            vein.addOre(
                    new GregTechOreVeinOre(
                            layer, material.getInternalName(), stoneName(stoneType), item));
            return true;
        }
    }

    static String stoneName(IStoneType stoneType) {
        return stoneType instanceof Enum ? ((Enum<?>) stoneType).name() : stoneType.toString();
    }
}