package com.atom.mekn.common.mekn.overwrite;

import com.atom.mekn.common.mekn.register.blocks.BlcokRegister;
import mekanism.api.text.ILangEntry;
import mekanism.common.block.attribute.*;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.lib.math.Pos3D;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;

import java.util.function.Supplier;

public class MekNMachine<TILE extends TileEntityMekanism> extends BlockTypeTile<TILE> {
    public MekNMachine(Supplier<TileEntityTypeRegistryObject<TILE>> tileEntityRegistrar, ILangEntry description) {
        super(tileEntityRegistrar, description);
        add(new AttributeParticleFX()
                .add(ParticleTypes.SMOKE, rand -> new Pos3D(rand.nextFloat() * 0.6F - 0.3F, rand.nextFloat() * 6.0F / 16.0F, 0.52))
                .add(DustParticleOptions.REDSTONE, rand -> new Pos3D(rand.nextFloat() * 0.6F - 0.3F, rand.nextFloat() * 6.0F / 16.0F, 0.52)));
        add(Attributes.ACTIVE_LIGHT, new AttributeStateFacing(), Attributes.INVENTORY, Attributes.SECURITY, Attributes.REDSTONE, Attributes.COMPARATOR,
                AttributeUpgradeSupport.DEFAULT_MACHINE_UPGRADES);
    }

    public static class MekNFactoryMachine<TILE extends TileEntityMekanism> extends MekNMachine<TILE> {
        public MekNFactoryMachine(Supplier<TileEntityTypeRegistryObject<TILE>> tileEntitySupplier, ILangEntry description, MekNFactoryType factoryType) {
            super(tileEntitySupplier, description);
            add(new MekNAttributeFactoryType(factoryType), new AttributeUpgradeable(() -> BlcokRegister.getFactory(FactoryTier.BASIC, getFactoryType())));
        }

        public MekNFactoryType getFactoryType() {
            return get(MekNAttributeFactoryType.class).getFactoryType();
        }
    }

    public static class MekNMachineBuilder<MACHINE extends MekNMachine<TILE>, TILE extends TileEntityMekanism, T extends MekNMachine.MekNMachineBuilder<MACHINE, TILE, T>> extends BlockTileBuilder<MACHINE, TILE, T> {
        protected MekNMachineBuilder(MACHINE holder) {
            super(holder);
        }

        public static <TILE extends TileEntityMekanism> MekNMachine.MekNMachineBuilder<MekNMachine<TILE>, TILE, ?> createMachine(Supplier<TileEntityTypeRegistryObject<TILE>> tileEntityRegistrar, ILangEntry description) {
            return new MekNMachine.MekNMachineBuilder<>(new MekNMachine<>(tileEntityRegistrar, description));
        }

        public static <TILE extends TileEntityMekanism> MekNMachine.MekNMachineBuilder<MekNMachine.MekNFactoryMachine<TILE>, TILE, ?> createFactoryMachine(Supplier<TileEntityTypeRegistryObject<TILE>> tileEntityRegistrar, ILangEntry description, MekNFactoryType factoryType) {
            return new MekNMachine.MekNMachineBuilder<>(new MekNMachine.MekNFactoryMachine<>(tileEntityRegistrar, description, factoryType));
        }
    }
}