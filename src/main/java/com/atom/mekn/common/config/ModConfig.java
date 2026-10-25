package com.atom.mekn.common.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfig {
    public static final ModConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    private ModConfig(ModConfigSpec.Builder builder) {
        builder.push("fission_reactor_meltdown");

        ConfigValue.fissionExplosionRadiusCoefficient = builder
                .comment("爆炸半径系数：半径 = 系数 * sqrt(体积)，然后限制在最大半径内")
                .defineInRange("coefficient", 1.1, 0.5, 10.0);

        ConfigValue.fissionExplosionMaxRadius = builder
                .comment("反应堆最大爆炸半径（格）")
                .defineInRange("fissionMaxRadius", 85.0, 10.0, 256.0);

        ConfigValue.AtomicBombExplosionDamageCoefficient = builder
                .comment("原子弹爆炸伤害")
                .defineInRange("atomicDamage", 888, 10, Integer.MAX_VALUE);

        ConfigValue.AtomicBombExplosionRadiusCoefficient = builder
                .comment("原子弹爆炸半径")
                .defineInRange("atomicRadius", 100, 10, Integer.MAX_VALUE);

        ConfigValue.AtomBomHeight = builder
                .comment("原子弹爆炸高度")
                .defineInRange("atomHeight", 80, 10, Integer.MAX_VALUE);

        ConfigValue.AtomRay = builder
                .comment("辐射强度")
                .defineInRange("atomRadiation", 800.0, 10, Double.MAX_VALUE);
        builder.pop();
    }


    static {
        Pair<ModConfig, ModConfigSpec> pair =
                new ModConfigSpec.Builder().configure(ModConfig::new);

        //Store the resulting values
        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }
}
