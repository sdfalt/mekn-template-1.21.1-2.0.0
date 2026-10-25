// com/atom/common/init/ModCreativeTabs.java
package com.atom.mekn.common.neoforge.register.register;

import com.atom.mekn.MekN;
import com.atom.mekn.common.mekn.register.blocks.BlcokRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MekN.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = CREATIVE_TABS.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + MekN.MODID))
                    .icon(() -> new ItemStack(BlcokRegister.NUCLEAR_ENGINEERING_TABLE)) // 使用核工程台作为图标，需替换为你的实际物品
                    .displayItems((parameters, output) -> {
                        for (Supplier<? extends Item> supplier : ModItem.itemlist){
                                output.accept(supplier.get());  // 显式指定堆叠数为 1
                        }
                    })
                    .build());


    public static void register(IEventBus bus){ CREATIVE_TABS.register(bus);}
}