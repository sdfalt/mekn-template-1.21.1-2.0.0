package com.atom.mekn.common.mekn.register.blocks;

import com.atom.mekn.MekN;
import com.atom.mekn.common.mekn.overwrite.MekNBlockFactoryMachine;
import com.atom.mekn.common.mekn.overwrite.MekNFactoryType;
import com.atom.mekn.common.mekn.overwrite.MekNItemBlockFactory;
import com.atom.mekn.common.mekn.overwrite.MekNTileEntityFactory;
import com.atom.mekn.common.mekn.register.blocks.blockentity.TileEntityIsotopicEncapsulationPress;
import com.atom.mekn.common.mekn.register.blocks.blockentity.TileEntityNitrogenExtractor;
import com.atom.mekn.common.mekn.register.blocks.blockentity.TileEntityNuclearEngineeringTable;
import com.atom.mekn.common.mekn.register.blocks.blockentity.TileEntitySuperCentrifuge;
import com.atom.mekn.common.mekn.register.blocks.blockentity.factory.TileEntityNitrogenExtractorFactory;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.util.EnumUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;

public class TileEntityTypeRegister {
    public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(MekN.MODID);

    private static final Table<FactoryTier, MekNFactoryType, TileEntityTypeRegistryObject<? extends MekNTileEntityFactory<?>>> FACTORIES = HashBasedTable.create();

    public static final TileEntityTypeRegistryObject<TileEntityNuclearEngineeringTable> NUCLEAR_ENGINEERING_TABLE = TILE_ENTITY_TYPES
            .mekBuilder(BlcokRegister.NUCLEAR_ENGINEERING_TABLE, TileEntityNuclearEngineeringTable::new) // 绑定方块和类
            .clientTicker(TileEntityMekanism::tickClient) // 统一的客户端 Tick 处理（用于动画渲染）
            .serverTicker(TileEntityMekanism::tickServer) // 统一的服务端 Tick 处理（用于配方逻辑、能量消耗）
            .withSimple(Capabilities.CONFIG_CARD)
            .build();

    public static final TileEntityTypeRegistryObject<TileEntitySuperCentrifuge> SUPER_CENTRIFUGE = TILE_ENTITY_TYPES
            .mekBuilder(BlcokRegister.SUPER_CENTRIFUGE, TileEntitySuperCentrifuge::new)
            .clientTicker(TileEntityMekanism::tickClient)
            .serverTicker(TileEntityMekanism::tickServer)
            .withSimple(Capabilities.CONFIG_CARD)
            .without(Capabilities.ITEM.block())
            .build();

    public static final TileEntityTypeRegistryObject<TileEntityIsotopicEncapsulationPress> ISOTOPIC_ENCAPSULATION_PRESS = TILE_ENTITY_TYPES
            .mekBuilder(BlcokRegister.ISOTOPIC_ENCAPSULATION_PRESS, TileEntityIsotopicEncapsulationPress::new)
            .clientTicker(TileEntityMekanism::tickClient)
            .serverTicker(TileEntityMekanism::tickServer)
            .withSimple(Capabilities.CONFIG_CARD)
            .without(Capabilities.ITEM.block())
            .build();
    public static final TileEntityTypeRegistryObject<TileEntityNitrogenExtractor> NITROGEN_EXTRACTOR = TILE_ENTITY_TYPES
            .mekBuilder(BlcokRegister.NITROGEN_EXTRACTOR, TileEntityNitrogenExtractor::new)
            .clientTicker(TileEntityMekanism::tickClient) // 统一的客户端 Tick 处理（用于动画渲染）
            .serverTicker(TileEntityMekanism::tickServer) // 统一的服务端 Tick 处理（用于配方逻辑、能量消耗）
            .withSimple(Capabilities.CONFIG_CARD)
            .build();

    static {
        for (FactoryTier tier : EnumUtils.FACTORY_TIERS) {
            registerFactory(tier, MekNFactoryType.NITROGEN_EXTRACTION, TileEntityNitrogenExtractorFactory::new);
        }
    }


    private static void registerFactory(FactoryTier tier, MekNFactoryType type, TileEntityTypeRegister.BlockEntityFactory<? extends MekNTileEntityFactory<?>> factoryConstructor) {
        BlockRegistryObject<MekNBlockFactoryMachine.MekNBlockFactory<?>, MekNItemBlockFactory> block = BlcokRegister.getFactory(tier, type);
        TileEntityTypeRegistryObject<? extends MekNTileEntityFactory<?>> tileRO = TILE_ENTITY_TYPES.mekBuilder(block, (pos, state) -> factoryConstructor.create(block, pos, state))
                .clientTicker(TileEntityMekanism::tickClient)
                .serverTicker(TileEntityMekanism::tickServer)
                .withSimple(Capabilities.CONFIG_CARD)
                .build();
        FACTORIES.put(tier, type, tileRO);
    }


    public static TileEntityTypeRegistryObject<? extends MekNTileEntityFactory<?>> getFactoryTile(FactoryTier tier, MekNFactoryType type) {
        return FACTORIES.get(tier, type);
    }


    @FunctionalInterface
    private interface BlockEntityFactory<BE extends BlockEntity> {
        BE create(Holder<Block> block, BlockPos pos, BlockState state);
    }


    public static void register(IEventBus bus ){TILE_ENTITY_TYPES.register(bus);}

}
