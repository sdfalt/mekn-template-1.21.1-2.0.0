package com.atom.mekn.common.compat.category;

import com.atom.mekn.common.mekn.recipes.RecipeTypesRegister;
import com.atom.mekn.common.mekn.recipes.recipe.IsotopicPressRecipe;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.recipe_viewer.jei.HolderRecipeCategory;
import mekanism.common.inventory.container.slot.SlotOverlay;
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

import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked")
public class IsotopicPressRecipeCategory extends HolderRecipeCategory<IsotopicPressRecipe> {

    private final GuiSlot chemicalSlot;
    private final GuiSlot itemInputSlot;
    private final GuiProgress progressBar;
    private final GuiSlot outputSlot;

    public IsotopicPressRecipeCategory(IGuiHelper helper) {
        super(helper, RecipeTypesRegister.ISOTOPIC_PRESS_TYPE.get());
        chemicalSlot = addSlot(SlotType.INPUT, 5, 20);
        itemInputSlot = addSlot(SlotType.INPUT, 27, 20);
        progressBar = addSimpleProgress(ProgressType.RIGHT, 49, 24);
        outputSlot = addSlot(SlotType.OUTPUT, 105, 20).with(SlotOverlay.PLUS);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder,
                          @NotNull RecipeHolder<IsotopicPressRecipe> recipeHolder,
                          @NotNull IFocusGroup focuses) {

        IsotopicPressRecipe recipe = recipeHolder.value();
        initChemical(builder, RecipeIngredientRole.INPUT, chemicalSlot, Collections.singletonList(recipe.getGasInput()));
        initItem(builder, RecipeIngredientRole.INPUT, itemInputSlot, List.of(recipe.getItemInput()));
        initItem(builder, RecipeIngredientRole.OUTPUT, outputSlot, List.of(recipe.getOutput()));
    }

    @Override
    public void createRecipeExtras(@NotNull IRecipeExtrasBuilder builder, @NotNull RecipeHolder<IsotopicPressRecipe> recipeHolder, IFocusGroup focuses) {
        super.createRecipeExtras(builder, recipeHolder, focuses);

        IsotopicPressRecipe recipe = recipeHolder.value();
        int ticks = recipe.getProcessTicks();
        double seconds = ticks / 20.0;
        long totalEnergy = 1000000L;
        builder.addDrawable(new IDrawable() {
            @Override
            public int getWidth() { return 150; }
            @Override
            public int getHeight() { return 20; }
            @Override
            public void draw(@NotNull GuiGraphics guiGraphics, int xOffset, int yOffset) {
                int textY = yOffset + 42;
                guiGraphics.drawString(Minecraft.getInstance().font, "时间: " + seconds + "s", xOffset + 5, textY, 0xFFFFFF, false);
                guiGraphics.drawString(Minecraft.getInstance().font, "耗电: " + totalEnergy + " fe", xOffset + 60, textY, 0xAAAAAA, false);
            }
        });
    }
}
