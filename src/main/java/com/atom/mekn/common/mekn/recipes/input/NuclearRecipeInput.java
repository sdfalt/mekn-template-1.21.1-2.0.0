package com.atom.mekn.common.mekn.recipes.input;

import mekanism.api.chemical.ChemicalStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record NuclearRecipeInput(
        List<ItemStack> items, // 25个物品
        FluidStack fluid,      // 机油
        ChemicalStack chemical // 氮气
) implements RecipeInput {

    public static NuclearRecipeInput from(java.util.List<mekanism.api.inventory.IInventorySlot> slots,
                                          mekanism.api.fluid.IExtendedFluidTank fluidTank,
                                          mekanism.api.chemical.IChemicalTank chemicalTank) {

        // 1. 将 Slot 列表转换为 ItemStack 列表
        List<ItemStack> itemStacks = slots.stream()
                .map(mekanism.api.inventory.IInventorySlot::getStack)
                .map(ItemStack::copy) // 建议拷贝，防止配方匹配过程中意外修改原堆栈
                .toList();

        // 2. 获取流体和化学品的快照
        return new NuclearRecipeInput(
                itemStacks,
                fluidTank.getFluid().copy(),
                chemicalTank.getStack().copy()
        );
    }
    @Override
    public @NotNull ItemStack getItem(int index) {
        return items.get(index);
    }

    @Override
    public int size() {
        return items.size();
    }

    public FluidStack getFluid() {
        return fluid;
    }

    public ChemicalStack getChemical() {
        return chemical;
    }
}