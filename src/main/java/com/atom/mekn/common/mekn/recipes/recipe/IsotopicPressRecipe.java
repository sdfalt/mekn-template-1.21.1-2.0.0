package com.atom.mekn.common.mekn.recipes.recipe;

import com.atom.mekn.common.mekn.recipes.RecipeSerializers;
import com.atom.mekn.common.mekn.recipes.RecipeTypesRegister;
import com.atom.mekn.common.mekn.recipes.input.IsotopicPressRecipeInput;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.MekanismRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

@NothingNullByDefault
public class IsotopicPressRecipe extends MekanismRecipe<IsotopicPressRecipeInput> {
    private final ItemStack itemInput;
    private final ChemicalStack gasInput;
    private final ItemStack output;
    private final int processTicks;

    public IsotopicPressRecipe(ItemStack itemInput, ChemicalStack gasInput, ItemStack output, int processTicks) {
        this.itemInput = itemInput;
        this.gasInput = gasInput;
        this.output = output;
        this.processTicks = processTicks;
    }

    @Override
    public boolean matches(IsotopicPressRecipeInput input, Level level) {
        return ItemStack.isSameItemSameComponents(this.itemInput, input.item()) &&
                input.item().getCount() >= this.itemInput.getCount() &&
                input.chemical().is(this.gasInput.getChemicalHolder()) &&
                input.chemical().getAmount() >= this.gasInput.getAmount();
    }

    @Override
    public ItemStack assemble(IsotopicPressRecipeInput input, HolderLookup.Provider provider) {
        return output.copy();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializers.ISOTOPIC_PRESS_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeTypesRegister.ISOTOPIC_PRESS_TYPE.get();
    }

    // Getter 供 Serializer 使用
    public ItemStack getItemInput() { return itemInput; }
    public ChemicalStack getGasInput() { return gasInput; }
    public ItemStack getOutput() { return output; }
    public int getProcessTicks() { return processTicks; }

    @Override
    public boolean isIncomplete() { return false; }

    // ---------- 序列化器 ----------
    public static class Serializer implements RecipeSerializer<IsotopicPressRecipe> {
        private static final MapCodec<IsotopicPressRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        ItemStack.CODEC.fieldOf("item_input").forGetter(IsotopicPressRecipe::getItemInput),
                        ChemicalStack.CODEC.fieldOf("gas_input").forGetter(IsotopicPressRecipe::getGasInput),
                        ItemStack.CODEC.fieldOf("output").forGetter(IsotopicPressRecipe::getOutput),
                        Codec.INT.fieldOf("process_ticks").forGetter(IsotopicPressRecipe::getProcessTicks)
                ).apply(instance, IsotopicPressRecipe::new)
        );

        private static final StreamCodec<RegistryFriendlyByteBuf, IsotopicPressRecipe> STREAM_CODEC = StreamCodec.of(
                (buf, recipe) -> {
                    ItemStack.STREAM_CODEC.encode(buf, recipe.itemInput);
                    ChemicalStack.STREAM_CODEC.encode(buf, recipe.gasInput);
                    ItemStack.STREAM_CODEC.encode(buf, recipe.output);
                    buf.writeInt(recipe.processTicks);
                },
                buf -> new IsotopicPressRecipe(
                        ItemStack.STREAM_CODEC.decode(buf),
                        ChemicalStack.STREAM_CODEC.decode(buf),
                        ItemStack.STREAM_CODEC.decode(buf),
                        buf.readInt()
                )
        );

        @Override
        public MapCodec<IsotopicPressRecipe> codec() { return CODEC; }
        @Override
        public StreamCodec<RegistryFriendlyByteBuf, IsotopicPressRecipe> streamCodec() { return STREAM_CODEC; }
    }
}