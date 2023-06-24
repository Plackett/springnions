package com.blueking6.springnions.entities;

import java.util.List;
import org.jetbrains.annotations.NotNull;

import com.blueking6.springnions.blocks.Cultivator;
import com.blueking6.springnions.init.EntityInit;
import com.blueking6.tools.ModifiedEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class CultivatorEntity extends BlockEntity {

	// variables used for container creation and menu creation
	public static final int SLOT_INPUT = 0;
	public static final int SLOT_INPUT_COUNT = 1;

	public static final int SLOT_OUTPUT = 0;
	public static final int SLOT_OUTPUT_COUNT = 9;

	public static final int SLOT_COUNT = SLOT_INPUT_COUNT + SLOT_OUTPUT_COUNT;

	public boolean cooldown = true;
	private int tickmeasure;
	public int litTime;

	private final ItemStackHandler inputItems = new ItemStackHandler(1) {
		// check if the built in generator can burn some more fuel, starts burn loop in
		// servertick
		@Override
		protected void onContentsChanged(int slot) {
			setChanged();
		}

		@Override
		public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
			if (!stack.getItem().isFireResistant()) {
				return ItemStack.EMPTY;
			}
			return stack;
		}

	};
	private final ItemStackHandler outputItems = new ItemStackHandler(9) {
		@Override
		protected void onContentsChanged(int slot) {
			setChanged();
		}
	};

	private final ModifiedEnergyStorage energy = new ModifiedEnergyStorage(256) {

		@Override
		public int receiveEnergy(int maxReceive, boolean simulate) {
			// calculate the harvests left
			if (maxReceive + this.energy <= this.capacity) {
				getBlockState().setValue(Cultivator.HARVESTS_LEFT, ((maxReceive + this.energy) / 4));
			} else {
				getBlockState().setValue(Cultivator.HARVESTS_LEFT, (this.capacity / 4));
			}
			setChanged();
			return super.receiveEnergy(maxReceive, simulate);
		}

	};

	private final LazyOptional<IItemHandler> itemHandler = LazyOptional
			.of(() -> new CombinedInvWrapper(inputItems, outputItems));
	private final LazyOptional<IItemHandler> inputItemHandler = LazyOptional.of(() -> inputItems);
	private final LazyOptional<IItemHandler> outputItemHandler = LazyOptional.of(() -> outputItems);
	private final LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(() -> energy);
	private int energybuffer;

	public CultivatorEntity(BlockPos pos, BlockState state) {
		super(EntityInit.CULTIVATOR.get(), pos, state);
	}

	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == ForgeCapabilities.ITEM_HANDLER) {
			if (side == null) {
				return itemHandler.cast();
			} else if (side == Direction.DOWN) {
				return outputItemHandler.cast();
			} else {
				return inputItemHandler.cast();
			}
		}
		if (cap == ForgeCapabilities.ENERGY) {
			return energyHandler.cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		itemHandler.invalidate();
		inputItemHandler.invalidate();
		outputItemHandler.invalidate();
		energyHandler.invalidate();
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		tag.put("ITEMS_INPUT_TAG", inputItems.serializeNBT());
		tag.put("ITEMS_OUTPUT_TAG", outputItems.serializeNBT());
		tag.putInt("ENERGY_TAG", energy.getEnergyStored());
		tag.putInt("BurnTime", this.litTime);
		super.saveAdditional(tag);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if (tag.contains("ITEMS_INPUT_TAG")) {
			inputItems.deserializeNBT(tag.getCompound("ITEMS_INPUT_TAG"));
		}
		if (tag.contains("ITEMS_OUTPUT_TAG")) {
			outputItems.deserializeNBT(tag.getCompound("ITEMS_OUTPUT_TAG"));
		}
		if (tag.contains("ENERGY_TAG")) {
			energy.setEnergy(tag.getInt("ENERGY_TAG"));
		}
		this.litTime = tag.getInt("BurnTime");
	}

	public void tick() {
		if (this.getLevel().isClientSide()) {
			return;
		}
		// gain enough energy for 1 harvest every 4 seconds from the internal generator
		if (this.litTime > 0) {
			--this.litTime;
			this.energybuffer++;
			if (this.energybuffer >= 80) {
				this.energybuffer = 0;
				energy.receiveEnergy(1, false);
			}
		} else {
			if (ForgeHooks.getBurnTime(new ItemStack(this.inputItems.getStackInSlot(0).getItem(), 1), null) > 0) {
				litTime += ForgeHooks.getBurnTime(this.inputItems.getStackInSlot(0), null);
				ItemStack returnstack = this.inputItems.getStackInSlot(0);
				returnstack.shrink(1);
				this.inputItems.setStackInSlot(0, returnstack);
			}
		}
		if (cooldown == true && getBlockState().getValue(Cultivator.HARVESTS_LEFT) > 0) {
			cooldown = false;
			if (Cultivator.isMature(this.getLevel().getBlockState(getBlockPos().above()), (ServerLevel) this.getLevel(),
					this.getBlockPos().above()) == true) {
				attemptInsert(Cultivator.Harvest(getLevel().getBlockState(getBlockPos().above()),
						(ServerLevel) getLevel(), getBlockPos().above()));
			}
			getLevel().setBlockAndUpdate(getBlockPos(), getBlockState().setValue(Cultivator.HARVESTS_LEFT,
					getBlockState().getValue(Cultivator.HARVESTS_LEFT) - 1));
		}
		if (cooldown == false) {
			tickmeasure++;
			if (tickmeasure == 40) {
				tickmeasure = 0;
				cooldown = true;
			}
		}
	}

	// attempt to insert items into a container
	public void attemptInsert(List<ItemStack> itemlist) {
		// loop through all items
		for (int v = 0; v < itemlist.size(); v++) {
			ItemStack item = itemlist.get(v);
			System.out.print(item.getDisplayName());
			// loop through all slots
			for (int i = 0; i < SLOT_OUTPUT_COUNT; i++) {
				// actually attempting to insert the item
				item = outputItems.insertItem(i, item, false);
				System.out.print(item.getDisplayName() + "attempted");
				// stop looping if nothing is left to insert
				if (item.isEmpty()) {
					System.out.print(item.getDisplayName() + "break");
					break;
				}
			}
			// drop itemstack on the ground if it can't fit into the inventory of the
			// machine
			if (!item.isEmpty()) {
				this.getLevel().addFreshEntity(new ItemEntity(this.getLevel(), (double) this.getBlockPos().getX(),
						(double) this.getBlockPos().getY(), (double) this.getBlockPos().getZ(), item));
			}
		}
		this.getLevel().setBlockAndUpdate(getBlockPos(),
				this.getLevel().getBlockState(getBlockPos()).getBlock().defaultBlockState());
	}

	public ItemStackHandler getInputItems() {
		return inputItems;
	}

	public ItemStackHandler getOutputItems() {
		return outputItems;
	}

	public EnergyStorage getEnergy() {
		return this.energy;
	}

}
