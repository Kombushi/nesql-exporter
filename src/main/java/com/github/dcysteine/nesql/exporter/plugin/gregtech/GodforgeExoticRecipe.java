package com.github.dcysteine.nesql.exporter.plugin.gregtech;

import com.github.dcysteine.nesql.exporter.plugin.base.factory.RecipeBuilder;
import gregtech.api.enums.Materials;
import gregtech.api.util.GTRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import tectech.loader.recipe.Godforge;

import java.util.ArrayList;
import java.util.List;

/**
 * Rebuilds the Forge of the Gods exotic module's inputs.
 *
 * <p>The module writes a fresh recipe every cycle, drawing its ingredients at random from its own
 * tables, so the recipe it registers for NEI is a placeholder naming none of them. Worse, NEI
 * shows the module's request tickets: at cycle start it hands out free samples of the materials
 * it rolled, and what it then actually consumes is the plasma form of each sampled material.
 * The temporal and spatial fluids it shows are handed out and taken back at identical amounts,
 * so no net input of either ever leaves the player's base.
 *
 * <p>Amounts are the dearest the module can roll. A recipe whose cost is drawn from a range has no
 * one true price, and quoting the ceiling keeps the export from promising a bargain that only
 * sometimes arrives.
 */
public final class GodforgeExoticRecipe {
    public static final String RECIPE_MAP = "gt.recipe.fog_exotic";

    /** Magmatter wants 144 mB of plasma per point of roll spread, at most 100 - 1. */
    private static final int MAX_MAGMATTER_PLASMA = 144 * 99;

    /** A rolled gas wants a bucket of its plasma per sampled millibucket, at most 64. */
    private static final int MAX_GAS_PLASMA = 64 * 1000;

    /** A rolled dust wants nine ingots of its plasma per sampled dust, at most 7. */
    private static final int MAX_DUST_PLASMA = 144 * 9 * 7;

    /** The module always rolls seven materials, splitting them between gases and dusts. */
    private static final int PLASMA_SLOTS = 7;

    private GodforgeExoticRecipe() {}

    public static boolean handles(String recipeMapShortName) {
        return RECIPE_MAP.equals(recipeMapShortName);
    }

    public static void addInputs(RecipeBuilder builder, GTRecipe recipe) {
        if (producesMagmatter(recipe)) {
            List<FluidStack> plasmas = new ArrayList<FluidStack>();
            for (ItemStack dust : Godforge.exoticModuleMagmatterItemMap.keySet()) {
                addDustPlasma(plasmas, dust, MAX_MAGMATTER_PLASMA);
            }
            builder.addFluidGroupInput(plasmas);
            return;
        }

        List<FluidStack> plasmas = new ArrayList<FluidStack>();
        for (FluidStack gas : Godforge.exoticModulePlasmaFluidMap.keySet()) {
            addGasPlasma(plasmas, gas, MAX_GAS_PLASMA);
        }
        for (ItemStack dust : Godforge.exoticModulePlasmaItemMap.keySet()) {
            addDustPlasma(plasmas, dust, MAX_DUST_PLASMA);
        }
        for (int slot = 0; slot < PLASMA_SLOTS; slot++) {
            builder.addFluidGroupInput(plasmas);
        }
    }

    /** Mirrors the module's own dust conversion: oredict name minus its prefix names the plasma. */
    private static void addDustPlasma(List<FluidStack> plasmas, ItemStack dust, int amount) {
        int[] oreIds = OreDictionary.getOreIDs(dust);
        if (oreIds.length == 0) {
            return;
        }
        String material = OreDictionary.getOreName(oreIds[0]).substring(4).toLowerCase();
        FluidStack plasma = FluidRegistry.getFluidStack("plasma." + material, amount);
        if (plasma != null) {
            plasmas.add(plasma);
        }
    }

    /** Mirrors the module's own fluid conversion: the unlocalized name's tail names the plasma. */
    private static void addGasPlasma(List<FluidStack> plasmas, FluidStack gas, int amount) {
        String[] parts = gas.getUnlocalizedName().split("\\.");
        FluidStack plasma = FluidRegistry.getFluidStack("plasma." + parts[parts.length - 1], amount);
        if (plasma != null) {
            plasmas.add(plasma);
        }
    }

    private static boolean producesMagmatter(GTRecipe recipe) {
        return recipe.mFluidOutputs != null
                && recipe.mFluidOutputs.length > 0
                && recipe.mFluidOutputs[0] != null
                && recipe.mFluidOutputs[0].isFluidEqual(Materials.MagMatter.getMolten(1L));
    }
}
