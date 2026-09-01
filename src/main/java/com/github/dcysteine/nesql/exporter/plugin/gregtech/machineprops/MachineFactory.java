package com.github.dcysteine.nesql.exporter.plugin.gregtech.machineprops;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.gregtech.machineprops.GregTechMachine;
import net.minecraft.item.ItemStack;

public class MachineFactory extends EntityFactory<GregTechMachine, String> {
    private final ItemFactory itemFactory;

    public MachineFactory(PluginExporter exporter) {
        super(exporter);
        itemFactory = new ItemFactory(exporter);
    }

    public GregTechMachine get(
            int metaId, ItemStack stack, String machineClass, Integer tier, boolean multiblock,
            boolean steam) {
        Item item = itemFactory.get(stack);

        String id = IdPrefixUtil.GREG_TECH_MACHINE.applyPrefix(String.valueOf(metaId));
        GregTechMachine entity =
                new GregTechMachine(id, item, machineClass, tier, multiblock, steam);
        return findOrPersist(GregTechMachine.class, entity);
    }
}
