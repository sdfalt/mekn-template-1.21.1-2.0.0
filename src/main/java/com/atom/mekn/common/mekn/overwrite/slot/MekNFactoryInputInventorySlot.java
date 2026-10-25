package com.atom.mekn.common.mekn.overwrite.slot;

import com.atom.mekn.common.mekn.overwrite.MekNTileEntityFactory;
import mekanism.api.IContentsListener;
import mekanism.api.inventory.IInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class MekNFactoryInputInventorySlot extends InputInventorySlot {
    public static MekNFactoryInputInventorySlot create(MekNTileEntityFactory<?> factory, int process, IInventorySlot outputSlot, @Nullable IContentsListener listener,
                                                   int x, int y) {
        return create(factory, process, outputSlot, null, listener, x, y);
    }

    public static MekNFactoryInputInventorySlot create(MekNTileEntityFactory<?> factory, int process, IInventorySlot outputSlot, @Nullable IInventorySlot secondaryOutputSlot,
                                                   @Nullable IContentsListener listener, int x, int y) {
        Objects.requireNonNull(factory, "Factory cannot be null");
        Objects.requireNonNull(outputSlot, "Primary output slot cannot be null");
        return new MekNFactoryInputInventorySlot(factory, process, outputSlot, secondaryOutputSlot, listener, x, y);
    }

    private MekNFactoryInputInventorySlot(MekNTileEntityFactory<?> factory, int process, IInventorySlot outputSlot, @Nullable IInventorySlot secondaryOutputSlot,
                                      @Nullable IContentsListener listener, int x, int y) {
        super(stack -> factory.isItemValidForSlot(stack) && factory.inputProducesOutput(process, stack, outputSlot, secondaryOutputSlot, false),
                factory::isValidInputItem, listener, x, y);
    }
}
