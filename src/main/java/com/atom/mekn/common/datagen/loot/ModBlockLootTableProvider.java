package com.atom.mekn.common.datagen.loot;

import com.atom.mekn.common.neoforge.register.register.ModBlocks;
import com.atom.mekn.common.neoforge.register.register.ModItem;
import com.atom.mekn.common.mekn.register.blocks.BlcokRegister;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static com.atom.mekn.common.mekn.register.blocks.BlcokRegister.getAllFactories;

public class ModBlockLootTableProvider {

    public static LootTableProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> reg) {
        return new LootTableProvider(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(ModBlockLoot::new, LootContextParamSets.BLOCK)
        ), reg);
    }

    public static class ModBlockLoot extends BlockLootSubProvider {
        protected ModBlockLoot(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.DEFAULT_FLAGS, registries);
        }

        @Override
        protected void generate() {
            // 普通矿石
            this.add(ModBlocks.ZIRCON_ORE.get(), block -> createOreDrop(block, ModItem.RAW_ZIRCON.get()));
            this.add(ModBlocks.BERYLLIUM_ORE.get(), block -> createOreDrop(block, ModItem.BERYLLIUM_RAW.get()));
            this.add(ModBlocks.MAGNESIUM_ORE.get(), block -> createOreDrop(block, ModItem.RAW_MAGNESIUM.get()));

            // 深层矿石
            this.add(ModBlocks.DEEPSLATE_ZIRCON_ORE.get(), block -> createOreDrop(block, ModItem.RAW_ZIRCON.get()));
            this.add(ModBlocks.DEEPSLATE_BERYLLIUM_ORE.get(), block -> createOreDrop(block, ModItem.BERYLLIUM_RAW.get()));
            this.add(ModBlocks.DEEPSLATE_MAGNESIUM_ORE.get(), block -> createOreDrop(block, ModItem.RAW_MAGNESIUM.get()));

            // 机器/方块掉落自身（或你希望的特殊处理，这里先统一掉落自身）
            this.dropSelf(BlcokRegister.NITROGEN_EXTRACTOR.get());
            this.dropSelf(BlcokRegister.NUCLEAR_ENGINEERING_TABLE.get());
            this.dropSelf(BlcokRegister.SUPER_CENTRIFUGE.get());
            this.dropSelf(BlcokRegister.ISOTOPIC_ENCAPSULATION_PRESS.get());
            this.dropSelf(ModBlocks.SUPER_CASING.get());
            // 工厂方块（遍历所有工厂）
            for (var factoryRegistryObject : getAllFactories()) {
                this.dropSelf(factoryRegistryObject.get());
            }
        }

        @Override
        protected @NotNull Iterable<Block> getKnownBlocks() {
            List<Block> blocks = new java.util.ArrayList<>(List.of(
                    ModBlocks.ZIRCON_ORE.get(),
                    ModBlocks.DEEPSLATE_ZIRCON_ORE.get(),
                    ModBlocks.BERYLLIUM_ORE.get(),
                    ModBlocks.DEEPSLATE_BERYLLIUM_ORE.get(),
                    ModBlocks.MAGNESIUM_ORE.get(),
                    ModBlocks.DEEPSLATE_MAGNESIUM_ORE.get(),
                    BlcokRegister.NITROGEN_EXTRACTOR.get(),
                    BlcokRegister.NUCLEAR_ENGINEERING_TABLE.get(),
                    BlcokRegister.SUPER_CENTRIFUGE.get(),
                    BlcokRegister.ISOTOPIC_ENCAPSULATION_PRESS.get(),
                    ModBlocks.SUPER_CASING.get()
            ));
            // 加入所有工厂方块
            BlcokRegister.FACTORIES.values().forEach(regObj -> {
                if (regObj != null) {
                    blocks.add(regObj.get());
                }
            });
            return blocks;
        }
    }
}