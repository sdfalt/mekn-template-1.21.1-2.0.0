package com.atom.mekn.mixin;

import com.atom.mekn.MekN;
import com.atom.mekn.common.config.ConfigValue;
import mekanism.common.Mekanism;
import mekanism.common.registries.MekanismAttachmentTypes;
import mekanism.generators.common.content.fission.FissionReactorMultiblockData;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = FissionReactorMultiblockData.class, remap = false)
public abstract class FissionReactorMixin {

    @Shadow(remap = false)
    private static final double EXPLOSION_CHANCE = 1.0 / 512_000.0;

    /**
     * @author Atom
     * @reason Scale explosion radius with reactor volume
     */
    @Overwrite
    private void createMeltdown(Level world) {
        MekN.LOGGER.info("mixin混入mek完成");

        FissionReactorMultiblockData self = (FissionReactorMultiblockData) (Object) this;
        int volume = self.getVolume();
        Mekanism.logger.info("FissionReactorMixin.createMeltdown called! Volume: {}", volume);

        double coefficient = ConfigValue.fissionExplosionRadiusCoefficient.get();
        double maxRadius =  ConfigValue.fissionExplosionMaxRadius.get();
        float finalRadius = (float) (coefficient * Math.sqrt(volume));
        finalRadius = (float) Math.min(finalRadius, maxRadius);
        Mekanism.logger.info("Computed radius: {} for volume {}", finalRadius, volume);
        Mekanism.logger.info("Calling Mekanism meltdown with radius: {}", finalRadius);
        world.getData(MekanismAttachmentTypes.MELTDOWN_DATA)
                .createMeltdown(self.getMinPos(), self.getMaxPos(), self.heatCapacitor.getHeat(),
                        EXPLOSION_CHANCE, finalRadius, self.inventoryID);
        Mekanism.logger.info("Meltdown call completed.");
    }
}