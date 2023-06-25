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

import com.blueking6.springnions.entities.OrganicGeneratorEntity;
import com.blueking6.springnions.gui.OrganicGeneratorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

public class OrganicGenerator extends Block implements EntityBlock {

	public OrganicGenerator(Properties prop) {
		super(prop);
	}

	public static final BooleanProperty LIT = BooleanProperty.create("lit");

	private static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 16, 16);

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(LIT,
				false);
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
		builder.add(FACING, LIT);
	}

	@Override
	public InteractionResult use(BlockState state, Level lvl, BlockPos pos, Player plr, InteractionHand hand,
			BlockHitResult hit) {
		if (!lvl.isClientSide()) {
			BlockEntity entity = lvl.getBlockEntity(pos);
			if (entity instanceof OrganicGeneratorEntity) {
				MenuProvider containerProvider = new MenuProvider() {
					@Override
					public Component getDisplayName() {
						return Component.literal("Organic Generator");
					}

					@Override
					public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory,
							Player playerEntity) {
						return new OrganicGeneratorMenu(windowId, playerEntity, pos);
					}
				};
				NetworkHooks.openScreen((ServerPlayer) plr, containerProvider, entity.getBlockPos());
			}
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new OrganicGeneratorEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
			BlockEntityType<T> type) {
		return level.isClientSide ? null : ($0, pos, $1, blockEntity) -> {
			if (blockEntity instanceof OrganicGeneratorEntity h) {
				h.tick();
			}
		};
	}

	@Override
	public void onRemove(BlockState state, Level lvl, BlockPos pos, BlockState state2, boolean bool) {
		if (!lvl.isClientSide()) {
			if (lvl.getBlockEntity(pos) instanceof OrganicGeneratorEntity h) {
				lvl.addFreshEntity(
						new ItemEntity(lvl, pos.getX(), pos.getY(), pos.getZ(), h.getInputItems().getStackInSlot(0)));
			}
		}
	}

}
