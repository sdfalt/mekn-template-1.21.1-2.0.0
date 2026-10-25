package com.atom.mekn.common.mekn.overwrite;

import com.atom.mekn.common.mekn.register.blocks.BlcokRegister;
import com.atom.mekn.common.mekn.register.blocks.BlockTypesRegister;
import mekanism.api.text.IHasTranslationKey;
import mekanism.api.text.ILangEntry;
import mekanism.common.registration.impl.BlockRegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.function.Supplier;

public enum MekNFactoryType implements IHasTranslationKey.IHasEnumNameTranslationKey {
    NITROGEN_EXTRACTION("nitrogen_extraction", MekNLang.NITROGEN_EXTRACTOR_BASIC, () -> BlockTypesRegister.NITROGEN_EXTRACTOR, () -> BlcokRegister.NITROGEN_EXTRACTOR);


    private final String registryNameComponent;
    private final ILangEntry langEntry;
    private final Supplier<MekNMachine.MekNFactoryMachine<?>> baseMachine;
    private final Supplier<BlockRegistryObject<?, ?>> baseBlock;

    MekNFactoryType(String registryNameComponent, ILangEntry langEntry, Supplier<MekNMachine.MekNFactoryMachine<?>> baseMachine, Supplier<BlockRegistryObject<?, ?>> baseBlock) {
        this.registryNameComponent = registryNameComponent;
        this.langEntry = langEntry;
        this.baseMachine = baseMachine;
        this.baseBlock = baseBlock;
    }

    public String getRegistryNameComponent() {
        return this.registryNameComponent;
    }

    public String getRegistryNameComponentCapitalized() {
        String name = getRegistryNameComponent();
        return name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1);
    }

    public MekNMachine.MekNFactoryMachine<?> getBaseMachine() {
        return baseMachine.get();
    }

    public BlockRegistryObject<?, ?> getBaseBlock() {
        return this.baseBlock.get();
    }

    public @NotNull String getTranslationKey() {
        return this.langEntry.getTranslationKey();
    }
}
