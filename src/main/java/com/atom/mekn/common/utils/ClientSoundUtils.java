package com.atom.mekn.common.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * 客户端专用声音工具。
 *
 * <p>这个类引用了 {@link net.minecraft.client.resources.sounds.SoundInstance} 等客户端类，
 * 只能从客户端代码（onUpdateClient / GUI / 渲染）调用。服务端代码一旦加载它，
 * NeoForge 的 DistCleaner 会抛
 * “Attempted to load class ... for invalid dist DEDICATED_SERVER”。</p>
 */
@OnlyIn(Dist.CLIENT)
public final class ClientSoundUtils {

    private ClientSoundUtils() {
    }

    /**
     * 【客户端专用】播放带有严格物理距离衰减的机器循环音效
     *
     * @param sound               要播放的声音事件
     * @param pos                 机器的方块坐标
     * @param attenuationDistance 衰减距离（单位：格，例如 12 代表 12 格外听不见）
     * @param volume              基础音量 (通常填 1.0F)
     * @param pitch               基础音调 (通常填 1.0F)
     */
    public static void playClientMachineSound(SoundEvent sound, BlockPos pos, float attenuationDistance, float volume, float pitch) {
        if (Minecraft.getInstance().player == null || Minecraft.getInstance().level == null) return;

        double realDist = Math.sqrt(Minecraft.getInstance().player.distanceToSqr(
                pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D));

        // 强制线性衰减公式
        float calculatedVolume = (float) Math.max(0.0D, 1.0D - (realDist / attenuationDistance)) * volume;

        if (calculatedVolume > 0.01F) {
            Minecraft.getInstance().getSoundManager().play(
                    new SimpleSoundInstance(sound, SoundSource.BLOCKS, calculatedVolume, pitch,
                            Minecraft.getInstance().level.random,
                            pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D)
            );
        }
    }

    /**
     * 【客户端专用】强制停止指定的声音（不管声音播到哪了，直接掐断）
     */
    public static void stopClientSound(SoundEvent sound) {
        Minecraft.getInstance().getSoundManager().stop(
                new SimpleSoundInstance(sound, SoundSource.BLOCKS, 1.0F, 1.0F,
                        Minecraft.getInstance().level != null ? Minecraft.getInstance().level.random : net.minecraft.util.RandomSource.create(),
                        0, 0, 0)
        );
    }
}
