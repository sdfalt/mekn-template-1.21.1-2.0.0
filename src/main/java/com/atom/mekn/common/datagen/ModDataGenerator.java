package com.atom.mekn.common.datagen;


import com.atom.mekn.MekN;
import com.atom.mekn.common.datagen.loot.ModBlockLootTableProvider;
import com.atom.mekn.common.datagen.models.ModItemModelProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = MekN.MODID)
public class ModDataGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput out = generator.getPackOutput();
        var reg = event.getLookupProvider();
        var efh = event.getExistingFileHelper();
        //世界生成相关
        //generator.addProvider(event.includeServer(), new ModWorldGenProvider(output, lookupProvider));

        //mek辐射物质
//        generator.addProvider(event.includeServer(), new GenerationRadioactiveGas(output, lookupProvider));
       // generator.addProvider(event.includeServer(), new ModLootModifiersProvider(out, reg));
//        generator.addProvider(event.includeServer(), new ModLootTablesProvider(output, lookupProvider));
          generator.addProvider(event.includeServer(), new ModItemModelProvider(out, efh));
//        generator.addProvider(event.includeServer(), new ModBlockTagsProvider(out, reg, efh));
        //generator.addProvider(event.includeServer(), ModBlockLootTableProvider.create(out, reg));
    }
}