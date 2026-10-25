package com.atom.mekn.common.compat.category;

import com.atom.mekn.common.mekn.recipes.RecipeTypesRegister;
import com.atom.mekn.common.mekn.recipes.recipe.SuperCentrifugeRecipe;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiChemicalGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.recipe_viewer.RecipeViewerUtils;
import mekanism.client.recipe_viewer.jei.HolderRecipeCategory;
import mekanism.common.tile.component.config.DataType;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("unchecked")
public class SuperCentrifugeRecipeCategory extends HolderRecipeCategory<SuperCentrifugeRecipe> {

    private final GuiGauge<?> inputGauge;
    private final GuiGauge<?> outputGauge;
    private final GuiProgress progressBar;
    public SuperCentrifugeRecipeCategory(IGuiHelper helper) {
        super(helper, RecipeTypesRegister.SUPER_CENTRIFUGE.get());
        inputGauge = addElement(GuiChemicalGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT), this, 5, 5));
        outputGauge = addElement(GuiChemicalGauge.getDummy(GaugeType.STANDARD.with(DataType.OUTPUT), this, 115, 5));
        progressBar = addSimpleProgress(ProgressType.RIGHT, 55, 30);
        addElement(new GuiVerticalPowerBar(this, RecipeViewerUtils.FULL_BAR, 135, 5, 56));
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder,
                          @NotNull RecipeHolder<SuperCentrifugeRecipe> recipeHolder,
                          @NotNull IFocusGroup focuses) {

        SuperCentrifugeRecipe recipe = recipeHolder.value();
        initChemical(builder, RecipeIngredientRole.INPUT, inputGauge, List.of(recipe.getInput()));
        initChemical(builder, RecipeIngredientRole.OUTPUT, outputGauge, List.of(recipe.getOutput()));
    }


    @Override
    public void createRecipeExtras(@NotNull IRecipeExtrasBuilder builder, @NotNull RecipeHolder<SuperCentrifugeRecipe> recipeHolder, IFocusGroup focuses) {
        super.createRecipeExtras(builder, recipeHolder, focuses);

        SuperCentrifugeRecipe recipe = recipeHolder.value();
        int ticks = recipe.getProcessTicks();
        double seconds = ticks / 20.0;
        long totalEnergy = recipe.getEnergyPerTick() * ticks;

        builder.addDrawable(new IDrawable() {
            @Override
            public int getWidth() { return 150; }
            @Override
            public int getHeight() { return 20; }

            @Override
            public void draw(@NotNull GuiGraphics guiGraphics, int xOffset, int yOffset) {
                int textX = xOffset + 5;
                int textY = yOffset + 66;

                guiGraphics.drawString(Minecraft.getInstance().font, "时间: " + seconds + "s", textX, textY, 0xFFFFFF, false);
                guiGraphics.drawString(Minecraft.getInstance().font, "耗电: " + totalEnergy + " fe", textX + 70, textY, 0xAAAAAA, false);
            }
        });
    }
}
