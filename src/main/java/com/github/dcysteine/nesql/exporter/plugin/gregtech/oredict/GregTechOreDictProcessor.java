package com.github.dcysteine.nesql.exporter.plugin.gregtech.oredict;

import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.PluginHelper;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.lang.reflect.Field;
import java.util.Map;

public class GregTechOreDictProcessor extends PluginHelper {
    public GregTechOreDictProcessor(PluginExporter exporter) {
        super(exporter);
    }

    public void process() {
        OrePrefixFactory prefixFactory = new OrePrefixFactory(exporter);
        logger.info("Processing {} ore prefixes...", OrePrefixes.VALUES.length);
        for (OrePrefixes prefix : OrePrefixes.VALUES) {
            prefixFactory.get(prefix);
        }

        Map<String, ItemStack> targets = readName2StackMap();
        ItemFactory itemFactory = new ItemFactory(exporter);
        UnificationFactory unificationFactory = new UnificationFactory(exporter);
        UnificationBlacklistFactory blacklistFactory = new UnificationBlacklistFactory(exporter);
        logger.info("Processing {} unified oredict names...", targets.size());
        int exported = 0;
        int blacklisted = 0;
        for (Map.Entry<String, ItemStack> entry : targets.entrySet()) {
            ItemStack target = entry.getValue();
            if (target == null || target.getItem() == null) {
                continue;
            }

            try {
                unificationFactory.get(entry.getKey(), itemFactory.get(target));
                exported++;
            } catch (Exception e) {
                logger.warn("Skipping unification target for " + entry.getKey(), e);
                continue;
            }

            for (ItemStack member : OreDictionary.getOres(entry.getKey())) {
                try {
                    if (member == null || member.getItem() == null
                            || member.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                        continue;
                    }
                    if (GTOreDictUnificator.isBlacklisted(member)) {
                        blacklistFactory.get(itemFactory.get(member));
                        blacklisted++;
                    }
                } catch (Exception e) {
                    logger.warn("Skipping blacklist check for a member of " + entry.getKey(), e);
                }
            }
        }

        exporterState.flushEntityManager();
        logger.info(
                "Finished processing GregTech oredict unification: {} names, {} blacklisted",
                exported, blacklisted);
    }

    /** The unification map is private; this is the only way at the pinned GT version. */
    @SuppressWarnings("unchecked")
    private static Map<String, ItemStack> readName2StackMap() {
        try {
            Field field = GTOreDictUnificator.class.getDeclaredField("sName2StackMap");
            field.setAccessible(true);
            return (Map<String, ItemStack>) field.get(null);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("GTOreDictUnificator.sName2StackMap is unreadable", e);
        }
    }
}
