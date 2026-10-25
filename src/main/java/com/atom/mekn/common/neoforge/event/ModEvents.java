package com.atom.mekn.common.neoforge.event;

import com.atom.mekn.MekN;
import com.atom.mekn.common.neoforge.multiblock.NuclearStructureHandler;
import com.atom.mekn.common.neoforge.network.handle.ScreenShakeHandler;
import com.atom.mekn.common.neoforge.network.pack.ClientboundScreenShakePacket;
import com.atom.mekn.common.neoforge.register.register.entity.biologicalentity.PhysicsAtomicBombEntity;
import mekanism.common.registries.MekanismItems;
import mekanism.common.resource.PrimaryResource;
import mekanism.common.resource.ResourceType;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.Objects;

@EventBusSubscriber(modid = MekN.MODID)
public class ModEvents {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        if (level.getBlockState(pos).is(Blocks.CRAFTING_TABLE)) {
            Item uraniumIngot = Objects.requireNonNull(MekanismItems.PROCESSED_RESOURCES.get(ResourceType.INGOT, PrimaryResource.URANIUM)).asItem();
            if (stack.is(uraniumIngot)) {
                if (NuclearStructureHandler.checkStructure(level, pos)) {
                    if (!level.isClientSide) {
                        if (!player.isCreative()) {
                            stack.shrink(1);
                        }
                        NuclearStructureHandler.transform(level, pos);
                    }

                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                } else {
                    // 如果结构不对
                    if (level.isClientSide) {
                        player.displayClientMessage(net.minecraft.network.chat.Component.literal("§c[MekN] 结构不正确！"), true);
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public static void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(MekN.MODID);
        registrar.playToClient(
                ClientboundScreenShakePacket.TYPE,
                ClientboundScreenShakePacket.STREAM_CODEC,
                (packet, context) -> context.enqueueWork(() -> {
                    if (context.player() instanceof LocalPlayer) {
                        ScreenShakeHandler.startShake(packet.durationTicks(), packet.intensity());
                    }
                })
        );
    }
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Pre event) {
        ScreenShakeHandler.clientTick();
    }
    @SubscribeEvent
    public static void onCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        float partialTick = (float) event.getPartialTick();
        float[] offsets = ScreenShakeHandler.computeShakeOffset(partialTick);
        event.setYaw(event.getYaw() + offsets[0]);
        event.setPitch(event.getPitch() + offsets[1]);
    }
    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        if (!event.loadedFromDisk() && event.getEntity() instanceof PhysicsAtomicBombEntity bomb) {
            if (!bomb.level().isClientSide() && bomb.level() instanceof ServerLevel serverLevel) {
                var players = serverLevel.getServer().getPlayerList().getPlayers();
                if (players.isEmpty()) return;
                Component message = Component.translatable("alert.mekn.atomic_bomb_spawned", bomb.getBlockX(), bomb.getBlockY(), bomb.getBlockZ());
                for (ServerPlayer player : players) {player.sendSystemMessage(message);}
            }
        }
    }
}