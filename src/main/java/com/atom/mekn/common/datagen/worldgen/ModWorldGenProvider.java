package com.atom.mekn.common.datagen.worldgen;

import com.atom.mekn.MekN;
import com.atom.mekn.common.neoforge.register.register.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.RegistryPatchGenerator;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModWorldGenProvider extends DatapackBuiltinEntriesProvider {
    public static final ResourceKey<ConfiguredFeature<?, ?>> ZIRCON_ORE_KEY = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(MekN.MODID, "zircon_ore")
    );
    public static final ResourceKey<PlacedFeature> ZIRCON_ORE_PLACED_KEY = ResourceKey.create(
            Registries.PLACED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(MekN.MODID, "zircon_ore_placed")
    );

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, bootstrap -> {
                var stoneTarget = OreConfiguration.target(
                        new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES),
                        ModBlocks.ZIRCON_ORE.get().defaultBlockState()
                );
                var deepslateTarget = OreConfiguration.target(
                        new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES),
                        ModBlocks.DEEPSLATE_ZIRCON_ORE.get().defaultBlockState()
                );
                var oreConfig = new OreConfiguration(List.of(stoneTarget, deepslateTarget), 6);
                bootstrap.register(ZIRCON_ORE_KEY, new ConfiguredFeature<>(Feature.ORE, oreConfig));
            })
            .add(Registries.PLACED_FEATURE, bootstrap -> {
                var configuredFeature = bootstrap.lookup(Registries.CONFIGURED_FEATURE)
                        .getOrThrow(ZIRCON_ORE_KEY);
                var placements = List.of(
                        CountPlacement.of(4),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64)),
                        BiomeFilter.biome()
                );
                bootstrap.register(ZIRCON_ORE_PLACED_KEY, new PlacedFeature(configuredFeature, placements));
            })
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, bootstrap -> {
                var placedFeature = bootstrap.lookup(Registries.PLACED_FEATURE)
                        .getOrThrow(ZIRCON_ORE_PLACED_KEY);
                var biomes = bootstrap.lookup(Registries.BIOME)
                        .getOrThrow(BiomeTags.IS_OVERWORLD);
                var modifier = new net.neoforged.neoforge.common.world.BiomeModifiers.AddFeaturesBiomeModifier(
                        biomes,
                        net.minecraft.core.HolderSet.direct(placedFeature),
                        GenerationStep.Decoration.UNDERGROUND_ORES
                );
                bootstrap.register(
                        ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS,
                                ResourceLocation.fromNamespaceAndPath(MekN.MODID, "add_zircon_ore")),
                        modifier
                );
            });

    public ModWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, RegistryPatchGenerator.createLookup(registries, BUILDER), Set.of(MekN.MODID));
    }
}