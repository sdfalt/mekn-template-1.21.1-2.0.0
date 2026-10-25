package com.atom.mekn.common.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.AABB;

import java.util.List;

/**
 * 服务端/通用声音工具。
 *
 * <p>这个类会被 BlockEntity 的 onUpdateServer 调用，在专用服务端上加载，
 * 所以绝不能引用任何客户端类（Minecraft / SoundInstance 等）。
 * 客户端声音播放/停止请用 {@link ClientSoundUtils}。</p>
 */
public final class SoundUtils {

    private SoundUtils() {
    }

    /**
     * 【服务端专用】向附近玩家发送停止声音的数据包
     * （用于停掉服务端 playSound 播放的第一声）
     */
    public static void stopServerSoundForNearby(ServerLevel level, BlockPos pos, SoundEvent sound, float attenuationDistance) {
        if (level == null) return;

        // 搜人范围 = 衰减距离 + 2格缓冲
        double range = attenuationDistance + 2.0D;
        AABB box = new AABB(pos.getCenter().subtract(range, range, range), pos.getCenter().add(range, range, range));
        List<ServerPlayer> players = level.getEntitiesOfClass(ServerPlayer.class, box);

        ClientboundStopSoundPacket packet = new ClientboundStopSoundPacket(sound.getLocation(), SoundSource.BLOCKS);
        for (ServerPlayer player : players) {
            player.connection.send(packet);
        }
    }
}
