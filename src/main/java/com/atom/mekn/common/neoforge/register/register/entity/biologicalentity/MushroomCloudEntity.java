package com.atom.mekn.common.neoforge.register.register.entity.biologicalentity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class MushroomCloudEntity extends Entity implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private int duration = 100;
    private int age = 0;
    private float explosionRadius = 1.0f;

    public MushroomCloudEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.setInvisible(true);
    }

    @Override
    public void tick() {
        if (this.level().isClientSide) {
            age++;
        } else {
            if (++age >= duration) {
                this.discard();
            }
        }
    }

    public float getProgress() {
        return Math.clamp(age / (float) duration, 0.0f, 1.0f);
    }

    public int getAge() {
        return age;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int d) {
        this.duration = d;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // 无动画，留空
    }

    public void setExplosionRadius(float r) { this.explosionRadius = r; }
    public float getExplosionRadius() { return explosionRadius; }

    @Override protected void defineSynchedData(SynchedEntityData.Builder b) {}
    @Override protected void readAdditionalSaveData(CompoundTag t) {}
    @Override protected void addAdditionalSaveData(CompoundTag t) {}
}