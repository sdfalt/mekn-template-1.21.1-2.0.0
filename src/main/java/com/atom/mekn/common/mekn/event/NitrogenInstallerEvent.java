package com.atom.mekn.common.mekn.event;

import com.atom.mekn.MekN;
import com.atom.mekn.common.mekn.overwrite.MekNBlockFactoryMachine;
import com.atom.mekn.common.mekn.overwrite.MekNFactoryType;
import com.atom.mekn.common.mekn.register.blocks.BlcokRegister;
import mekanism.common.block.BlockBounding;
import mekanism.common.item.ItemTierInstaller;
import mekanism.common.tier.FactoryTier;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.Objects;

@EventBusSubscriber(modid = MekN.MODID)
public class NitrogenInstallerEvent {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().isClientSide()) return;
        if (!event.getEntity().isShiftKeyDown()) return;

        ItemStack heldStack = event.getItemStack();
        if (!(heldStack.getItem() instanceof ItemTierInstaller installer)) return;

        ServerPlayer player = (ServerPlayer) event.getEntity();
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        Block targetBlock = state.getBlock();

        // 处理 bounding block
        if (targetBlock instanceof BlockBounding) {
            BlockPos mainPos = BlockBounding.getMainBlockPos(level, pos);
            if (mainPos != null) {
                pos = mainPos;
                state = level.getBlockState(mainPos);
                targetBlock = state.getBlock();
            } else {
                return;
            }
        }

        Block resultBlock = null;

        // 1. 基础机器 → 基础工厂
        if (targetBlock == BlcokRegister.NITROGEN_EXTRACTOR.get()) {
            if (installer.getToTier() == FactoryTier.BASIC.getBaseTier()) {
                resultBlock = Objects.requireNonNull(
                        BlcokRegister.FACTORIES.get(FactoryTier.BASIC, MekNFactoryType.NITROGEN_EXTRACTION)
                ).get();
            }
        }
        // 2. 工厂 → 下一级工厂（通过反向查找获取等级和类型）
        else if (targetBlock instanceof MekNBlockFactoryMachine.MekNBlockFactory<?>) {
            FactoryTier currentTier = null;
            MekNFactoryType currentType = null;

            // 遍历注册表找到当前方块对应的等级和类型
            for (var cell : BlcokRegister.FACTORIES.cellSet()) {
                if (cell.getValue().get() == targetBlock) {
                    currentTier = cell.getRowKey();
                    currentType = cell.getColumnKey();
                    break;
                }
            }

            if (currentTier != null && currentType != null) {
                FactoryTier nextTier = getNextTier(currentTier);
                if (nextTier != null && installer.getToTier() == nextTier.getBaseTier()) {
                    resultBlock = Objects.requireNonNull(
                            BlcokRegister.FACTORIES.get(nextTier, currentType)
                    ).get();
                }
            }
        }

        // 执行替换
        if (resultBlock != null) {
            event.setCanceled(true);
            BlockState newState = resultBlock.defaultBlockState();
            if (state.hasProperty(HorizontalDirectionalBlock.FACING)) {
                newState = newState.setValue(HorizontalDirectionalBlock.FACING,
                        state.getValue(HorizontalDirectionalBlock.FACING));
            }
            if (level.setBlockAndUpdate(pos, newState)) {
                if (!player.isCreative()) {
                    heldStack.shrink(1);
                }
            }
        }
    }

    private static FactoryTier getNextTier(FactoryTier current) {
        return switch (current) {
            case BASIC -> FactoryTier.ADVANCED;
            case ADVANCED -> FactoryTier.ELITE;
            case ELITE -> FactoryTier.ULTIMATE;
            case ULTIMATE -> null;
            default -> null;
        };
    }
}