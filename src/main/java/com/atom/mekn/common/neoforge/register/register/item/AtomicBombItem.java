package com.atom.mekn.common.neoforge.register.register.item;

import com.atom.mekn.common.neoforge.client.render.item.AtomicBombItemRenderer;
import com.atom.mekn.common.neoforge.register.register.entity.biologicalentity.PhysicsAtomicBombEntity;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class AtomicBombItem extends Item implements GeoItem {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public AtomicBombItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        Direction face = context.getClickedFace();

        if (level.isClientSide()) {return InteractionResult.SUCCESS;}

        Vec3 spawnPos = Vec3.atCenterOf(clickedPos.relative(face)).add(0, 1, 0);  // 提高一点，给下落空间

        PhysicsAtomicBombEntity bomb = PhysicsAtomicBombEntity.create(level, spawnPos.x, spawnPos.y, spawnPos.z);
        level.addFreshEntity(bomb);

        if (context.getPlayer() != null && !context.getPlayer().isCreative()) {context.getItemInHand().shrink(1);}
        return InteractionResult.SUCCESS;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}
    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private AtomicBombItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.renderer == null) {
                    this.renderer = new AtomicBombItemRenderer();
                }
                return this.renderer;
            }
        });
    }
}