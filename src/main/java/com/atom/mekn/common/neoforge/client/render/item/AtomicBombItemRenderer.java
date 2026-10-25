package com.atom.mekn.common.neoforge.client.render.item;

import com.atom.mekn.MekN;
import com.atom.mekn.common.neoforge.register.register.item.AtomicBombItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.model.GeoModel;

import javax.annotation.Nullable;

public class AtomicBombItemRenderer extends GeoItemRenderer<AtomicBombItem> {

    public AtomicBombItemRenderer() {
        super(new GeoModel<>() {
            @Override
            public ResourceLocation getModelResource(AtomicBombItem animatable) {
                return ResourceLocation.fromNamespaceAndPath(MekN.MODID, "geo/atomic_bomb.geo.json");
            }

            @Override
            public ResourceLocation getTextureResource(AtomicBombItem animatable) {
                return ResourceLocation.fromNamespaceAndPath(MekN.MODID, "textures/entity/atomic_bomb.png");
            }

            @Override
            public ResourceLocation getAnimationResource(AtomicBombItem animatable) {
                return ResourceLocation.fromNamespaceAndPath(MekN.MODID, "animations/atomic_bomb.animation.json");
            }
        });

        this.withScale(0.3f, 0.3f);
    }

    @Override
    public void preRender(PoseStack poseStack, AtomicBombItem animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        poseStack.translate(0f, 1.0f, 0f);
    }
}
