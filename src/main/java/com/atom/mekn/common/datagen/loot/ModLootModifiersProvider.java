package com.atom.mekn.common.datagen.loot;

import com.atom.mekn.MekN;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import java.util.concurrent.CompletableFuture;

public class ModLootModifiersProvider extends GlobalLootModifierProvider {

    // 统一使用的战利品表路径（你也可以为每种动物单独指定路径）
    private static final ResourceKey<net.minecraft.world.level.storage.loot.LootTable> ANIMAL_FAT_LOOT_TABLE =
            ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(MekN.MODID, "additions/animal_fat"));

    public ModLootModifiersProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, MekN.MODID);
    }

    @Override
    protected void start() {
        // 定义需要添加掉落物的动物列表
        EntityType<?>[] animals = {
                EntityType.COW,
                EntityType.SHEEP,
                EntityType.CHICKEN,
                EntityType.PIG,
                EntityType.RABBIT,
                EntityType.GOAT,
                EntityType.HORSE,
                EntityType.DONKEY,
                EntityType.MULE,
                EntityType.LLAMA,
                EntityType.TRADER_LLAMA,
                EntityType.FOX,
                EntityType.WOLF,
                EntityType.CAT,
                EntityType.PARROT
        };

        for (EntityType<?> entity : animals) {
            // 为每种动物生成一个唯一的名字（例如 add_animal_fat_cow）
            String modifierName = "add_animal_fat_" + entity.getDescriptionId().replace("entity.minecraft.", "");
            add(modifierName,
                    new AddTableLootModifier(
                            new LootItemCondition[]{
                                    LootItemEntityPropertyCondition.hasProperties(
                                            LootContext.EntityTarget.THIS,
                                            EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(entity)).build()
                                    ).build()
                            },
                            ANIMAL_FAT_LOOT_TABLE
                    )
            );
        }
    }
}