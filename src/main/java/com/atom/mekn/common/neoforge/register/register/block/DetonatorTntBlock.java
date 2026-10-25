package com.atom.mekn.common.neoforge.register.register.block;

import com.atom.mekn.common.neoforge.register.register.entity.blockentity.DetonatorTntEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

public class DetonatorTntBlock extends TntBlock {

    public DetonatorTntBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void onCaughtFire(BlockState state, Level level, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
        if (!level.isClientSide) {
            spawnOurTnt(level, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, igniter);
        }
    }

    @Override
    public void wasExploded(Level level, BlockPos pos, Explosion explosion) {
        if (!level.isClientSide) {
            LivingEntity igniter = explosion.getIndirectSourceEntity() instanceof LivingEntity living ? living : null;
            spawnOurTnt(level, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, igniter);
        }
    }

    private void spawnOurTnt(Level level, double x, double y, double z, @Nullable LivingEntity igniter) {
        DetonatorTntEntity primedTnt = new DetonatorTntEntity(level, x, y, z, igniter, this);
        int i = primedTnt.getFuse();
        primedTnt.setFuse((short)(level.random.nextInt(i / 4) + i / 8));

        level.addFreshEntity(primedTnt);
        level.playSound(null, primedTnt.getX(), primedTnt.getY(), primedTnt.getZ(),
                SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(igniter, GameEvent.PRIME_FUSE, BlockPos.containing(x, y, z));
    }
}
