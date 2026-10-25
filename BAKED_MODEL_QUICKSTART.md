# 自定义烘焙模型 - 快速开始指南

## 已完成的工作

✅ 已为以下机器创建自定义烘焙模型类：
- 同位素封装压力机 (`IsotopicEncapsulationPressBakedModel`)
- 超级离心机 (`SuperCentrifugeBakedModel`)
- 核工程工作台 (`NuclearEngineeringTableBakedModel`)
- 氮气提取器 (`NitrogenExtractorBakedModel`)

✅ 已创建客户端事件注册类 (`ModelClientEvents`)

## 如何测试

### 1. 编译项目

```bash
./gradlew build
```

### 2. 运行游戏

```bash
./gradlew runClient
```

### 3. 验证模型

在游戏中放置以下方块，确认它们正常显示：
- `/give @p mekn:isotopic_encapsulation_press`
- `/give @p mekn:super_centrifuge`
- `/give @p mekn:nuclear_engineering_table`
- `/give @p mekn:nitrogen_extractor`

## 当前状态

目前的烘焙模型是**基础版本**，它们只是简单地包装原始模型，没有添加任何特殊效果。

如果你想添加自定义渲染效果，请编辑对应的烘焙模型类中的 `createKey` 方法。

## 示例：添加激活状态发光效果

编辑 `IsotopicEncapsulationPressBakedModel.java`：

```java
@Nullable
@Override
protected QuadsKey<Void> createKey(QuadsKey<Void> key, ModelData data) {
    // 获取机器激活状态（需要从 TileEntity 传递）
    Boolean isActive = data.get(YOUR_CUSTOM_PROPERTY);
    
    if (isActive != null && isActive) {
        // 激活时应用全亮度变换
        return key.transform(QuadTransformation.filtered_fullbright);
    }
    
    return null; // 非激活时使用原始模型
}
```

## 下一步

1. **测试基础功能**：确认模型能正常加载和显示
2. **添加自定义效果**：根据需求修改 `createKey` 方法
3. **性能优化**：确保自定义逻辑不会显著影响帧率
4. **文档完善**：记录你添加的任何特殊效果

## 故障排除

### 问题：模型显示为紫黑格子

**解决方案**：
1. 检查资源文件是否存在：`src/main/resources/assets/mekn/models/block/`
2. 确认方块注册名称与模型文件名匹配
3. 查看日志中的错误信息

### 问题：自定义效果没有生效

**解决方案**：
1. 确认 `createKey` 方法返回了正确的变换
2. 检查 ModelData 是否正确传递
3. 使用调试输出验证代码执行路径

### 问题：游戏崩溃

**解决方案**：
1. 查看 crash report
2. 确认所有导入的类都存在
3. 检查是否有空指针异常

## 参考资料

- [Mekanism DigitalMinerBakedModel](file:///C:/Users/micheal/Desktop/workestation/Mekanism-1.21.x/Mekanism-1.21.x/src/main/java/mekanism/client/model/baked/DigitalMinerBakedModel.java)
- [ExtensionBakedModel 基类](file:///C:/Users/micheal/Desktop/workestation/Mekanism-1.21.x/Mekanism-1.21.x/src/main/java/mekanism/client/model/baked/ExtensionBakedModel.java)
- [完整文档](CUSTOM_BAKED_MODELS.md)
