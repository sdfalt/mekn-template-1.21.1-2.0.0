package com.atom.mekn.common.mekn.recipes.recipe.recipetype;

import com.atom.mekn.common.mekn.recipes.input.SuperCentrifugeRecipeInput;
import com.atom.mekn.common.mekn.recipes.recipe.SuperCentrifugeRecipe;
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

public class SuperCentrifugeRecipeType<RECIPE extends MekanismRecipe<SuperCentrifugeRecipeInput>>
        implements RecipeType<RECIPE>, IRecipeViewerRecipeType<RECIPE> {

    private final ResourceLocation id;

    public SuperCentrifugeRecipeType(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public ResourceLocation id() {
        return id;
    }

    @Override
    public @NotNull String toString() {
        return id.toString();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends RECIPE> recipeClass() {
        return (Class<? extends RECIPE>) (Class<?>) SuperCentrifugeRecipe.class;
    }

    @Override
    public boolean requiresHolder() { return true; }

    @Override
    public int width() { return 150; }

    @Override
    public int height() { return 80; }

    @Override
    public int xOffset() { return 0; }

    @Override
    public int yOffset() { return 0; }

    @Override
    public @NotNull Component getTextComponent() {
        return Component.translatable("jei.mekn.super_centrifuge");
    }

    @Override
    public ResourceLocation icon() { return null; }

    @Override
    public ItemStack iconStack() {
        return new ItemStack(BlcokRegister.SUPER_CENTRIFUGE.get());
    }

    @Override
    public @NotNull List<ItemLike> workstations() {
        return List.of(BlcokRegister.SUPER_CENTRIFUGE.get());
    }
}
