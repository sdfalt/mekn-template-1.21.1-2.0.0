package com.atom.mekn.common.mekn.overwrite.gui;
import com.atom.mekn.common.mekn.overwrite.MekNTileEntityFactory;
import com.atom.mekn.common.mekn.register.blocks.blockentity.factory.TileEntityNitrogenExtractorFactory;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.GuiDumpButton;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiChemicalGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.inventory.warning.WarningTracker;
import mekanism.common.tier.FactoryTier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MekNGuiFactory extends GuiConfigurableTile<MekNTileEntityFactory<?>, MekanismTileContainer<MekNTileEntityFactory<?>>> {

    @Nullable
    private GuiDumpButton<?> dumpButton;

    public MekNGuiFactory(MekanismTileContainer<MekNTileEntityFactory<?>> container, Inventory inv, Component title) {
        super(container, inv, title);
        if (tile.hasSecondaryResourceBar()) {
            imageHeight += 11;
            inventoryLabelY = 85;
        } else {
            inventoryLabelY = 75;
        }
        if (tile.tier == FactoryTier.ULTIMATE) {
            imageWidth += 34;
            inventoryLabelX = 26;
        }
        titleLabelY = 4;
        dynamicSlots = true;
    }
    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        //排序按钮
        //addRenderableWidget(new MekNGuiSortingTab(this, tile));

        //右侧能量条
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), imageWidth - 12, 16, 52))
                .warning(WarningTracker.WarningType.NOT_ENOUGH_ENERGY, tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY, 0));
        //根据具体的工厂类型渲染专属 UI

        if (tile instanceof TileEntityNitrogenExtractorFactory nitrogenFactory) {

            //横排渲染独立化学罐，替换掉进度条
            int baseX = tile.tier == FactoryTier.BASIC ? 50 : tile.tier == FactoryTier.ADVANCED ? 30 : tile.tier == FactoryTier.ELITE ? 24 : 22;
            int baseXMult = tile.tier == FactoryTier.BASIC ? 38 : tile.tier == FactoryTier.ADVANCED ? 26 : 19;

            for (int i = 0; i < tile.tier.processes; i++) {
                int index = i;
                // y 坐标设为 15，这样储罐底部刚好在 73 左右，完美卡在玩家背包上方
                addRenderableWidget(new GuiChemicalGauge(
                        () -> nitrogenFactory.getChemicalTank(index),
                        () -> nitrogenFactory.getChemicalTanks(null),
                        GaugeType.STANDARD, this, 4 + baseX + (i * baseXMult), 15));
            }

            // 【氮气提取器专属】：读取自定义的真实能耗变量
            addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), nitrogenFactory::getClientEnergyUsed));

        } else {
            // 【其他常规工厂】：原版逻辑
            addRenderableWidget(new GuiEnergyTab(this, tile.getEnergyContainer(), tile::getLastUsage));
            // ... 你原来的其他 else 逻辑 ...
        }

        // 注意：要把进度条的循环挪到 else 分支外面，但要在里面加个判断，跳过氮气工厂！
        if (!(tile instanceof TileEntityNitrogenExtractorFactory)) {
            int baseX = tile.tier == FactoryTier.BASIC ? 55 : tile.tier == FactoryTier.ADVANCED ? 35 : tile.tier == FactoryTier.ELITE ? 29 : 27;
            int baseXMult = tile.tier == FactoryTier.BASIC ? 38 : tile.tier == FactoryTier.ADVANCED ? 26 : 19;

            for (int i = 0; i < tile.tier.processes; i++) {
                int cacheIndex = i;
                addRenderableWidget(new GuiProgress(() -> tile.getScaledProgress(1, cacheIndex), ProgressType.DOWN, this, 4 + baseX + (i * baseXMult), 33))
                        .recipeViewerCategory(tile)
                        .warning(WarningTracker.WarningType.INPUT_DOESNT_PRODUCE_OUTPUT, tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT, cacheIndex));
            }
        }
    }

        @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        renderInventoryText(guiGraphics, dumpButton == null ? getXSize() : dumpButton.getRelativeX());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }
}
