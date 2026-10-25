package com.atom.mekn.common.neoforge.client.render.model;

import com.atom.mekn.MekN;
import com.atom.mekn.common.neoforge.register.register.entity.biologicalentity.MushroomCloudEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MushroomCloudModel extends GeoModel<MushroomCloudEntity> {

    @Override
    public ResourceLocation getModelResource(MushroomCloudEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(MekN.MODID, "geo/mushroom_cloud.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MushroomCloudEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(MekN.MODID, "textures/entity/mushroom_cloud.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MushroomCloudEntity entity) {
        return null;
    }
}