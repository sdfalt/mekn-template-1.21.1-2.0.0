package com.atom.mekn.common.mekn.recipes.recipe.recipetype;

import com.atom.mekn.common.mekn.recipes.input.NuclearRecipeInput;
import com.atom.mekn.common.mekn.recipes.recipe.BasicNuclearRecipe;
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

@SuppressWarnings("unchecked")
public class NuclearRecipeType<RECIPE extends MekanismRecipe<NuclearRecipeInput>> implements RecipeType<RECIPE>, IRecipeViewerRecipeType<RECIPE> {

    private final ResourceLocation registryName;

    public NuclearRecipeType(ResourceLocation registryName) {
        this.registryName = registryName;
    }

    @Override
    public ResourceLocation id() {
        return registryName;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends RECIPE> recipeClass() {
        return (Class<? extends RECIPE>) BasicNuclearRecipe.class;
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
        return 194;
    }

    @Override
    public int height() {
        return 120;
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
    public @NotNull Component getTextComponent() {
        return Component.translatable("jei.mekn.nuclear_engineering");
    }

    @Override
    public ResourceLocation icon() {
        return null; // 不使用路径图标，改用 iconStack
    }

    @Override
    public ItemStack iconStack() {
        return new ItemStack(BlcokRegister.NUCLEAR_ENGINEERING_TABLE.get());
    }

    /** 该配方关联的工作方块列表，用于 JEI 的催化剂点击区域 */
    @Override
    public @NotNull List<ItemLike> workstations() {
        return List.of(BlcokRegister.NUCLEAR_ENGINEERING_TABLE.get());
    }


}