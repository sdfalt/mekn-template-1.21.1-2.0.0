package com.atom.mekn.common.mekn.recipes;


import com.atom.mekn.MekN;
import com.atom.mekn.common.mekn.recipes.recipe.BasicNuclearRecipe;
import com.atom.mekn.common.mekn.recipes.recipe.IsotopicPressRecipe;
import com.atom.mekn.common.mekn.recipes.recipe.SuperCentrifugeRecipe;
import com.atom.mekn.common.mekn.recipes.recipe.recipetype.IsotopicPressRecipeType;
import com.atom.mekn.common.mekn.recipes.recipe.recipetype.NuclearRecipeType;
import com.atom.mekn.common.mekn.recipes.recipe.recipetype.SuperCentrifugeRecipeType;
import com.atom.mekn.common.utils.Modutils;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class RecipeTypesRegister{
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, MekN.MODID);

    public static final DeferredHolder<RecipeType<?>, NuclearRecipeType<BasicNuclearRecipe>> NUCLEAR_ENGINEERING =
            RECIPE_TYPES.register("nuclear_engineering", () -> new NuclearRecipeType<>(Modutils.rl("nuclear_engineering")));

    public static final DeferredHolder<RecipeType<?>, SuperCentrifugeRecipeType<SuperCentrifugeRecipe>> SUPER_CENTRIFUGE =
            RECIPE_TYPES.register("super_centrifuge", () -> new SuperCentrifugeRecipeType<>(Modutils.rl("super_centrifuge")));

    public static final DeferredHolder<RecipeType<?>, IsotopicPressRecipeType<IsotopicPressRecipe>> ISOTOPIC_PRESS_TYPE =
            RECIPE_TYPES.register("isotopic_pressing", () -> new IsotopicPressRecipeType<>(Modutils.rl("isotopic_pressing")));
    public static void register(IEventBus bus) {
        RECIPE_TYPES.register(bus);
    }
}
