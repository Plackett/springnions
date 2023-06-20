package com.blueking6.springnions.blocks;

import com.blueking6.springnions.entities.CultivatorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.MelonBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.PumpkinBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SweetBerryBushBlock;
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
				NetworkHooks.openScreen(((ServerPlayer) plr), (CultivatorEntity) entity, pos);
			} else {
				throw new IllegalStateException("uh, the menu doesnt exist?");
			}
		}

		return InteractionResult.sidedSuccess(lvl.isClientSide());
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource src) {
		if (isMature(state, level, pos.above())) {
			System.out.print("mature!");
		}
	}

	// I cant take credit for this function since I used Industrial Foregoing's
	// API as inspiration for writing it, check it out at their github to compare!
	public boolean isMature(BlockState state, ServerLevel level, BlockPos pos) {
		BlockState block = level.getBlockState(pos);
		// for regular crops
		if (block.getBlock() instanceof CropBlock) {
			return ((CropBlock) block.getBlock()).isMaxAge(state);
			// for berry bushes
		} else if (state.hasProperty(SweetBerryBushBlock.AGE)) {
			return state.getValue(SweetBerryBushBlock.AGE) == 3;
			// for nether wart
		} else if (block.getBlock() instanceof NetherWartBlock) {
			return state.getValue(NetherWartBlock.AGE) == 3;
			// for cactus, sugarcane, and bamboo
		} else if (block.getBlock() == level.getBlockState(pos.above()).getBlock()) {
			return true;
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

}