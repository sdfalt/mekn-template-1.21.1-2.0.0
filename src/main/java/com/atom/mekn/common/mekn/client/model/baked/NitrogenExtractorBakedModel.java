package com.atom.mekn.common.mekn.client.model.baked;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.client.model.baked.ExtensionBakedModel;
import net.minecraft.client.resources.model.BakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;

/**
 * 氮气提取器的自定义烘焙模型
 * 用于添加特殊的渲染效果或动画
 */
@NothingNullByDefault
public class NitrogenExtractorBakedModel extends ExtensionBakedModel<Void> {

    public NitrogenExtractorBakedModel(BakedModel original) {
        super(original);
    }

    @Nullable
    @Override
    protected QuadsKey<Void> createKey(QuadsKey<Void> key, ModelData data) {
        // 这里可以添加自定义的变换逻辑
        // 例如：根据机器激活状态改变模型外观
        // 目前返回 null 表示使用原始模型
        return null;
    }

    @Override
    protected NitrogenExtractorBakedModel wrapModel(BakedModel model) {
        return new NitrogenExtractorBakedModel(model);
    }
}
