package com.atom.mekn.common.neoforge.register.register;

import com.atom.mekn.MekN;
import com.atom.mekn.common.mekn.overwrite.MekNBlockFactoryMachine;
import com.atom.mekn.common.mekn.overwrite.MekNItemBlockFactory;
import com.atom.mekn.common.mekn.register.blocks.BlcokRegister;
import com.atom.mekn.common.neoforge.register.register.item.AtomicBombItem;
import mekanism.common.registration.impl.BlockRegistryObject;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModItem {
    public static List<Supplier<? extends Item>> itemlist = new ArrayList<>();
    public static DeferredRegister.Items ITEMS = DeferredRegister.createItems(MekN.MODID);

    //public static final DeferredItem<Item> ZIRCON = ITEMS.register("zircon", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ZIRCON_INGOT = ITEMS.register("zircon_ingot", () -> new Item(new Item.Properties()));
    public static final DeferredItem<BlockItem> ZIRCON_ORE_ITEM = ITEMS.register("zircon_ore", () -> new BlockItem(ModBlocks.ZIRCON_ORE.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> DEEPSLATE_ZIRCON_ORE_ITEM = ITEMS.register("deepslate_zircon_ore", () -> new BlockItem(ModBlocks.DEEPSLATE_ZIRCON_ORE.get(), new Item.Properties()));
    //锆
    public static final DeferredItem<Item> ZIRCON_CRYSTAL = ITEMS.register("zircon_crystal", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ZIRCON_SHARD = ITEMS.register("zircon_shard", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ZIRCON_CLUMP = ITEMS.register("zircon_clump", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DIRTY_ZIRCON_DUST = ITEMS.register("dirty_zircon_dust", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ZIRCON_DUST = ITEMS.register("zircon_dust", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAW_ZIRCON = ITEMS.register("raw_zircon", () -> new Item(new Item.Properties()));
    //铍
    public static final DeferredItem<Item> BERYLLIUM_RAW = ITEMS.register("raw_beryllium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BERYLLIUM_INGOT = ITEMS.register("beryllium_ingot", () -> new Item(new Item.Properties()));
    public static final DeferredItem<BlockItem> BERYLLIUM_ORE_ITEM = ITEMS.register("beryllium_ore", () -> new BlockItem(ModBlocks.BERYLLIUM_ORE.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> DEEPSLATE_BERYLLIUM_ORE_ITEM = ITEMS.register("deepslate_beryllium_ore", () -> new BlockItem(ModBlocks.DEEPSLATE_BERYLLIUM_ORE.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> BERYLLIUM_BLOCK_ITEM = ITEMS.register("beryllium_block", () -> new BlockItem(ModBlocks.BERYLLIUM_BLOCK.get(), new Item.Properties()));
    public static final DeferredItem<Item> BERYLLIUM_CRYSTAL = ITEMS.register("beryllium_crystal", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BERYLLIUM_SHARD = ITEMS.register("beryllium_shard", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BERYLLIUM_CLUMP = ITEMS.register("beryllium_clump", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DIRTY_BERYLLIUM_DUST = ITEMS.register("dirty_beryllium_dust", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BERYLLIUM_DUST = ITEMS.register("beryllium_dust", () -> new Item(new Item.Properties()));
    //氟化铀酰晶体
    public static final DeferredItem<Item> URANIUM_OXIDE_CRYSTAL = ITEMS.register("uranium_oxide_crystal", () -> new Item(new Item.Properties()));
    //二氧化铀粉末
    public static final DeferredItem<Item> URANIUM_DIOXIDE_DUST = ITEMS.register("uranium_dioxide_dust", () -> new Item(new Item.Properties()));
    //铀金属核心
    public static final DeferredItem<Item> URANIUM_METAL_CORE = ITEMS.register("uranium_metal_core", () -> new Item(new Item.Properties()));
    // 镁相关物品
    public static final DeferredItem<Item> RAW_MAGNESIUM = ITEMS.register("raw_magnesium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> MAGNESIUM_INGOT = ITEMS.register("magnesium_ingot", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> MAGNESIUM_DUST = ITEMS.register("magnesium_dust", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DIRTY_MAGNESIUM_DUST = ITEMS.register("dirty_magnesium_dust", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> MAGNESIUM_CRYSTAL = ITEMS.register("magnesium_crystal", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> MAGNESIUM_CLUMP = ITEMS.register("magnesium_clump", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> MAGNESIUM_SHARD = ITEMS.register("magnesium_shard", () -> new Item(new Item.Properties()));
    public static final DeferredItem<BlockItem> MAGNESIUM_ORE_ITEM = ITEMS.register("magnesium_ore", () -> new BlockItem(ModBlocks.MAGNESIUM_ORE.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> DEEPSLATE_MAGNESIUM_ORE_ITEM = ITEMS.register("deepslate_magnesium_ore", () -> new BlockItem(ModBlocks.DEEPSLATE_MAGNESIUM_ORE.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> MAGNESIUM_BLOCK_ITEM = ITEMS.register("magnesium_block", () -> new BlockItem(ModBlocks.MAGNESIUM_BLOCK.get(), new Item.Properties()));
    public static final DeferredItem<Item> SINTERED_BERYLLIUM_SHELL = ITEMS.register("sintered_beryllium_shell", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> STEEL_MOLD = ITEMS.register("steel_mold", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> BERYLLIUM_SHELL = ITEMS.register("beryllium_shell", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BERYLLIUM_SHELL_FRAGMENT = ITEMS.register("beryllium_shell_fragment", () -> new Item(new Item.Properties()));
    //中子源
    public static final DeferredItem<Item> POLONIUM_BERYLLIUM_NEUTRON_SOURCE = ITEMS.register("polonium_beryllium_neutron_source", () -> new Item(new Item.Properties()));
    //油脂
    public static final DeferredItem<Item> ANIMAL_FAT = ITEMS.register("animal_fat", () -> new Item(new Item.Properties()));
    //硝酸甘油球
    public static final DeferredItem<Item> NITROGLYCERIN_PELLET = ITEMS.register("nitroglycerin_pellet", () -> new Item(new Item.Properties()));
    //炸药透镜
    public static final DeferredItem<Item> EXPLOSIVE_LENS = ITEMS.register("explosive_lens", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DETONATOR_TNT_ITEM = ITEMS.register("detonator_tnt", () -> new BlockItem(ModBlocks.DETONATOR_TNT.get(), new Item.Properties()));
    public static final DeferredItem<Item> ZIRCONIUM_BLOCK_ITEM = ITEMS.register("zirconium_block", () -> new BlockItem(ModBlocks.ZIRCONIUM_BLOCK.get(), new Item.Properties()));
    public static final DeferredItem<Item> ATOMIC_BOMB = ITEMS.register("atomic_bomb", () -> new AtomicBombItem(new Item.Properties().stacksTo(1)));


    //子弹膜具
    // ========== 子弹模具 (Bullet Molds) ==========tacz
    public static final DeferredItem<Item> BULLET_MOLD_68X51FURY = ITEMS.register("bullet_mold_68x51fury", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> BULLET_MOLD_338 = ITEMS.register("bullet_mold_338", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> BULLET_MOLD_308 = ITEMS.register("bullet_mold_308", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> BULLET_MOLD_357MAG = ITEMS.register("bullet_mold_357mag", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> BULLET_MOLD_46X30 = ITEMS.register("bullet_mold_46x30", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> BULLET_MOLD_57X28 = ITEMS.register("bullet_mold_57x28", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> BULLET_MOLD_45ACP = ITEMS.register("bullet_mold_45acp", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> BULLET_MOLD_50BMG = ITEMS.register("bullet_mold_50bmg", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> BULLET_MOLD_40MM = ITEMS.register("bullet_mold_40mm", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> BULLET_MOLD_762X25 = ITEMS.register("bullet_mold_762x25", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> BULLET_MOLD_556X45 = ITEMS.register("bullet_mold_556x45", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> BULLET_MOLD_545X39 = ITEMS.register("bullet_mold_545x39", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> BULLET_MOLD_RPG_ROCKET = ITEMS.register("bullet_mold_rpg_rocket", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> BULLET_MOLD_58X42 = ITEMS.register("bullet_mold_58x42", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> BULLET_MOLD_762X39 = ITEMS.register("bullet_mold_762x39", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> BULLET_MOLD_762X54 = ITEMS.register("bullet_mold_762x54", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> BULLET_MOLD_9MM = ITEMS.register("bullet_mold_9mm", () -> new Item(new Item.Properties().stacksTo(64)));


    //子弹膜具
    // ========== 子弹模具 (Bullet Molds) ==========卓越前线
    public static final DeferredItem<Item> BULLET_MOLD_RIFLE = ITEMS.register("bullet_mold_rifle", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> BULLET_MOLD_PISTOL = ITEMS.register("bullet_mold_pistol", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> BULLET_MOLD_SNIPER = ITEMS.register("bullet_mold_sniper", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> MOLD_SMALL_CANNON_SHELL = ITEMS.register("mold_small_cannon_shell", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> MOLD_SHOTGUN_SHELL = ITEMS.register("mold_shotgun_shell", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> MOLD_HEAVY_AMMO = ITEMS.register("mold_heavy_ammo", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> MOLD_GRENADE = ITEMS.register("mold_grenade", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> MOLD_SMALL_ROCKET = ITEMS.register("mold_small_rocket", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> MOLD_MEDIUM_AVIATION_BOMB = ITEMS.register("mold_medium_aviation_bomb", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> MOLD_HIGH_EXPLOSIVE = ITEMS.register("mold_high_explosive", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> MOLD_ARMOR_PIERCING = ITEMS.register("mold_armor_piercing", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> MOLD_CLUSTER = ITEMS.register("mold_cluster", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> MOLD_GRAPESHOT = ITEMS.register("mold_grapeshot", () -> new Item(new Item.Properties().stacksTo(64)));

    //main
    public static final DeferredItem<BlockItem> SUPER_CASING = ITEMS.register("super_casing", () -> new BlockItem(ModBlocks.SUPER_CASING.get(), new Item.Properties()));


    static {
        itemlist.add(ZIRCON_INGOT);
        //itemlist.add(ZIRCON);
        itemlist.add(ZIRCON_ORE_ITEM);
        itemlist.add(DEEPSLATE_ZIRCON_ORE_ITEM);
        itemlist.add(ZIRCON_CRYSTAL);
        itemlist.add(ZIRCON_SHARD);
        itemlist.add(ZIRCON_CLUMP);
        itemlist.add(DIRTY_ZIRCON_DUST);
        itemlist.add(ZIRCON_DUST);
        itemlist.add(RAW_ZIRCON);

        itemlist.add(BERYLLIUM_RAW);
        itemlist.add(BERYLLIUM_INGOT);
        itemlist.add(BERYLLIUM_ORE_ITEM);
        itemlist.add(DEEPSLATE_BERYLLIUM_ORE_ITEM);
        itemlist.add(BERYLLIUM_BLOCK_ITEM);
        itemlist.add(BERYLLIUM_CRYSTAL);
        itemlist.add(BERYLLIUM_SHARD);
        itemlist.add(BERYLLIUM_CLUMP);
        itemlist.add(DIRTY_BERYLLIUM_DUST);
        itemlist.add(BERYLLIUM_DUST);
        itemlist.add(BlcokRegister.NUCLEAR_ENGINEERING_TABLE::asItem);
        itemlist.add(BlcokRegister.SUPER_CENTRIFUGE::asItem);
        itemlist.add(URANIUM_OXIDE_CRYSTAL);
        itemlist.add(URANIUM_DIOXIDE_DUST);
        itemlist.add(URANIUM_METAL_CORE);
        itemlist.add(RAW_MAGNESIUM);
        itemlist.add(MAGNESIUM_INGOT);
        itemlist.add(MAGNESIUM_DUST);
        itemlist.add(DIRTY_MAGNESIUM_DUST);
        itemlist.add(MAGNESIUM_CRYSTAL);
        itemlist.add(MAGNESIUM_CLUMP);
        itemlist.add(MAGNESIUM_SHARD);
        itemlist.add(MAGNESIUM_ORE_ITEM);
        itemlist.add(DEEPSLATE_MAGNESIUM_ORE_ITEM);
        itemlist.add(MAGNESIUM_BLOCK_ITEM);
        itemlist.add(BERYLLIUM_SHELL);
        itemlist.add(SINTERED_BERYLLIUM_SHELL);
        itemlist.add(STEEL_MOLD);
        itemlist.add(BERYLLIUM_SHELL_FRAGMENT);
        itemlist.add(BlcokRegister.ISOTOPIC_ENCAPSULATION_PRESS::asItem);
        itemlist.add(POLONIUM_BERYLLIUM_NEUTRON_SOURCE);
        itemlist.add(BlcokRegister.NITROGEN_EXTRACTOR::asItem);
        for (BlockRegistryObject<MekNBlockFactoryMachine.MekNBlockFactory<?>, MekNItemBlockFactory> factory : BlcokRegister.getAllFactories()) {
            itemlist.add(factory::asItem);
        }
        itemlist.add(ANIMAL_FAT);
        itemlist.add(NITROGLYCERIN_PELLET);
        itemlist.add(EXPLOSIVE_LENS);
        itemlist.add(DETONATOR_TNT_ITEM);
        itemlist.add(ZIRCONIUM_BLOCK_ITEM);
        itemlist.add(ATOMIC_BOMB);
        itemlist.add(SUPER_CASING);
        //子弹磨具
        //判断tacz是否加入模组，如果在，添加进列表
        if (ModList.get().isLoaded("tacz")) {
            itemlist.add(BULLET_MOLD_68X51FURY);
            itemlist.add(BULLET_MOLD_338);
            itemlist.add(BULLET_MOLD_308);
            itemlist.add(BULLET_MOLD_357MAG);
            itemlist.add(BULLET_MOLD_46X30);
            itemlist.add(BULLET_MOLD_57X28);
            itemlist.add(BULLET_MOLD_45ACP);
            itemlist.add(BULLET_MOLD_50BMG);
            itemlist.add(BULLET_MOLD_40MM);
            itemlist.add(BULLET_MOLD_762X25);
            itemlist.add(BULLET_MOLD_556X45);
            itemlist.add(BULLET_MOLD_545X39);
            itemlist.add(BULLET_MOLD_RPG_ROCKET);
            itemlist.add(BULLET_MOLD_58X42);
            itemlist.add(BULLET_MOLD_762X39);
            itemlist.add(BULLET_MOLD_762X54);
            itemlist.add(BULLET_MOLD_9MM);
        }

        if (ModList.get().isLoaded("superbwarfare")) {
            itemlist.add(BULLET_MOLD_RIFLE);
            itemlist.add(BULLET_MOLD_PISTOL);
            itemlist.add(BULLET_MOLD_SNIPER);
            itemlist.add(MOLD_SMALL_CANNON_SHELL);
            itemlist.add(MOLD_GRENADE);
            itemlist.add(MOLD_HEAVY_AMMO);
            itemlist.add(MOLD_SHOTGUN_SHELL);
            itemlist.add(MOLD_CLUSTER);
            itemlist.add(MOLD_HIGH_EXPLOSIVE);
            itemlist.add(MOLD_ARMOR_PIERCING);
            itemlist.add(MOLD_MEDIUM_AVIATION_BOMB);
            itemlist.add(MOLD_SMALL_ROCKET);
            itemlist.add(MOLD_GRAPESHOT);
        }
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}