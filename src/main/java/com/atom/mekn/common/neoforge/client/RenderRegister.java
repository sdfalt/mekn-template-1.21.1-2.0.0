package com.atom.mekn.common.neoforge.client;


import com.atom.mekn.common.neoforge.client.render.entity.MushroomCloudRenderer;
import com.atom.mekn.common.neoforge.client.render.entity.PhysicsAtomicBombRenderer;
import com.atom.mekn.common.neoforge.register.register.ModEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = "mekn",value = Dist.CLIENT)
public class RenderRegister {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.ATOMIC_BOMB_ENTITY.get(), PhysicsAtomicBombRenderer::new);
        event.registerEntityRenderer(ModEntities.MUSHROOM_CLOUD.get(), MushroomCloudRenderer::new);
    }
}
