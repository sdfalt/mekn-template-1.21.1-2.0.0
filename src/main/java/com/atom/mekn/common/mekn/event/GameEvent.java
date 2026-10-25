package com.atom.mekn.common.mekn.event;

import com.atom.mekn.MekN;
import com.atom.mekn.common.neoforge.register.register.ModItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = MekN.MODID)
public class GameEvent {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) {return;}

        boolean holdingItem = false;
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();
        if (!mainHand.isEmpty() && mainHand.is(ModItem.NITROGLYCERIN_PELLET.get())) {holdingItem = true;}
        else if (!offHand.isEmpty() && offHand.is(ModItem.NITROGLYCERIN_PELLET.get())) {holdingItem = true;}
        if (!holdingItem) {return;}
        double deltaX = player.getX() - player.xOld;
        double deltaZ = player.getZ() - player.zOld;
        double currentSpeed = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        if (!player.isCreative() && player.isSprinting() && !player.isShiftKeyDown() && currentSpeed > 0.01) {
            triggerExplosion(player);
            return;
        }
        if (currentSpeed > 0.25) {
            triggerExplosion(player);
            return;
        }
    }

    private static void triggerExplosion(Player player) {
        if (player.level() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().getPlayerList().broadcastSystemMessage(
                    net.minecraft.network.chat.Component.literal("硝酸甘油极其不稳定！").withStyle(net.minecraft.ChatFormatting.RED, net.minecraft.ChatFormatting.BOLD), false);
            double explosionY = player.getY() + (player.getBbHeight() / 2.0);
            serverLevel.explode(player, player.getX(), explosionY, player.getZ(), 4.0F, false, ServerLevel.ExplosionInteraction.BLOCK);
            ItemStack main = player.getMainHandItem();
            ItemStack off = player.getOffhandItem();
            if (!main.isEmpty() && main.is(ModItem.NITROGLYCERIN_PELLET.get())) {
                main.shrink(1);
            } else if (!off.isEmpty() && off.is(ModItem.NITROGLYCERIN_PELLET.get())) {
                off.shrink(1);
            }
        }
    }



}
