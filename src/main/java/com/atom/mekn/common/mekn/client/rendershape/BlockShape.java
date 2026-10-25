package com.atom.mekn.common.mekn.client.rendershape;

import com.atom.mekn.common.mekn.overwrite.MekNFactoryType;
import mekanism.common.tier.FactoryTier;
import mekanism.common.util.EnumUtils;
import mekanism.common.util.VoxelShapeUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockShape {

    // 声明为 final 确保安全
    public static final VoxelShape[] ISOTOPIC_ENCAPSULATION_PRESS = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];
    public static final VoxelShape[] NITROGEN_EXTRACTION_FACTORY = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];
    public static final VoxelShape[] SUPER_CENTRIFUGE = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];
    static {
        VoxelShape pressShape = VoxelShapeUtils.combine(
                box(-15, 0, -15, 31, 5, 31),    // 底座 (最底层，y=0到5，原y=-16到-11)
                box(-13, 5, -13, 29, 10, 29),     // 底座链接层 (y=5到10，原y=-11到-6)
                box(2, 10, 0, 14, 14, 29),          // 中部衔接下部 (y=10到14，原y=-6到-2)
                box(2, 10, -11, 14, 12, 0),         // 前小中部衔接 (y=10到12，原y=-6到-4)
                box(-14, 8, -14, 0, 16, 30),        // 右基座 (y=8到16，原y=-8到0)
                box(16, 8, -14, 30, 27, 4),        // 左前基座 (y=8到27，原y=-8到11)
                box(16, 8, 4, 29, 21, 26),          // 左中基座 (y=8到21，原y=-8到5)
                box(16, 8, 26, 30, 27, 30),        // 左后基座 (y=8到27，原y=-8到11)
                box(-12, 20, 3, -3, 44, 27),         // 右机体 (y=20到44，原y=4到28)
                box(-14, 23, 6, -12, 41, 24),        // 右盖 (y=23到41，原y=7到25)
                box(-10, 30, 2, -5, 37, 3),         // 有机体舱口 (y=30到37，原y=14到21)
                box(-10, 40, 2, -5, 42, 3),         // 副口 (y=40到42，原y=24到26)
                box(2, 16, -2, 14, 41, 13),          // 中舱主体 (y=16到41，原y=0到25)
                box(2, 41, 2, 14, 45, 14),          // 中仓上块 (y=41到45，原y=25到29)
                box(2, 40, 2, 14, 43, 8),           // 中仓上块坡板 (倾斜部分，y=40到43，原y=24到27)
                box(-3, 23, 10, 25, 41, 20),         // 竖主缸 (y=23到41，原y=7到25)
                box(-3, 27, 6, 25, 37, 24),         // 横主缸 (y=27到37，原y=11到21)
                box(-3, 36, 7, 25, 41, 27),         // 正斜主缸 (倾斜部分，y=36到41，原y=20到25)
                box(-3, 20, 19, 25, 40, 24),         // 反斜主缸 (倾斜部分，y=20到40，原y=4到24)
                box(22, 23, 10, 29, 43, 20),         // 左支撑 (y=23到43，原y=7到27)
                box(27, 25, 9, 31, 38, 22),          // 左盖 (y=25到38，原y=9到22)
                box(16, 21, 10, 29, 23, 20),          // 左支座 (y=21到23，原y=5到7)
                box(18, 27, -12, 28, 29, 2),        // 左前基座顶部 (y=27到29，原y=11到13)
                box(4, 47, 4, 12, 48, 12),          // 上接口 (y=47到48，原y=31到32)
                box(5, 45, 5, 11, 47, 11)           // 上衔接 (y=45到47，原y=29到31)
        );
        VoxelShapeUtils.setShape(pressShape, ISOTOPIC_ENCAPSULATION_PRESS, false, false);

        VoxelShapeUtils.setShape(VoxelShapeUtils.combine(
                box(-16, 0, -15, 32, 4, 32),
                box(-14.75, 23.5, -13.75, 30.5, 26.25, 31.5),
                box(-16, 27, -15, 32, 28, 32),
                box(28.5, 3, -13.5, 31.5, 27, 31),
                box(-15.5, 3, -13.5, -12.5, 27, 31),
                box(-15.125, 1.5, 28.65, 30.875, 26, 31.9),
                box(-11.5, 3, -13, 28.5, 5, 31),
                box(-4.5, 5, -10, 20.5, 6, 24),
                box(0, 4, 0, 15, 8, 15),
                box(-14, 5, -14.75, -7, 13, -12.75),  // 左侧内柱
                box(7, 7, 7, 8, 25, 8),               // 中心细柱
                box(20.75, 3.5, -14, 31, 15.25, 4),
                box(20.75, 15.5, -14, 31, 26.25, 4),
                box(-15.475, 2.5, -14, -5.35, 14.25, 5),
                box(-15.475, 14.5, -14, -5.225, 26.25, 5),
                box(-14.5, 16, -14.75, -6.5, 25, -12.75),
                box(-2, 18, -13, 1, 26, 30),
                box(17, 15, -13, 20, 23, 30),
                box(0, 5, -10, 15, 6, 24)  // 与上述有重叠，但无所谓
        ), SUPER_CENTRIFUGE);



        // 氮气提取机工厂：标准 16×16×16 立方体碰撞箱（单方块）
        VoxelShape cube = Block.box(0, 0, 0, 16, 16, 16);
        VoxelShapeUtils.setShape(cube, NITROGEN_EXTRACTION_FACTORY, false, false);
    }


    public static VoxelShape[] getShape(FactoryTier tier, MekNFactoryType type) {
        return switch (type) {
            case NITROGEN_EXTRACTION -> NITROGEN_EXTRACTION_FACTORY;
        };
    }

    private static VoxelShape box(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return Block.box(minX, minY, minZ, maxX, maxY, maxZ);
    }
}