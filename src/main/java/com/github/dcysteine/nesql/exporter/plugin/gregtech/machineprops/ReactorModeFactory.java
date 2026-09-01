package com.github.dcysteine.nesql.exporter.plugin.gregtech.machineprops;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.FluidFactory;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.ItemFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.base.item.Item;
import com.github.dcysteine.nesql.sql.gregtech.machineprops.GregTechReactorMode;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class ReactorModeFactory extends EntityFactory<GregTechReactorMode, String> {
    private final ItemFactory itemFactory;
    private final FluidFactory fluidFactory;

    public ReactorModeFactory(PluginExporter exporter) {
        super(exporter);
        itemFactory = new ItemFactory(exporter);
        fluidFactory = new FluidFactory(exporter);
    }

    public GregTechReactorMode get(
            int metaId, ItemStack machineStack, String kind, FluidStack fluidStack,
            Integer factor) {
        Item machine = itemFactory.get(machineStack);
        Fluid fluid = fluidFactory.get(fluidStack);

        String id =
                IdPrefixUtil.GREG_TECH_REACTOR_MODE.applyPrefix(
                        String.valueOf(metaId), kind, fluid.getId());
        GregTechReactorMode entity =
                new GregTechReactorMode(id, machine, kind, fluid, fluidStack.amount, factor);
        return findOrPersist(GregTechReactorMode.class, entity);
    }
}
