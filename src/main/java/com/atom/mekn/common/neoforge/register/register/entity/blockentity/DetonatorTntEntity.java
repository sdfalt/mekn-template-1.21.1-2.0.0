package com.atom.mekn.common.neoforge.register.register.entity.blockentity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;

public class DetonatorTntEntity extends PrimedTnt {

    private final float explosionPower = 8.0F; // 自定义威力，原版是4.0F

    public DetonatorTntEntity(Level level, double x, double y, double z, @Nullable LivingEntity igniter, Block block) {
        super(level, x, y, z, igniter);
        this.setBlockState(block.defaultBlockState());
    }
    @Override
    protected void explode() {
        this.level().explode(this, Explosion.getDefaultDamageSource(this.level(), this), null,
                this.getX(), this.getY(0.0625), this.getZ(),
                this.explosionPower, false, Level.ExplosionInteraction.TNT);
    }
}
