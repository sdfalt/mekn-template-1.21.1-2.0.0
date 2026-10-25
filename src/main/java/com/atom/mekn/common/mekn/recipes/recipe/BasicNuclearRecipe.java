package com.atom.mekn.common.mekn.recipes.recipe;

import com.atom.mekn.common.mekn.recipes.RecipeSerializers;
import com.atom.mekn.common.mekn.recipes.RecipeTypesRegister;
import com.atom.mekn.common.mekn.recipes.input.NuclearRecipeInput;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.common.Mekanism;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BasicNuclearRecipe extends MekanismRecipe<NuclearRecipeInput> {

    // 1. 定义配方要求
    private final List<ItemStackIngredient> itemInputs; // 必须是 25 个
    private final FluidStackIngredient fluidInput;
    private final ChemicalStackIngredient chemicalInput;

    // 2. 定义配方产出
    private final ItemStack outputItem;

    private int ticksRequired;      // 加工所需总 tick 数
    private long energyPerTick;     // 每 tick 消耗能量

    public BasicNuclearRecipe(List<ItemStackIngredient> itemInputs,
                              FluidStackIngredient fluidInput,
                              ChemicalStackIngredient chemicalInput,
                              ItemStack outputItem,int ticksRequired, long energyPerTick) {
        System.out.println("✅ 成功加载了一个核工程配方！输出物：" + outputItem.getItem());
        System.out.println("【原子模组日志】成功加载核工程配方，输出物: " + outputItem.getDescriptionId());
        this.itemInputs = itemInputs;
        this.fluidInput = fluidInput;
        this.chemicalInput = chemicalInput;
        this.outputItem = outputItem;
        this.ticksRequired = ticksRequired;
        this.energyPerTick = energyPerTick;
    }
    public int getTicksRequired() { return ticksRequired; }
    public long getEnergyPerTick() { return energyPerTick; }

    public List<ItemStackIngredient> getItemInputs() {
        return itemInputs;
    }

    public FluidStackIngredient getFluidInput() {
        return fluidInput;
    }

    public ChemicalStackIngredient getChemicalInput() {
        return chemicalInput;
    }

    /**
     * 最关键的方法：判断当前的机器状态是否匹配本配方
     */
    @Override
    public boolean matches(NuclearRecipeInput input, @NotNull Level level) {
        // 1. 检查流体和化学品
        if (!fluidInput.test(input.getFluid())) {
            Mekanism.logger.debug("Fluid mismatch: required {} but got {}", fluidInput, input.getFluid());
            return false;
        }
        if (!chemicalInput.test(input.getChemical())) {
            Mekanism.logger.debug("Chemical mismatch: required {} but got {}", chemicalInput, input.getChemical());
            return false;
        }

        // 2. 检查物品输入
        for (ItemStackIngredient ingredient : itemInputs) {
            long needed = ingredient.ingredient().count();
            long total = 0;
            for (int i = 0; i < input.size(); i++) {
                ItemStack stack = input.getItem(i);
                if (!stack.isEmpty() && ingredient.testType(stack)) {  // 改为 testType
                    total += stack.getCount();
                }
            }
            if (total < needed) {
                Mekanism.logger.debug("Item mismatch: needed {} but found {} for ingredient {}", needed, total, ingredient);
                return false;
            }
        }
        return true;
    }
    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeSerializers.NUCLEAR_SERIALIZER.get();
    }
    @Override
    public @NotNull ItemStack getResultItem(@NotNull HolderLookup.Provider registries) {
        return outputItem;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeTypesRegister.NUCLEAR_ENGINEERING.get();
    }
    @Override
    public boolean isIncomplete() {
        boolean itemsIncomplete = itemInputs.stream().anyMatch(ItemStackIngredient::hasNoMatchingInstances);
        boolean fluidIncomplete = fluidInput.hasNoMatchingInstances();
        boolean chemicalIncomplete = chemicalInput.hasNoMatchingInstances();
        return itemsIncomplete || fluidIncomplete || chemicalIncomplete;
    }
    @Override
    public @NotNull ItemStack assemble(@NotNull NuclearRecipeInput input, @NotNull HolderLookup.Provider registries) {
        return outputItem.copy();
    }

    public ItemStack getOutput() {
        return outputItem;
    }

    public long getItemAmount(ItemStackIngredient ingredient) {
        return ingredient.ingredient().count();
    }

    public static class Serializer implements RecipeSerializer<BasicNuclearRecipe> {

        private static final MapCodec<BasicNuclearRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ItemStackIngredient.CODEC.listOf().fieldOf("itemInputs").forGetter(r -> r.itemInputs),
                FluidStackIngredient.CODEC.fieldOf("fluidInput").forGetter(r -> r.fluidInput),
                ChemicalStackIngredient.CODEC.fieldOf("chemicalInput").forGetter(r -> r.chemicalInput),
                ItemStack.CODEC.fieldOf("output").forGetter(r -> r.outputItem),
                Codec.INT.fieldOf("ticks").forGetter(BasicNuclearRecipe::getTicksRequired),      // 新增
                Codec.LONG.fieldOf("energy_per_tick").forGetter(BasicNuclearRecipe::getEnergyPerTick)
        ).apply(instance, BasicNuclearRecipe::new));
        private static final StreamCodec<RegistryFriendlyByteBuf, BasicNuclearRecipe> STREAM_CODEC = StreamCodec.composite(
                ItemStackIngredient.STREAM_CODEC.apply(net.minecraft.network.codec.ByteBufCodecs.list()), BasicNuclearRecipe::getItemInputs,
                FluidStackIngredient.STREAM_CODEC, BasicNuclearRecipe::getFluidInput,
                ChemicalStackIngredient.STREAM_CODEC, BasicNuclearRecipe::getChemicalInput,
                ItemStack.STREAM_CODEC, BasicNuclearRecipe::getOutput, // 统一使用 getter
                ByteBufCodecs.VAR_INT, BasicNuclearRecipe::getTicksRequired,
                ByteBufCodecs.VAR_LONG, BasicNuclearRecipe::getEnergyPerTick,
                BasicNuclearRecipe::new
        );


        @Override
        public @NotNull MapCodec<BasicNuclearRecipe> codec() { return CODEC; }
        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, BasicNuclearRecipe> streamCodec() { return STREAM_CODEC; }
    }
}