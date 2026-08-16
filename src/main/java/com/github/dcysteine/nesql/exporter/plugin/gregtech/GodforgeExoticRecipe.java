package com.github.dcysteine.nesql.exporter.plugin.gregtech;

import com.github.dcysteine.nesql.exporter.plugin.base.factory.RecipeBuilder;
import gregtech.api.enums.Materials;
import gregtech.api.util.GTRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import tectech.loader.recipe.Godforge;

import java.util.ArrayList;
import java.util.List;

/**
 * Rebuilds the Forge of the Gods exotic module's inputs.
 *
 * <p>The module writes a fresh recipe every cycle, drawing its ingredients at random from its own
 * tables, so the recipe it registers for NEI is a placeholder naming none of them: it advertises
 * one iron dust and two millibuckets of unrelated molten metal. A NEI frontend swaps the display
 * out at draw time, which the exporter never sees.
 *
 * <p>Amounts are the dearest the module can roll. A recipe whose cost is drawn from a range has no
 * one true price, and quoting the ceiling keeps the export from promising a bargain that only
 * sometimes arrives.
 */
public final class GodforgeExoticRecipe {
    public static final String RECIPE_MAP = "gt.recipe.fog_exotic";

    /** Molten Time, at the top of the module's 1000-50000 range. */
    private static final long MAX_TIME = 50 * 1000L;

    /** Molten Space, at the top of the module's 51000-100000 range. */
    private static final long MAX_SPACE = 100 * 1000L;

    /** One plasma, at the top of the module's 1000-64000 range. */
    private static final long MAX_PLASMA = 64 * 1000L;

    /** The module always draws seven plasmas, splitting them between fluid and item form. */
    private static final int PLASMA_SLOTS = 7;

    private GodforgeExoticRecipe() {}

    public static boolean handles(String recipeMapShortName) {
        return RECIPE_MAP.equals(recipeMapShortName);
    }

    public static void addInputs(RecipeBuilder builder, GTRecipe recipe) {
        if (producesMagmatter(recipe)) {
            builder.addItemGroupInput(
                    new ArrayList<ItemStack>(Godforge.exoticModuleMagmatterItemMap.keySet()));
            builder.addFluidInput(Materials.Time.getMolten(MAX_TIME));
            builder.addFluidInput(Materials.Space.getMolten(MAX_SPACE));
            return;
        }

        // Item plasmas are converted to their fluid form before the recipe runs, so every slot is
        // one plasma however it was fed in.
        List<FluidStack> plasmas = new ArrayList<FluidStack>();
        for (FluidStack plasma : Godforge.exoticModulePlasmaFluidMap.keySet()) {
            plasmas.add(new FluidStack(plasma, (int) MAX_PLASMA));
        }
        for (int slot = 0; slot < PLASMA_SLOTS; slot++) {
            builder.addFluidGroupInput(plasmas);
        }
    }

    private static boolean producesMagmatter(GTRecipe recipe) {
        return recipe.mFluidOutputs != null
                && recipe.mFluidOutputs.length > 0
                && recipe.mFluidOutputs[0] != null
                && recipe.mFluidOutputs[0].isFluidEqual(Materials.MagMatter.getMolten(1L));
    }
}
