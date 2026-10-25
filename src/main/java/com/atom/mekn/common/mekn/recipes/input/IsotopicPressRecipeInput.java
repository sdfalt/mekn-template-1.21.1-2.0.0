package com.atom.mekn.common.mekn.recipes.input;

import mekanism.api.chemical.ChemicalStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

public record IsotopicPressRecipeInput(
        ItemStack item,        // 输入槽物品
        ChemicalStack chemical // 氮气罐中的气体
) implements RecipeInput {

    public static IsotopicPressRecipeInput from(ItemStack stack, ChemicalStack gas) {
        return new IsotopicPressRecipeInput(stack.copy(), gas.copy());
    }

    @Override
    public @NotNull ItemStack getItem(int index) {
        return index == 0 ? item : ItemStack.EMPTY;
    }

    @Override
    public int size() {
        return 1;
    }
}