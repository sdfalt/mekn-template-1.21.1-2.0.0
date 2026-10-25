package com.atom.mekn.common.mekn.recipes.input;

import mekanism.api.chemical.ChemicalStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

public record SuperCentrifugeRecipeInput(ChemicalStack chemical) implements RecipeInput {
    @Override
    public int size() {
        return 1; // 化学输入视为一个元素
    }

    @Override
    @NotNull
    public ItemStack getItem(int index) {
        return ItemStack.EMPTY; // 不返回物品，仅满足接口
    }
}