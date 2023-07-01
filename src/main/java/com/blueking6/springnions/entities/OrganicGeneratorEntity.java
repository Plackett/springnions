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

import org.jetbrains.annotations.NotNull;

import com.blueking6.config.SpringnionsCommonConfigs;
import com.blueking6.springnions.blocks.OrganicGenerator;
import com.blueking6.springnions.init.EntityInit;
import com.blueking6.tools.ModifiedEnergyStorage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.IItemHandler;

public class OrganicGeneratorEntity extends BlockEntity {

	public OrganicGeneratorEntity(BlockPos pos, BlockState state) {
		super(EntityInit.ORGANIC_GENERATOR.get(), pos, state);
	}

	// issue 0002: inserting items would duplicate the stack, fixed by stopping
	// returning of the stack in insertItem
	private final ItemStackHandler inputItems = new ItemStackHandler(1) {
		@Override
		protected void onContentsChanged(int slot) {
			setChanged();
		}

	};
	private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> inputItems);
	private final ModifiedEnergyStorage energy = new ModifiedEnergyStorage(
			SpringnionsCommonConfigs.ORGANIC_GENERATOR_CAPACITY.get());
	private final LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(() -> energy);
	private int litTime;

	public final ContainerData dataAccess = new ContainerData() {
		@Override
		public int get(int index) {
			return OrganicGeneratorEntity.this.litTime;
		}

		@Override
		public void set(int index, int value) {
			OrganicGeneratorEntity.this.litTime = value;
		}

		@Override
		public int getCount() {
			return 1;
		}
	};

	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == ForgeCapabilities.ITEM_HANDLER) {
			if (side != Direction.DOWN) {
				return itemHandler.cast();
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
		energyHandler.invalidate();
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		tag.put("ITEMS_INPUT_TAG", inputItems.serializeNBT());
		// fix bug with energy still being stored even though it was higher than max value
		tag.putInt("ENERGY_TAG", Math.max(0,
				Math.min(SpringnionsCommonConfigs.ORGANIC_GENERATOR_CAPACITY.get(), energy.getEnergyStored())));
		tag.putInt("BurnTime", this.litTime);
		super.saveAdditional(tag);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if (tag.contains("ITEMS_INPUT_TAG")) {
			inputItems.deserializeNBT(tag.getCompound("ITEMS_INPUT_TAG"));
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
		// gain enough energy for 1 harvest every 256 ticks(about 13 seconds) from the
		// internal generator
		if (this.litTime > 0) {
			if (getBlockState().getValue(OrganicGenerator.LIT) == false) {
				this.getLevel().setBlockAndUpdate(getBlockPos(), getBlockState().setValue(OrganicGenerator.LIT, true));
			}
			--this.litTime;
			this.energy.receiveEnergy(SpringnionsCommonConfigs.ORGANIC_GENERATOR_RATE.get(), false);
		} else {
			if (ForgeHooks.getBurnTime(new ItemStack(this.inputItems.getStackInSlot(0).getItem(), 1), null) > 0
					&& this.energy.getEnergyStored() < this.energy.getMaxEnergyStored()
					&& SpringnionsCommonConfigs.ORGANIC_GENERATOR_RATE.get() > 0) {
				litTime += ForgeHooks.getBurnTime(this.inputItems.getStackInSlot(0), null);
				ItemStack returnstack = this.inputItems.getStackInSlot(0);
				if (returnstack.getItem() != Items.LAVA_BUCKET) {
					returnstack.shrink(1);
				}
				this.inputItems.setStackInSlot(0, returnstack);
			} else {
				if (getBlockState().getValue(OrganicGenerator.LIT) != false) {
					this.getLevel().setBlockAndUpdate(getBlockPos(),
							getBlockState().setValue(OrganicGenerator.LIT, false));
				}
			}
		}
		for (int i = 0; i < 6; i++) {
			Direction d = Direction.NORTH;
			switch (i) {
			case 0:
				d = Direction.NORTH;
				break;
			case 1:
				d = Direction.EAST;
				break;
			case 2:
				d = Direction.SOUTH;
				break;
			case 3:
				d = Direction.WEST;
				break;
			case 4:
				d = Direction.UP;
				break;
			case 5:
				d = Direction.DOWN;
				break;
			}
			if (this.getLevel().getBlockEntity(getBlockPos().relative(d, 1)) != null) {
				this.getLevel().getBlockEntity(getBlockPos().relative(d, 1)).getCapability(ForgeCapabilities.ENERGY)
						.ifPresent(handler -> {
							int remainder = handler.getMaxEnergyStored() - handler.getEnergyStored();
							if (remainder > 0 && this.energy.getEnergyStored() > 0) {
								if (this.energy.getEnergyStored() <= remainder) {
									handler.receiveEnergy(this.energy.getEnergyStored(), false);
									this.energy.extractEnergy(this.energy.getEnergyStored(), false);
								} else {
									handler.receiveEnergy(remainder, false);
									this.energy.extractEnergy(remainder, false);
								}
							}
						});
			}
		}
	}

	public ItemStackHandler getInputItems() {
		return this.inputItems;
	}
}
