package com.atom.mekn.common.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.ParticleTypes;

import java.util.List;
import java.util.Random;

public class Explosion {

    private final Level level;
    private final double x, y, z;
    private final float finalRadius;
    private final float damage;
    private final Random random = new Random();
    private float currentRadius = 1.0f;      // 起始半径 1
    private final float expandPerTick = 0.5f; // 每 tick 扩张量
    private boolean finished = false;
    private boolean damageDone = false;

    private static final int MAX_BLOCKS_PER_SHELL = 8888;

    public Explosion(Level level, double x, double y, double z, float finalRadius, float damage) {
        this.level = level;
        this.x = x;
        this.y = y;
        this.z = z;
        this.finalRadius = finalRadius;
        this.damage = damage;
    }

    /**
     * 每 tick 调用，返回 false 表示爆炸已结束
     */
    public boolean tick() {
        if (finished || level.isClientSide) return false;

        if (!damageDone) {
            damageEntities(finalRadius);
            damageDone = true;
        }
        playEffects(currentRadius);
        float prevRadius = currentRadius - expandPerTick;
        if (prevRadius < 0) prevRadius = 0;
        destroyShell(prevRadius, currentRadius);
        currentRadius += expandPerTick;
        if (currentRadius >= finalRadius) {
            destroyShell(currentRadius - expandPerTick, finalRadius);
            playEffects(finalRadius);
            finished = true;
            return false;
        }
        return true;
    }

    private void destroyShell(float innerR, float outerR) {
        int minX = (int) Math.floor(x - outerR);
        int maxX = (int) Math.floor(x + outerR);
        int minY = (int) Math.floor(y - outerR);
        int maxY = (int) Math.floor(y + outerR);
        int minZ = (int) Math.floor(z - outerR);
        int maxZ = (int) Math.floor(z + outerR);

        int count = 0;
        for (int bx = minX; bx <= maxX && count < MAX_BLOCKS_PER_SHELL; bx++) {
            for (int by = minY; by <= maxY && count < MAX_BLOCKS_PER_SHELL; by++) {
                for (int bz = minZ; bz <= maxZ && count < MAX_BLOCKS_PER_SHELL; bz++) {
                    BlockPos pos = new BlockPos(bx, by, bz);
                    double distSq = pos.distToCenterSqr(x, y, z);
                    if (distSq > outerR * outerR || distSq <= innerR * innerR) continue;
                    BlockState state = level.getBlockState(pos);
                    if (state.isAir() || state.getDestroySpeed(level, pos) < 0) continue;
                    level.destroyBlock(pos, false);
                    count++;
                }
            }
        }

        if (level instanceof ServerLevel serverLevel && outerR / 2.0f > 0 ) {
            serverLevel.explode(null, x, y, z, outerR, true, Level.ExplosionInteraction.NONE);
        }
    }

    private void damageEntities(float radius) {
        AABB aabb = new AABB(
                x - radius, y - radius, z - radius,
                x + radius, y + radius, z + radius
        );
        List<Entity> entities = level.getEntitiesOfClass(Entity.class, aabb,
                e -> e.isAlive() && !e.isSpectator() && e.distanceToSqr(x, y, z) <= radius * radius);

        for (Entity entity : entities) {
            double dist = Math.sqrt(entity.distanceToSqr(x, y, z));
            float damageScale = 1.0F - (float) (dist / radius);
            float finalDamage = damage * damageScale;
            entity.hurt(level.damageSources().explosion(null, null), finalDamage);

            Vec3 dir = entity.position().subtract(x, y, z).normalize();
            entity.setDeltaMovement(entity.getDeltaMovement().add(dir.scale(1.5 * damageScale)));
        }
    }

    private void playEffects(float radius) {
        // 只在第一帧（半径仍为初始1时）播放爆炸音效
        if (radius <= 1.0f + expandPerTick) {
            level.playSound(null, x, y, z, SoundEvents.GENERIC_EXPLODE,
                    SoundSource.BLOCKS, 4.0F,
                    (1.0F + (random.nextFloat() - random.nextFloat()) * 0.2F) * 0.7F);
        }

        if (level instanceof ServerLevel serverLevel) {
            // 在半径为 radius 的球面上生成粒子，粒子数随半径增加
            int particles = (int) (radius * 25);
            if (particles < 10) particles = 10;
            for (int i = 0; i < particles; i++) {
                double theta = random.nextDouble() * Math.PI * 2;
                double phi = random.nextDouble() * Math.PI;
                double rx = Math.sin(phi) * Math.cos(theta) * radius;
                double ry = Math.cos(phi) * radius;
                double rz = Math.sin(phi) * Math.sin(theta) * radius;
                serverLevel.sendParticles(ParticleTypes.EXPLOSION,
                        x + rx, y + ry, z + rz,
                        1, 0, 0, 0, 0.05);
            }
            // 中心闪光
            if (radius <= 2.0f) {
                serverLevel.sendParticles(ParticleTypes.FLASH, x, y, z, 5, 0.5, 1, 0.5, 0);
            }
        }
    }
}