package com.atom.mekn.common.mekn.client.gui;

import com.atom.mekn.common.mekn.register.blocks.blockentity.TileEntityIsotopicEncapsulationPress;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiChemicalGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class GuiIsotopicEncapsulationPress extends GuiMekanismTile<TileEntityIsotopicEncapsulationPress, MekanismTileContainer<TileEntityIsotopicEncapsulationPress>> {

    public GuiIsotopicEncapsulationPress(MekanismTileContainer<TileEntityIsotopicEncapsulationPress> container, Inventory inv, Component title) {
        super(container, inv, title);
        // 设定 GUI 画布大小
        this.imageWidth = 176;
        this.imageHeight = 175;

        this.inventoryLabelY = this.imageHeight - 94; // 调整“库存”文字的位置
        this.dynamicSlots = true; // 自动渲染 Tile 中定义的物品槽位
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();

        // 1. 能量条：垂直样式，放在最左侧 (坐标 7, 16)
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(0, null), 7, 16));

        // 2. 能量选项卡：点击可以查看详细能耗
        addRenderableWidget(new GuiEnergyTab(this, (MachineEnergyContainer<?>) tile.getEnergyContainer(0, null), tile::getEnergyUsed));

        // 3. 化学品仪表：放在能量条旁边，显示消耗的气体 (坐标 26, 13)
        // 假设你在 Tile 里定义的变量名叫 gasTank
        addRenderableWidget(new GuiChemicalGauge(() -> tile.gasTank, () -> tile.getChemicalTanks(null),
                GaugeType.STANDARD, this, 26, 13));

        // 4. 进度条：放在中间，指向右侧 (坐标 77, 37)
        // 注意：getProgress 需要在 Tile 中返回 0.0 到 1.0 之间的 double
        addRenderableWidget(new GuiProgress(tile::getProgress, ProgressType.RIGHT, this, 77, 37));

        addRenderableWidget(Button.builder(Component.translatable("gui.back"), (button) -> this.onClose())
                .bounds(getGuiLeft() + 4, getGuiTop() + 4, 40, 20)
                .build());
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // 渲染机器名称（居中）
        renderTitleText(guiGraphics);
        // 渲染“玩家库存”文字
        renderInventoryText(guiGraphics);
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }




}