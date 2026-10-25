package com.atom.mekn.common.mekn.overwrite;

import mekanism.common.block.prefab.BlockTile;
import mekanism.common.block.states.IStateFluidLoggable;

import mekanism.common.resource.BlockResourceInfo;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.UnaryOperator;

public class MekNBlockFactoryMachine <TILE extends TileEntityMekanism, MACHINE extends MekNMachine.MekNFactoryMachine<TILE>> extends BlockTile<TILE, MACHINE> {

    public MekNBlockFactoryMachine(MACHINE machine, UnaryOperator<Properties> propertiesModifier) {
        super(machine, propertiesModifier);
    }

    public static class MekNBlockFactoryMachineModel<TILE extends TileEntityMekanism, MACHINE extends MekNMachine.MekNFactoryMachine<TILE>> extends MekNBlockFactoryMachine<TILE, MACHINE> implements IStateFluidLoggable {

        public MekNBlockFactoryMachineModel(MACHINE machineType, UnaryOperator<BlockBehaviour.Properties> propertiesModifier) {
            super(machineType, propertiesModifier);
        }
    }

    public static class MekNBlockFactory<TILE extends MekNTileEntityFactory<?>> extends MekNBlockFactoryMachine.MekNBlockFactoryMachineModel<TILE, MekNFactory<TILE>> {

        public MekNBlockFactory(MekNFactory<TILE> factoryType) {
            super(factoryType, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor()));
        }

    }
}
