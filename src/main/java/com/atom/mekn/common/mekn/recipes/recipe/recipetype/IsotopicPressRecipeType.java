package com.atom.mekn.common.mekn.recipes.recipe.recipetype;

import com.atom.mekn.common.mekn.recipes.input.IsotopicPressRecipeInput;
import com.atom.mekn.common.mekn.recipes.recipe.IsotopicPressRecipe;
import com.atom.mekn.common.mekn.register.blocks.BlcokRegister;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.client.recipe_viewer.type.IRecipeViewerRecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class IsotopicPressRecipeType<RECIPE extends MekanismRecipe<IsotopicPressRecipeInput>> implements RecipeType<RECIPE>, IRecipeViewerRecipeType<RECIPE> {

    private final ResourceLocation registryName;

    public IsotopicPressRecipeType(ResourceLocation registryName) {
        this.registryName = registryName;
    }

    @Override
    public ResourceLocation id() {
        return registryName;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends RECIPE> recipeClass() {
        return (Class<? extends RECIPE>) IsotopicPressRecipe.class;
    }

    @Override
    public String toString() {
        return registryName.toString();
    }

    @Override
    public boolean requiresHolder() {
        return true;
    }

    @Override
    public int width() {
        return 130;
    }

    @Override
    public int height() {
        return 60;
    }

    @Override
    public int xOffset() {
        return 0;
    }

    @Override
    public int yOffset() {
        return 0;
    }

    @Override
    public Component getTextComponent() {
        return Component.translatable("jei.mekn.isotopic_press"); // 记得在 zh_cn.json 里加翻译
    }

    @Override
    public ResourceLocation icon() {
        return null;
    }

    @Override
    public ItemStack iconStack() {
        // ⚠️ 把这里换成你离心机的方块注册名！
        return new ItemStack(BlcokRegister.ISOTOPIC_ENCAPSULATION_PRESS.get());
    }

    @Override
    public @NotNull List<ItemLike> workstations() {
        // ⚠️ 把这里换成你离心机的方块注册名！
        return List.of(BlcokRegister.ISOTOPIC_ENCAPSULATION_PRESS.get());
    }
}
