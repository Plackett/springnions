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

import com.blueking6.springnions.init.EntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

public class OnionShelfEntity extends BlockEntity {

	protected final ItemStackHandler inventory;
	protected LazyOptional<ItemStackHandler> handler;

	public OnionShelfEntity(BlockPos pos, BlockState state) {
		super(EntityInit.ONION_SHELF.get(), pos, state);

		this.inventory = createInventory();
		this.handler = LazyOptional.of(() -> this.inventory);
	}

	private ItemStackHandler createInventory() {
		return new ItemStackHandler(9) {
			@Override
			public ItemStack extractItem(int slot, int amount, boolean simulate) {
				return super.extractItem(slot, amount, simulate);
			}

			@Override
			public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
				return super.insertItem(slot, stack, simulate);
			}

			@Override
			public int getSlotLimit(int slot) {
				return 1;
			}
		};
	}

	@Override
	protected void saveAdditional(CompoundTag compound) {
		final var items = new ListTag();
		for (int slot = 0; slot < this.inventory.getSlots(); slot++) {
			if (!this.inventory.getStackInSlot(slot).isEmpty()) {
				final var item = new CompoundTag();
				item.putInt("Slot", slot);
				this.inventory.getStackInSlot(slot).save(item);
				items.add(item);
			}
		}

		final var inventory = new CompoundTag();
		inventory.put("Items", items);
		inventory.putInt("Size", this.inventory.getSlots());
		compound.put("Inventory", inventory);
	}

	@Override
	public void load(CompoundTag compound) {
		final CompoundTag inventory = compound.getCompound("Inventory");
		this.inventory.setSize(
				inventory.contains("Size", Tag.TAG_INT) ? inventory.getInt("Size") : this.inventory.getSlots());

		final ListTag items = inventory.getList("Items", Tag.TAG_COMPOUND);
		for (int index = 0; index < items.size(); index++) {
			final CompoundTag itemTags = items.getCompound(index);
			final int slot = itemTags.getInt("Slot");

			if (slot >= 0 && slot < this.inventory.getSlots()) {
				final var stack = ItemStack.of(itemTags);
				stack.setCount(itemTags.getInt("RealCount"));
				this.inventory.setStackInSlot(slot, stack);
			}
		}

		this.setChanged();
	}

	public ItemStack appendItem(ItemStack item) {
		ItemStack returnitem = ItemStack.EMPTY;
		for (var i = 0; i < 8; i++) {
			if (this.inventory.getStackInSlot(i).isEmpty()) {
				if (item.isEmpty()) {
					break;
				} else {
					item = inventory.insertItem(i, item, false);
					returnitem = item;
				}
			}
		}
		return returnitem;

	}

	public ItemStack returnItem() {
		for (var i = 8; i >= 0; i--) {
			if (this.inventory.getStackInSlot(i).isEmpty() == false) {
				ItemStack extraction = inventory.extractItem(i, 1, false);
				return (extraction);
			}
		}
		return ItemStack.EMPTY;
	}

	public int getNumberOfItems(Level level, BlockPos pos, OnionShelfEntity shelf) {
		int v = 0;
		for (int i = 0; i < 8; i++) {
			if (this.inventory.getStackInSlot(i).isEmpty() == false) {
				v++;
			}
		}
		return v;
	}

	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return cap == ForgeCapabilities.ITEM_HANDLER ? this.handler.cast()
				: super.getCapability(cap, side);
	}
}
