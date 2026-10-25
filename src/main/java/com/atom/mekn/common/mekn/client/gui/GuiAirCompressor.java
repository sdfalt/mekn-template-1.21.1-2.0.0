package com.atom.mekn.common.mekn.client.gui;

import com.atom.mekn.common.mekn.register.blocks.blockentity.TileEntityNitrogenExtractor;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.bar.GuiHorizontalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiChemicalGauge;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class GuiAirCompressor extends GuiConfigurableTile<TileEntityNitrogenExtractor, MekanismTileContainer<TileEntityNitrogenExtractor>> {

    public GuiAirCompressor(MekanismTileContainer<TileEntityNitrogenExtractor> container, Inventory inv, Component title) {
        super(container, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 180;
        this.dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();

        // 添加能量条 - 水平样式，放在右侧
        addRenderableWidget(new GuiHorizontalPowerBar(this, tile.getEnergyContainer(0, null), 115, 75));

        // 添加能量选项卡 - 显示能耗信息
        addRenderableWidget(new GuiEnergyTab(this, (MachineEnergyContainer<?>) tile.getEnergyContainer(0, null), tile::getEnergyUsed));

        // 添加输出化学品仪表 - 显示氮气输出罐状态
        addRenderableWidget(new GuiChemicalGauge(() -> tile.getChemicalTanks(null).getFirst(), () -> tile.getChemicalTanks(null),
                GaugeType.STANDARD, this, 25, 13));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        renderInventoryText(guiGraphics);
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }
}