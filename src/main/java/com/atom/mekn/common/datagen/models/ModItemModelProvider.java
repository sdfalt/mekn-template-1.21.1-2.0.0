// src/main/java/com/atom/common/data/ModItemModelProvider.java
package com.atom.mekn.common.datagen.models;

import com.atom.mekn.MekN;
import com.atom.mekn.common.neoforge.register.register.ModItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MekN.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // ---------- 在这里手动添加你要生成模型的物品 ----------
        // 示例：
        // addBasicItem(ModItem.ZIRCON_DUST);
        // addBasicItem(ModItem.ZIRCON_INGOT);
        // addBlockItem(ModItem.ZIRCON_ORE_ITEM);
        // addBlockItem(ModItem.SUPER_CASING);
        // ...

//        addItemWithTexture(ModItem.ZIRCON_INGOT, "zircon_ingot");
//        addItemWithTexture(ModItem.BERYLLIUM_CLUMP, "beryllium_clump");
//        addItemWithTexture(ModItem.BERYLLIUM_INGOT, "beryllium_ingot");
//        addItemWithTexture(ModItem.BERYLLIUM_SHARD, "beryllium_shard");
//        addItemWithTexture(ModItem.MAGNESIUM_SHARD, "magnesium_shard");
//        addItemWithTexture(ModItem.BERYLLIUM_DUST, "beryllium_dust");
//        addItemWithTexture(ModItem.MAGNESIUM_DUST, "magnesium_dust");
//        addItemWithTexture(ModItem.MAGNESIUM_INGOT, "magnesium_ingot");
//        addItemWithTexture(ModItem.MAGNESIUM_CLUMP, "magnesium_clump");
//        addItemWithTexture(ModItem.DIRTY_BERYLLIUM_DUST, "dirty_beryllium_dust");
//        addItemWithTexture(ModItem.DIRTY_MAGNESIUM_DUST, "dirty_magnesium_dust");
//        addItemWithTexture(ModItem.BERYLLIUM_CRYSTAL, "beryllium_crystal");
//        addItemWithTexture(ModItem.MAGNESIUM_CRYSTAL, "magnesium_crystal");
//          addItemWithTexture(ModItem.BULLET_MOLD_40MM, "bullet_mold");
//          addItemWithTexture(ModItem.BULLET_MOLD_45ACP, "bullet_mold");
//          addItemWithTexture(ModItem.BULLET_MOLD_46X30, "bullet_mold");
//          addItemWithTexture(ModItem.BULLET_MOLD_50BMG, "bullet_mold");
//          addItemWithTexture(ModItem.BULLET_MOLD_308, "bullet_mold");
//          addItemWithTexture(ModItem.BULLET_MOLD_57X28, "bullet_mold");
//          addItemWithTexture(ModItem.BULLET_MOLD_68X51FURY, "bullet_mold");
//          addItemWithTexture(ModItem.BULLET_MOLD_338, "bullet_mold");
//          addItemWithTexture(ModItem.BULLET_MOLD_357MAG, "bullet_mold");
//          addItemWithTexture(ModItem.BULLET_MOLD_762X25, "bullet_mold");
//          addItemWithTexture(ModItem.BULLET_MOLD_556X45, "bullet_mold");
//          addItemWithTexture(ModItem.BULLET_MOLD_545X39, "bullet_mold");
//          addItemWithTexture(ModItem.BULLET_MOLD_RPG_ROCKET, "bullet_mold");
//          addItemWithTexture(ModItem.BULLET_MOLD_58X42, "bullet_mold");
//          addItemWithTexture(ModItem.BULLET_MOLD_762X39, "bullet_mold");
//          addItemWithTexture(ModItem.BULLET_MOLD_762X54, "bullet_mold");
         // addItemWithTexture(ModItem.BULLET_MOLD_9MM, "bullet_mold");

        addItemWithTexture(ModItem.MOLD_SMALL_CANNON_SHELL, "bullet_mold");
        addItemWithTexture(ModItem.MOLD_SHOTGUN_SHELL, "bullet_mold");
        addItemWithTexture(ModItem.MOLD_HEAVY_AMMO, "bullet_mold");
        addItemWithTexture(ModItem.MOLD_GRENADE, "bullet_mold");
        addItemWithTexture(ModItem.MOLD_SMALL_ROCKET, "bullet_mold");
        addItemWithTexture(ModItem.MOLD_MEDIUM_AVIATION_BOMB, "bullet_mold");
        addItemWithTexture(ModItem.MOLD_HIGH_EXPLOSIVE, "bullet_mold");
        addItemWithTexture(ModItem.MOLD_ARMOR_PIERCING, "bullet_mold");
        addItemWithTexture(ModItem.MOLD_CLUSTER, "bullet_mold");
        addItemWithTexture(ModItem.MOLD_GRAPESHOT, "bullet_mold");

    }


    protected void addBasicItem(Supplier<? extends Item> itemSupplier) {
        Item item = itemSupplier.get();
        basicItem(item);
    }

    protected void addBlockItem(Supplier<? extends BlockItem> blockItemSupplier) {
        BlockItem blockItem = blockItemSupplier.get();
        String name = BuiltInRegistries.ITEM.getKey(blockItem).getPath();
        withExistingParent(name, modLoc("block/" + name));
    }

    protected void addItemWithTexture(Supplier<? extends Item> itemSupplier, String texturePath) {
        Item item = itemSupplier.get();
        String name = BuiltInRegistries.ITEM.getKey(item).getPath();
        withExistingParent(name, mcLoc("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(MekN.MODID, "item/" + texturePath));
    }
}