package com.atom.mekn.common.mekn.register.blocks.blockentity;

import com.atom.mekn.common.mekn.recipes.RecipeTypesRegister;
import com.atom.mekn.common.mekn.recipes.input.NuclearRecipeInput;
import com.atom.mekn.common.mekn.recipes.recipe.BasicNuclearRecipe;
import com.atom.mekn.common.mekn.register.blocks.BlcokRegister;
import com.atom.mekn.common.mekn.register.sounds.SoundRegister;

import com.atom.mekn.common.utils.ClientSoundUtils;
import com.atom.mekn.common.utils.SoundUtils;
import mekanism.api.*;
import mekanism.api.chemical.BasicChemicalTank;
import mekanism.api.chemical.IChemicalTank;
import mekanism.api.fluid.IExtendedFluidTank;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.inventory.container.sync.SyncableLong;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TileEntityNuclearEngineeringTable extends TileEntityConfigurableMachine {
    private MachineEnergyContainer<TileEntityNuclearEngineeringTable> energyContainer;
    private IExtendedFluidTank oilTank;
    private IChemicalTank nitrogenTank;
//    private TileComponentConfig configComponent;
//    private TileComponentEjector ejectorComponent;
    EnergyInventorySlot energySlot;
    private IInventorySlot outputSlot;
    public int operatingTicks = 0;
    public int ticksRequired = 0;
    public long energyPerTick = 0;

    private int lastInputHash = 0;
    private BasicNuclearRecipe currentRecipe = null;
    private boolean hasAttemptedRecipe = false;

    // 3秒  = 0.05 分钟
    // 5秒  = 0.083 分钟
    // 10秒 = 0.166 分钟
    //单位为分钟
    private static final double SOUND_DURATION_MINUTES = 0.0175;
    private boolean lastClientActive = false;
    private int soundLoopTimer = -1;

    @Override
    protected boolean canPlaySound() {
        return false;
    }


    private void stopSoundForNearbyPlayers() {
        if (this.level != null && !this.level.isClientSide()) {
            var center = this.getBlockPos().getCenter();
            AABB box = new AABB(center.subtract(16, 16, 16), center.add(16, 16, 16));
            List<ServerPlayer> players = this.level.getEntitiesOfClass(ServerPlayer.class, box);
            for (ServerPlayer player : players) {
                player.connection.send(new ClientboundStopSoundPacket(SoundRegister.MACHINE_WORK.get().getLocation(), SoundSource.BLOCKS));
            }
        }
    }
    @Override
    protected void onUpdateClient() {
        super.onUpdateClient();
        if (isRemote()) {
            boolean isActive = getActive();
            if (isActive && !lastClientActive) {
                soundLoopTimer = (int)(SOUND_DURATION_MINUTES * 60.0 * 20.0);
            }

            if (isActive) {
                soundLoopTimer--;
                if (soundLoopTimer <= 0) {
                    ClientSoundUtils.playClientMachineSound(SoundRegister.MACHINE_WORK.get(), this.worldPosition, 12.0F, 1.0F, 1.0F);
                    soundLoopTimer = (int)(SOUND_DURATION_MINUTES * 60.0 * 20.0);
                }
            } else if (!isActive && lastClientActive) {
                ClientSoundUtils.stopClientSound(SoundRegister.MACHINE_WORK.get());
                soundLoopTimer = -1;
            }
            lastClientActive = isActive;
        }
    }

    public TileEntityNuclearEngineeringTable(BlockPos pos, BlockState state) {
        super(BlcokRegister.NUCLEAR_ENGINEERING_TABLE, pos, state);
        addComponent(upgradeComponent);
        configComponent.setupInputConfig(TransmissionType.FLUID, oilTank);
        configComponent.setupInputConfig(TransmissionType.CHEMICAL, nitrogenTank);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        configComponent.setupOutputConfig(TransmissionType.ITEM, outputSlot);
        registerInputSlots();


        ConfigInfo itemConfig = configComponent.getConfig(TransmissionType.ITEM);
        ConfigInfo fluidConfig = configComponent.getConfig(TransmissionType.FLUID);
        ConfigInfo chemicalConfig = configComponent.getConfig(TransmissionType.CHEMICAL);
        ConfigInfo energyConfig = configComponent.getConfig(TransmissionType.ENERGY);

        if (itemConfig != null) {
            itemConfig.setDataType(DataType.INPUT, RelativeSide.LEFT);
            itemConfig.setDataType(DataType.INPUT, RelativeSide.TOP);
            itemConfig.setDataType(DataType.OUTPUT, RelativeSide.BOTTOM);
            itemConfig.setDataType(DataType.OUTPUT, RelativeSide.RIGHT);
        }
        if (fluidConfig != null) {
            fluidConfig.setDataType(DataType.INPUT, RelativeSide.TOP);
            fluidConfig.setDataType(DataType.INPUT, RelativeSide.BOTTOM);
        }
        if (chemicalConfig != null) {
            chemicalConfig.setDataType(DataType.INPUT, RelativeSide.LEFT);
            chemicalConfig.setDataType(DataType.INPUT, RelativeSide.RIGHT);
        }
        if (energyConfig != null) {
            for (RelativeSide side : RelativeSide.values()) {
                energyConfig.setDataType(DataType.INPUT, side);
            }
        }

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM);
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this);
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                InputInventorySlot slot = InputInventorySlot.at(stack -> true, listener, 5 + col * 18, 85 + row * 18);
                builder.addSlot(slot);         // 添加到槽位系统
            }
        }
        outputSlot = OutputInventorySlot.at(listener, 152, 121);
        builder.addSlot(outputSlot);
        builder.addSlot(energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 152, 139));
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
    protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this);
        builder.addTank(oilTank = BasicFluidTank.input(50_000, fluid -> true, listener));
        return builder.build();
    }

    @NotNull
    @Override
    public IChemicalTankHolder getInitialChemicalTanks(IContentsListener listener) {
        ChemicalTankHelper builder = ChemicalTankHelper.forSide(this::getDirection);
        builder.addTank(nitrogenTank = BasicChemicalTank.inputModern(50_000, chem -> true, listener));
        return builder.build();
    }

    private int computeInputHash() {
        int result = 1;
        List<IInventorySlot> actualSlots = getInventorySlots(null);
        for (int i = 0; i < 25 && i < actualSlots.size(); i++) {
            ItemStack stack = actualSlots.get(i).getStack();
            if (stack.isEmpty()) {
                result = 31 * result;
            } else {
                result = 31 * result + ItemStack.hashItemAndComponents(stack);
                result = 31 * result + stack.getCount();
            }
        }
        if (oilTank != null && !oilTank.isEmpty()) {
            result = 31 * result + oilTank.getFluid().hashCode();
        } else {
            result = 31 * result;
        }
        if (nitrogenTank != null && !nitrogenTank.isEmpty()) {
            result = 31 * result + nitrogenTank.getStack().hashCode();
        } else {
            result = 31 * result;
        }
        return result;
    }

    private boolean canOutput(BasicNuclearRecipe recipe) {
        ItemStack result = null;
        if (level != null) {
            result = recipe.getResultItem(level.registryAccess());
        }
        if (result != null && result.isEmpty()) return true;
        ItemStack current = outputSlot.getStack();
        if (current.isEmpty()) return true;
        if (!ItemStack.isSameItemSameComponents(current, result)) return false;
        return current.getCount() + result.getCount() <= outputSlot.getLimit(result);
    }

    @Override
    public boolean onUpdateServer() {
        super.onUpdateServer();
        energySlot.fillContainerOrConvert();

        boolean sendUpdatePacket = false;
        int currentHash = computeInputHash();

        if (currentHash != lastInputHash) {
            lastInputHash = currentHash;
            hasAttemptedRecipe = false;
        }

        if (!hasAttemptedRecipe) {
            hasAttemptedRecipe = true;
            List<IInventorySlot> slots = getInventorySlots(null);
            List<ItemStack> items = new ArrayList<>(25);
            for (int i = 0; i < 25 && i < slots.size(); i++) {
                items.add(slots.get(i).getStack().copy());
            }
            NuclearRecipeInput input = new NuclearRecipeInput(items, oilTank.getFluid(), nitrogenTank.getStack());

            if (level != null) {
                currentRecipe = level.getRecipeManager()
                        .getRecipeFor(RecipeTypesRegister.NUCLEAR_ENGINEERING.get(), input, level)
                        .map(RecipeHolder::value)
                        .orElse(null);
            }
            if (currentRecipe != null) {
                this.energyPerTick = currentRecipe.getEnergyPerTick();
                this.ticksRequired = getEffectiveTicksRequired(currentRecipe.getTicksRequired());
                sendUpdatePacket = true;
            } else {
                operatingTicks = 0;
                if (getActive()) {
                    // ★ 一行代码停掉服务端声音包
                    SoundUtils.stopServerSoundForNearby((net.minecraft.server.level.ServerLevel) this.level, this.worldPosition, SoundRegister.MACHINE_WORK.get(), 12.0F);
                    setActive(false);
                    sendUpdatePacket = true;
                }
            }
        }

        if (currentRecipe != null) {
            if (!canOutput(this.currentRecipe)) {
                if (getActive()) {
                    SoundUtils.stopServerSoundForNearby((net.minecraft.server.level.ServerLevel) this.level, this.worldPosition, SoundRegister.MACHINE_WORK.get(), 12.0F);
                    setActive(false);
                    sendUpdatePacket = true;
                }
                operatingTicks = 0;
                return sendUpdatePacket;
            }
            if (this.energyContainer.getEnergy() >= this.energyPerTick) {
                if (!getActive()) {
                    this.level.playSound(null, this.worldPosition, SoundRegister.MACHINE_WORK.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                    setActive(true);
                    sendUpdatePacket = true;
                }
                this.energyContainer.extract(this.energyPerTick, Action.EXECUTE, AutomationType.INTERNAL);
                if (this.operatingTicks < this.ticksRequired) {
                    operatingTicks++;
                } else {
                    finishRecipe(this.currentRecipe);

                    if (!canOutput(this.currentRecipe)) {
                        SoundUtils.stopServerSoundForNearby((net.minecraft.server.level.ServerLevel) this.level, this.worldPosition, SoundRegister.MACHINE_WORK.get(), 12.0F);
                        setActive(false);
                        sendUpdatePacket = true;
                        operatingTicks = 0;
                        return sendUpdatePacket;
                    }

                    operatingTicks = 0;
                    hasAttemptedRecipe = false;
                    markForSave();
                    return sendUpdatePacket;
                }
            } else {
                if (getActive()) {
                    SoundUtils.stopServerSoundForNearby((net.minecraft.server.level.ServerLevel) this.level, this.worldPosition, SoundRegister.MACHINE_WORK.get(), 12.0F);
                    setActive(false);
                    sendUpdatePacket = true;
                }
            }
        } else {
            if (getActive()) {
                SoundUtils.stopServerSoundForNearby((net.minecraft.server.level.ServerLevel) this.level, this.worldPosition, SoundRegister.MACHINE_WORK.get(), 12.0F);
                setActive(false);
                sendUpdatePacket = true;
            }
            operatingTicks = 0;
        }
        return sendUpdatePacket;
    }

    @Override
    protected void presetVariables() {
        super.presetVariables();
    }


    public IChemicalTank getNitrogenTank() {
        return nitrogenTank;
    }

    public IExtendedFluidTank getOilTank() {
        return oilTank;
    }

    public MachineEnergyContainer<TileEntityNuclearEngineeringTable> getEnergyContainer() {
        return energyContainer;
    }

    public long getEnergyUsed() {
        return getActive() ? energyPerTick : 0L;
    }

    private void finishRecipe(BasicNuclearRecipe recipe) {
        List<IInventorySlot> slots = getInventorySlots(null);
        var fluidTanks = this.getFluidTanks(null);
        var chemicalTanks = this.getChemicalTanks(null);
        for (ItemStackIngredient ingredient : recipe.getItemInputs()) {
            long remaining = ingredient.ingredient().count();
            for (int i = 0; i < 25 && remaining > 0; i++) {
                IInventorySlot slot = slots.get(i);
                ItemStack stack = slot.getStack();
                if (!stack.isEmpty() && ingredient.testType(stack)) {
                    long toTake = Math.min(remaining, stack.getCount());
                    slot.shrinkStack((int) toTake, Action.EXECUTE);
                    remaining -= toTake;
                }
            }
        }
        if (!fluidTanks.isEmpty()) {
            IExtendedFluidTank tank = fluidTanks.getFirst();
            long fluidAmount = recipe.getFluidInput().getNeededAmount(tank.getFluid());
            tank.shrinkStack((int) fluidAmount, Action.EXECUTE);
        }
        if (!chemicalTanks.isEmpty()) {
            IChemicalTank tank = chemicalTanks.getFirst();
            long chemicalAmount = recipe.getChemicalInput().getNeededAmount(tank.getStack());
            tank.shrinkStack(chemicalAmount, Action.EXECUTE);
        }
        ItemStack result = recipe.getResultItem(this.level.registryAccess());
        if (!result.isEmpty()) {
            outputSlot.insertItem(result.copy(), Action.EXECUTE, AutomationType.INTERNAL);
        }
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag nbt, @NotNull HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        if (nbt.contains("progress")) {
            this.operatingTicks = nbt.getInt("progress");
        }
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt, @NotNull HolderLookup.Provider provider) {
        super.saveAdditional(nbt, provider);
        nbt.putInt("progress", this.operatingTicks);
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableInt.create(() -> operatingTicks, value -> operatingTicks = value));
        container.track(SyncableInt.create(() -> ticksRequired, value -> ticksRequired = value));
        container.track(SyncableLong.create(() -> energyPerTick, value -> energyPerTick = value));
    }

    private int getEffectiveTicksRequired(int baseTicks) {
        if (upgradeComponent == null) {
            return baseTicks;
        }
        int speed = this.upgradeComponent.getUpgrades(Upgrade.SPEED);
        double multiplier = Math.pow(10, -speed / 8.0);
        return Math.max(1, (int) Math.ceil(baseTicks * multiplier));
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (upgrade == Upgrade.SPEED && currentRecipe != null) {
            this.ticksRequired = getEffectiveTicksRequired(currentRecipe.getTicksRequired());
            markForSave();
        }
    }

    @Override
    public boolean supportsUpgrade(Upgrade upgrade) {
        return super.supportsUpgrade(upgrade);
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        if (getActive()) {
            stopSoundForNearbyPlayers();
            setActive(false);
        }
    }

    private void registerInputSlots() {
        ConfigInfo itemConfig = configComponent.getConfig(TransmissionType.ITEM);
        if (itemConfig == null) return;

        // 收集所有 InputInventorySlot
        List<IInventorySlot> inputs = new ArrayList<>();
        for (IInventorySlot slot : getInventorySlots(null)) {
            if (slot instanceof InputInventorySlot) {
                inputs.add(slot);
            }
        }

        // 创建一个包含所有输入槽的 InventorySlotInfo
        InventorySlotInfo slotInfo = new InventorySlotInfo(true, false, inputs);
        itemConfig.addSlotInfo(DataType.INPUT, slotInfo);
    }
}
