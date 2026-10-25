package com.atom.mekn.common.datagen.loot;

import com.atom.mekn.MekN;
import com.atom.mekn.common.neoforge.register.register.ModItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ModLootTablesProvider extends LootTableProvider {

    public ModLootTablesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(
                output,
                Set.of(),
                List.of(new SubProviderEntry(Sub::new, LootContextParamSets.ENTITY)), // 使用 ENTITY 上下文
                registries
        );
    }

    // 完美实现你发的接口
    public static class Sub implements LootTableSubProvider {

        public Sub(HolderLookup.Provider registries) {
        }

        @Override
        public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
            consumer.accept(
                    ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(MekN.MODID, "additions/animal_fat")),
                    LootTable.lootTable()
                            .withPool(
                                    LootPool.lootPool()
                                            .setRolls(ConstantValue.exactly(1))
                                            .add(
                                                    // 【注意】这里的 ModItem.ANIMAL_FAT 必须换成你真实的变量名！
                                                    LootItem.lootTableItem(ModItem.ANIMAL_FAT.get())
                                                            // 50% 基础概率
                                                            .when(LootItemRandomChanceCondition.randomChance(0.5f))
                                                            // 1~3个
                                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f)))
                                            )
                            )
            );
        }
    }
}
