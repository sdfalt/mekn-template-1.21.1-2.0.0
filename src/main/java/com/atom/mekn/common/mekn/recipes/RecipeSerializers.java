package com.atom.mekn.common.mekn.recipes;

import com.atom.mekn.MekN;

import com.atom.mekn.common.mekn.recipes.recipe.BasicNuclearRecipe;
import com.atom.mekn.common.mekn.recipes.recipe.IsotopicPressRecipe;
import com.atom.mekn.common.mekn.recipes.recipe.SuperCentrifugeRecipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, MekN.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BasicNuclearRecipe>> NUCLEAR_SERIALIZER =
            SERIALIZERS.register("nuclear_engineering", BasicNuclearRecipe.Serializer::new);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<SuperCentrifugeRecipe>> SUPER_CENTRIFUGE_SERIALIZER =
            SERIALIZERS.register("super_centrifuge", SuperCentrifugeRecipe.Serializer::new);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<IsotopicPressRecipe>> ISOTOPIC_PRESS_SERIALIZER =
            SERIALIZERS.register("isotopic_pressing", IsotopicPressRecipe.Serializer::new);
    public static void register(IEventBus bus) {
        SERIALIZERS.register(bus);
    }
}
