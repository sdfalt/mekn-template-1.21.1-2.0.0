package com.atom.mekn.common.mekn.fissionrecipe.recipe.serializer;

import com.atom.mekn.common.mekn.fissionrecipe.misc.Heat;
import com.atom.mekn.common.mekn.fissionrecipe.recipe.FissionRecipe;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mekanism.api.SerializationConstants;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.ChemicalToChemicalRecipe;
import mekanism.api.recipes.basic.BasicChemicalToChemicalRecipe;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;

public record FissionRecipeSerializer(MapCodec<FissionRecipe> codec, StreamCodec<RegistryFriendlyByteBuf, FissionRecipe> streamCodec) implements RecipeSerializer<FissionRecipe> {
    public static FissionRecipeSerializer create() {
        return new FissionRecipeSerializer(RecordCodecBuilder.mapCodec(instance -> instance.group(
                IngredientCreatorAccess.chemicalStack().codec().fieldOf(SerializationConstants.INPUT).forGetter(FissionRecipe::getInput),
                ChemicalStack.MAP_CODEC.fieldOf(SerializationConstants.OUTPUT).forGetter(FissionRecipe::getOutputRaw),
                Codec.either(Codec.DOUBLE, Codec.STRING).fieldOf("heat").forGetter(recipe -> recipe.getHeat().getEither())
        ).apply(instance, FissionRecipe::new)), StreamCodec.composite(
                IngredientCreatorAccess.chemicalStack().streamCodec(), ChemicalToChemicalRecipe::getInput,
                ChemicalStack.STREAM_CODEC, BasicChemicalToChemicalRecipe::getOutputRaw,
                Heat.STREAM_CODEC, FissionRecipe::getHeat,
                FissionRecipe::new
        ));
    }
}
