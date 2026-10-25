package com.atom.mekn.common.mekn.client.gui;

import com.atom.mekn.common.mekn.register.blocks.blockentity.TileEntitySuperCentrifuge;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.bar.GuiHorizontalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiChemicalGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;


public class GuiSuperCentrifuge extends GuiMekanismTile<TileEntitySuperCentrifuge, MekanismTileContainer<TileEntitySuperCentrifuge>> {
    public GuiSuperCentrifuge(MekanismTileContainer<TileEntitySuperCentrifuge> container, Inventory inv, Component title) {
        super(container, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 180;  // 足够容纳所有元素
        this.inventoryLabelX += 40;
        //不加不会渲染物品栏
        this.dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements(); // 基础：注册槽位、升级窗口、红石、安全选项卡

        // 能量条（水平，模仿离心机位置）
        addRenderableWidget(new GuiHorizontalPowerBar(this, tile.getEnergyContainer(0, null), 115, 75));

        // 能量选项卡（显示能耗，支持单位切换）
        addRenderableWidget(new GuiEnergyTab(this, (MachineEnergyContainer<?>) tile.getEnergyContainer(0, null), tile::getEnergyUsed));

        // 输入化学品仪表（左侧）
        addRenderableWidget(new GuiChemicalGauge(() -> tile.inputTank, () -> tile.getChemicalTanks(null),
                GaugeType.STANDARD, this, 25, 13));

        // 输出化学品仪表（右侧）
        addRenderableWidget(new GuiChemicalGauge(() -> tile.outputTank, () -> tile.getChemicalTanks(null),
                GaugeType.STANDARD, this, 133, 13));

        addRenderableWidget(new GuiProgress(tile::getProgress, ProgressType.LARGE_RIGHT, this, 64, 39));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        renderInventoryText(guiGraphics);
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

}