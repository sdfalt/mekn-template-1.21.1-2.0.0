package com.atom.mekn.common.neoforge.client.render.entity;

import com.atom.mekn.common.neoforge.client.render.model.MushroomCloudModel;
import com.atom.mekn.common.neoforge.register.register.entity.biologicalentity.MushroomCloudEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.Nullable;

public class MushroomCloudRenderer extends GeoEntityRenderer<MushroomCloudEntity> {
    public MushroomCloudRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new MushroomCloudModel());
        this.shadowRadius = 0;
    }
    @Override
    public void render(MushroomCloudEntity entity, float entityYaw, float partialTicks, @NotNull PoseStack stack,
                       @NotNull MultiBufferSource buffer, int light) {
        float progress = Mth.lerp(partialTicks,
                (entity.getAge() - 1) / (float) entity.getDuration(),
                entity.getProgress());
        float radius = entity.getExplosionRadius();

        float maxHeight = 10f + (float) (Math.sqrt(radius) * 15f);
        float maxScale  = 2f  + (float) (Math.sqrt(radius) * 10f);

        float riseHeight = progress * maxHeight;

        float scale;
        if (progress < 0.7f) {
            scale = (progress / 0.7f) * maxScale;
        } else {
            scale = maxScale;
        }

        float shake = (float) Math.sin((entity.getAge() + partialTicks) * 0.5f) * 0.15f;

        stack.pushPose();
        stack.translate(shake, riseHeight, shake);
        stack.scale(scale, scale, scale);
        super.render(entity, entityYaw, partialTicks, stack, buffer, light);
        stack.popPose();
    }

    @Override
    public RenderType getRenderType(MushroomCloudEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }
}