package com.atom.mekn.common.mekn.overwrite;

import mekanism.common.block.attribute.Attribute;
import org.jetbrains.annotations.NotNull;

public class MekNAttributeFactoryType implements Attribute {
    private final MekNFactoryType type;

    public MekNAttributeFactoryType(MekNFactoryType type) {
        this.type = type;
    }

    @NotNull
    public MekNFactoryType getFactoryType() {
        return type;
    }
}
