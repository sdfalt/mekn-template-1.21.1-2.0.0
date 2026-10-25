package com.atom.mekn.common.neoforge.register.register.entity.biologicalentity;

import com.atom.mekn.common.config.ConfigValue;
import com.atom.mekn.common.neoforge.network.pack.ClientboundScreenShakePacket;
import com.atom.mekn.common.neoforge.register.register.ModEntities;
import com.atom.mekn.common.neoforge.register.register.ModItem;
import com.atom.mekn.common.utils.Explosion;
import com.atom.mekn.common.utils.utilsclass.entity.GravityEntity;
import mekanism.common.lib.radiation.RadiationManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class PhysicsAtomicBombEntity extends GravityEntity {

    //累计高度计
    private static final float DETONATION_HEIGHT = ConfigValue.AtomBomHeight.get();
    private double fallStartY = Double.NaN;
    private final float explosionRadius = ConfigValue.AtomicBombExplosionRadiusCoefficient.get();
    private final float damage = ConfigValue.AtomicBombExplosionDamageCoefficient.get();
    private final double ray = ConfigValue.AtomRay.get();

    private Explosion explosion = null;  // 当前爆炸对象
    @Nullable
    private UUID ownerUUID;
    public PhysicsAtomicBombEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    public void setOwner(@Nullable UUID uuid) {
        this.ownerUUID = uuid;
    }

    @Nullable
    public UUID getOwnerUUID() {
        return ownerUUID;
    }
    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (explosion != null) {
                if (!explosion.tick()) {
                    this.discard();
                }
                return;
            }
            if (!this.onGround()) {
                if (Double.isNaN(fallStartY)) {
                    fallStartY = this.getY();
                }
            } else if (!Double.isNaN(fallStartY)) {
                float fallDistance = (float)(fallStartY - this.getY());
                fallStartY = Double.NaN;
                if (ownerUUID != null) {
                    Player owner = this.level().getPlayerByUUID(ownerUUID);
                    if (owner != null) {
                        owner.sendSystemMessage(Component.translatable("message.mekn.bomb_fall_distance", String.format("%.1f", fallDistance)));
                    }
                }
                if (fallDistance >= DETONATION_HEIGHT) {
                    startDetonation();
                    MushroomCloudEntity cloud = new MushroomCloudEntity(ModEntities.MUSHROOM_CLOUD.get(), this.level());
                    cloud.setPos(this.getX(), this.getY() + this.getBbHeight() / 2, this.getZ());
                    cloud.setExplosionRadius(this.explosionRadius);
                    cloud.setDuration(Math.max( 160, 100 + (int)(this.explosionRadius * 3.5) ));   // 持续时间 = 半径 * 10 tick，例如12半径=120tick=6秒
                    this.level().addFreshEntity(cloud);
                }
            }
        }
    }
    private void startDetonation() {

        explosion = new Explosion(
                this.level(),
                this.getX(),
                this.getY() + this.getBbHeight() / 2.0,
                this.getZ(),
                this.explosionRadius,
                this.damage
        );
        //设置为不显示
        this.setInvisible(true);

        //释放核辐射
        RadiationManager.get().radiate(this.level(), this.blockPosition(), this.ray);

        if (this.level() instanceof ServerLevel serverLevel) {
            double range = 128.0;
            List<ServerPlayer> players = serverLevel.getEntitiesOfClass(ServerPlayer.class,
                    new AABB(getX() - range, getY() - range, getZ() - range,
                            getX() + range, getY() + range, getZ() + range));
            ClientboundScreenShakePacket shakePacket = new ClientboundScreenShakePacket(60, 3.0f);
            for (ServerPlayer player : players) {
                player.connection.send(shakePacket);
            }
        }
    }

    @Override
    public @NotNull InteractionResult interact(@NotNull Player player, @NotNull InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }
        if (player.getItemInHand(hand).isEmpty() && player.isShiftKeyDown()) {
            if (!this.level().isClientSide) {
                ItemStack bombStack = new ItemStack(ModItem.ATOMIC_BOMB.get());
                if (!player.addItem(bombStack)) {this.spawnAtLocation(bombStack);}
                this.discard();
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.interact(player, hand);
    }


    //辅助
    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {}
    public static PhysicsAtomicBombEntity create(Level level, double x, double y, double z) {
        PhysicsAtomicBombEntity bomb = new PhysicsAtomicBombEntity(ModEntities.ATOMIC_BOMB_ENTITY.get(), level);
        bomb.setPos(x, y + 0.2, z);
        return bomb;
    }
    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pose) {
        return EntityDimensions.scalable(1.5f, 0.81f);
    }


    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (ownerUUID != null) {
            tag.putUUID("Owner", ownerUUID);
        }
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("Owner")) {
            this.ownerUUID = tag.getUUID("Owner");
        }
    }


}