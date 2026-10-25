package com.atom.mekn.common.mekn.chemicals.fluids;

import com.atom.mekn.MekN;
import com.atom.mekn.common.mekn.chemicals.ChemicalConstants;
import mekanism.common.registration.impl.FluidDeferredRegister;
import mekanism.common.registration.impl.FluidRegistryObject;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.block.LiquidBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public class ModFluids {
    public static final FluidDeferredRegister FLUIDS = new FluidDeferredRegister(MekN.MODID);

    public static final FluidRegistryObject<FluidDeferredRegister.MekanismFluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing, LiquidBlock, BucketItem> MACHINE_OIL = FLUIDS.registerLiquidChemical(ChemicalConstants.MACHINE_OIL);
    public static final FluidRegistryObject<FluidDeferredRegister.MekanismFluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing, LiquidBlock, BucketItem> NITROGEN = FLUIDS.registerLiquidChemical(ChemicalConstants.NITROGEN);
    public static final FluidRegistryObject<FluidDeferredRegister.MekanismFluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing, LiquidBlock, BucketItem> HIGHLY_ENRICHED_URANIUM = FLUIDS.registerLiquidChemical(ChemicalConstants.HIGHLY_ENRICHED_URANIUM);
    public static final FluidRegistryObject<FluidDeferredRegister.MekanismFluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing, LiquidBlock, BucketItem> NITRIC_ACID = FLUIDS.registerLiquidChemical(ChemicalConstants.NITRIC_ACID);

    public static void register(IEventBus bus) {
        FLUIDS.register(bus);
    }

}