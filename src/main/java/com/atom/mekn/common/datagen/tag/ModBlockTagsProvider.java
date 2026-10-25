package com.atom.mekn.common.datagen.tag;

import com.atom.mekn.MekN;
import com.atom.mekn.common.neoforge.register.register.ModBlocks;
import com.atom.mekn.common.mekn.register.blocks.BlcokRegister;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, MekN.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        var pickaxe = tag(BlockTags.MINEABLE_WITH_PICKAXE);
        var ironTool = tag(BlockTags.NEEDS_IRON_TOOL);

        // 矿石
        pickaxe.add(
                ModBlocks.ZIRCON_ORE.get(),
                ModBlocks.DEEPSLATE_ZIRCON_ORE.get(),
                ModBlocks.BERYLLIUM_ORE.get(),
                ModBlocks.DEEPSLATE_BERYLLIUM_ORE.get(),
                ModBlocks.MAGNESIUM_ORE.get(),
                ModBlocks.DEEPSLATE_MAGNESIUM_ORE.get()
        );
        ironTool.add(
                ModBlocks.ZIRCON_ORE.get(),
                ModBlocks.DEEPSLATE_ZIRCON_ORE.get(),
                ModBlocks.BERYLLIUM_ORE.get(),
                ModBlocks.DEEPSLATE_BERYLLIUM_ORE.get(),
                ModBlocks.MAGNESIUM_ORE.get(),
                ModBlocks.DEEPSLATE_MAGNESIUM_ORE.get()
        );

        // 机器
        pickaxe.add(
                BlcokRegister.NITROGEN_EXTRACTOR.get(),
                BlcokRegister.NUCLEAR_ENGINEERING_TABLE.get(),
                BlcokRegister.SUPER_CENTRIFUGE.get(),
                BlcokRegister.ISOTOPIC_ENCAPSULATION_PRESS.get(),
                ModBlocks.SUPER_CASING.get()
        );
        ironTool.add(
                BlcokRegister.NITROGEN_EXTRACTOR.get(),
                BlcokRegister.NUCLEAR_ENGINEERING_TABLE.get(),
                BlcokRegister.SUPER_CENTRIFUGE.get(),
                BlcokRegister.ISOTOPIC_ENCAPSULATION_PRESS.get(),
                ModBlocks.SUPER_CASING.get()
        );

        // 工厂（所有等级）
        for (var factoryRegistryObject : BlcokRegister.getAllFactories()) {
            Block block = factoryRegistryObject.get();
            pickaxe.add(block);
            ironTool.add(block);
        }
    }
}