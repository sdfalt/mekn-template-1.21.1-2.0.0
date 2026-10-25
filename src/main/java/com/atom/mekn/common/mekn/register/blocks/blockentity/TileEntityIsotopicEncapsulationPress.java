package com.atom.mekn.common.mekn.register.blocks.blockentity;

import com.atom.mekn.common.mekn.recipes.RecipeTypesRegister;
import com.atom.mekn.common.mekn.recipes.input.IsotopicPressRecipeInput;
import com.atom.mekn.common.mekn.recipes.recipe.IsotopicPressRecipe;
import com.atom.mekn.common.mekn.register.blocks.BlcokRegister;
import com.atom.mekn.common.mekn.register.sounds.SoundRegister;
import com.atom.mekn.common.utils.ClientSoundUtils;
import com.atom.mekn.common.utils.SoundUtils;
import mekanism.api.*;
import mekanism.api.chemical.BasicChemicalTank;
import mekanism.api.chemical.IChemicalTank;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.inventory.container.sync.SyncableLong;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.interfaces.IBoundingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TileEntityIsotopicEncapsulationPress extends TileEntityMekanism implements IBoundingBlock {

    public IChemicalTank gasTank;
    private MachineEnergyContainer<TileEntityIsotopicEncapsulationPress> energyContainer;

    private InputInventorySlot inputSlot;
    private OutputInventorySlot outputSlot;
    private EnergyInventorySlot energySlot;

    private int progress;
    private int totalTicks;
    private static final long ENERGY_USAGE = 1000000L;
    private double multiplier = 1.0; // 速度乘数，默认1.0x

    private IsotopicPressRecipe cachedRecipe;

    private static final float SOUND_ATTENUATION_DISTANCE = 16.0F;
    private static final int SOUND_LOOP_INTERVAL_TICKS = 101; // 你的音频时长 tick
    private boolean lastClientActive = false;
    private int soundLoopTimer = -1;

    public TileEntityIsotopicEncapsulationPress(BlockPos pos, BlockState state) {
        super(BlcokRegister.ISOTOPIC_ENCAPSULATION_PRESS, pos, state);
        addComponent(upgradeComponent);
    }

    @Override
    protected boolean canPlaySound() {
        return false; // 彻底禁用 Mek 原生音效，防止鬼畜
    }

    @NotNull
    @Override
    public IChemicalTankHolder getInitialChemicalTanks(IContentsListener listener) {
        ChemicalTankHelper builder = ChemicalTankHelper.forSide(this::getDirection);
        gasTank = BasicChemicalTank.createModern(
                150_000,
                (chem, automation) -> automation != AutomationType.INTERNAL,
                (chem, automation) -> automation != AutomationType.INTERNAL,
                chem -> true,
                ChemicalAttributeValidator.ALWAYS_ALLOW,
                listener
        );
        builder.addTank(gasTank, RelativeSide.LEFT, RelativeSide.RIGHT);
        return builder.build();
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSide(this::getDirection);
        builder.addContainer(energyContainer = MachineEnergyContainer.input(this, listener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);
        inputSlot = InputInventorySlot.at(stack -> true, listener, 56, 35);
        builder.addSlot(inputSlot, RelativeSide.FRONT, RelativeSide.TOP);
        outputSlot = OutputInventorySlot.at(listener, 116, 35);
        builder.addSlot(outputSlot, RelativeSide.TOP);
        energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 152, 5);
        builder.addSlot(energySlot, RelativeSide.BOTTOM);
        return builder.build();
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
                    ClientSoundUtils.playClientMachineSound(SoundRegister.KEHUAN.get(), this.worldPosition, SOUND_ATTENUATION_DISTANCE, 1.0F, 1.0F);
                    soundLoopTimer = SOUND_LOOP_INTERVAL_TICKS;
                }
            } else if (!isActive && lastClientActive) {
                ClientSoundUtils.stopClientSound(SoundRegister.KEHUAN.get());
                soundLoopTimer = -1;
            }
            lastClientActive = isActive;
        }
    }
    // ================================

    @Override
    protected boolean onUpdateServer() {
        boolean sendUpdatePacket = super.onUpdateServer();
        energySlot.fillContainerOrConvert();
        IsotopicPressRecipeInput input = new IsotopicPressRecipeInput(inputSlot.getStack(), gasTank.getStack());
        long actualEnergyPerTick = (long) (ENERGY_USAGE * multiplier);
        if (cachedRecipe == null) {
            if (tryStartNewRecipe(input)) {
                int newTotalTicks = (int) Math.max(1, cachedRecipe.getProcessTicks() / multiplier);
                if (totalTicks != newTotalTicks) {
                    totalTicks = newTotalTicks;
                    sendUpdatePacket = true;
                }
                if (progress != 0) {
                    progress = 0;
                    sendUpdatePacket = true;
                }
            }
        }
        if (cachedRecipe != null) {

            if (energyContainer.getEnergy() < actualEnergyPerTick) {
                if (getActive()) {
                    stopSoundForNearbyPlayers(); // 停机时清理声音
                    setActive(false);
                    sendUpdatePacket = true;
                }
                return sendUpdatePacket;
            }

            if (!isInputStillValid(input)) {
                cancelCurrentRecipe();
                sendUpdatePacket = true;
                return sendUpdatePacket;
            }
            if (!this.getActive()) {

                if (this.level != null) {
                    this.level.playSound(null, this.worldPosition, SoundRegister.KEHUAN.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                setActive(true);
                sendUpdatePacket = true;
            }
            energyContainer.extract(actualEnergyPerTick, Action.EXECUTE, AutomationType.INTERNAL);
            progress++;
            sendUpdatePacket = true;
            if (progress >= totalTicks) {
                if (consumeInputsAndFinish(input)) {
                    cancelCurrentRecipe();
                } else {
                    cancelCurrentRecipe();
                }
                sendUpdatePacket = true;
            }
        } else {
            if (getActive()) {
                stopSoundForNearbyPlayers(); // 停机时清理声音
                setActive(false);
                sendUpdatePacket = true;
            }
        }

        return sendUpdatePacket;
    }

    // ========== 停止声音的数据包广播 ==========
    private void stopSoundForNearbyPlayers() {
        if (this.level != null && !this.level.isClientSide()) {
            var center = this.getBlockPos().getCenter();
            AABB box = new AABB(center.subtract(16, 16, 16), center.add(16, 16, 16));
            List<ServerPlayer> players = this.level.getEntitiesOfClass(ServerPlayer.class, box);
            for (ServerPlayer player : players) {
                player.connection.send(new ClientboundStopSoundPacket(
                        SoundRegister.KEHUAN.get().getLocation(),
                        SoundSource.BLOCKS));
            }
        }
    }
    // ======================================

    private void cancelCurrentRecipe() {
        boolean wasActive = this.getActive();
        cachedRecipe = null;
        if (progress != 0) {
            progress = 0;
        }
        if (totalTicks != 0) {
            totalTicks = 0;
        }
        if (wasActive) {
            setActive(false);
        }
    }

    private boolean tryStartNewRecipe(IsotopicPressRecipeInput input) {
        if (input.item().isEmpty() || input.chemical().isEmpty()) return false;
        if (energyContainer.getEnergy() < ENERGY_USAGE) return false;
        IsotopicPressRecipe recipe = findRecipe(input);
        if (recipe == null) return false;
        ItemStack possibleOutput = recipe.assemble(input, level.registryAccess());
        if (!canInsertOutput(possibleOutput)) return false;
        int requiredItems = recipe.getItemInput().getCount();
        long requiredGas = recipe.getGasInput().getAmount();
        if (inputSlot.getStack().getCount() < requiredItems) return false;
        if (gasTank.getStored() < requiredGas) return false;

        cachedRecipe = recipe;
        return true;
    }

    private boolean isInputStillValid(IsotopicPressRecipeInput input) {
        if (cachedRecipe == null) return false;
        return cachedRecipe.matches(input, level);
    }

    private boolean consumeInputsAndFinish(IsotopicPressRecipeInput input) {
        if (cachedRecipe == null) return false;
        int requiredItems = cachedRecipe.getItemInput().getCount();
        long requiredGas = cachedRecipe.getGasInput().getAmount();

        if (inputSlot.getStack().getCount() < requiredItems) return false;
        if (gasTank.getStored() < requiredGas) return false;

        inputSlot.shrinkStack(requiredItems, Action.EXECUTE);
        gasTank.shrinkStack(requiredGas, Action.EXECUTE);

        ItemStack result = null;
        if (level != null) {
            result = cachedRecipe.assemble(input, level.registryAccess());
        }
        if (result != null) {
            outputSlot.insertItem(result, Action.EXECUTE, AutomationType.INTERNAL);
        }
        return true;
    }

    private IsotopicPressRecipe findRecipe(IsotopicPressRecipeInput input) {
        if (level == null) return null;
        if (cachedRecipe != null && cachedRecipe.matches(input, level)) {
            return cachedRecipe;
        }
        return level.getRecipeManager()
                .getRecipeFor(RecipeTypesRegister.ISOTOPIC_PRESS_TYPE.get(), input, level)
                .map(RecipeHolder::value)
                .orElse(null);
    }

    private boolean canInsertOutput(ItemStack stack) {
        return outputSlot.insertItem(stack, Action.SIMULATE, AutomationType.INTERNAL).isEmpty();
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableLong.create(this::getEnergyUsed, value -> {}));
        container.track(SyncableInt.create(() -> progress, value -> progress = value));
        container.track(SyncableInt.create(() -> totalTicks, value -> totalTicks = value));
    }

    public double getProgress() {
        if (totalTicks <= 0) return 0;
        return (double) progress / totalTicks;
    }

    public long getEnergyUsed() {
        return ENERGY_USAGE;
    }

    @Override
    public boolean isOffsetCapabilityDisabled(@NotNull BlockCapability<?, @Nullable Direction> capability, Direction side, @NotNull Vec3i offset) {
        if (capability == Capabilities.ENERGY.block()) {
            return !(offset.getX() == 0 && offset.getY() == 0 && offset.getZ() == 1);
        }
        if (capability == Capabilities.CHEMICAL.block()) {
            return !(offset.getY() == 0 && offset.getZ() == 0 && (offset.getX() == 1 || offset.getX() == -1));
        }
        if (capability == Capabilities.ITEM.block()) {
            return !(offset.getX() == 0 && offset.getZ() == 0 && offset.getY() == 2);
        }
        return true;
    }

    @Nullable
    @Override
    public <T> T getOffsetCapabilityIfEnabled(@NotNull BlockCapability<T, @Nullable Direction> capability, Direction side, @NotNull Vec3i offset) {
        Direction facing = getDirection();

        if (capability == Capabilities.ENERGY.block()) {
            if (offset.getX() == 0 && offset.getY() == 0 && offset.getZ() == 1) {
                if (side == facing.getOpposite()) {
                    return energyHandlerManager != null ? energyHandlerManager.resolve(capability, side) : null;
                }
            }
        }

        if (capability == Capabilities.CHEMICAL.block()) {
            if (offset.getY() == 0 && offset.getZ() == 0) {
                if (offset.getX() == 1 || offset.getX() == -1) {
                    if (side == facing.getClockWise() || side == facing.getCounterClockWise()) {
                        return chemicalHandlerManager != null ? chemicalHandlerManager.resolve(capability, side) : null;
                    }
                }
            }
        }

        if (capability == Capabilities.ITEM.block()) {
            if (offset.getX() == 0 && offset.getZ() == 0 && offset.getY() == 2) {
                if (side == Direction.UP) {
                    return itemHandlerManager != null ? itemHandlerManager.resolve(capability, side) : null;
                }
            }
        }

        return null;
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (upgrade == Upgrade.SPEED) {
            int speed = upgradeComponent.getUpgrades(Upgrade.SPEED);

            double baseMultiplier = 1.0;
            for (int i = 0; i < speed; i++) {
                baseMultiplier *= 2;
                if (baseMultiplier > 128.0) baseMultiplier = 128.0;
            }

            double progressRatio = totalTicks > 0 ? (double) progress / totalTicks : 0;

            if (cachedRecipe != null) {
                totalTicks = (int) Math.max(1, cachedRecipe.getProcessTicks() / baseMultiplier);
                progress = (int) (progressRatio * totalTicks);
            }
            this.multiplier = baseMultiplier;
            setChanged();
        }
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
