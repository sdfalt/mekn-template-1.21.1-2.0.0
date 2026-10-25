package com.atom.mekn.common.neoforge.client.render.entity; // 你的包名可能略有不同


import com.atom.mekn.MekN;
import com.atom.mekn.common.neoforge.register.register.entity.biologicalentity.PhysicsAtomicBombEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.model.GeoModel;

public class PhysicsAtomicBombRenderer extends GeoEntityRenderer<PhysicsAtomicBombEntity> {

    public PhysicsAtomicBombRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GeoModel<>() {
            @Override
            public ResourceLocation getModelResource(PhysicsAtomicBombEntity animatable) {
                return ResourceLocation.fromNamespaceAndPath(MekN.MODID, "geo/atomic_bomb.geo.json");
            }

            @Override
            public ResourceLocation getTextureResource(PhysicsAtomicBombEntity animatable) {
                return ResourceLocation.fromNamespaceAndPath(MekN.MODID, "textures/entity/atomic_bomb.png");
            }

            @Override
            public ResourceLocation getAnimationResource(PhysicsAtomicBombEntity animatable) {
                return null;
            }
        });
        this.shadowRadius = 1.0f;
    }
    @Override
    public void preRender(PoseStack poseStack, PhysicsAtomicBombEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        poseStack.translate(0, 0, 0);
    }

}
