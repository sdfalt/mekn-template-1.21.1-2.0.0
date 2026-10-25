package com.atom.mekn.common.mekn.event;

import com.atom.mekn.MekN;
import com.atom.mekn.common.mekn.register.blocks.BlcokRegister;
import mekanism.client.model.baked.ExtensionBakedModel.TransformedBakedModel;
import mekanism.client.render.lib.QuadTransformation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

import static mekanism.client.ClientRegistration.addCustomModel;

/**
 * 客户端模型注册事件处理类。
 */
@EventBusSubscriber(modid = MekN.MODID, value = Dist.CLIENT)
public class ModelClientEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // 同位素封装压力机：模型整体向上平移 1 格
        addCustomModel(BlcokRegister.ISOTOPIC_ENCAPSULATION_PRESS,
                (orig, evt) -> new TransformedBakedModel<Void>(orig, QuadTransformation.translate(0, 1, 0)));
    }
}
