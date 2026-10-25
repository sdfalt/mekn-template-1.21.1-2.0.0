package com.atom.mekn.common.data.modify;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;


import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class AddItemLootModifier extends LootModifier {
    public static final MapCodec<AddItemLootModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            codecStart(inst).and(
                    inst.group(
                            BuiltInRegistries.ITEM.byNameCodec()
                                    .fieldOf("item").forGetter(m -> m.item),
                            net.minecraft.util.ExtraCodecs.POSITIVE_INT.fieldOf("count").forGetter(m -> m.count)
                    )
            ).apply(inst, AddItemLootModifier::new)
    );

    private final Item item;
    private final int count;

    public AddItemLootModifier(LootItemCondition[] conditionsIn, Item item, int count) {
        super(conditionsIn);
        this.item = item;
        this.count = count;
    }

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, @NotNull LootContext context) {
        generatedLoot.add(new ItemStack(this.item, this.count));
        return generatedLoot;
    }

    @Override
    public @NotNull MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }


    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIERS = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, "mekn");

    public static final Supplier<MapCodec<AddItemLootModifier>> ADD_ITEM_MODIFIER = GLOBAL_LOOT_MODIFIERS.register("add_item", () -> AddItemLootModifier.CODEC);
}
