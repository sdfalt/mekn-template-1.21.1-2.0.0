package com.atom.mekn.common.mekn.client.model.baked;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.client.model.baked.ExtensionBakedModel;
import net.minecraft.client.resources.model.BakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;

/**
 * 核工程工作台的自定义烘焙模型
 * 用于添加特殊的渲染效果或动画
 */
@NothingNullByDefault
public class NuclearEngineeringTableBakedModel extends ExtensionBakedModel<Void> {

    public NuclearEngineeringTableBakedModel(BakedModel original) {
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
    protected NuclearEngineeringTableBakedModel wrapModel(BakedModel model) {
        return new NuclearEngineeringTableBakedModel(model);
    }
}
