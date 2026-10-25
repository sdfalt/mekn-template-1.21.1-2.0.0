package com.atom.mekn.common.mekn.register.blocks;

import com.atom.mekn.common.mekn.client.rendershape.BlockShape;
import com.atom.mekn.common.mekn.overwrite.MekNFactory;
import com.atom.mekn.common.mekn.overwrite.MekNFactoryType;
import com.atom.mekn.common.mekn.overwrite.MekNLang;
import com.atom.mekn.common.mekn.overwrite.MekNMachine;
import com.atom.mekn.common.mekn.register.blocks.blockentity.TileEntityIsotopicEncapsulationPress;
import com.atom.mekn.common.mekn.register.blocks.blockentity.TileEntityNitrogenExtractor;
import com.atom.mekn.common.mekn.register.blocks.blockentity.TileEntityNuclearEngineeringTable;
import com.atom.mekn.common.mekn.register.blocks.blockentity.TileEntitySuperCentrifuge;
import com.atom.mekn.common.mekn.register.sounds.SoundRegister;
import com.atom.mekn.common.utils.Modutils;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import mekanism.api.Upgrade;
import mekanism.common.block.attribute.*;
import mekanism.common.content.blocktype.BlockShapes;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tier.FactoryTier;
import mekanism.common.util.EnumUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BlockTypesRegister {


    public static final Machine<TileEntityNuclearEngineeringTable> NUCLEAR_ENGINEERING_TABLE = Machine.MachineBuilder
            .createMachine(() -> TileEntityTypeRegister.NUCLEAR_ENGINEERING_TABLE, MekNLang.DESCRIPTION_NUCLEAR_ENGINEERING_TABLE)
            .withGui(() -> ContainerTypeRegister.NUCLEAR_ENGINEERING_TABLE) // 绑定你的 5x5 GUI 容器
            .withSound(SoundRegister.MACHINE_WORK) // 借用粉碎机声音，或者使用自定义声音
            .withEnergyConfig(() -> 125_000L, () -> 200_000_000L)
            .with(AttributeUpgradeSupport.SPEED_ENERGY)
            .withComputerSupport("nuclearEngineeringTable")
            .with(new AttributeStateFacing(), Attributes.SECURITY, Attributes.COMPARATOR, Attributes.INVENTORY)
            .withSideConfig(TransmissionType.CHEMICAL, TransmissionType.ENERGY, TransmissionType.ITEM, TransmissionType.FLUID)
            .replace(Attributes.ACTIVE_LIGHT)
            .build();

    public static final Machine<TileEntitySuperCentrifuge> SUPER_CENTRIFUGE = Machine.MachineBuilder
            .createMachine(() -> TileEntityTypeRegister.SUPER_CENTRIFUGE, MekNLang.DESCRIPTION_SUPER_CENTRIFUGE)
            .withGui(() -> ContainerTypeRegister.SUPER_CENTRIFUGE)
            .withSound(SoundRegister.LIXINJI)  // 或者注册你自己的音效
            .withEnergyConfig(
                    () -> Modutils.feToJoules(1_000_000),       // 输入速率 100,000 FE/t
                    () -> Modutils.feToJoules(100_000_000)    // 缓存 200,000,000 FE (80M FE)
            )
            .withSupportedUpgrades(Upgrade.SPEED, Upgrade.ENERGY)
            .withComputerSupport("superCentrifuge")
            .replace(Attributes.ACTIVE_LIGHT)
            .with(new AttributeStateFacing(), Attributes.SECURITY, Attributes.COMPARATOR, Attributes.INVENTORY)
            .withCustomShape(BlockShape.SUPER_CENTRIFUGE)                     // 使用采矿机的 VoxelShape
            .with(AttributeCustomSelectionBox.JSON)
            .withBounding(new AttributeHasBounding.HandleBoundingBlock() {
                @Override
                public <DATA> boolean handle(Level level, BlockPos pos, BlockState state, DATA data,
                                             AttributeHasBounding.TriBooleanFunction<Level, BlockPos, DATA> consumer) {
                    BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
                    for (int x = -1; x <= 1; x++) {
                        for (int y = 0; y <= 1; y++) {
                            for (int z = -1; z <= 1; z++) {
                                if (x != 0 || y != 0 || z != 0) {
                                    mutable.setWithOffset(pos, x, y, z);
                                    if (!consumer.accept(level, mutable, data)) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                    return true;
                }
            })// 启用采矿机的精细选择框
            .build();

    public static final Machine<TileEntityIsotopicEncapsulationPress> ISOTOPIC_ENCAPSULATION_PRESS = Machine.MachineBuilder
            .createMachine(() -> TileEntityTypeRegister.ISOTOPIC_ENCAPSULATION_PRESS, MekNLang.DESCRIPTION_ISOTOPIC_ENCAPSULATION_PRESS)
            .withGui(() -> ContainerTypeRegister.ISOTOPIC_ENCAPSULATION_PRESS)
            .withSound(SoundRegister.KEHUAN) // 压制机建议使用富集机的声音
            .withEnergyConfig(
                    () -> Modutils.feToJoules(250_000),    // 功率稍大，因为要处理放射性压制
                    () -> Modutils.feToJoules(400_000_000) // 缓存 400M FE
            )
            .withSupportedUpgrades(Upgrade.SPEED, Upgrade.ENERGY) // 支持静音，因为它是大机器
            .withComputerSupport("isotopicEncapsulationPress")
            .replace(Attributes.ACTIVE_LIGHT)
            .with(Attributes.SECURITY, Attributes.COMPARATOR, Attributes.INVENTORY)
            // 使用大型形状（高度为 4，底面 3x3）
            .withCustomShape(BlockShape.ISOTOPIC_ENCAPSULATION_PRESS)
            .with(AttributeCustomSelectionBox.JSON)
            .withBounding(new AttributeHasBounding.HandleBoundingBlock() {
                @Override
                public <DATA> boolean handle(Level level, BlockPos pos, BlockState state, DATA data,
                                             AttributeHasBounding.TriBooleanFunction<Level, BlockPos, DATA> consumer) {
                    BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
                    // 3x3x4 的范围：x(-1~1), y(0~3), z(-1~1)
                    for (int x = -1; x <= 1; x++) {
                        for (int y = 0; y <= 2; y++) { // 高度设为 3
                            for (int z = -1; z <= 1; z++) {
                                if (x != 0 || y != 0 || z != 0) {
                                    mutable.setWithOffset(pos, x, y, z);
                                    if (!consumer.accept(level, mutable, data)) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                    return true;
                }
            })
            .build();

    public static final MekNMachine.MekNFactoryMachine<TileEntityNitrogenExtractor> NITROGEN_EXTRACTOR = MekNMachine.MekNMachineBuilder
            .createFactoryMachine(() -> TileEntityTypeRegister.NITROGEN_EXTRACTOR, MekNLang.DESCRIPTION_NITROGEN_EXTRACTOR, MekNFactoryType.NITROGEN_EXTRACTION)
            .withGui(() -> ContainerTypeRegister.NITROGEN_EXTRACTOR)
            .withSound(SoundRegister.JINGHUA)
            .withEnergyConfig(
                    () -> Modutils.feToJoules(5_000L),
                    () -> Modutils.feToJoules(2_000_000L)
            )
            .withSideConfig(TransmissionType.CHEMICAL, TransmissionType.ENERGY)
            .withComputerSupport("nitrogenExtractor")
            .with(new AttributeUpgradeable(() -> BlcokRegister.getFactory(FactoryTier.BASIC, MekNFactoryType.NITROGEN_EXTRACTION)))
            .build();



    private static final Table<FactoryTier, MekNFactoryType, MekNFactory<?>> FACTORIES = HashBasedTable.create();

    static {
        for (FactoryTier tier : EnumUtils.FACTORY_TIERS) {
            for (MekNFactoryType type : MekNFactoryType.values()) {
                FACTORIES.put(tier, type, MekNFactory.MekNFactoryBuilder.createFactory(() -> TileEntityTypeRegister.getFactoryTile(tier, type), type, tier).build());
            }
        }
    }

    public static MekNFactory<?> getFactory(FactoryTier tier, MekNFactoryType type) {
        return FACTORIES.get(tier, type);
    }


}
