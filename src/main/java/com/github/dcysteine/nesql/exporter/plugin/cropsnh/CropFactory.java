package com.github.dcysteine.nesql.exporter.plugin.cropsnh;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.cropsnh.CropsNhAlternateSeed;
import com.github.dcysteine.nesql.sql.cropsnh.CropsNhCrop;
import com.github.dcysteine.nesql.sql.cropsnh.CropsNhCropDrop;
import com.github.dcysteine.nesql.sql.cropsnh.CropsNhUnderBlock;
import com.gtnewhorizon.cropsnh.api.ICropCard;
import com.gtnewhorizon.cropsnh.api.IGrowthRequirement;
import com.gtnewhorizon.cropsnh.api.ISoilList;
import com.gtnewhorizon.cropsnh.farming.SeedStats;
import com.gtnewhorizon.cropsnh.farming.requirements.BlockUnderRequirement;
import com.gtnewhorizon.cropsnh.farming.requirements.growth.MaxLightLevelGrowthRequirement;
import com.gtnewhorizon.cropsnh.farming.requirements.growth.MinLightLevelGrowthRequirement;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.lang.reflect.Field;
import java.util.Map;

public class CropFactory extends EntityFactory<CropsNhCrop, String> {
    private final ItemFactory itemFactory;

    public CropFactory(PluginExporter exporter) {
        super(exporter);
        this.itemFactory = new ItemFactory(exporter);
    }

    public CropsNhCrop get(ICropCard card) {
        String id = IdPrefixUtil.CROPSNH_CROP.applyPrefix(card.getId());

        ItemStack seedStack = getSeedStack(card);
        ISoilList soils = card.getSoilTypes();

        Integer minLightLevel = null;
        Integer maxLightLevel = null;
        for (IGrowthRequirement requirement : card.getGrowthRequirements()) {
            if (requirement instanceof MinLightLevelGrowthRequirement) {
                minLightLevel = reflectInt(requirement, "minLightLevel");
            } else if (requirement instanceof MaxLightLevelGrowthRequirement) {
                maxLightLevel = reflectInt(requirement, "maxLightLevel");
            }
        }

        CropsNhCrop crop =
                new CropsNhCrop(
                        id, card.getId(), getName(card, seedStack), card.getTier(),
                        card.getMachineBreedingRecipeTier(), card.getMinSeedBedTier(),
                        card.getGrowthDuration(), card.getDropChance(), card.hideFromNEI(),
                        soils == null ? null : soils.getId(), minLightLevel, maxLightLevel,
                        seedStack == null ? null : itemFactory.get(seedStack));

        for (ItemStack stack : card.getAlternateSeeds()) {
            if (stack != null && stack.getItem() != null) {
                crop.addAlternateSeed(new CropsNhAlternateSeed(itemFactory.get(stack)));
            }
        }

        for (Map.Entry<ItemStack, Integer> entry : card.getDropTable().entrySet()) {
            ItemStack stack = entry.getKey();
            if (stack != null && stack.getItem() != null) {
                crop.addDrop(new CropsNhCropDrop(itemFactory.get(stack), entry.getValue()));
            }
        }

        for (IGrowthRequirement requirement : card.getGrowthRequirements()) {
            if (!(requirement instanceof BlockUnderRequirement)) {
                continue;
            }
            for (ItemStack stack : ((BlockUnderRequirement) requirement).getItemsForNEI()) {
                if (stack != null && stack.getItem() != null) {
                    crop.addUnderBlock(new CropsNhUnderBlock(itemFactory.get(stack)));
                }
            }
        }

        return findOrPersist(CropsNhCrop.class, crop);
    }

    private ItemStack getSeedStack(ICropCard card) {
        try {
            ItemStack stack = card.getSeedItem(SeedStats.DEFAULT_ANALYZED);
            return stack == null || stack.getItem() == null ? null : stack;
        } catch (Exception e) {
            // Internal crops such as weeds have no seed item.
            return null;
        }
    }

    private static String getName(ICropCard card, ItemStack seedStack) {
        if (seedStack != null) {
            return seedStack.getDisplayName();
        }
        return StatCollector.translateToLocal(card.getUnlocalizedName());
    }

    /** The light level requirements expose their threshold only via a private field. */
    private static Integer reflectInt(Object target, String fieldName) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getInt(target);
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }
}
