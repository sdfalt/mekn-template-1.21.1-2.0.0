package com.atom.mekn.common.neoforge.network.pack;

import com.atom.mekn.MekN;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record ClientboundScreenShakePacket(int durationTicks, float intensity) implements CustomPacketPayload {

    public static final Type<ClientboundScreenShakePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(MekN.MODID, "screen_shake"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundScreenShakePacket> STREAM_CODEC =
            StreamCodec.ofMember(
                    ClientboundScreenShakePacket::write,
                    ClientboundScreenShakePacket::new
            );

    private ClientboundScreenShakePacket(RegistryFriendlyByteBuf buf) {
        this(buf.readVarInt(), buf.readFloat());
    }

    private void write(RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(this.durationTicks);
        buf.writeFloat(this.intensity);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}