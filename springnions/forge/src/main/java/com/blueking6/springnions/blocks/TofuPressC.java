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

package com.blueking6.springnions.blocks;

import java.util.ArrayList;
import java.util.List;

import com.blueking6.springnions.entities.TofuPressEntityC;
import com.blueking6.springnions.init.ItemInit;
import com.blueking6.springnions.init.EntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

public class TofuPressC extends TofuPress {

	public final static IntegerProperty anim = IntegerProperty.create("animation", 0, 5);
	public static final IntegerProperty power = IntegerProperty.create("power", 0, 25);
	
	public TofuPressC(Properties prop) {
		super(prop);
	}

	public static void powerCheckC(Level lvl, BlockPos pos) {
		int temppower = 25;
		lvl.setBlockAndUpdate(pos, lvl.getBlockState(pos).setValue(TofuPressC.power, temppower));
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state,
			net.minecraft.world.level.storage.loot.LootParams.Builder p_60538_) {
		List<ItemStack> items = new ArrayList<ItemStack>();
		if (state.getValue(TofuPressC.anim) == 0) {
			items.add(ItemStack.EMPTY);
		} else if (state.getValue(TofuPressC.anim) < 5) {
			items.add(new ItemStack(ItemInit.SOYBEANS.get(), 1));
		} else {
			items.add(new ItemStack(ItemInit.TOFU.get(), 1));
		}
		return items;
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return EntityInit.TOFU_PRESSC.get().create(pos, state);
	}

	@Override
	public InteractionResult use(BlockState state, Level lvl, BlockPos pos, Player player, InteractionHand hand,
			BlockHitResult result) {
		int animation = lvl.getBlockState(pos).getValue(TofuPress3.anim);

		if (!lvl.isClientSide && player.getItemInHand(hand).getItem() == ItemInit.SOYBEANS.get() && animation == 0
				&& lvl.getBlockEntity(pos) instanceof final TofuPressEntityC tpress) {
			if (player.getItemInHand(hand).getCount() >= 4) {
				player.getItemInHand(hand).shrink(4);
			} else {
				player.displayClientMessage(
						Component.translatable("Must have at least 4 soybeans to make a batch of tofu"), true);
				return InteractionResult.FAIL;
			}
			lvl.setBlockAndUpdate(pos, state.setValue(TofuPress3.anim, 1));
			return InteractionResult.CONSUME;
		} else if (!lvl.isClientSide && animation == 5
				&& lvl.getBlockEntity(pos) instanceof final TofuPressEntityC tpress) {
			if (player.getItemInHand(hand) == ItemStack.EMPTY) {
				player.setItemInHand(hand, tpress.returnItem(animation));
			} else if (player.getItemInHand(hand).getItem() == ItemInit.TOFU.get()
					&& player.getItemInHand(hand).getCount() <= 61) {
				player.getInventory().add(tpress.returnItem(animation));
			} else {
				player.addItem(tpress.returnItem(animation));
			}
			player.addItem(tpress.returnPulp(animation));
			lvl.setBlockAndUpdate(pos, state.setValue(TofuPressC.anim, 0));
			return InteractionResult.SUCCESS;
		} else {
			return InteractionResult.PASS;
		}
	}

	// make it tick
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level lvl, BlockState state,
			BlockEntityType<T> type) {
		return lvl.isClientSide ? null : ($0, pos, $1, blockEntity) -> {
			if (blockEntity instanceof TofuPressEntityC tofupress) {
				tofupress.tick();
			}
		};
	}

}
