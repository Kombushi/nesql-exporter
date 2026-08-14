package com.github.dcysteine.nesql.exporter.plugin.gregtech.worldgen;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.FluidFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.gregtech.worldgen.GregTechUndergroundFluid;
import com.github.dcysteine.nesql.sql.gregtech.worldgen.GregTechUndergroundFluidDimension;
import gtneioreplugin.util.GT5UndergroundFluidHelper;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.List;

public class UndergroundFluidFactory extends EntityFactory<GregTechUndergroundFluid, String> {
    private final FluidFactory fluidFactory;

    public UndergroundFluidFactory(PluginExporter exporter) {
        super(exporter);
        this.fluidFactory = new FluidFactory(exporter);
    }

    public GregTechUndergroundFluid get(
            String fluidName, List<GT5UndergroundFluidHelper.UndergroundFluidWrapper> wrappers) {
        String id = IdPrefixUtil.GREG_TECH_UNDERGROUND_FLUID.applyPrefix(fluidName);

        net.minecraftforge.fluids.Fluid forgeFluid = FluidRegistry.getFluid(fluidName);
        Fluid fluid = forgeFluid == null ? null : fluidFactory.get(forgeFluid);

        GregTechUndergroundFluid entity = new GregTechUndergroundFluid(id, fluidName, fluid);
        for (GT5UndergroundFluidHelper.UndergroundFluidWrapper wrapper : wrappers) {
            entity.addDimension(
                    new GregTechUndergroundFluidDimension(
                            wrapper.dimension,
                            wrapper.chance / 10_000d,
                            wrapper.minAmount,
                            wrapper.maxAmount));
        }

        return findOrPersist(GregTechUndergroundFluid.class, entity);
    }
}