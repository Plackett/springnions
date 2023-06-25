//MIT License
//
//Copyright (c) 2023 Blueking6
//
//Permission is hereby granted, free of charge, to any person obtaining a copy
//of this software and associated documentation files (the "Software"), to deal
//in the Software without restriction, including without limitation the rights
//to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//copies of the Software, and to permit persons to whom the Software is
//furnished to do so, subject to the following conditions:
//
//The above copyright notice and this permission notice shall be included in all
//copies or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//SOFTWARE.

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

public class Soy extends CropBlock {
	public static final IntegerProperty AGE = BlockStateProperties.AGE_7;

	public Soy(Properties properties) {
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
			for(int i = 2;i < SpringnionsCommonConfigs.SOYBEAN_RETURNS.get(); i++) {
				if(value <= chance/i) {
					count++;
				} else { break; }
			}
			returnitems.add(new ItemStack(ItemInit.SOYBEANS.get(),count+1));
		} else {
			returnitems.add(new ItemStack(ItemInit.SOYBEANS.get(),count));
		}
		return returnitems;
	}

	@Override
	protected ItemLike getBaseSeedId() {
		// TODO Auto-generated method stub
		return ItemInit.SOYBEANS.get();
	}
}
