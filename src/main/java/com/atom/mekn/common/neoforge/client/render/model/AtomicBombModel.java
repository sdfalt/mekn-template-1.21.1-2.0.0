package com.atom.mekn.common.neoforge.client.render.model;

import com.atom.mekn.MekN;
import com.atom.mekn.common.neoforge.register.register.item.AtomicBombItem;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class AtomicBombModel extends GeoModel<AtomicBombItem> {
    @Override
    public ResourceLocation getModelResource(AtomicBombItem animatable, @Nullable GeoRenderer<AtomicBombItem> renderer) {
        return ResourceLocation.fromNamespaceAndPath(MekN.MODID, "geo/atomic_bomb.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AtomicBombItem animatable, @Nullable GeoRenderer<AtomicBombItem> renderer) {
        return ResourceLocation.fromNamespaceAndPath(MekN.MODID, "textures/entity/atomic_bomb.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AtomicBombItem animatable) {
        return null;
    }

    @Deprecated @Override public ResourceLocation getModelResource(AtomicBombItem a) { return getModelResource(a, null); }
    @Deprecated @Override public ResourceLocation getTextureResource(AtomicBombItem a) { return getTextureResource(a, null); }


}
