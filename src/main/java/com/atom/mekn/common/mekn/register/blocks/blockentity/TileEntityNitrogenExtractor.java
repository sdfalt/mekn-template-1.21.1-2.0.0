package com.atom.mekn.common.mekn.register.blocks.blockentity; // ⚠️ 改成你的包名

import com.atom.mekn.common.mekn.chemicals.chemical.ModChemicals;
import com.atom.mekn.common.mekn.register.blocks.BlcokRegister;
import com.atom.mekn.common.mekn.register.sounds.SoundRegister; // ⚠️ 确保导入你的声音注册类
import com.atom.mekn.common.utils.Modutils;
import com.atom.mekn.common.utils.ClientSoundUtils;
import com.atom.mekn.common.utils.SoundUtils; // ⚠️ 确保导入你的 SoundUtils
import mekanism.api.*;
import mekanism.api.chemical.BasicChemicalTank;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalTank;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TileEntityNitrogenExtractor extends TileEntityConfigurableMachine {
    public static final long OUTPUT_CAPACITY = 50_000;
    public static final long ENERGY_PER_TICK = Modutils.feToJoules(5000L);
    public static final int BASE_NITROGEN_PER_TICK = 10;

    private IChemicalTank outputTank;
    private MachineEnergyContainer<TileEntityNitrogenExtractor> energyContainer;
    private EnergyInventorySlot energySlot;

    private long clientEnergyUsed = 0L;

    private static final int SOUND_LOOP_INTERVAL_TICKS = 20;
    private boolean lastClientActive = false;
    private int soundLoopTimer = -1;

    @Override
    protected boolean canPlaySound() {
        return false; // 彻底禁用 Mek 原生音效，防止鬼畜
    }

    private void stopSoundForNearbyPlayers() {
        if (this.level != null && !this.level.isClientSide()) {
            var center = this.getBlockPos().getCenter();
            AABB box = new AABB(center.subtract(16, 16, 16), center.add(16, 16, 16));
            List<ServerPlayer> players = this.level.getEntitiesOfClass(ServerPlayer.class, box);
            for (ServerPlayer player : players) {
                player.connection.send(new ClientboundStopSoundPacket(SoundRegister.JINGHUA.get().getLocation(), SoundSource.BLOCKS));
            }
        }
    }

    @Override
    protected void onUpdateClient() {
        super.onUpdateClient();
        if (isRemote()) {
            boolean isActive = getActive();

            if (isActive && !lastClientActive) {
                soundLoopTimer = SOUND_LOOP_INTERVAL_TICKS;
            }
            if (isActive) {
                soundLoopTimer--;
                if (soundLoopTimer <= 0) {
                    ClientSoundUtils.playClientMachineSound(SoundRegister.JINGHUA.get(), this.worldPosition, 12.0F, 1.0F, 1.0F);
                    soundLoopTimer = SOUND_LOOP_INTERVAL_TICKS;
                }
            }
            else if (!isActive && lastClientActive) {
                ClientSoundUtils.stopClientSound(SoundRegister.JINGHUA.get());
                soundLoopTimer = -1;
            }
            lastClientActive = isActive;
        }
    }
    public TileEntityNitrogenExtractor(BlockPos pos, BlockState state) {
        super(BlcokRegister.NITROGEN_EXTRACTOR, pos, state);

        configComponent.setupOutputConfig(TransmissionType.CHEMICAL, outputTank, RelativeSide.FRONT, RelativeSide.BACK, RelativeSide.LEFT, RelativeSide.RIGHT, RelativeSide.BOTTOM).setCanEject(true);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);

        ConfigInfo energyConfig = configComponent.getConfig(TransmissionType.ENERGY);
        if (energyConfig != null) {
            for (RelativeSide side : RelativeSide.values()) {
                energyConfig.setDataType(DataType.INPUT, side);
            }
        }
        ConfigInfo chemicalConfig = configComponent.getConfig(TransmissionType.CHEMICAL);
        if (chemicalConfig != null) {
            for (RelativeSide side : RelativeSide.values()) {
                chemicalConfig.setDataType(DataType.OUTPUT, side);
            }
        }

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.CHEMICAL);
    }
    @NotNull
    @Override
    public IChemicalTankHolder getInitialChemicalTanks(IContentsListener listener) {
        ChemicalTankHelper builder = ChemicalTankHelper.forSide(facingSupplier);
        outputTank = BasicChemicalTank.output(OUTPUT_CAPACITY, listener);
        builder.addTank(outputTank, RelativeSide.FRONT, RelativeSide.BACK, RelativeSide.LEFT, RelativeSide.RIGHT, RelativeSide.BOTTOM);
        return builder.build();
    }
    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this);
        builder.addContainer(energyContainer = MachineEnergyContainer.input(this, listener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this);
        builder.addSlot(energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 152, 5));
        return builder.build();
    }
    @Override
    protected boolean onUpdateServer() {
        super.onUpdateServer();

        energySlot.fillContainerOrConvert();
        if (energyContainer.getEnergy() >= ENERGY_PER_TICK) {
            int speedUpgrades = upgradeComponent.getUpgrades(Upgrade.SPEED);
            double speedMultiplier = 1.0 + speedUpgrades * 0.2;
            int toProduce = (int) Math.min(BASE_NITROGEN_PER_TICK * speedMultiplier, outputTank.getNeeded());
            long actualEnergyPerTick = (long) (ENERGY_PER_TICK * speedMultiplier);

            if (energyContainer.getEnergy() >= actualEnergyPerTick && toProduce > 0) {
                energyContainer.extract(actualEnergyPerTick, Action.EXECUTE, AutomationType.INTERNAL);
                ChemicalStack nitrogen = new ChemicalStack(ModChemicals.NITROGEN, toProduce);
                outputTank.insert(nitrogen, Action.EXECUTE, AutomationType.INTERNAL);
                clientEnergyUsed = actualEnergyPerTick;

                // ✅ 启动时播第一声（仅服务端发一次包触发，后续全交由客户端接管）
                if (!getActive()) {
                    this.level.playSound(null, this.worldPosition, SoundRegister.JINGHUA.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                setActive(true);
                return true;
            }
        }

        if (getActive()) {
            stopSoundForNearbyPlayers();
            setActive(false);
        }
        clientEnergyUsed = 0L;
        return false;
    }

    public long getEnergyUsed() {
        return clientEnergyUsed;
    }

    public double getProgress() {
        return getActive() ? 1.0 : 0.0;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        if (getActive()) {
            stopSoundForNearbyPlayers();
            setActive(false);
        }
    }
}
