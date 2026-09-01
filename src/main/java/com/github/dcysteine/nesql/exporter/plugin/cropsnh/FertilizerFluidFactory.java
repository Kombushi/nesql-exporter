package com.github.dcysteine.nesql.exporter.plugin.cropsnh;

import com.github.dcysteine.nesql.exporter.plugin.EntityFactory;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.plugin.base.factory.FluidFactory;
import com.github.dcysteine.nesql.exporter.util.IdPrefixUtil;
import com.github.dcysteine.nesql.sql.base.fluid.Fluid;
import com.github.dcysteine.nesql.sql.cropsnh.CropsNhFertilizerFluid;

public class FertilizerFluidFactory extends EntityFactory<CropsNhFertilizerFluid, String> {
    private final FluidFactory fluidFactory;

    public FertilizerFluidFactory(PluginExporter exporter) {
        super(exporter);
        fluidFactory = new FluidFactory(exporter);
    }

    public CropsNhFertilizerFluid get(net.minecraftforge.fluids.Fluid fluid, int potency) {
        Fluid fluidEntity = fluidFactory.get(fluid);

        String id = IdPrefixUtil.CROPSNH_FERTILIZER_FLUID.applyPrefix(fluidEntity.getId());
        CropsNhFertilizerFluid entity = new CropsNhFertilizerFluid(id, fluidEntity, potency);
        return findOrPersist(CropsNhFertilizerFluid.class, entity);
    }
}
