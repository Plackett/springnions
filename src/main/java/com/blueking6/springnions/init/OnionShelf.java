package com.blueking6.springnions.init;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class OnionShelf extends Block implements EntityBlock {
	public static final IntegerProperty HASITEM = IntegerProperty.create("has_item", 0, 8);
	private static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	private static final VoxelShape SHAPE =  Block.box(0, 0, 0, 16, 32, 16);
	
	public OnionShelf(Block.Properties properties) {
		super(properties);
	}

	@Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return TileEntityInit.ONION_SHELF.get().create(pos, state);
    }
    
    @Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
			InteractionHand hand, BlockHitResult result) {
    	 if (level.isClientSide && level.getBlockEntity(pos) instanceof final OnionShelfTile shelf) {
    		 if(player.getItemInHand(hand).is(ItemInit.ONION_CRATE.get())) {
    			 player.setItemInHand(hand, shelf.appendItem(player.getItemInHand(hand)));
    			 state = UpdateState(level, pos, state, shelf);
    			 level.setBlockAndUpdate(pos, state);
    		 } else {
    			 if(player.getItemInHand(hand).isEmpty()) {
    				 player.setItemInHand(hand, shelf.returnItem());
        			 state = UpdateState(level, pos, state, shelf);
        			 level.setBlockAndUpdate(pos, state);
    			 }
    		 }
             return InteractionResult.SUCCESS;
          } else {
             return InteractionResult.CONSUME;
          }
	}
    
    public BlockState UpdateState(Level level, BlockPos pos, BlockState state, OnionShelfTile shelf) {
    	int v = 0;
    	for(int i = 0; i < 7; i++) {
			 if(shelf.getItemInSlot(i).isEmpty() == false) {
				 v++;
			 }
		 }
    	return state.setValue(HASITEM, v);
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
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(FACING, HASITEM); }
	
}
