package com.atom.mekn.common.mekn.event;


import com.atom.mekn.MekN;
import com.atom.mekn.common.mekn.client.gui.GuiAirCompressor;
import com.atom.mekn.common.mekn.client.gui.GuiIsotopicEncapsulationPress;
import com.atom.mekn.common.mekn.client.gui.GuiNuclearEngineeringTable;
import com.atom.mekn.common.mekn.client.gui.GuiSuperCentrifuge;
import com.atom.mekn.common.mekn.overwrite.gui.MekNGuiFactory;
import com.atom.mekn.common.mekn.register.blocks.ContainerTypeRegister;
import mekanism.client.ClientRegistrationUtil;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = MekN.MODID, value = Dist.CLIENT)
public class ScreenClientEvents {

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        ClientRegistrationUtil.registerScreen(event, ContainerTypeRegister.NUCLEAR_ENGINEERING_TABLE, GuiNuclearEngineeringTable::new);
        ClientRegistrationUtil.registerScreen(event, ContainerTypeRegister.SUPER_CENTRIFUGE, GuiSuperCentrifuge::new);
        ClientRegistrationUtil.registerScreen(event, ContainerTypeRegister.ISOTOPIC_ENCAPSULATION_PRESS, GuiIsotopicEncapsulationPress::new);
        ClientRegistrationUtil.registerScreen(event, ContainerTypeRegister.NITROGEN_EXTRACTOR, GuiAirCompressor::new);

        //factory
        ClientRegistrationUtil.registerScreen(event, ContainerTypeRegister.FACTORY, MekNGuiFactory::new);
    }
}
