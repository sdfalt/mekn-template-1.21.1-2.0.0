package com.atom.mekn.common.mekn.client.model.baked;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.client.model.baked.ExtensionBakedModel;
import mekanism.client.render.lib.QuadTransformation;
import net.minecraft.client.resources.model.BakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;

/**
 * 超级离心机的自定义烘焙模型。
 * 用于添加特殊渲染效果或动画，同时把模型向上偏移，避免下半部分埋进地面。
 */
@NothingNullByDefault
public class SuperCentrifugeBakedModel extends ExtensionBakedModel<Void> {

    // 向上偏移 0.5 个方块（8 像素）
    private static final QuadTransformation Y_OFFSET_TRANSFORM = QuadTransformation.translate(0, 0.5F, 0);

    public SuperCentrifugeBakedModel(BakedModel original) {
        super(original);
    }

    @Nullable
    @Override
    protected QuadsKey<Void> createKey(QuadsKey<Void> key, ModelData data) {
        return key.transform(Y_OFFSET_TRANSFORM);
    }

    @Override
    protected SuperCentrifugeBakedModel wrapModel(BakedModel model) {
        return new SuperCentrifugeBakedModel(model);
    }
}
