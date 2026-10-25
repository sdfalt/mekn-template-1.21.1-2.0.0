# MekN 自定义烘焙模型说明

## 概述

本项目已按照 Mekanism 的 `DigitalMinerBakedModel` 方式为以下机器添加了自定义烘焙模型支持：

1. **同位素封装压力机** (Isotopic Encapsulation Press)
2. **超级离心机** (Super Centrifuge)
3. **核工程工作台** (Nuclear Engineering Table)
4. **氮气提取器** (Nitrogen Extractor)

## 文件结构

### 烘焙模型类

所有自定义烘焙模型类位于：
```
src/main/java/com/atom/mekn/common/mekn/client/model/baked/
```

包含以下文件：
- `IsotopicEncapsulationPressBakedModel.java` - 同位素封装压力机
- `SuperCentrifugeBakedModel.java` - 超级离心机
- `NuclearEngineeringTableBakedModel.java` - 核工程工作台
- `NitrogenExtractorBakedModel.java` - 氮气提取器

### 事件注册类

客户端模型注册事件处理类：
```
src/main/java/com/atom/mekn/common/mekn/event/ModelClientEvents.java
```

## 工作原理

### 1. 继承 ExtensionBakedModel

每个自定义烘焙模型都继承自 `mekanism.client.model.baked.ExtensionBakedModel<Void>`，这是 Mekanism 提供的基类。

```java
public class IsotopicEncapsulationPressBakedModel extends ExtensionBakedModel<Void> {
    public IsotopicEncapsulationPressBakedModel(BakedModel original) {
        super(original);
    }
    
    @Nullable
    @Override
    protected QuadsKey<Void> createKey(QuadsKey<Void> key, ModelData data) {
        // 在这里添加自定义变换逻辑
        return null; // 返回 null 表示使用原始模型
    }
    
    @Override
    protected IsotopicEncapsulationPressBakedModel wrapModel(BakedModel model) {
        return new IsotopicEncapsulationPressBakedModel(model);
    }
}
```

### 2. 注册烘焙模型

在 `ModelClientEvents` 类中，通过监听 `ModelEvent.ModifyBakingResult` 事件来替换原始模型：

```java
@SubscribeEvent
public static void modifyBakingResult(ModelEvent.ModifyBakingResult event) {
    Map<ResourceLocation, BakedModel> models = event.getModels();
    
    // 替换同位素封装压力机的模型
    replaceModel(models, BlcokRegister.ISOTOPIC_ENCAPSULATION_PRESS.getId(), 
        orig -> new IsotopicEncapsulationPressBakedModel(orig));
    
    // ... 其他机器
}
```

## 如何添加自定义渲染效果

### 示例 1：根据激活状态改变模型

如果你想让机器在激活时显示不同的外观，可以修改 `createKey` 方法：

```java
@Nullable
@Override
protected QuadsKey<Void> createKey(QuadsKey<Void> key, ModelData data) {
    // 从 ModelData 中获取机器激活状态
    Boolean isActive = data.get(YOUR_MODEL_PROPERTY);
    
    if (isActive != null && isActive) {
        // 如果机器激活，应用发光变换
        return key.transform(QuadTransformation.filtered_fullbright);
    }
    
    return null; // 非激活状态使用原始模型
}
```

### 示例 2：添加粒子效果或动画

你可以在 TileEntity 的 `tickClient` 方法中添加粒子效果，烘焙模型主要用于改变模型的几何形状或纹理。

### 示例 3：根据方向旋转模型

```java
@Nullable
@Override
protected QuadsKey<Void> createKey(QuadsKey<Void> key, ModelData data) {
    BlockState state = key.getBlockState();
    if (state != null) {
        Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        // 根据朝向应用不同的变换
        return key.transform(getRotationTransform(facing));
    }
    return null;
}
```

## 注意事项

1. **性能考虑**：烘焙模型会被缓存，避免在 `createKey` 中执行昂贵的计算
2. **线程安全**：模型烘焙可能在任意线程执行，确保代码是线程安全的
3. **ModelData**：如果需要传递额外数据给烘焙模型，需要在 TileEntity 中设置 ModelData
4. **兼容性**：确保自定义模型与 Mekanism 的其他系统（如配置卡、升级等）兼容

## 扩展建议

1. **添加节日特效**：参考 `DigitalMinerBakedModel` 中的 `ClientHolidayInfo.getMinerTransform()`
2. **动态纹理**：根据机器状态切换不同的纹理
3. **多部分模型**：为复杂机器创建多个子模型并组合
4. **LOD 支持**：根据距离使用不同详细程度的模型

## 调试技巧

如果模型没有正确显示：

1. 检查 `BlcokRegister` 中的方块 ID 是否正确
2. 确认资源文件（模型 JSON、纹理）存在且路径正确
3. 使用 `/reload` 命令重新加载资源
4. 查看日志中是否有模型加载错误

## 参考资源

- Mekanism 源码：`DigitalMinerBakedModel.java`
- NeoForge 文档：模型系统相关章节
- Minecraft Wiki：模型和烘焙相关内容
