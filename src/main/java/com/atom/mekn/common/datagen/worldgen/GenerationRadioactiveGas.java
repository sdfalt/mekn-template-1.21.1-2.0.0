package com.atom.mekn.common.datagen.worldgen;

import com.atom.mekn.common.mekn.chemicals.chemical.ModChemicals;
import mekanism.api.datamaps.IMekanismDataMapTypes;
import mekanism.api.datamaps.chemical.attribute.ChemicalRadioactivity;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class GenerationRadioactiveGas extends DataMapProvider {
    public GenerationRadioactiveGas(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.@NotNull Provider provider) {
        // 获取放射性 DataMap 类型
        var radioactivityType = IMekanismDataMapTypes.INSTANCE.chemicalRadioactivity();

        // 为高浓缩铀添加放射性 (剂量 5.0 Sv/t)
        this.builder(radioactivityType)
                .add(ModChemicals.HIGHLY_ENRICHED_URANIUM.getKey(), new ChemicalRadioactivity(5.0), false);
    }
}
