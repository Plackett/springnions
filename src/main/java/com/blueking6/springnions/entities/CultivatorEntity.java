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
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
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
	public int litTime = 0;
	public int hleft = 0;
	public int energybuffer = 0;

	// issue 0002: inserting items would duplicate the stack, fixed by stopping
	// returning of the stack in insertItem
	private final ItemStackHandler inputItems = new ItemStackHandler(1) {
		@Override
		protected void onContentsChanged(int slot) {
			setChanged();
		}
	};
	private final ItemStackHandler outputItems = new ItemStackHandler(9) {
		@Override
		protected void onContentsChanged(int slot) {
			setChanged();
		}
	};

	private final ModifiedEnergyStorage energy = new ModifiedEnergyStorage(1024);

	private final LazyOptional<IItemHandler> itemHandler = LazyOptional
			.of(() -> new CombinedInvWrapper(inputItems, outputItems));
	private final LazyOptional<IItemHandler> inputItemHandler = LazyOptional.of(() -> inputItems);
	private final LazyOptional<IItemHandler> outputItemHandler = LazyOptional.of(() -> outputItems);
	private final LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(() -> energy);

	public CultivatorEntity(BlockPos pos, BlockState state) {
		super(EntityInit.CULTIVATOR.get(), pos, state);
	}

	public final ContainerData dataAccess = new ContainerData() {
		public int get(int index) {
			switch (index) {
			case 0:
				return CultivatorEntity.this.litTime;
			case 1:
				return CultivatorEntity.this.energybuffer;
			default:
				return 0;
			}
		}

		public void set(int index, int value) {
			switch (index) {
			case 0:
				CultivatorEntity.this.litTime = value;
				break;
			case 1:
				CultivatorEntity.this.energybuffer = value;
				break;
			}

		}

		public int getCount() {
			return 2;
		}
	};

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
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
		if (cap == ForgeCapabilities.ENERGY) {
			return energyHandler.cast();
		}
		return super.getCapability(cap);
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
		tag.putInt("EnergyBuffer", this.energybuffer);
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
		this.energybuffer = tag.getInt("EnergyBuffer");
	}

	public void tick() {
		if (this.getLevel().isClientSide()) {
			return;
		}
		// gain enough energy for 1 harvest every 256 ticks(about 13 seconds) from the
		// internal generator
		if (this.litTime > 0) {
			--this.litTime;
			this.energy.receiveEnergy(1, false);
		} else {
			if (ForgeHooks.getBurnTime(new ItemStack(this.inputItems.getStackInSlot(0).getItem(), 1), null) > 0
					&& this.energy.getEnergyStored() < this.energy.getMaxEnergyStored()) {
				litTime += ForgeHooks.getBurnTime(this.inputItems.getStackInSlot(0), null);
				ItemStack returnstack = this.inputItems.getStackInSlot(0);
				if (returnstack.getItem() == Items.LAVA_BUCKET) {
					returnstack = new ItemStack(Items.BUCKET, 1);
				} else {
					returnstack.shrink(1);
				}
				this.inputItems.setStackInSlot(0, returnstack);
			}
		}
		if (hleft < 1 && this.energy.getEnergyStored() > 0 && this.energybuffer < 256) {
			int remainder = 256 - energybuffer;
			if (this.energy.getEnergyStored() >= remainder) {
				this.energy.extractEnergy(remainder, false);
				this.energybuffer += remainder;
			} else {
				remainder = this.energy.getEnergyStored();
				this.energy.extractEnergy(this.energy.getEnergyStored(), false);
				this.energybuffer += remainder;
			}
			if (this.energybuffer == 256) {
				hleft++;
			}
		}

		if (cooldown == true && hleft > 0) {
			cooldown = false;
			if (Cultivator.isMature(this.getLevel().getBlockState(getBlockPos().above()), (ServerLevel) this.getLevel(),
					this.getBlockPos().above()) == true
					&& this.getLevel().getBlockState(getBlockPos().above()).getBlock() != Blocks.AIR) {
				this.energybuffer = 0;
				hleft--;
				attemptInsert(Cultivator.Harvest(getLevel().getBlockState(getBlockPos().above()),
						(ServerLevel) getLevel(), getBlockPos().above()));
			}
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
			// loop through all slots
			for (int i = 0; i < SLOT_OUTPUT_COUNT; i++) {
				// actually attempting to insert the item
				item = outputItems.insertItem(i, item, false);
				// stop looping if nothing is left to insert
				if (item.isEmpty()) {
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

	public ModifiedEnergyStorage getEnergy() {
		return this.energy;
	}

}
