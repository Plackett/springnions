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

import java.util.List;

import com.blueking6.springnions.entities.CultivatorEntity;
import com.blueking6.springnions.gui.CultivatorMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CaveVinesBlock;
import net.minecraft.world.level.block.CaveVinesPlantBlock;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.MelonBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.PitcherCropBlock;
import net.minecraft.world.level.block.PumpkinBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.TwistingVinesBlock;
import net.minecraft.world.level.block.TwistingVinesPlantBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.WeepingVinesBlock;
import net.minecraft.world.level.block.WeepingVinesPlantBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.network.NetworkHooks;

public class Cultivator extends Block implements EntityBlock {

	private static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 16, 16);

	public Cultivator(Properties prop) {
		super(prop);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing,
			IPlantable plantable) {
		return true;
	}

	@Override
	public InteractionResult use(BlockState state, Level lvl, BlockPos pos, Player plr, InteractionHand hand,
			BlockHitResult result) {
		if (!lvl.isClientSide()) {
			BlockEntity entity = lvl.getBlockEntity(pos);
			if (entity instanceof CultivatorEntity) {
				MenuProvider containerProvider = new MenuProvider() {
					@Override
					public Component getDisplayName() {
						return Component.literal("Cultivator");
					}

					@Override
					public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory,
							Player playerEntity) {
						return new CultivatorMenu(windowId, playerEntity, pos);
					}
				};
				NetworkHooks.openScreen((ServerPlayer) plr, containerProvider, entity.getBlockPos());
			}
		}

		return InteractionResult.sidedSuccess(lvl.isClientSide());
	}

	// I cant take credit for this function since I used Industrial Foregoing's
	// API as inspiration for writing it, check it out at their github to compare!
	public static boolean isMature(BlockState state, ServerLevel level, BlockPos pos) {
		BlockState block = level.getBlockState(pos);
		// for regular crops
		if (block.getBlock() instanceof CropBlock) {
			return ((CropBlock) block.getBlock()).isMaxAge(state);
			// for berry bushes
		} else if (state.hasProperty(SweetBerryBushBlock.AGE)) {
			return state.getValue(SweetBerryBushBlock.AGE) >= 3;
			// for pitcher plant and torchflower
		} else if (block.getBlock() instanceof PitcherCropBlock || block.getBlock() == Blocks.TORCHFLOWER) {
			if (block.getBlock() instanceof PitcherCropBlock) {
				return state.getValue(PitcherCropBlock.AGE) >= 4;
			} else {
				return true;
			}
			// for nether wart
		} else if (block.getBlock() instanceof NetherWartBlock) {
			return state.getValue(NetherWartBlock.AGE) >= 3;
			// for all types of vines
		} else if (block.getBlock() instanceof VineBlock || block.getBlock() instanceof CaveVinesPlantBlock
				|| block.getBlock() instanceof CaveVinesBlock || block.getBlock() instanceof WeepingVinesPlantBlock
				|| block.getBlock() instanceof WeepingVinesBlock || block.getBlock() instanceof TwistingVinesPlantBlock
				|| block.getBlock() instanceof TwistingVinesBlock) {
			if (block.getBlock() instanceof TwistingVinesPlantBlock || block.getBlock() instanceof TwistingVinesBlock) {
				return level.getBlockState(pos.above()).getBlock() instanceof TwistingVinesPlantBlock
						|| level.getBlockState(pos.above()).getBlock() instanceof TwistingVinesBlock;
			} else {
				return true;
			}

			// for cocoa beans
		} else if (state.hasProperty(CocoaBlock.AGE))

		{
			return state.getValue(CocoaBlock.AGE) >= 2;
			// for cactus, sugarcane, and bamboo
		} else if (block.getBlock() == level.getBlockState(pos.above()).getBlock()) {
			return (block.getBlock().equals(Blocks.SUGAR_CANE) || block.getBlock().equals(Blocks.CACTUS)
					|| block.getBlock().equals(Blocks.BAMBOO));
			// for pumpkins and melons
		} else if (block.getBlock() instanceof MelonBlock || block.getBlock() instanceof PumpkinBlock) {
			return true;
			// for kelp
		} else if (block.getBlock() == Blocks.KELP_PLANT || block.getBlock() == Blocks.KELP) {
			return level.getBlockState(pos.above()).getBlock() == Blocks.KELP
					|| level.getBlockState(pos.above()).getBlock() == Blocks.KELP_PLANT;
		}
		return false;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CultivatorEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
			BlockEntityType<T> type) {
		return level.isClientSide ? null : ($0, pos, $1, blockEntity) -> {
			if (blockEntity instanceof CultivatorEntity h) {
				h.tick();
			}
		};
	}

	// also based on industrial foregoing but more original code to combine their
	// blockutils and harvesting code, harvests blocks and returns drops.
	public static List<ItemStack> Harvest(BlockState state, ServerLevel lvl, BlockPos pos) {
		BlockState block = lvl.getBlockState(pos);
		NonNullList<ItemStack> items = NonNullList.create();
		// for regular crops
		if (block.getBlock() instanceof CropBlock) {
			items.addAll(Block.getDrops(state, lvl, pos, lvl.getBlockEntity(pos)));
			lvl.setBlockAndUpdate(pos, block.getBlock().defaultBlockState());
			// for berry bushes
		} else if (state.hasProperty(SweetBerryBushBlock.AGE)) {
			items.addAll(Block.getDrops(state, lvl, pos, lvl.getBlockEntity(pos)));
			lvl.setBlockAndUpdate(pos, block.getBlock().defaultBlockState());
			// for pitcher plant and torchflower
		} else if (block.getBlock() instanceof PitcherCropBlock || block.getBlock() == Blocks.TORCHFLOWER) {
			items.addAll(Block.getDrops(state, lvl, pos, lvl.getBlockEntity(pos)));
			if (block.getBlock() == Blocks.TORCHFLOWER) {
				lvl.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			} else {
				lvl.setBlockAndUpdate(pos, block.getBlock().defaultBlockState());
			}
			// for nether wart
		} else if (block.getBlock() instanceof NetherWartBlock) {
			items.addAll(Block.getDrops(state, lvl, pos, lvl.getBlockEntity(pos)));
			lvl.setBlockAndUpdate(pos, block.getBlock().defaultBlockState());
			// for all types of vines
		} else if (block.getBlock() instanceof VineBlock || block.getBlock() instanceof CaveVinesPlantBlock
				|| block.getBlock() instanceof CaveVinesBlock || block.getBlock() instanceof WeepingVinesPlantBlock
				|| block.getBlock() instanceof WeepingVinesBlock || block.getBlock() instanceof TwistingVinesPlantBlock
				|| block.getBlock() instanceof TwistingVinesBlock) {
			if (block.getBlock() instanceof TwistingVinesPlantBlock || block.getBlock() instanceof TwistingVinesBlock) {
				items.addAll(Block.getDrops(lvl.getBlockState(pos.above()), lvl, pos.above(),
						lvl.getBlockEntity(pos.above())));
				for (int i = 2; i < 26; i++) {
					if (lvl.getBlockState(pos.above(i)).getBlock() == Blocks.TWISTING_VINES_PLANT
							|| lvl.getBlockState(pos.above(i)).getBlock() == Blocks.TWISTING_VINES) {
						items.addAll(Block.getDrops(state, lvl, pos.above(i), lvl.getBlockEntity(pos)));
						lvl.setBlockAndUpdate(pos.above(i), Blocks.AIR.defaultBlockState());
					}
				}
				lvl.setBlockAndUpdate(pos.above(), Blocks.AIR.defaultBlockState());
			} else if (block.getBlock() instanceof VineBlock) {
				items.add(new ItemStack(Items.VINE, 1));
				lvl.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			} else if (block.getBlock() instanceof WeepingVinesBlock
					|| block.getBlock() instanceof WeepingVinesPlantBlock) {
				items.add(new ItemStack(Items.WEEPING_VINES, 1));
				lvl.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			} else {
				items.addAll(Block.getDrops(state, lvl, pos, lvl.getBlockEntity(pos)));
				lvl.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			}

			// issue 0003: when harvested the cocoa bean would face the wrong way fixed by
			// preserving facing value
			// for cocoa beans
		} else if (state.hasProperty(CocoaBlock.AGE)) {
			items.addAll(Block.getDrops(state, lvl, pos, lvl.getBlockEntity(pos)));
			lvl.setBlockAndUpdate(pos, block.getBlock().defaultBlockState().setValue(CocoaBlock.FACING,
					state.getValue(CocoaBlock.FACING)));
			// for cactus, sugarcane, and bamboo, if bamboo check up to 16 blocks above and
			// replace air
		} else if (block.getBlock() == lvl.getBlockState(pos.above()).getBlock()) {
			items.addAll(Block.getDrops(state, lvl, pos.above(2), lvl.getBlockEntity(pos)));
			items.addAll(Block.getDrops(state, lvl, pos.above(1), lvl.getBlockEntity(pos)));
			lvl.setBlockAndUpdate(pos.above(), Blocks.AIR.defaultBlockState());
			if (block.getBlock() == Blocks.BAMBOO) {
				for (int i = 2; i < 16; i++) {
					if (lvl.getBlockState(pos.above(i)).getBlock() == Blocks.BAMBOO) {
						items.addAll(Block.getDrops(state, lvl, pos.above(i), lvl.getBlockEntity(pos)));
						lvl.setBlockAndUpdate(pos.above(i), Blocks.AIR.defaultBlockState());
					}
				}
				lvl.setBlockAndUpdate(pos, Blocks.BAMBOO_SAPLING.defaultBlockState());
			} else {
				lvl.setBlockAndUpdate(pos, block.getBlock().defaultBlockState());
			}
			// for pumpkins and melons
		} else if (block.getBlock() instanceof MelonBlock || block.getBlock() instanceof PumpkinBlock) {
			items.addAll(Block.getDrops(state, lvl, pos, lvl.getBlockEntity(pos)));
			lvl.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			// for kelp
		} else if (block.getBlock() == Blocks.KELP_PLANT || block.getBlock() == Blocks.KELP) {
			items.addAll(Block.getDrops(state, lvl, pos.above(), lvl.getBlockEntity(pos.above())));
			lvl.setBlockAndUpdate(pos.above(), Blocks.WATER.defaultBlockState());
			lvl.setBlockAndUpdate(pos, Blocks.KELP.defaultBlockState());
		}
		for (int index = 0; index < items.size(); index++) {
			if (block.getBlock() instanceof CropBlock crop) {
				if (crop.getBaseSeedId() == items.get(index).getItem()) {
					ItemStack seed = items.get(index);
					seed.shrink(1);
					items.set(index, seed);
				}
			}
		}
		return items;
	}

	// issue 0001: Blockentity not being deleted on block destruction, fix was to
	// add back the super constructor.
	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, Level lvl, BlockPos pos, BlockState state2, boolean bool) {
		if (!lvl.isClientSide()) {
			if (lvl.getBlockEntity(pos) instanceof CultivatorEntity h) {
				lvl.addFreshEntity(
						new ItemEntity(lvl, pos.getX(), pos.getY(), pos.getZ(), h.getInputItems().getStackInSlot(0)));
				for (int i = 0; i < 9; i++) {
					lvl.addFreshEntity(new ItemEntity(lvl, pos.getX(), pos.getY(), pos.getZ(),
							h.getOutputItems().getStackInSlot(i)));
				}
			}
		}
		super.onRemove(state, lvl, pos, state2, bool);
	}

}
