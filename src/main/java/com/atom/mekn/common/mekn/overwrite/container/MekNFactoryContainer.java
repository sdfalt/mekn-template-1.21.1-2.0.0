package com.atom.mekn.common.mekn.overwrite.container;

import com.atom.mekn.common.mekn.overwrite.MekNTileEntityFactory;
import com.atom.mekn.common.mekn.register.blocks.ContainerTypeRegister;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tier.FactoryTier;
import net.minecraft.world.entity.player.Inventory;

public class MekNFactoryContainer extends MekanismTileContainer<MekNTileEntityFactory<?>> {
    public MekNFactoryContainer(int id, Inventory inv, MekNTileEntityFactory<?> tile) {
        super(ContainerTypeRegister.FACTORY, id, inv, tile);
    }
    @Override
    protected int getInventoryYOffset() {
        if (tile.hasSecondaryResourceBar()) {
            return 95;
        }
        return 85;
    }

    @Override
    protected int getInventoryXOffset() {
        return tile.tier == FactoryTier.ULTIMATE ? 26 : 8;
    }

}
