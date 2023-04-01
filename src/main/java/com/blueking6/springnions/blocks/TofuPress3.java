package com.blueking6.springnions.blocks;

import java.util.ArrayList;
import java.util.List;

import com.blueking6.springnions.init.ItemInit;
import com.blueking6.springnions.init.TileEntityInit;

import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;

public class TofuPress3 extends TofuPress {
	
	public static final IntegerProperty anim = IntegerProperty.create("animation", 0, 5);
	public static final IntegerProperty power = IntegerProperty.create("power", 0, 25);

	public TofuPress3(Properties prop) {
		super(prop);
	}

	public static void powerCheck3(Level lvl, BlockPos pos) {
		int temppower = 0;
		// emulating a 2d array with for loops XD
		for (int i = 0; i < 5; i++) {
			for (int v = 0; v < 5; v++) {
				// check for flowing water
				if (lvl.getBlockState(pos.below(1).north(2 - i).east(2 - v)).getFluidState().is(Fluids.FLOWING_WATER)) {
					temppower++;
				}
			}
		}
		lvl.setBlockAndUpdate(pos, lvl.getBlockState(pos).setValue(TofuPress3.power, temppower));
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state,
			net.minecraft.world.level.storage.loot.LootContext.Builder p_60538_) {
		List<ItemStack> items = new ArrayList<ItemStack>();
		if (state.getValue(TofuPress3.anim) == 0) {
			items.add(ItemStack.EMPTY);
		} else if (state.getValue(TofuPress3.anim) < 5) {
			items.add(new ItemStack(ItemInit.SOYBEANS.get(), 1));
		} else {
			items.add(new ItemStack(ItemInit.TOFU.get(), 1));
		}
		return items;
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return TileEntityInit.TOFU_PRESS3.get().create(pos, state);
	}

	@Override
	public InteractionResult use(BlockState state, Level lvl, BlockPos pos, Player player, InteractionHand hand,
			BlockHitResult result) {
		int animation = lvl.getBlockState(pos).getValue(TofuPress3.anim);

		if (!lvl.isClientSide && player.getItemInHand(hand).getItem() == ItemInit.SOYBEANS.get()
				&& animation == 0
				&& lvl.getBlockEntity(pos) instanceof final TofuPressEntity3 tpress) {
			player.getItemInHand(hand).shrink(1);
			lvl.setBlockAndUpdate(pos, state.setValue(TofuPress3.anim, 1));
			return InteractionResult.CONSUME;
		} else if (!lvl.isClientSide && animation == 5
				&& lvl.getBlockEntity(pos) instanceof final TofuPressEntity3 tpress) {
			if (player.getItemInHand(hand) == ItemStack.EMPTY) {
				player.setItemInHand(hand, tpress.returnItem(animation));
			} else if (player.getItemInHand(hand).getItem() == ItemInit.TOFU.get()
					&& player.getItemInHand(hand).getCount() <= 63) {
				player.getInventory().add(tpress.returnItem(animation));
			} else {
				player.drop(tpress.returnItem(animation), true);
			}
			lvl.setBlockAndUpdate(pos, state.setValue(TofuPress3.anim, 0));
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
			if (blockEntity instanceof TofuPressEntity3 tofupress) {
				tofupress.tick();
			}
		};
	}

}
