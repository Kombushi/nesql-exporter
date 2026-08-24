package com.github.dcysteine.nesql.exporter.plugin.gregtech.machineprops;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.gregtech.machineprops.GregTechMultiblockMachine;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.List;

public class MultiblockMachineFactory extends EntityFactory<GregTechMultiblockMachine, String> {
    private final ItemFactory itemFactory;
    private final MultiblockBonusParser parser;

    public MultiblockMachineFactory(PluginExporter exporter) {
        super(exporter);
        itemFactory = new ItemFactory(exporter);
        parser = new MultiblockBonusParser();
    }

    public GregTechMultiblockMachine get(int metaId, MTEMultiBlockBase multiblock) {
        ItemStack stack = multiblock.getStackForm(1);
        Item item = itemFactory.get(stack);

        Integer maxParallelRecipes = null;
        try {
            maxParallelRecipes = multiblock.getMaxParallelRecipes();
        } catch (Exception e) {
            // Structure-dependent parallel suppliers need a live structure; leave null.
        }

        String id = IdPrefixUtil.GREG_TECH_MULTIBLOCK.applyPrefix(String.valueOf(metaId));
        GregTechMultiblockMachine entity =
                new GregTechMultiblockMachine(id, item, maxParallelRecipes);

        @SuppressWarnings("unchecked")
        List<String> tooltip =
                (List<String>) stack.getTooltip(Minecraft.getMinecraft().thePlayer, true);
        for (String line : tooltip) {
            parser.parse(line).ifPresent(entity::addBonus);
        }

        return findOrPersist(GregTechMultiblockMachine.class, entity);
    }
}
