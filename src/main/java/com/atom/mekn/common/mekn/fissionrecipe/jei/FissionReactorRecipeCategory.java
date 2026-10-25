package com.atom.mekn.common.mekn.fissionrecipe.jei;

import com.atom.mekn.MekN;
import com.atom.mekn.common.mekn.fissionrecipe.recipe.FluidCoolantRecipe;
import com.atom.mekn.common.mekn.fissionrecipe.recipe.GasCoolantRecipe;
import com.mojang.serialization.Codec;
import mekanism.api.heat.HeatAPI;
import mekanism.api.text.EnumColor;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiChemicalGauge;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.recipe_viewer.jei.BaseRecipeCategory;
import mekanism.common.MekanismLang;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UnitDisplayUtils;
import mekanism.common.util.text.BooleanStateDisplay;
import mekanism.common.util.text.TextUtils;
import mekanism.generators.common.GeneratorsLang;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.ICodecHelper;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FissionReactorRecipeCategory extends BaseRecipeCategory<FissionJEIRecipe> {
    private final GuiGauge<?> coolantTank;
    private final GuiGauge<?> fuelTank;
    private final GuiGauge<?> heatedCoolantTank;
    private final GuiGauge<?> wasteTank;

    public FissionReactorRecipeCategory(IGuiHelper helper) {
        super(helper, FissionRecipeViewType.FISSION);
        addElement(new GuiInnerScreen(this, 45, 17, 105, 56, () -> List.of(
                MekanismLang.STATUS.translate(EnumColor.BRIGHT_GREEN, BooleanStateDisplay.ActiveDisabled.of(true)),
                GeneratorsLang.GAS_BURN_RATE.translate(1.0),
                GeneratorsLang.FISSION_HEATING_RATE.translate(0),
                MekanismLang.TEMPERATURE.translate(EnumColor.BRIGHT_GREEN, MekanismUtils.getTemperatureDisplay(HeatAPI.AMBIENT_TEMP, UnitDisplayUtils.TemperatureUnit.KELVIN, true)),
                GeneratorsLang.FISSION_DAMAGE.translate(EnumColor.BRIGHT_GREEN, TextUtils.getPercent(0))
        )).spacing(1));
        coolantTank = addElement(GuiFluidGauge.getDummy(GaugeType.STANDARD, this, 6, 13).setLabel(GeneratorsLang.FISSION_COOLANT_TANK.translateColored(EnumColor.AQUA)));
        fuelTank = addElement(GuiChemicalGauge.getDummy(GaugeType.STANDARD, this, 25, 13).setLabel(GeneratorsLang.FISSION_FUEL_TANK.translateColored(EnumColor.DARK_GREEN)));
        heatedCoolantTank = addElement(GuiChemicalGauge.getDummy(GaugeType.STANDARD, this, 152, 13).setLabel(GeneratorsLang.FISSION_HEATED_COOLANT_TANK.translateColored(EnumColor.GRAY)));
        wasteTank = addElement(GuiChemicalGauge.getDummy(GaugeType.STANDARD, this, 171, 13).setLabel(GeneratorsLang.FISSION_WASTE_TANK.translateColored(EnumColor.BROWN)));
    }

    public static List<FluidCoolantRecipe> getFluidCoolants() {
        return Objects.requireNonNull(Minecraft.getInstance().getConnection()).getRecipeManager().getAllRecipesFor(MekN.RecipeTypes_Fiss.FLUID_COOLANT.get()).stream().map(RecipeHolder::value).toList();
    }

    public static List<GasCoolantRecipe> getGasCoolants() {
        return Objects.requireNonNull(Minecraft.getInstance().getConnection()).getRecipeManager().getAllRecipesFor(MekN.RecipeTypes_Fiss.GAS_COOLANT.get()).stream().map(holder -> (GasCoolantRecipe) holder.value()).toList();
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, FissionJEIRecipe recipe, @NotNull IFocusGroup focusGroup) {
        if (recipe.isFluidCoolant()) {
            initFluid(builder, RecipeIngredientRole.INPUT, coolantTank, recipe.getFluidInputs());
        } else {
            initChemical(builder, RecipeIngredientRole.INPUT, coolantTank, recipe.getGasInputs());
        }
        initChemical(builder, RecipeIngredientRole.INPUT, fuelTank, recipe.getInput().getRepresentations());
        initChemical(builder, RecipeIngredientRole.OUTPUT, heatedCoolantTank, Collections.singletonList(recipe.getOutput()));
        initChemical(builder, RecipeIngredientRole.OUTPUT, wasteTank, Collections.singletonList(recipe.getOutputRaw()));
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName(@NotNull FissionJEIRecipe recipe) {
        return recipe.getId();
    }

    @Override
    public @NotNull Codec<FissionJEIRecipe> getCodec(@NotNull ICodecHelper codecHelper, @NotNull IRecipeManager recipeManager) {
        // FissionJEIRecipe is a wrapper for JEI display only, no serialization needed
        return Codec.unit(() -> null);
    }
}
