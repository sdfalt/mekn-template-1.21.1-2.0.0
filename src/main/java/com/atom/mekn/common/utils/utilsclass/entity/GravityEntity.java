package com.atom.mekn.common.utils.utilsclass.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class GravityEntity extends Entity implements GeoEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public GravityEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = false;
        this.setNoGravity(false);
    }

    @Override
    public void tick() {
        super.tick();
        this.setDeltaMovement(this.getDeltaMovement().add(0, -0.098, 0));
        this.move(MoverType.SELF, this.getDeltaMovement());
        if (this.onGround()) {
            this.setDeltaMovement(Vec3.ZERO);
        }
    }

    // ---- 确保实体可碰撞 ----
    @Override public boolean isPickable() { return true; }
    @Override public boolean isPushable() { return true; }
    @Override public boolean canBeCollidedWith() { return true; }

    // ---- GeckoLib 必要实现（空） ----
    @Override public AnimatableInstanceCache getAnimatableInstanceCache() { return cache; }
    @Override public void registerControllers(AnimatableManager.ControllerRegistrar c) {}
    @Override protected void readAdditionalSaveData(@NotNull CompoundTag tag) {}
    @Override protected void addAdditionalSaveData(@NotNull CompoundTag tag) {}
    @Override public boolean hurt(net.minecraft.world.damagesource.@NotNull DamageSource s, float a) { return false; }

    @Override public abstract @NotNull EntityDimensions getDimensions(@NotNull Pose pose);
}