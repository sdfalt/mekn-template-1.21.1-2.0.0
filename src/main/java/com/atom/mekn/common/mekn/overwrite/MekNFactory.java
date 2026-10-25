package com.atom.mekn.common.mekn.overwrite;

import com.atom.mekn.common.mekn.client.rendershape.BlockShape;
import com.atom.mekn.common.mekn.register.blocks.BlcokRegister;
import com.atom.mekn.common.mekn.register.blocks.ContainerTypeRegister;
import mekanism.api.math.MathUtils;
import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.*;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.lib.math.Pos3D;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tier.FactoryTier;
import mekanism.common.util.EnumUtils;
import net.minecraft.core.particles.ParticleTypes;

import java.util.function.Supplier;


public class MekNFactory<TILE extends MekNTileEntityFactory<?>> extends MekNMachine.MekNFactoryMachine<TILE> {

    private final MekNMachine.MekNFactoryMachine<?> origMachine;

    public MekNFactory(Supplier<TileEntityTypeRegistryObject<TILE>> tileEntityRegistrar,
                       Supplier<ContainerTypeRegistryObject<? extends MekanismContainer>> containerRegistrar,
                       MekNMachine.MekNFactoryMachine<?> origMachine,
                       FactoryTier tier) {
        super(tileEntityRegistrar, MekanismLang.DESCRIPTION_FACTORY, origMachine.getFactoryType());
        this.origMachine = origMachine;
        setMachineData(tier);
        add(new AttributeGui(containerRegistrar, null), new AttributeTier<>(tier));

        if (tier.ordinal() < EnumUtils.FACTORY_TIERS.length - 1) {
            add(new AttributeUpgradeable(() -> BlcokRegister.getFactory(EnumUtils.FACTORY_TIERS[tier.ordinal() + 1], origMachine.getFactoryType())));
        }
    }
    private void setMachineData(FactoryTier tier) {
        setFrom(origMachine, AttributeSound.class, MekNAttributeFactoryType.class, AttributeUpgradeSupport.class);
        AttributeEnergy origEnergy = origMachine.get(AttributeEnergy.class);
        add(new AttributeEnergy(origEnergy::getUsage, () -> MathUtils.clampToLong(Math.max(origEnergy.getConfigStorage() * 0.5, origEnergy.getUsage()) * tier.processes)));
    }



    public static class MekNFactoryBuilder<FACTORY extends MekNFactory<TILE>, TILE extends MekNTileEntityFactory<?>, T extends MekNMachineBuilder<FACTORY, TILE, T>>
            extends BlockTileBuilder<FACTORY, TILE, T> {

        protected MekNFactoryBuilder(FACTORY holder) {
            super(holder);
        }

        @SuppressWarnings("unchecked")
        public static <TILE extends MekNTileEntityFactory<?>> MekNFactory.MekNFactoryBuilder<MekNFactory<TILE>, TILE, ?> createFactory(Supplier<?> tileEntityRegistrar, MekNFactoryType type, FactoryTier tier) {

            MekNFactoryBuilder<MekNFactory<TILE>, TILE, ?> builder = new MekNFactoryBuilder<>(new MekNFactory<>((Supplier<TileEntityTypeRegistryObject<TILE>>) tileEntityRegistrar,
                    () -> ContainerTypeRegister.FACTORY, type.getBaseMachine(), tier));

            builder.withComputerSupport(tier, type.getRegistryNameComponentCapitalized() + "Factory");
            builder.withCustomShape(BlockShape.getShape(tier, type));
            builder.with(switch (type) {
                case NITROGEN_EXTRACTION -> AttributeSideConfig.create(TransmissionType.CHEMICAL, TransmissionType.ENERGY);
            });
            builder.replace(new AttributeParticleFX().addDense(ParticleTypes.SMOKE, 5, rand -> new Pos3D(
                    rand.nextFloat() * 0.7F - 0.3F,
                    rand.nextFloat() * 0.1F + 0.7F,
                    rand.nextFloat() * 0.7F - 0.3F
            )));

            return builder;
        }
    }
}