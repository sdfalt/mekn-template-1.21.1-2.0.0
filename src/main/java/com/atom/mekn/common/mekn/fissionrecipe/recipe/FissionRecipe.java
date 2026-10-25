package com.atom.mekn.common.mekn.fissionrecipe.recipe;

import com.atom.mekn.MekN;
import com.atom.mekn.common.mekn.fissionrecipe.misc.Heat;
import com.mojang.datafixers.util.Either;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.basic.BasicChemicalToChemicalRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.common.Mekanism;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

import javax.script.ScriptException;

public class FissionRecipe extends BasicChemicalToChemicalRecipe {
    private final Heat heat;

    public FissionRecipe(ChemicalStackIngredient input, ChemicalStack output, Heat heat) {
        super(input, output, MekN.RecipeTypes_Fiss.FISSION.get());
        this.heat = heat;
    }

    public FissionRecipe(ChemicalStackIngredient input, ChemicalStack output, Either<Double, String> either) {
        this(input, output, Heat.fromEither(either));
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return MekN.RecipeSerializers_Fiss.FISSION.get();
    }

    public double getHeat(double toBurn) {
        if (!this.heat.isEqt()) return toBurn * this.heat.constant();
        String substituted = this.heat.equation().replaceAll("x", Double.toString(toBurn));
        try {
            return (double) Heat.JS_ENGINE.eval(substituted);
        } catch (ScriptException e) {
            Mekanism.logger.error("Failed to evaluate Fission Recipe equation.");
            e.printStackTrace();
            return this.heat.constant();
        }
    }

    public Heat getHeat() {
        return this.heat;
    }
}
