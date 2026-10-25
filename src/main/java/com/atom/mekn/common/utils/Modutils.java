package com.atom.mekn.common.utils;


import net.minecraft.resources.ResourceLocation;


import static com.atom.mekn.MekN.MODID;


public class Modutils {

    private static final double J_TO_FE_RATIO = 2.5;  // 1 FE = 2.5 J
    private static final double FE_TO_J_RATIO = 1.0 / J_TO_FE_RATIO; // 1 J = 0.4 FE
    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
    public static long joulesToFE(long joules) {
        return (long)(joules / J_TO_FE_RATIO);
    }

    public static long feToJoules(long fe) {
        return (long)(fe * J_TO_FE_RATIO);
    }

    public static long joulesToFERounded(long joules) {
        return Math.round(joules / J_TO_FE_RATIO);
    }

    public static long feToJoulesRounded(long fe) {
        return Math.round(fe * J_TO_FE_RATIO);
    }

    public static String formatEnergy(long joules) {
        long fe = joulesToFE(joules);
        if (fe < 1000) {
            return fe + " FE/t";
        } else if (fe < 1_000_000) {
            return String.format("%.1f kFE/t", fe / 1000.0);
        } else if (fe < 1_000_000_000) {
            return String.format("%.1f MFE/t", fe / 1_000_000.0);
        } else {
            return String.format("%.1f GFE/t", fe / 1_000_000_000.0);
        }
    }

    public static String formatPressure(long pressure) {
        if (pressure >= 1_000_000) {
            double mpa = pressure / 1_000_000.0;
            return mpa == (long) mpa ? String.format("%d MPa", (long) mpa) : String.format("%.1f MPa", mpa);
        } else if (pressure >= 1_000) {
            double kpa = pressure / 1_000.0;
            return kpa == (long) kpa ? String.format("%d kPa", (long) kpa) : String.format("%.1f kPa", kpa);
        } else {
            return pressure + " Pa";
        }
    }

}
