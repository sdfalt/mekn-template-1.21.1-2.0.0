package com.atom.mekn.common.mekn.client.gui;

import com.atom.mekn.common.mekn.register.blocks.blockentity.TileEntityNuclearEngineeringTable;
import com.atom.mekn.common.utils.Modutils;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiChemicalGauge;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.common.MekanismLang;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tile.component.config.DataType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class GuiNuclearEngineeringTable extends GuiConfigurableTile<TileEntityNuclearEngineeringTable,MekanismTileContainer<TileEntityNuclearEngineeringTable>>{
    public GuiNuclearEngineeringTable(MekanismTileContainer<TileEntityNuclearEngineeringTable> container, Inventory inv, Component title) {
        super(container, inv, title);
        this.dynamicSlots = true;
        this.imageHeight = 265 ;
        this.imageWidth = 176;
        this.inventoryLabelX += 40;

    }

    @Override
    protected void addGuiElements() {

        super.addGuiElements();
        addRenderableWidget(new GuiChemicalGauge(tile::getNitrogenTank, () -> tile.getChemicalTanks(null), GaugeType.STANDARD.with(DataType.INPUT), this, 5, 20)
                .setLabel(Component.literal("氮气储备")));
        addRenderableWidget(new GuiFluidGauge(tile::getOilTank, () -> tile.getFluidTanks(null), GaugeType.STANDARD.with(DataType.INPUT_1), this, 26, 20)
                .setLabel(Component.literal("机油储备")));
        addRenderableWidget(new GuiInnerScreen(this, 47, 20, 110, 50, () -> List.of(
                Component.literal("系统状态: 运转中"),
                Component.literal("能量消耗: " + Modutils.formatEnergy(tile.getEnergyUsed()) )
        )).padding(3));
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), 162, 20));
        addRenderableWidget(new GuiEnergyTab(this, () -> {
            var energyContainer = tile.getEnergyContainer();
            return List.of(MekanismLang.USING.translate(tile.getEnergyUsed()),
                    MekanismLang.NEEDED.translate(energyContainer.getNeeded()));
        }));
        addRenderableWidget(new GuiProgress(
                () -> (double) tile.operatingTicks / tile.ticksRequired,
                ProgressType.RIGHT,
                this,
                104, 129    // 进度条对齐输出槽行
        ));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTitleText(guiGraphics);
        renderInventoryText(guiGraphics);
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

}