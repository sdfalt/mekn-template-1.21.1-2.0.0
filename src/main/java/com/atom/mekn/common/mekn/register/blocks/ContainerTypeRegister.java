package com.atom.mekn.common.mekn.register.blocks;

import com.atom.mekn.MekN;

import com.atom.mekn.common.mekn.overwrite.MekNTileEntityFactory;
import com.atom.mekn.common.mekn.overwrite.container.MekNFactoryContainer;
import com.atom.mekn.common.mekn.register.blocks.blockentity.TileEntityIsotopicEncapsulationPress;
import com.atom.mekn.common.mekn.register.blocks.blockentity.TileEntityNitrogenExtractor;
import com.atom.mekn.common.mekn.register.blocks.blockentity.TileEntityNuclearEngineeringTable;
import com.atom.mekn.common.mekn.register.blocks.blockentity.TileEntitySuperCentrifuge;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import net.neoforged.bus.api.IEventBus;

public class ContainerTypeRegister {
    public static final ContainerTypeDeferredRegister CONTAINER_TYPES = new ContainerTypeDeferredRegister(MekN.MODID);

    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityNuclearEngineeringTable>> NUCLEAR_ENGINEERING_TABLE =
            CONTAINER_TYPES.custom("nuclear_engineering_table", TileEntityNuclearEngineeringTable.class)
                    .offset(0, 95)
                    .build();
    //大型离心机
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntitySuperCentrifuge>> SUPER_CENTRIFUGE =
            CONTAINER_TYPES.register("super_centrifuge", TileEntitySuperCentrifuge.class);
    // 在 ContainerTypeRegister 类中添加
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityIsotopicEncapsulationPress>> ISOTOPIC_ENCAPSULATION_PRESS =
            CONTAINER_TYPES.register("isotopic_encapsulation_press", TileEntityIsotopicEncapsulationPress.class);

    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityNitrogenExtractor>> NITROGEN_EXTRACTOR =
            CONTAINER_TYPES.register("nitrogen_extractor", TileEntityNitrogenExtractor.class);


    //factory
    public static final ContainerTypeRegistryObject<MekanismTileContainer<MekNTileEntityFactory<?>>> FACTORY = CONTAINER_TYPES.register("factory", factoryClass(), MekNFactoryContainer::new);
    public static void register(IEventBus bus ){CONTAINER_TYPES.register(bus);}

    private static Class<MekNTileEntityFactory<?>> factoryClass() {
        return (Class) MekNTileEntityFactory.class;
    }
}
