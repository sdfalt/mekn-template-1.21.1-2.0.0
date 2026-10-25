package com.atom.mekn.common.mekn.overwrite;

// 可配置的传输效率
public enum TransferEfficiency {
    SLOW(5000, 10),    // 2000 mB/5 ticks, 50 FE/1000 mB
    NORMAL(10000, 10), // 4000 mB/2 ticks, 100 FE/1000 mB
    FAST(16000, 10),   // 8000 mB/1 tick, 150 FE/1000 mB
    ULTRA(20000, 10); // 16000 mB/1 tick, 200 FE/1000 mB

    private final long maxPerTransfer;
    private final int intervalTicks;

    TransferEfficiency(long maxPerTransfer, int intervalTicks) {
        this.maxPerTransfer = maxPerTransfer;
        this.intervalTicks = intervalTicks;
    }

    public long getMaxPerTransfer() { return maxPerTransfer; }
    public int getIntervalTicks() { return intervalTicks; }
}

