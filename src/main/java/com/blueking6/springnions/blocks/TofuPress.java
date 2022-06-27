package com.blueking6.springnions.blocks;

import com.blueking6.springnions.init.TileEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class TofuPress extends Block implements EntityBlock {

	public static final IntegerProperty anim = IntegerProperty.create("animation", 0, 0);
	
	public TofuPress(Properties p_49795_) {
		super(p_49795_);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return TileEntityInit.TOFU_PRESS.get().create(pos, state);
	}
	
	

}
