package com.blueking6.springnions.init;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class onion extends CropBlock {
	public static final IntegerProperty AGE = BlockStateProperties.AGE_5;
	
	public onion(Properties properties) {
		super(properties);
	}

	@Override
	public int getMaxAge() {
		// TODO Auto-generated method stub
		return 7;
	}

	@Override
	protected ItemLike getBaseSeedId() {
		// TODO Auto-generated method stub
		return ItemInit.ONION.get();
	}
}
