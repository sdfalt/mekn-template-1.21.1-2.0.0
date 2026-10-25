package com.atom.mekn.common.mekn.register.blocks;

import com.atom.mekn.MekN;
import com.atom.mekn.common.mekn.chemicals.chemical.ModChemicals;
import com.atom.mekn.common.mekn.chemicals.fluids.ModFluids;
import com.atom.mekn.common.mekn.overwrite.*;
import com.atom.mekn.common.mekn.register.blocks.blockentity.TileEntityIsotopicEncapsulationPress;
import com.atom.mekn.common.mekn.register.blocks.blockentity.TileEntityNitrogenExtractor;
import com.atom.mekn.common.mekn.register.blocks.blockentity.TileEntityNuclearEngineeringTable;
import com.atom.mekn.common.mekn.register.blocks.blockentity.TileEntitySuperCentrifuge;
import com.atom.mekn.common.utils.Modutils;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import mekanism.api.tier.ITier;
import mekanism.common.attachments.containers.ContainerType;
import mekanism.common.attachments.containers.chemical.ChemicalTanksBuilder;
import mekanism.common.attachments.containers.energy.EnergyContainersBuilder;
import mekanism.common.attachments.containers.fluid.FluidTanksBuilder;
import mekanism.common.attachments.containers.item.ItemSlotsBuilder;
import mekanism.common.block.attribute.AttributeTier;
import mekanism.common.block.interfaces.IHasDescription;
import mekanism.common.block.prefab.BlockFactoryMachine;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.tier.FactoryTier;
import mekanism.common.util.EnumUtils;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Supplier;



public class BlcokRegister {

    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(MekN.MODID);

    //核工作台
    public static final BlockRegistryObject<BlockTile.BlockTileModel<TileEntityNuclearEngineeringTable, Machine<TileEntityNuclearEngineeringTable>>, BlockItem> NUCLEAR_ENGINEERING_TABLE =
            BLOCKS.register("nuclear_engineering_table",
                            () -> new BlockTile.BlockTileModel<>(BlockTypesRegister.NUCLEAR_ENGINEERING_TABLE,
                                    properties -> properties.mapColor(MapColor.COLOR_GRAY)))
                    .forItemHolder(holder -> holder
                            // 1. 能量附件 (Energy)
                            .addAttachmentOnlyContainers(ContainerType.ENERGY, () -> EnergyContainersBuilder.builder()
                                    .addBasic(() -> 10_000L, () -> 100_000L)
                                    .build())
                            // 2. 流体附件 (用于液体机油 - Fluid)
                            .addAttachmentOnlyContainers(ContainerType.FLUID, () -> FluidTanksBuilder.builder()
                                    .addBasic(16_000, fluid -> fluid.is(ModFluids.MACHINE_OIL.get()))
                                    .build())
                            // 3. 化学品附件 (用于氮气 - Chemical)
                            .addAttachmentOnlyContainers(ContainerType.CHEMICAL, () -> ChemicalTanksBuilder.builder()
                                    .addBasic(16_000, chem -> chem.is(ModChemicals.NITROGEN.get()))
                                    .build())
                            // 4. 物品槽位附件 (25个合成槽 + 1个能量槽)
                            .addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                                    .addBasic(26) // 这就是你的 5x5 核心槽位
                                    .addEnergy()  // 专门放电池/能量卡的槽位
                                    .build())
                    );
    public static final BlockRegistryObject<BlockTile.BlockTileModel<TileEntitySuperCentrifuge, Machine<TileEntitySuperCentrifuge>>, ItemBlockTooltip<BlockTile.BlockTileModel<TileEntitySuperCentrifuge, Machine<TileEntitySuperCentrifuge>>>> SUPER_CENTRIFUGE = BLOCKS.register("super_centrifuge",
            () -> new BlockTile.BlockTileModel<>(BlockTypesRegister.SUPER_CENTRIFUGE,
                    properties -> properties.mapColor(MapColor.METAL)),
            (block, properties) -> new ItemBlockTooltip<>(block, true, properties
                    //这里删除过一个弹出组件
            )
    ).forItemHolder(holder -> holder
            .addAttachmentOnlyContainers(ContainerType.CHEMICAL, () -> ChemicalTanksBuilder.builder()
                    .addBasic(10L * FluidType.BUCKET_VOLUME, chem -> true) // 输入储罐
                    .addBasic(10L * FluidType.BUCKET_VOLUME)                // 输出储罐
                    .build())
            .addAttachmentOnlyContainers(ContainerType.ENERGY, () -> EnergyContainersBuilder.builder()
                    .addBasic(() -> Modutils.feToJoules(100_000), () -> Modutils.feToJoules(200_000_000))
                    .build())
            .addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                    .addChemicalFillSlot(0)   // 对应化学输入槽的充填槽
                    .addChemicalDrainSlot(1)  // 对应化学输出槽的排泄槽
                    .addEnergy()
                    .build())
    );

    // 在 BlcokRegister 类中添加
    public static final BlockRegistryObject<BlockTile.BlockTileModel<TileEntityIsotopicEncapsulationPress, Machine<TileEntityIsotopicEncapsulationPress>>, ItemBlockTooltip<BlockTile.BlockTileModel<TileEntityIsotopicEncapsulationPress, Machine<TileEntityIsotopicEncapsulationPress>>>> ISOTOPIC_ENCAPSULATION_PRESS =
            BLOCKS.register("isotopic_encapsulation_press",
                    () -> new BlockTile.BlockTileModel<>(BlockTypesRegister.ISOTOPIC_ENCAPSULATION_PRESS,
                            properties -> properties.mapColor(MapColor.COLOR_BLACK)),
                    ItemBlockTooltip::new
            ).forItemHolder(holder -> holder
                    // 1. 化学品储罐 (用于接收钋气)
                    .addAttachmentOnlyContainers(ContainerType.CHEMICAL, () -> ChemicalTanksBuilder.builder()
                            .addBasic(32_000, chem -> true)
                            .build())
                    // 2. 能量容器
                    .addAttachmentOnlyContainers(ContainerType.ENERGY, () -> EnergyContainersBuilder.builder()
                            .addBasic(() -> Modutils.feToJoules(250_000), () -> Modutils.feToJoules(400_000_000))
                            .build())
                    // 3. 物品槽 (输入槽、中间回流槽、最终产出槽、能量槽)
                    .addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                            .addBasic(4) // 槽位布局：原料、辅助料、产物、备用
                            .addEnergy()
                            .build())
            );
    public static final BlockRegistryObject<MekNBlockFactoryMachine<TileEntityNitrogenExtractor, MekNMachine.MekNFactoryMachine<TileEntityNitrogenExtractor>>, ItemBlockTooltip<MekNBlockFactoryMachine<TileEntityNitrogenExtractor, MekNMachine.MekNFactoryMachine<TileEntityNitrogenExtractor>>>> NITROGEN_EXTRACTOR =
            BLOCKS.register("nitrogen_extractor",
                    () -> new MekNBlockFactoryMachine<>(BlockTypesRegister.NITROGEN_EXTRACTOR,
                            properties -> properties.mapColor(MapColor.METAL)),
                    (block, properties) -> new ItemBlockTooltip<>(block, true, properties)
            ).forItemHolder(holder -> holder
                    .addAttachmentOnlyContainers(ContainerType.CHEMICAL, () -> ChemicalTanksBuilder.builder()
                            .build())
                    .addAttachmentOnlyContainers(ContainerType.ENERGY, () -> EnergyContainersBuilder.builder()
                            .addBasic(() -> 100_000, () -> 10_000_000)
                            .build())
            );

    //register
    private static <BLOCK extends Block & IHasDescription> BlockRegistryObject<BLOCK, ItemBlockTooltip<BLOCK>> registerBlock(String name, Supplier<? extends BLOCK> blockSupplier) {
        return BLOCKS.register(name, blockSupplier, ItemBlockTooltip::new);
    }

    private static <BLOCK extends Block & IHasDescription> BlockRegistryObject<BLOCK, ItemBlockTooltip<BLOCK>> registerBlock(String name, Supplier<? extends BLOCK> blockSupplier, Rarity rarity) {
        return BLOCKS.register(name, blockSupplier, (block, props) -> new ItemBlockTooltip<>(block, props.rarity(rarity)));
    }

    public static BlockRegistryObject<MekNBlockFactoryMachine.MekNBlockFactory<?>, MekNItemBlockFactory> getFactory(@NotNull FactoryTier tier, @NotNull MekNFactoryType type) {
        return FACTORIES.get(tier, type);
    }

    @SuppressWarnings("unchecked")
    public static BlockRegistryObject<BlockFactoryMachine.BlockFactory<?>, MekNItemBlockFactory>[] getFactoryBlocks() {
        return FACTORIES.values().toArray(new BlockRegistryObject[0]);
    }


    private static <TILE extends MekNTileEntityFactory<?>> BlockRegistryObject<MekNBlockFactoryMachine.MekNBlockFactory<?>, MekNItemBlockFactory> registerFactory(MekNFactory<TILE> type) {
        FactoryTier tier = (FactoryTier) type.get(AttributeTier.class).tier();
        BlockRegistryObject<MekNBlockFactoryMachine.MekNBlockFactory<?>, MekNItemBlockFactory> factory = BlcokRegister.registerTieredBlock(tier, "_" + type.getFactoryType().getRegistryNameComponent() + "_factory", () -> new MekNBlockFactoryMachine.MekNBlockFactory<>(type), MekNItemBlockFactory::new);
        factory.forItemHolder(holder -> {
            int processes = tier.processes;
            long baseEnergyUsage = 1000L * processes / 3;      // BASIC 等级 processes=3, 得出 1000
            long baseMaxEnergy = 1_000_000L * processes / 3;   // BASIC 等级 processes=3, 得出 1_000_000
            switch (type.getFactoryType()) {
                case NITROGEN_EXTRACTION -> {
                    long tankCapacity = TileEntityNitrogenExtractor.OUTPUT_CAPACITY * processes;
                    holder.addAttachmentOnlyContainers(ContainerType.CHEMICAL, () -> ChemicalTanksBuilder.builder()
                            .addBasic(tankCapacity, null, null)
                            .build());
                    long energyStorage = baseMaxEnergy * processes;
                    holder.addAttachmentOnlyContainers(ContainerType.ENERGY, () -> EnergyContainersBuilder.builder()
                            .addBasic(() -> baseEnergyUsage, () -> energyStorage)
                            .build());
                    holder.addAttachmentOnlyContainers(ContainerType.ITEM, () -> ItemSlotsBuilder.builder()
                            .addEnergy()
                            .build());
                }
            }
        });
        return factory;
    }


    public static final Table<FactoryTier, MekNFactoryType, BlockRegistryObject<MekNBlockFactoryMachine.MekNBlockFactory<?>, MekNItemBlockFactory>> FACTORIES = HashBasedTable.create();

    static {
        for (FactoryTier tier : EnumUtils.FACTORY_TIERS) {
            for (MekNFactoryType type : MekNFactoryType.values()) {
                FACTORIES.put(tier, type, registerFactory(BlockTypesRegister.getFactory(tier, type)));
            }
        }
    }

    private static <BLOCK extends Block, ITEM extends BlockItem> BlockRegistryObject<BLOCK, ITEM> registerTieredBlock(ITier tier, String suffix,
                                                                                                                      Supplier<? extends BLOCK> blockSupplier, BiFunction<BLOCK, Item.Properties, ITEM> itemCreator) {
        return BLOCKS.register(tier.getBaseTier().getLowerName() + suffix, blockSupplier, itemCreator);
    }


    public static Collection<BlockRegistryObject<MekNBlockFactoryMachine.MekNBlockFactory<?>, MekNItemBlockFactory>> getAllFactories() {
        return FACTORIES.values();
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }


}
