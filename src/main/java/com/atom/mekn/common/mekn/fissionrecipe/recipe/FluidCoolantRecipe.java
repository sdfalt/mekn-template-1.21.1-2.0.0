package com.atom.mekn.common.mekn.fissionrecipe.recipe;

import com.atom.mekn.MekN;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.vanilla_input.SingleFluidRecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;


public class FluidCoolantRecipe extends MekanismRecipe<SingleFluidRecipeInput> implements Predicate<FluidStack> {
    private final FluidStackIngredient input;
    private final ChemicalStack output;
    private final double thermalEnthalpy,  conductivity, efficiency;

    public FluidCoolantRecipe(FluidStackIngredient input, ChemicalStack output, double thermalEnthalpy, double conductivity, double efficiency) {
        this.input = input;
        this.output = output;
        this.thermalEnthalpy = thermalEnthalpy;
        this.conductivity = conductivity;
        this.efficiency = efficiency;
    }

    @Override
    public boolean test(FluidStack fluidStack) {
        return this.input.test(fluidStack);
    }

    public FluidStackIngredient getInput() {
        return this.input;
    }

    public ChemicalStack getOutput() {
        return this.output;
    }

    public ChemicalStack getOutputRepresentation() {
        return this.output;
    }

    @Override
    public boolean isIncomplete() {
        return input.hasNoMatchingInstances();
    }

    @Override
    public boolean matches(@NotNull SingleFluidRecipeInput input, @NotNull Level level) {
        return !this.isIncomplete() && this.test(input.fluid());
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return MekN.RecipeSerializers_Fiss.FLUID_COOLANT.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return MekN.RecipeTypes_Fiss.FLUID_COOLANT.get();
    }

    public double getThermalEnthalpy() {
        return thermalEnthalpy;
    }

    public double getConductivity() {
        return conductivity;
    }

    public double getEfficiency() {
        return efficiency;
    }
}
