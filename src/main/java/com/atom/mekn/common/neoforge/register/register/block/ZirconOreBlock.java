package com.atom.mekn.common.neoforge.register.register.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class ZirconOreBlock extends Block {
    public ZirconOreBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .strength(3.0F, 3.0F)
                .sound(SoundType.STONE)
                .requiresCorrectToolForDrops()
        );
    }
}