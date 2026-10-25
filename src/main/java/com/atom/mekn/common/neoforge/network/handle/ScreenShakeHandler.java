package com.atom.mekn.common.neoforge.network.handle;

public class ScreenShakeHandler {
    private static int shakeTicks = 0;
    private static int maxShakeTicks = 0;
    private static float shakeIntensity = 0.0f;

    public static void startShake(int duration, float intensity) {
        maxShakeTicks = duration;
        shakeTicks = duration;
        shakeIntensity = intensity;
    }

    public static float[] computeShakeOffset(float partialTick) {

        //晃动设置
        if (shakeTicks <= 0) return new float[]{0, 0};
        float remaining = (shakeTicks - 1 + partialTick) / maxShakeTicks;
        float strength = shakeIntensity * remaining;
        long seed = 31L * shakeTicks;
        float yaw = (float) (Math.sin(seed + partialTick * 25.0) * strength);
        float pitch = (float) (Math.cos(seed + 7 + partialTick * 20.0) * strength * 0.5);
        return new float[]{yaw, pitch};
    }

    public static void clientTick() {
        if (shakeTicks > 0) shakeTicks--;
    }
}