package com.blueking6.springnions.init;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class Onion extends CropBlock {
	public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
	
	public Onion(Properties properties) {
		super(properties);
	}

	@Override
	public int getMaxAge() {
		return 7;
	}

	@Override
	protected ItemLike getBaseSeedId() {
		// TODO Auto-generated method stub
		return ItemInit.ONION.get();
	}
}
