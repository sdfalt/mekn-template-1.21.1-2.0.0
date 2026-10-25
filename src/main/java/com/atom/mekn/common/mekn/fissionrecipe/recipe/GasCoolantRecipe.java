package com.atom.mekn.common.mekn.fissionrecipe.recipe;

import com.atom.mekn.MekN;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.basic.BasicChemicalToChemicalRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

public class GasCoolantRecipe extends BasicChemicalToChemicalRecipe {
    private final double thermalEnthalpy, conductivity;

    public GasCoolantRecipe(ChemicalStackIngredient input, ChemicalStack output, double thermalEnthalpy, double conductivity) {
        super(input, output, MekN.RecipeTypes_Fiss.GAS_COOLANT.get());
        this.thermalEnthalpy = thermalEnthalpy;
        this.conductivity = conductivity;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return MekN.RecipeSerializers_Fiss.GAS_COOLANT.get();
    }

    public double getThermalEnthalpy() {
        return thermalEnthalpy;
    }

    public double getConductivity() {
        return conductivity;
    }
}
