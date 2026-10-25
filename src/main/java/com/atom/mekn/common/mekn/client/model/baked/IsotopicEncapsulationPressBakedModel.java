package com.atom.mekn.common.mekn.client.model.baked;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.client.model.baked.ExtensionBakedModel;
import mekanism.client.render.lib.QuadTransformation;
import net.minecraft.client.resources.model.BakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;

/**
 * 同位素封装压力机的自定义烘焙模型
 * 用于添加特殊的渲染效果或动画
 * 同时调整模型位置，使其向上偏移以避免埋入地下
 */
@NothingNullByDefault
public class IsotopicEncapsulationPressBakedModel extends ExtensionBakedModel<Void> {

    // 向上偏移 1.0 个方块（16像素），使模型底部（Y=-16）对齐到地面（Y=0）
    // 注意：模型最低点在Y=-16，需要+16像素才能到达Y=0
    private static final QuadTransformation Y_OFFSET_TRANSFORM = QuadTransformation.translate(0, 1.0F, 0);

    public IsotopicEncapsulationPressBakedModel(BakedModel original) {
        super(original);
    }

    @Nullable
    @Override
    protected QuadsKey<Void> createKey(QuadsKey<Void> key, ModelData data) {
        // 关键：必须返回 transform 后的 key，这样变换才会被应用
        return key.transform(Y_OFFSET_TRANSFORM);
    }

    @Override
    protected IsotopicEncapsulationPressBakedModel wrapModel(BakedModel model) {
        return new IsotopicEncapsulationPressBakedModel(model);
    }
}
