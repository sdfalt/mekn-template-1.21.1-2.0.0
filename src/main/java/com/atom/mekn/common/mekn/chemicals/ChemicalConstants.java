
package com.atom.mekn.common.mekn.chemicals;

import mekanism.common.base.IChemicalConstant;

public enum ChemicalConstants implements IChemicalConstant {
    NITROGEN("nitrogen", 0xFF88AACC, 0, 77.0F, 1.25F),
    MACHINE_OIL("machine_oil", 0xFFAA8866, 0, 300.0F, 900.0F),
    DIRTY_ZIRCON_SLURRY("dirty_zircon_slurry", 0xFF9E6F3E, 0, 300.0F, 1200.0F),
    CLEAN_ZIRCON_SLURRY("clean_zircon_slurry", 0xFFC4A27E, 0, 300.0F, 1100.0F),
    LOW_ENRICHED_URANIUM("medium_concentration_uranium_hexafluoride", 0xFF66CC66, 0, 300.0F, 5.0F),
    HIGHLY_ENRICHED_URANIUM("high_concentration_uranium_hexafluoride", 0xFF22DD22, 1, 300.0F, 5.0F),
    URANIUM_OXYFLUORIDE("uranium_oxide_gas", 0xFFD4B85A, 1, 300.0F, 6.0F),
    HYDROFLUORIC_ACID("hydrofluoric_acid", 0xFFCD5C5C, 0, 293.0F, 1.0F),
    NITRIC_ACID("nitric_acid", 0xFFA52A2A, 0, 300.0F, 1.5F),
    AMMONIA("ammonia", 0xFFA0A0A0, 0, 300.0F, 0.6F),
    NITRIC_OXIDE("nitric_oxide", 0xFFA0D0A0, 0, 300.0F, 1.2F),
    NITROGEN_DIOXIDE("nitrogen_dioxide", 0xFFD09030, 0, 300.0F, 1.5F);

    private final String name;
    private final int color;
    private final int lightLevel;
    private final float temperature;
    private final float density;

    ChemicalConstants(String name, int color, int lightLevel, float temperature, float density) {
        this.name = name;
        this.color = color;
        this.lightLevel = lightLevel;
        this.temperature = temperature;
        this.density = density;
    }

    @Override public String getName() { return name; }
    @Override public int getColor() { return color; }
    @Override public int getLightLevel() { return lightLevel; }
    @Override public float getTemperature() { return temperature; }
    @Override public float getDensity() { return density; }
}