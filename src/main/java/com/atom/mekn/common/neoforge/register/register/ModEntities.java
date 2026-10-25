package com.atom.mekn.common.neoforge.register.register;

import com.atom.mekn.MekN;
import com.atom.mekn.common.neoforge.register.register.entity.biologicalentity.MushroomCloudEntity;
import com.atom.mekn.common.neoforge.register.register.entity.biologicalentity.PhysicsAtomicBombEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, MekN.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<PhysicsAtomicBombEntity>> ATOMIC_BOMB_ENTITY =
            ENTITY_TYPES.register("atomic_bomb",
                    () -> EntityType.Builder.of(PhysicsAtomicBombEntity::new, MobCategory.MISC)
                            .setTrackingRange(10)
                            .setUpdateInterval(3)
                            .build("atomic_bomb")
            );

    public static final DeferredHolder<EntityType<?>, EntityType<MushroomCloudEntity>> MUSHROOM_CLOUD =
            ENTITY_TYPES.register("mushroom_cloud",
                    () -> EntityType.Builder.of(MushroomCloudEntity::new, MobCategory.MISC)
                            .sized(1.0f, 1.0f)
                            .noSave()
                            .setUpdateInterval(3)
                            .setTrackingRange(128)
                            .build("mushroom_cloud")
            );


    public static void register(IEventBus bus){
        ENTITY_TYPES.register(bus);
    }
}
