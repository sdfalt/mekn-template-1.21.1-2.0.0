package com.atom.mekn.common.compat.category;

import com.atom.mekn.common.mekn.recipes.RecipeTypesRegister;
import com.atom.mekn.common.mekn.recipes.recipe.BasicNuclearRecipe;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiChemicalGauge;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.recipe_viewer.RecipeViewerUtils;
import mekanism.client.recipe_viewer.jei.HolderRecipeCategory;
import mekanism.common.tile.component.config.DataType;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class NuclearRecipeCategory extends HolderRecipeCategory<BasicNuclearRecipe> {

    private final GuiGauge<?> fluidGauge;
    private final GuiGauge<?> chemicalGauge;
    private GuiSlot outputSlot;
    private final GuiProgress progressBar;
    private final List<GuiSlot> inputSlots = new ArrayList<>();

    public NuclearRecipeCategory(IGuiHelper helper) {
        super(helper, RecipeTypesRegister.NUCLEAR_ENGINEERING.get());

        // 5x5 物品输入
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                inputSlots.add(addSlot(SlotType.INPUT, 5 + col * 18, 5 + row * 18));
            }
        }

        outputSlot = addSlot(SlotType.OUTPUT, 170, 75);
        fluidGauge = addElement(GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT), this, 110, 10));
        chemicalGauge = addElement(GuiChemicalGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT_2), this, 130, 10));
        progressBar = addSimpleProgress(ProgressType.RIGHT, 120, 79);
        addElement(new GuiVerticalPowerBar(this, RecipeViewerUtils.FULL_BAR, 150, 10, 56));
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder,
                          @NotNull RecipeHolder<BasicNuclearRecipe> recipeHolder,
                          @NotNull IFocusGroup focuses) {

        BasicNuclearRecipe recipe = recipeHolder.value();
        List<ItemStackIngredient> itemInputs = recipe.getItemInputs();
        for (int i = 0; i < itemInputs.size() && i < inputSlots.size(); i++) {
            initItem(builder, RecipeIngredientRole.INPUT, inputSlots.get(i), itemInputs.get(i).getRepresentations());
        }

        initFluid(builder, RecipeIngredientRole.INPUT, fluidGauge, recipe.getFluidInput().getRepresentations());
        initChemical(builder, RecipeIngredientRole.INPUT, chemicalGauge, recipe.getChemicalInput().getRepresentations());
        initItem(builder, RecipeIngredientRole.OUTPUT, outputSlot, List.of(recipe.getOutput()));

    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, @NotNull RecipeHolder<BasicNuclearRecipe> recipeHolder, IFocusGroup focuses) {
        super.createRecipeExtras(builder, recipeHolder, focuses);

        BasicNuclearRecipe recipe = recipeHolder.value();
        int ticks = recipe.getTicksRequired();
        double seconds = ticks / 20.0;
        long totalEnergy = recipe.getEnergyPerTick() * ticks;

        builder.addDrawable(new IDrawable() {
            @Override
            public int getWidth() { return 100; }
            @Override
            public int getHeight() { return 30; }
            @Override
            public void draw(@NotNull GuiGraphics guiGraphics, int xOffset, int yOffset) {
                int textX = xOffset + 110;
                int textY = yOffset + 100;

                guiGraphics.drawString(net.minecraft.client.Minecraft.getInstance().font, "时间: " + seconds + "s", textX, textY, 0xFFFFFF, false);
                guiGraphics.drawString(net.minecraft.client.Minecraft.getInstance().font, "耗电: " + totalEnergy + " J", textX, textY + 12, 0xAAAAAA, false);
            }
        });
    }

}
