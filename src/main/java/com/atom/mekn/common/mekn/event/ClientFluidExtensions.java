package com.atom.mekn.common.mekn.event;

import com.atom.mekn.MekN;
import com.atom.mekn.common.mekn.chemicals.fluids.ModFluids;
import mekanism.client.ClientRegistrationUtil;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;


@EventBusSubscriber(modid = MekN.MODID, value = Dist.CLIENT)
public class ClientFluidExtensions {
    @SubscribeEvent
    public static void registerFluidExtensions(RegisterClientExtensionsEvent event) {
        // 调用原始注册方法
        ClientRegistrationUtil.registerFluidExtensions(event, ModFluids.FLUIDS);
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        for (var fluidRO : ModFluids.FLUIDS.getFluidEntries()) {
            event.register((stack, tintIndex) -> {
                if (tintIndex == 1) {
                    var fluidStack = net.neoforged.neoforge.fluids.FluidUtil.getFluidContained(stack).orElse(null);
                    if (fluidStack != null && !fluidStack.isEmpty()) {
                        return net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions.of(fluidStack.getFluid().getFluidType()).getTintColor(fluidStack);
                    }
                }
                return -1;
            }, fluidRO.get().getBucket());
        }
    }
}