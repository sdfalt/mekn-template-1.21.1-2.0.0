package com.atom.mekn.common.mekn.register.blocks.blockentity;

import com.atom.mekn.common.mekn.overwrite.TransferEfficiency;
import com.atom.mekn.common.mekn.recipes.RecipeTypesRegister;
import com.atom.mekn.common.mekn.recipes.recipe.SuperCentrifugeRecipe;
import com.atom.mekn.common.mekn.register.blocks.BlcokRegister;
import com.atom.mekn.common.mekn.register.sounds.SoundRegister;
import com.atom.mekn.common.utils.ClientSoundUtils;
import com.atom.mekn.common.utils.SoundUtils;
import mekanism.api.*;
import mekanism.api.chemical.*;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.functions.ConstantPredicates;
import mekanism.common.attachments.containers.ContainerType;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.integration.energy.EnergyCompatUtils;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.inventory.container.sync.SyncableLong;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.chemical.ChemicalInventorySlot;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.interfaces.IBoundingBlock;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TileEntitySuperCentrifuge extends TileEntityMekanism implements IBoundingBlock {

    public static final long MAX_GAS = 10L * 10000;
    public IChemicalTank inputTank;
    public IChemicalTank outputTank;
    private long clientEnergyUsed = 0L;
    private float speedMultiplier = 1.0f;
    private SuperCentrifugeRecipe currentRecipe;
    private int processTicks;
    private int totalTicks;
    private MachineEnergyContainer<TileEntitySuperCentrifuge> energyContainer;
    ChemicalInventorySlot inputSlot;
    ChemicalInventorySlot outputSlot;
    EnergyInventorySlot energySlot;

    @Nullable
    private BlockCapabilityCache<IChemicalHandler, @Nullable Direction> selfEjectChemical;
    @Nullable
    private BlockCapabilityCache<IChemicalHandler, @Nullable Direction> ejectChemical;
    private int ejectDelayTicks;
    private final TransferEfficiency currentEfficiency = TransferEfficiency.ULTRA;
    private int cachedProcessTicks = 0;
    private int cachedTotalTicks = 0;
    private SuperCentrifugeRecipe cachedRecipe;
    private Holder<Chemical> lastInputChemical;
    private long baseEnergyPerTick;
    private long currentEnergyPerTick;
    private int currentBatchSize = 1;
    private static final long EXTRA_ENERGY_PER_TICK = 1000;

    private static final float SOUND_ATTENUATION_DISTANCE = 16.0F;
    private static final int SOUND_LOOP_INTERVAL_TICKS = 41; // 改成你音频实际的 tick 长度
    private boolean lastClientActive = false;
    private int soundLoopTimer = -1;

    private int serverSoundTimer = -1;

    public TileEntitySuperCentrifuge(BlockPos pos, BlockState state) {
        super(BlcokRegister.SUPER_CENTRIFUGE, pos, state);
        ejectDelayTicks = 0;
    }

    @NotNull
    @Override
    public IChemicalTankHolder getInitialChemicalTanks(IContentsListener listener) {
        ChemicalTankHelper builder = ChemicalTankHelper.forSide(facingSupplier);
        builder.addTank(inputTank = BasicChemicalTank.createModern(MAX_GAS,
                ChemicalTankHelper.radioactiveInputTankPredicate(() -> outputTank),
                ConstantPredicates.alwaysTrueBi(), this::containsRecipe,
                ChemicalAttributeValidator.ALWAYS_ALLOW, listener), RelativeSide.BACK, RelativeSide.TOP);
        builder.addTank(outputTank = BasicChemicalTank.output(MAX_GAS, listener), RelativeSide.FRONT);
        return builder.build();
    }
    @Override
    protected boolean canPlaySound() {
        return false; // 彻底禁用 Mek 原生音效
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSide(facingSupplier);
        builder.addContainer(energyContainer = MachineEnergyContainer.input(this, listener), RelativeSide.LEFT, RelativeSide.RIGHT, RelativeSide.BOTTOM);
        return builder.build();
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSide(facingSupplier);
        builder.addSlot(inputSlot = ChemicalInventorySlot.fill(inputTank, listener, 5, 56));
        builder.addSlot(outputSlot = ChemicalInventorySlot.drain(outputTank, listener, 155, 56));
        builder.addSlot(energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 155, 14));
        inputSlot.setSlotType(ContainerSlotType.INPUT);
        inputSlot.setSlotOverlay(SlotOverlay.MINUS);
        outputSlot.setSlotType(ContainerSlotType.OUTPUT);
        outputSlot.setSlotOverlay(SlotOverlay.PLUS);
        return builder.build();
    }

    private static final float BASE_SPEED_MULTIPLIER = 3.0f;

    @Override
    protected void presetVariables() {
        super.presetVariables();
        this.speedMultiplier = BASE_SPEED_MULTIPLIER;
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (upgrade == Upgrade.SPEED) {
            int speed = upgradeComponent.getUpgrades(Upgrade.SPEED);
            speed = Math.min(6, speed);
            currentBatchSize = 1 + speed * 2;

            if (currentBatchSize > 32) currentBatchSize = 32;
            float upgradeMultiplier = 1 + speed;
            float totalMultiplier = BASE_SPEED_MULTIPLIER * upgradeMultiplier;
            speedMultiplier = Math.min(totalMultiplier, 30.0f);
            if (currentRecipe != null) {
                updateEnergyPerTick();
                int baseTicks = currentRecipe.getProcessTicks();
                totalTicks = Math.max(1, (int) Math.ceil(baseTicks / speedMultiplier));
            }
        }
    }

    @Override
    protected boolean onUpdateServer() {
        boolean sendUpdatePacket = super.onUpdateServer();
        performSlotInteractions();
        processRecipe();
        autoEjectOutput();

        if (processTicks != cachedProcessTicks || totalTicks != cachedTotalTicks) {
            cachedProcessTicks = processTicks;
            cachedTotalTicks = totalTicks;
            sendUpdatePacket = true;
        }

        // 停止声音逻辑
        if (!isMachineActuallyRunning()) {
            SoundUtils.stopServerSoundForNearby((ServerLevel) this.level, this.worldPosition,
                    SoundRegister.LIXINJI.get(), SOUND_ATTENUATION_DISTANCE);
            serverSoundTimer = -1; // 重置定时器
        } else {
            // 机器运行时循环播放声音
            serverSoundTimer--;
            if (serverSoundTimer <= 0) {
                if (this.level != null) {
                    this.level.playSound(null, this.worldPosition,
                            SoundRegister.LIXINJI.get(),
                            net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                serverSoundTimer = SOUND_LOOP_INTERVAL_TICKS; // 重置为音频长度（21 tick）
            }
        }

        return sendUpdatePacket;
    }


    @Override
    protected void onUpdateClient() {
        super.onUpdateClient();
        boolean isNowRunning = isMachineActuallyRunning();
        if (isNowRunning && !lastClientActive) {
            soundLoopTimer = SOUND_LOOP_INTERVAL_TICKS;
        }
        if (isNowRunning) {
            soundLoopTimer--;
            if (soundLoopTimer <= 0) {
                ClientSoundUtils.playClientMachineSound(SoundRegister.LIXINJI.get(), this.worldPosition, SOUND_ATTENUATION_DISTANCE, 1.0F, 1.0F);
                soundLoopTimer = SOUND_LOOP_INTERVAL_TICKS;
            }
        } else if (!isNowRunning && lastClientActive) {
            ClientSoundUtils.stopClientSound(SoundRegister.LIXINJI.get());
            soundLoopTimer = -1;
        }
        lastClientActive = isNowRunning;
    }


    private boolean isMachineActuallyRunning() {
        if (inputTank == null || inputTank.isEmpty()) {
            return false;
        }
        return currentRecipe != null || cachedRecipe != null;
    }

    private void performSlotInteractions() {
        energySlot.fillContainerOrConvert();
        inputSlot.fillTank();
        outputSlot.drainTank();
    }

    private void processRecipe() {
        if (currentRecipe != null && processTicks < totalTicks) {
            ChemicalStack currentInput = inputTank.getStack();
            if (!currentRecipe.matches(currentInput)) {
                currentRecipe = null;
                processTicks = 0;
                totalTicks = 0;
                setActive(false);
            }
        }
        if (currentRecipe == null || processTicks >= totalTicks) {
            trySelectNewRecipe();
        }
        if (currentRecipe != null && processTicks < totalTicks) {
            processCurrentRecipeTick();
        } else {
            setActive(false);
        }
    }

    private void trySelectNewRecipe() {
        ChemicalStack input = inputTank.getStack();
        if (input.isEmpty()) {
            currentRecipe = null;
            cachedRecipe = null;
            lastInputChemical = null;
            return;
        }
        boolean typeChanged = lastInputChemical == null || !input.is(lastInputChemical);
        if (typeChanged) {
            cachedRecipe = findRecipe(input);
            if (cachedRecipe != null) {
                lastInputChemical = input.getChemicalHolder();
            } else {
                lastInputChemical = null;
            }
        }
        if (cachedRecipe == null) {
            currentRecipe = null;
            return;
        }
        long requiredAmount = cachedRecipe.getInput().getAmount();
        if (input.getAmount() < requiredAmount) {
            currentRecipe = null;
            return;
        }
        if (!canProcess(cachedRecipe, currentBatchSize)) {
            currentRecipe = null;
            return;
        }
        // 成功匹配新配方
        currentRecipe = cachedRecipe;
        baseEnergyPerTick = currentRecipe.getEnergyPerTick();
        updateEnergyPerTick();
        int baseTicks = currentRecipe.getProcessTicks();
        totalTicks = Math.max(1, (int) Math.ceil(baseTicks / speedMultiplier));
        processTicks = 0;

    }

    private void processCurrentRecipeTick() {
        if (currentRecipe == null) return;
        long singleInput = currentRecipe.getInput().getAmount();
        long totalRequiredInput = singleInput * currentBatchSize;
        long totalOutput = currentRecipe.getOutput().getAmount() * currentBatchSize;
        int speed = upgradeComponent.getUpgrades(Upgrade.SPEED);
        long batchEnergy = currentEnergyPerTick * currentBatchSize;
        long extraEnergy = EXTRA_ENERGY_PER_TICK * (1 + speed);
        long totalEnergy = batchEnergy + extraEnergy;

        if (inputTank.getStored() < totalRequiredInput) { return; }
        if (outputTank.getCapacity() - outputTank.getStored() < totalOutput) { return; }
        if (energyContainer.getEnergy() < totalEnergy) { return; }

        energyContainer.extract(totalEnergy, Action.EXECUTE, AutomationType.INTERNAL);
        clientEnergyUsed += totalEnergy;
        inputTank.extract(totalRequiredInput, Action.EXECUTE, AutomationType.INTERNAL);

        ChemicalStack output = currentRecipe.getOutput().copyWithAmount(totalOutput);
        outputTank.insert(output, Action.EXECUTE, AutomationType.INTERNAL);
        processTicks += currentBatchSize;

        if (processTicks >= totalTicks) {
            currentRecipe = null;
            processTicks = 0;
            totalTicks = 0;
            baseEnergyPerTick = 0;
            currentEnergyPerTick = 0;
            setActive(false); // 配方结束，停止
        }else {
            setActive(true); // 恢复这一行
        }
        // 不再需要 else { setActive(true); }，因为开始新配方时已设置
    }

    private void autoEjectOutput() {
        if (ejectDelayTicks == 0) {
            Direction front = getDirection();
            BlockPos boundingPos = worldPosition.relative(front);
            BlockPos pipePos = boundingPos.relative(front);
            if (selfEjectChemical == null) {
                selfEjectChemical = Capabilities.CHEMICAL.createCache((ServerLevel) level, worldPosition, front);
            }
            IChemicalHandler sourceHandler = selfEjectChemical.getCapability();
            if (ejectChemical == null) {
                ejectChemical = Capabilities.CHEMICAL.createCache((ServerLevel) level, pipePos, front.getOpposite());
            }
            IChemicalHandler targetHandler = ejectChemical.getCapability();

            if (sourceHandler != null && targetHandler != null && !outputTank.isEmpty()) {
                long amount = currentEfficiency.getMaxPerTransfer();
                ChemicalStack stack = outputTank.extract(amount, Action.SIMULATE, AutomationType.INTERNAL);
                if (!stack.isEmpty()) {
                    ChemicalStack remainder = targetHandler.insertChemical(stack, Action.EXECUTE);
                    if (!remainder.isEmpty()) {
                        long inserted = stack.getAmount() - remainder.getAmount();
                        if (inserted > 0) {
                            outputTank.extract(inserted, Action.EXECUTE, AutomationType.INTERNAL);
                        }
                    } else {
                        outputTank.extract(amount, Action.EXECUTE, AutomationType.INTERNAL);
                    }
                }
            }
            ejectDelayTicks = currentEfficiency.getIntervalTicks();
        } else {
            ejectDelayTicks--;
        }
    }

    private SuperCentrifugeRecipe findRecipe(ChemicalStack input) {
        if (level == null) return null;
        var all = level.getRecipeManager().getAllRecipesFor(RecipeTypesRegister.SUPER_CENTRIFUGE.get());
        for (var holder : all) {
            SuperCentrifugeRecipe recipe = holder.value();
            if (recipe.matches(input)) return recipe;
        }
        return null;
    }

    private boolean canProcess(SuperCentrifugeRecipe recipe, int batch) {
        long requiredOutput = recipe.getOutput().getAmount() * batch;
        long availableSpace = outputTank.getCapacity() - outputTank.getStored();
        if (availableSpace < requiredOutput) return false;
        ChemicalStack existingOutput = outputTank.getStack();
        if (!existingOutput.isEmpty() && !existingOutput.is(recipe.getOutput().getChemicalHolder())) return false;
        int speed = upgradeComponent.getUpgrades(Upgrade.SPEED);
        long baseEnergy = (long) Math.ceil(recipe.getEnergyPerTick() * (1 + speed) * batch);
        long extraEnergy = EXTRA_ENERGY_PER_TICK * (1 + speed);
        long requiredEnergy = baseEnergy + extraEnergy;
        return energyContainer.getEnergy() >= requiredEnergy;
    }
    public boolean containsRecipe(@NotNull ChemicalStack stack) { return true; }
    public double getProgress() {
        if (totalTicks <= 0) return 0;
        return (double) processTicks / totalTicks;
    }
    public long getEnergyUsed() { return clientEnergyUsed; }
    @Override
    public int getRedstoneLevel() {
        return MekanismUtils.redstoneLevelFromContents(inputTank.getStored(), inputTank.getCapacity());
    }
    @Override
    protected boolean makesComparatorDirty(ContainerType<?, ?, ?> type) { return type == ContainerType.CHEMICAL; }
    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableLong.create(this::getEnergyUsed, value -> clientEnergyUsed = value));
        container.track(SyncableInt.create(() -> processTicks, value -> processTicks = value));
        container.track(SyncableInt.create(() -> totalTicks, value -> totalTicks = value));
        container.track(SyncableLong.create(() -> currentEnergyPerTick, value -> currentEnergyPerTick = value));
    }
    @Override
    public boolean isOffsetCapabilityDisabled(@NotNull BlockCapability<?, @Nullable Direction> capability, Direction side, @NotNull Vec3i offset) {
        if (EnergyCompatUtils.isEnergyCapability(capability)) return notEnergyPort(side, offset);
        else if (capability == Capabilities.CHEMICAL.block()) return notChemicalPort(side, offset);
        return notEnergyPort(side, offset) && notChemicalPort(side, offset);
    }
    private boolean notChemicalPort(Direction side, Vec3i offset) {
        if (offset.equals(new Vec3i(0, 1, 0))) return side != Direction.UP;
        Direction back = getOppositeDirection();
        if (offset.equals(new Vec3i(back.getStepX(), 0, back.getStepZ()))) return side != back;
        Direction front = getDirection();
        if (offset.equals(new Vec3i(front.getStepX(), 0, front.getStepZ()))) return side != front;
        return true;
    }
    private boolean notEnergyPort(Direction side, Vec3i offset) {
        if (offset.equals(Vec3i.ZERO)) return side != Direction.DOWN;
        Direction left = getLeftSide();
        if (offset.equals(new Vec3i(left.getStepX(), 0, left.getStepZ()))) return side != left;
        Direction right = left.getOpposite();
        if (offset.equals(new Vec3i(right.getStepX(), 0, right.getStepZ()))) return side != right;
        return true;
    }
    @Override
    public void loadAdditional(@NotNull CompoundTag nbt, @NotNull HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        processTicks = nbt.getInt("processTicks");
        totalTicks = nbt.getInt("totalTicks");
        currentRecipe = null;
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt, @NotNull HolderLookup.Provider provider) {
        super.saveAdditional(nbt, provider);
        nbt.putInt("processTicks", processTicks);
        nbt.putInt("totalTicks", totalTicks);
    }
    @Override
    protected void invalidateDirectionCaches(Direction newDirection) {
        super.invalidateDirectionCaches(newDirection);
        selfEjectChemical = null;
        ejectChemical = null;
    }
    private void updateEnergyPerTick() {
        if (currentRecipe == null) return;
        int speed = upgradeComponent.getUpgrades(Upgrade.SPEED);
        float energyMultiplier = 1 + speed;
        currentEnergyPerTick = (long) Math.ceil(baseEnergyPerTick * energyMultiplier);
    }
}
