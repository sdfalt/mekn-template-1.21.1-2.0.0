package com.atom.mekn.common.neoforge.multiblock;

import com.atom.mekn.common.mekn.register.blocks.BlcokRegister;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.resource.PrimaryResource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class NuclearStructureHandler {

    private static Block getLeadBlock() {
        return MekanismBlocks.PROCESSED_RESOURCE_BLOCKS.get(PrimaryResource.LEAD).value();
    }

    /**
     * @param centerPos 玩家点击的位置（即原版工作台，y=2）
     */
    public static boolean checkStructure(Level level, BlockPos centerPos) {
        Block lead = getLeadBlock();
        Block steelCasing = MekanismBlocks.STEEL_CASING.value();

        BlockPos baseCenter = centerPos.below(2);

        if (!checkHorizontalLayer(level, baseCenter, lead)) return false;

        if (!checkCorners(level, baseCenter.above(1), steelCasing)) return false;
        if (!level.getBlockState(baseCenter.above(1)).is(MekanismBlocks.ADVANCED_INDUCTION_CELL.value())) return false;

        if (!checkCorners(level, centerPos, steelCasing)) return false;

        if (!checkCorners(level, baseCenter.above(3), steelCasing)) return false;
        if (!level.getBlockState(baseCenter.above(3)).is(MekanismBlocks.ULTIMATE_CHEMICAL_TANK.value())) return false;

        if (!checkHorizontalLayer(level, baseCenter.above(4), lead)) return false;

        return true;
    }

    private static boolean checkHorizontalLayer(Level level, BlockPos pos, Block target) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (!level.getBlockState(pos.offset(x, 0, z)).is(target)) return false;
            }
        }
        return true;
    }

    private static boolean checkCorners(Level level, BlockPos pos, Block target) {
        int[] pts = {-1, 1};
        for (int x : pts) {
            for (int z : pts) {
                if (!level.getBlockState(pos.offset(x, 0, z)).is(target)) return false;
            }
        }
        return true;
    }

    public static void transform(Level level, BlockPos clickedPos) {
        // 定位底部中心位置
        BlockPos basePos = clickedPos.below(2);

        // 清理整个 3x5x3 区域
        BlockPos.betweenClosed(basePos.offset(-1, 0, -1), basePos.offset(1, 4, 1)).forEach(p -> {
            BlockState state = level.getBlockState(p);
            if (!state.isAir()) {
                level.levelEvent(2001, p, Block.getId(state));
                level.setBlock(p, Blocks.AIR.defaultBlockState(), 3);
            }
        });

        // --- 核心改动：在底部生成核工作台 ---
        level.setBlock(basePos, BlcokRegister.NUCLEAR_ENGINEERING_TABLE.value().defaultBlockState(), 3);

        // 粒子效果改在底部中心生成
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER,
                    basePos.getX() + 0.5, basePos.getY() + 0.5, basePos.getZ() + 0.5,
                    1, 0, 0, 0, 0);
        }
    }
}