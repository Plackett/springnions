package com.blueking6.springnions.init;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

public class soymilkblock extends LiquidBlock{

	public soymilkblock(Supplier<? extends FlowingFluid> p_54694_, Properties p_54695_) {
		super(p_54694_, p_54695_);
	}

	
	
	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if(!level.isClientSide()) {
			System.out.println("SERVER KNOWS CLIENT TOUCHED SOYMILK");
			((LivingEntity)entity).removeAllEffects();
		} else {
			System.out.println("CLIENT TOUCHED SOYMILK");
		}
	}
	
}
