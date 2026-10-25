package com.atom.mekn.common.compat.category; // ⚠️ 改成你的包名

import com.atom.mekn.common.mekn.chemicals.chemical.ModChemicals;
import com.atom.mekn.common.mekn.register.blocks.BlcokRegister;
import com.mojang.serialization.Codec;
import mekanism.api.chemical.ChemicalStack;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiChemicalGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.recipe_viewer.RecipeViewerUtils;
import mekanism.client.recipe_viewer.jei.BaseRecipeCategory;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.ICodecHelper;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NitrogenExtractorCategory extends BaseRecipeCategory<NitrogenExtractorCategory.Wrapper> {

    public static final RecipeType<Wrapper> TYPE = new RecipeType<>(
            ResourceLocation.parse("atom:nitrogen_extractor"),
            Wrapper.class
    );

    private final IDrawable icon;
    private final GuiGauge<?> outputGauge;
    private final GuiProgress progressBar;

    public NitrogenExtractorCategory(IGuiHelper helper) {
        super(helper, TYPE, Component.translatable("jei.atom.nitrogen_extractor"),
                helper.createDrawableIngredient(mezz.jei.api.constants.VanillaTypes.ITEM_STACK, new ItemStack(BlcokRegister.NITROGEN_EXTRACTOR.asItem())),
                0, 0, 146, 80);
        this.icon = helper.createDrawableIngredient(mezz.jei.api.constants.VanillaTypes.ITEM_STACK, new ItemStack(BlcokRegister.NITROGEN_EXTRACTOR.asItem()));
        this.outputGauge = addElement(GuiChemicalGauge.getDummy(GaugeType.STANDARD, this, 64, 5));
        this.progressBar = addSimpleProgress(ProgressType.DOWN, 40, 5);
        addElement(new GuiVerticalPowerBar(this, RecipeViewerUtils.FULL_BAR, 120, 5, 56));
    }
    @Override
    public @NotNull IDrawable getIcon() {
        return icon;
    }
    @Nullable
    @Override
    public ResourceLocation getRegistryName(@NotNull Wrapper recipe) {
        return null;
    }

    @Override
    public @NotNull Codec<Wrapper> getCodec(@NotNull ICodecHelper codecHelper, @NotNull IRecipeManager recipeManager) {
        throw new UnsupportedOperationException("Nitrogen Extractor does not have a real recipe to export.");
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull Wrapper recipe, @NotNull IFocusGroup focuses) {
        initChemical(builder, RecipeIngredientRole.OUTPUT, outputGauge, List.of(
                new ChemicalStack(ModChemicals.NITROGEN, 1000)
        ));
    }

    @Override
    public void createRecipeExtras(@NotNull IRecipeExtrasBuilder builder, @NotNull Wrapper recipe, @NotNull IFocusGroup focuses) {
        super.createRecipeExtras(builder, recipe, focuses);

        builder.addDrawable(new IDrawable() {
            @Override
            public int getWidth() { return 146; }
            @Override
            public int getHeight() { return 80; }

            @Override
            public void draw(@NotNull GuiGraphics guiGraphics, int xOffset, int yOffset) {
                int textX = xOffset + 5;
                int textY = yOffset + 62;
                guiGraphics.drawString(Minecraft.getInstance().font, "耗电: 5000 FE", textX, textY, 0xAAAAAA, false);
            }
        });
    }

    public static class Wrapper {
        public Wrapper() {}
    }
}
