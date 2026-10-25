package com.atom.mekn.common.neoforge.register.register;


import com.atom.mekn.MekN;
import com.atom.mekn.common.neoforge.register.register.block.DetonatorTntBlock;
import com.atom.mekn.common.neoforge.register.register.block.ZirconOreBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MekN.MODID);

    public static final DeferredBlock<Block> ZIRCON_ORE = BLOCKS.register("zircon_ore", ZirconOreBlock::new);
    public static final DeferredBlock<Block> DEEPSLATE_ZIRCON_ORE = BLOCKS.register("deepslate_zircon_ore", ZirconOreBlock::new);

    public static final DeferredBlock<Block> BERYLLIUM_ORE = BLOCKS.register("beryllium_ore", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(3.0F, 3.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> DEEPSLATE_BERYLLIUM_ORE = BLOCKS.register("deepslate_beryllium_ore", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> BERYLLIUM_BLOCK = BLOCKS.register("beryllium_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> MAGNESIUM_ORE = BLOCKS.register("magnesium_ore", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(3.0F, 3.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> DEEPSLATE_MAGNESIUM_ORE = BLOCKS.register("deepslate_magnesium_ore", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> MAGNESIUM_BLOCK = BLOCKS.register("magnesium_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> DETONATOR_TNT = BLOCKS.register("detonator_tnt", () -> new DetonatorTntBlock(BlockBehaviour.Properties.of()
                    .ignitedByLava()
                    .destroyTime(0.0F)
                    .explosionResistance(0.0F))
    );
    public static final DeferredHolder<Block, Block> ZIRCONIUM_BLOCK = BLOCKS.register("zirconium_block", () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
            )
    );
    public static final DeferredBlock<Block> SUPER_CASING =
            BLOCKS.register("super_casing", () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(8.0F, 20.0F)
                    .sound(SoundType.METAL)));

    public static void register(IEventBus bus){
        BLOCKS.register(bus);
    }
}