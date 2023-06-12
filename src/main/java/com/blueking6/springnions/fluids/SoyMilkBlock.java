package com.blueking6.springnions.fluids;

import java.util.function.Supplier;

import org.joml.Vector3f;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

public class SoyMilkBlock extends LiquidBlock {

	public SoyMilkBlock(Supplier<? extends FlowingFluid> p_54694_, Properties p_54695_) {
		super(p_54694_, p_54695_);
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (!level.isClientSide()) {
			if (entity instanceof LivingEntity) {
				((LivingEntity) entity).removeAllEffects();
				level.addParticle(new DustParticleOptions(new Vector3f(pos.getX(), pos.getY(), pos.getZ()), 5.0F),
						(double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), 0.0D, 0.0D, 0.0D);
			}
		} else {

		}
	}

}
