package com.blueking6.springnions.crops;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.blueking6.config.SpringnionsCommonConfigs;
import com.blueking6.springnions.init.ItemInit;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams.Builder;

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
	public List<ItemStack> getDrops(BlockState state, Builder b) {
		List<ItemStack> returnitems = new ArrayList<ItemStack>();
		Random rand = new Random();
		float value = rand.nextFloat();
		int count = 1;
		float chance = 0.5714286f;
		if(state.getValue(AGE) == 7) {
			for(int i = 2;i < SpringnionsCommonConfigs.ONION_RETURNS.get(); i++) {
				if(value <= chance/i) {
					count++;
				} else { break; }
			}
			returnitems.add(new ItemStack(ItemInit.ONION.get(),count+1));
		} else {
			returnitems.add(new ItemStack(ItemInit.ONION.get(),count));
		}
		return returnitems;
	}

	@Override
	protected ItemLike getBaseSeedId() {
		// TODO Auto-generated method stub
		return ItemInit.ONION.get();
	}
}
