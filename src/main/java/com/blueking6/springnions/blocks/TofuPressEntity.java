package com.blueking6.springnions.blocks;

import com.blueking6.springnions.init.TileEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TofuPressEntity extends BlockEntity{

	public TofuPressEntity(BlockPos pos, BlockState state) {
		super(TileEntityInit.TOFU_PRESS.get(), pos, state);
	}

}
