package com.atom.mekn;

import com.atom.mekn.common.data.modify.AddItemLootModifier;
import com.atom.mekn.common.mekn.chemicals.chemical.ModChemicals;
import com.atom.mekn.common.mekn.chemicals.fluids.ModFluids;
import com.atom.mekn.common.mekn.fissionrecipe.recipe.FluidCoolantRecipe;
import com.atom.mekn.common.mekn.fissionrecipe.recipe.serializer.FissionRecipeSerializer;
import com.atom.mekn.common.mekn.fissionrecipe.recipe.serializer.FluidCoolantRecipeSerializer;
import com.atom.mekn.common.mekn.fissionrecipe.recipe.serializer.GasCoolantRecipeSerializer;
import com.atom.mekn.common.mekn.recipes.RecipeSerializers;
import com.atom.mekn.common.mekn.recipes.RecipeTypesRegister;
import com.atom.mekn.common.mekn.register.blocks.BlcokRegister;
import com.atom.mekn.common.mekn.register.blocks.ContainerTypeRegister;
import com.atom.mekn.common.mekn.register.blocks.TileEntityTypeRegister;
import com.atom.mekn.common.mekn.register.sounds.SoundRegister;
import com.atom.mekn.common.neoforge.register.register.ModCreativeTabs;
import com.atom.mekn.common.neoforge.register.register.ModBlocks;
import com.atom.mekn.common.neoforge.register.register.ModEntities;
import com.atom.mekn.common.neoforge.register.register.ModItem;
import mekanism.api.recipes.ChemicalToChemicalRecipe;
import mekanism.common.Mekanism;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(MekN.MODID)
public class MekN {

    public static final String MODID = "mekn";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MekN(IEventBus modEventBus, ModContainer modContainer) {

        ModChemicals.register(modEventBus);
        ModFluids.register(modEventBus);
        ModItem.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);


        //mekN
        TileEntityTypeRegister.register(modEventBus);
        BlcokRegister.register(modEventBus);
        ContainerTypeRegister.register(modEventBus);


        //先后
        RecipeTypesRegister.register(modEventBus);
        RecipeSerializers.register(modEventBus);
        RecipeSerializers_Fiss.register(modEventBus);
        RecipeTypes_Fiss.register(modEventBus);


        //sound
        SoundRegister.register(modEventBus);

        //loot
        AddItemLootModifier.GLOBAL_LOOT_MODIFIERS.register(modEventBus);

        ModEntities.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, com.atom.mekn.common.config.ModConfig.CONFIG_SPEC);
        modEventBus.addListener(this::commonSetup);
        ModCreativeTabs.register(modEventBus);
        NeoForge.EVENT_BUS.register(this);
    }


    public static class RecipeTypes_Fiss {
        private static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, Mekanism.MODID);
        public static final DeferredHolder<RecipeType<?>, RecipeType<ChemicalToChemicalRecipe>> FISSION = TYPES.register("fission", () -> RecipeType.simple(Mekanism.rl("fission")));
        public static final DeferredHolder<RecipeType<?>, RecipeType<FluidCoolantRecipe>> FLUID_COOLANT = TYPES.register("fluid_coolant", () -> RecipeType.simple(Mekanism.rl("fluid_coolant")));
        public static final DeferredHolder<RecipeType<?>, RecipeType<ChemicalToChemicalRecipe>> GAS_COOLANT = TYPES.register("gas_coolant", () -> RecipeType.simple(Mekanism.rl("gas_coolant")));

        private static void register(IEventBus bus) {
            TYPES.register(bus);
        }
    }


    public static class RecipeSerializers_Fiss {
        private static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Mekanism.MODID);
        public static final DeferredHolder<RecipeSerializer<?>, FissionRecipeSerializer> FISSION = SERIALIZERS.register("fission", FissionRecipeSerializer::create);
        public static final DeferredHolder<RecipeSerializer<?>, FluidCoolantRecipeSerializer> FLUID_COOLANT = SERIALIZERS.register("fluid_coolant", FluidCoolantRecipeSerializer::create);
        public static final DeferredHolder<RecipeSerializer<?>, GasCoolantRecipeSerializer> GAS_COOLANT = SERIALIZERS.register("gas_coolant", GasCoolantRecipeSerializer::create);

        private static void register(IEventBus bus) {
            SERIALIZERS.register(bus);
        }
    }

    private void commonSetup(FMLCommonSetupEvent event) {

        LOGGER.info("HELLO FROM COMMON SETUP");
    }
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }


}
