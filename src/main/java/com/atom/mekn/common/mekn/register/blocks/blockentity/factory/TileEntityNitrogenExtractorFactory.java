package com.atom.mekn.common.mekn.register.blocks.blockentity.factory;

import com.atom.mekn.common.mekn.chemicals.chemical.ModChemicals;
import com.atom.mekn.common.mekn.overwrite.MekNTileEntityFactory;
import com.atom.mekn.common.mekn.overwrite.slot.MekNFactoryInputInventorySlot;
import com.atom.mekn.common.mekn.register.sounds.SoundRegister;
import com.atom.mekn.common.utils.Modutils;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.Upgrade;
import mekanism.api.chemical.BasicChemicalTank;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalTank;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import mekanism.common.tile.component.config.slot.ChemicalSlotInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TileEntityNitrogenExtractorFactory extends MekNTileEntityFactory<MekanismRecipe<?>> {

    public static final long OUTPUT_CAPACITY_PER_PROCESS = 50_000L;
    public static final long ENERGY_PER_TICK = Modutils.feToJoules(5000L);
    public static final int BASE_NITROGEN_PER_TICK = 10;

    private static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE
    );
    private static final Set<RecipeError> GLOBAL_ERROR_TYPES = Set.of(
            RecipeError.NOT_ENOUGH_ENERGY
    );

    private List<IChemicalTank> outputTanks;
    private long clientEnergyUsed = 0L;


    private static final float SOUND_ATTENUATION_DISTANCE = 16.0F;
    private static final int SOUND_LOOP_INTERVAL_TICKS = 21; // 你的音频时长 tick
    private boolean lastClientActive = false;
    private int soundLoopTimer = -1;

    public TileEntityNitrogenExtractorFactory(Holder<Block> blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES, GLOBAL_ERROR_TYPES);

        // 能量面正常注册
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        ConfigInfo energyConfig = configComponent.getConfig(TransmissionType.ENERGY);
        if (energyConfig != null) {
            for (RelativeSide side : RelativeSide.values()) {
                energyConfig.setDataType(DataType.INPUT, side);
            }
        }

        //注册化学面
        if (!outputTanks.isEmpty()) {
            configComponent.setupOutputConfig(TransmissionType.CHEMICAL, outputTanks.getFirst());
            ConfigInfo chemicalConfig = configComponent.getConfig(TransmissionType.CHEMICAL);

            if (chemicalConfig != null) {
                chemicalConfig.addSlotInfo(DataType.OUTPUT, new ChemicalSlotInfo(false, true, outputTanks));
                for (RelativeSide side : RelativeSide.values()) {
                    chemicalConfig.setDataType(DataType.OUTPUT, side);
                }
            }
        }

        //绑定弹出组件
        ejectorComponent.setOutputData(configComponent, TransmissionType.CHEMICAL);
    }


    protected boolean canPlaySound() {
        return false;
    }

    @Override
    protected void presetVariables() {
        super.presetVariables();
        outputTanks = new ArrayList<>(tier.processes);
        for (int i = 0; i < tier.processes; i++) {
            outputTanks.add(BasicChemicalTank.output(OUTPUT_CAPACITY_PER_PROCESS, this));
        }

        // 幽灵槽位骗过基类
        processInfoSlots = new ProcessInfo[tier.processes];
        for (int i = 0; i < tier.processes; i++) {
            OutputInventorySlot dummyOutput = OutputInventorySlot.at(this, -9999, -9999);
            MekNFactoryInputInventorySlot dummyInput = MekNFactoryInputInventorySlot.create(this, i, dummyOutput,
                    recipeCacheLookupMonitors[i], -9999, -9999);
            processInfoSlots[i] = new ProcessInfo(i, dummyInput, dummyOutput, null);
        }

    }

    @Override
    protected void addSlots(InventorySlotHelper builder, IContentsListener listener, IContentsListener updateSortingListener) {}
    @NotNull
    @Override
    public IChemicalTankHolder getInitialChemicalTanks(IContentsListener listener) {
        ChemicalTankHelper builder = ChemicalTankHelper.forSideWithConfig(this);
        for (IChemicalTank tank : outputTanks) {
            builder.addTank(tank);
        }
        return builder.build();
    }

    @Override
    protected void onUpdateClient() {
        super.onUpdateClient();
        if (isRemote()) {
            boolean isActive = getActive();

            if (isActive && !lastClientActive) {soundLoopTimer = SOUND_LOOP_INTERVAL_TICKS;}
            if (isActive) {
                soundLoopTimer--;
                if (soundLoopTimer <= 0) {
                    com.atom.mekn.common.utils.ClientSoundUtils.playClientMachineSound(
                            SoundRegister.JINGHUA.get(),
                            this.worldPosition, SOUND_ATTENUATION_DISTANCE, 1.0F, 1.0F);
                    soundLoopTimer = SOUND_LOOP_INTERVAL_TICKS;
                }
            } else if (!isActive && lastClientActive) {
                com.atom.mekn.common.utils.ClientSoundUtils.stopClientSound(
                        SoundRegister.JINGHUA.get());
                soundLoopTimer = -1;
            }
            lastClientActive = isActive;
        }
    }

    @Override
    protected boolean onUpdateServer() {
        boolean sendUpdatePacket = super.onUpdateServer();
        int speedUpgrades = upgradeComponent.getUpgrades(Upgrade.SPEED);
        double speedMultiplier = 1.0 + speedUpgrades * 0.2;

        long actualEnergyPerProcess = (long) (ENERGY_PER_TICK * speedMultiplier);
        boolean anyActive = false;
        long totalEnergyUsedThisTick = 0L;

        for (int i = 0; i < tier.processes; i++) {
            IChemicalTank currentTank = outputTanks.get(i);
            int toProduce = (int) (BASE_NITROGEN_PER_TICK * speedMultiplier);
            if (energyContainer.getEnergy() >= actualEnergyPerProcess && currentTank.getNeeded() >= toProduce) {
                currentTank.insert(new ChemicalStack(ModChemicals.NITROGEN, toProduce), Action.EXECUTE, AutomationType.INTERNAL);
                energyContainer.extract(actualEnergyPerProcess, Action.EXECUTE, AutomationType.INTERNAL);
                totalEnergyUsedThisTick += actualEnergyPerProcess;
                setActiveState(true, i);
                anyActive = true;
            } else {
                setActiveState(false, i);
            }
        }
        if (anyActive) {
            if (!getActive()) {
                if (this.level != null) {
                    this.level.playSound(null, this.worldPosition, SoundRegister.JINGHUA.get(), net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }
            setActive(true);
        } else {
            if (getActive()) {
                stopSoundForNearbyPlayers();
                setActive(false);
            }
        }
        clientEnergyUsed = anyActive ? totalEnergyUsedThisTick : 0L;
        return sendUpdatePacket;
    }
    @Override
    public boolean hasSecondaryResourceBar() {
        return true;
    }

    public IChemicalTank getChemicalTank(int index) {
        if (index >= 0 && index < outputTanks.size()) {
            return outputTanks.get(index);
        }
        return null;
    }

    public long getClientEnergyUsed() {
        return clientEnergyUsed;
    }

    private void stopSoundForNearbyPlayers() {
        if (this.level != null && !this.level.isClientSide()) {
            var center = this.getBlockPos().getCenter();
            net.minecraft.world.phys.AABB box = new net.minecraft.world.phys.AABB(center.subtract(16, 16, 16), center.add(16, 16, 16));
            List<net.minecraft.server.level.ServerPlayer> players = this.level.getEntitiesOfClass(net.minecraft.server.level.ServerPlayer.class, box);
            for (net.minecraft.server.level.ServerPlayer player : players) {
                player.connection.send(new net.minecraft.network.protocol.game.ClientboundStopSoundPacket(
                        SoundRegister.JINGHUA.get().getLocation(),
                        net.minecraft.sounds.SoundSource.BLOCKS));
            }
        }
    }

    // ========== 配方存根（不变） ==========
    @Nullable
    @Override
    protected MekanismRecipe<?> findRecipe(int process, @NotNull ItemStack fallbackInput, @NotNull IInventorySlot outputSlot, @Nullable IInventorySlot secondaryOutputSlot) { return null; }
    @Override
    protected boolean isCachedRecipeValid(@Nullable CachedRecipe<MekanismRecipe<?>> cached, @NotNull ItemStack stack) { return false; }
    @Override
    protected int getNeededInput(MekanismRecipe<?> recipe, ItemStack inputStack) { return 1; }
    @Override
    public boolean isItemValidForSlot(@NotNull ItemStack stack) { return false; }
    @Override
    public boolean isValidInputItem(@NotNull ItemStack stack) { return false; }
    @Nullable
    @Override
    public MekanismRecipe<?> getRecipe(int cacheIndex) { return null; }
    @NotNull
    @Override
    public CachedRecipe<MekanismRecipe<?>> createNewCachedRecipe(@NotNull MekanismRecipe<?> recipe, int cacheIndex) {
        throw new UnsupportedOperationException("This factory does not use recipes");
    }
    @Override
    public @NotNull mekanism.common.recipe.IMekanismRecipeTypeProvider<?, MekanismRecipe<?>, ?> getRecipeType() { return null; }
}
