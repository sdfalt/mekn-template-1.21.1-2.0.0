package com.atom.mekn.common.mekn.recipes.recipe;

import com.atom.mekn.common.mekn.recipes.RecipeSerializers;
import com.atom.mekn.common.mekn.recipes.RecipeTypesRegister;
import com.atom.mekn.common.mekn.recipes.input.SuperCentrifugeRecipeInput;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.MekanismRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

@NothingNullByDefault
public class SuperCentrifugeRecipe extends MekanismRecipe<SuperCentrifugeRecipeInput> {
    private final ResourceLocation id;
    private final ChemicalStack input;
    private final ChemicalStack output;
    private final int processTicks;
    private final long energyPerTick;

    // 全参构造（供反序列化后设置 id）
    public SuperCentrifugeRecipe(ResourceLocation id, ChemicalStack input, ChemicalStack output,
                                 int processTicks, long energyPerTick) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.processTicks = processTicks;
        this.energyPerTick = energyPerTick;
    }

    // 无 id 构造（供序列化器 codec 使用）
    public SuperCentrifugeRecipe(ChemicalStack input, ChemicalStack output,
                                 int processTicks, long energyPerTick) {
        this(null, input, output, processTicks, energyPerTick);
    }

    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializers.SUPER_CENTRIFUGE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeTypesRegister.SUPER_CENTRIFUGE.get();
    }

    // 匹配 ChemicalStack：化学物质相同且输入量不少于配方要求
    public boolean matches(ChemicalStack stack) {
        return stack.is(input.getChemicalHolder()) && stack.getAmount() >= input.getAmount();
    }

    @Override
    public boolean matches(SuperCentrifugeRecipeInput wrapper, Level level) {
        return matches(wrapper.chemical());
    }

    public ChemicalStack getInput() {
        return input.copy();
    }

    public ChemicalStack getOutput() {
        return output.copy();
    }

    public int getProcessTicks() {
        return processTicks;
    }

    public long getEnergyPerTick() {
        return energyPerTick;
    }

    @Override
    public boolean isIncomplete() {
        return false;
    }

    @Override
    public ItemStack assemble(SuperCentrifugeRecipeInput input, HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    // ---------- 序列化器 ----------
    public static class Serializer implements RecipeSerializer<SuperCentrifugeRecipe> {
        // 注意：这里不包含 id 字段，id 由外部提供
        private static final MapCodec<SuperCentrifugeRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        ChemicalStack.CODEC.fieldOf("input").forGetter(SuperCentrifugeRecipe::getInput),
                        ChemicalStack.CODEC.fieldOf("output").forGetter(SuperCentrifugeRecipe::getOutput),
                        Codec.INT.fieldOf("process_ticks").forGetter(SuperCentrifugeRecipe::getProcessTicks),
                        Codec.LONG.fieldOf("energy_per_tick").forGetter(SuperCentrifugeRecipe::getEnergyPerTick)
                ).apply(instance, SuperCentrifugeRecipe::new)
        );

        private static final StreamCodec<RegistryFriendlyByteBuf, SuperCentrifugeRecipe> STREAM_CODEC = StreamCodec.of(
                (buf, recipe) -> {
                    ChemicalStack.STREAM_CODEC.encode(buf, recipe.input);
                    ChemicalStack.STREAM_CODEC.encode(buf, recipe.output);
                    buf.writeInt(recipe.processTicks);
                    buf.writeLong(recipe.energyPerTick);
                },
                buf -> new SuperCentrifugeRecipe(
                        ChemicalStack.STREAM_CODEC.decode(buf),
                        ChemicalStack.STREAM_CODEC.decode(buf),
                        buf.readInt(),
                        buf.readLong()
                )
        );

        @Override
        public MapCodec<SuperCentrifugeRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SuperCentrifugeRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}