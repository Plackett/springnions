package com.blueking6.springnions.entities;

import org.jetbrains.annotations.NotNull;

import com.blueking6.config.SpringnionsCommonConfigs;
import com.blueking6.springnions.blocks.TofuPress2;
import com.blueking6.springnions.blocks.TofuPressC;
import com.blueking6.springnions.init.ItemInit;
import com.blueking6.springnions.init.SoundInit;
import com.blueking6.springnions.init.EntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

public class TofuPressEntity2 extends BlockEntity {

	protected final ItemStackHandler inventory;
	protected LazyOptional<ItemStackHandler> handler;
	private int progressc = 0;

	public TofuPressEntity2(BlockPos pos, BlockState state) {
		super(EntityInit.TOFU_PRESS2.get(), pos, state);

		this.inventory = createInventory();
		this.handler = LazyOptional.of(() -> this.inventory);
	}

	public void tick() {
		int animation = getBlockState().getValue(TofuPress2.anim);
		int powerlvl = getBlockState().getValue(TofuPress2.power);
		if (this.level == null) {
			return;
		}
		// update stuff for processing item
		if (!this.level.isClientSide && powerlvl >= 1 && animation >= 1) {
			if (this.progressc >= SpringnionsCommonConfigs.TOFU_PRESS_SPEED.get() / powerlvl && animation <= 4) {
				if (animation == 1 || animation == 3) {
					this.level.playSound(null, this.getBlockPos(), SoundInit.TOFU_PRESS_PROCESS.get(),
							SoundSource.BLOCKS, 0.5f, 1f);
				}
				this.progressc = 0;
				this.level.setBlockAndUpdate(this.getBlockPos(),
						getBlockState().setValue(TofuPress2.anim, animation + 1));
			}
			if (animation != 0 && animation != 5) {
				this.progressc++;
			} else {
				this.progressc = 0;
			}
		}
		if (this.level.getGameTime() % 100 == 0) {
			TofuPress2.powerCheck2(this.level, this.getBlockPos());
		}
		if (this.inventory.getStackInSlot(0).getItem() == ItemInit.SOYBEANS.get()
				&& this.inventory.getStackInSlot(1).getItem() == ItemInit.SOYBEANS.get()
				&& this.inventory.getStackInSlot(2).getItem() == ItemInit.SOYBEANS.get()
				&& this.inventory.getStackInSlot(3).getItem() == ItemInit.SOYBEANS.get() && animation == 0) {
			this.level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(TofuPress2.anim, 1));
		} else if (this.inventory.getStackInSlot(0) == ItemStack.EMPTY && animation == 1) {
			this.inventory.setStackInSlot(0, new ItemStack(ItemInit.SOYBEANS.get(), 1));
			this.inventory.setStackInSlot(1, new ItemStack(ItemInit.SOYBEANS.get(), 1));
			this.inventory.setStackInSlot(2, new ItemStack(ItemInit.SOYBEANS.get(), 1));
			this.inventory.setStackInSlot(3, new ItemStack(ItemInit.SOYBEANS.get(), 1));
		} else if (this.inventory.getStackInSlot(0).getItem() == ItemInit.SOYBEANS.get() && animation == 5) {
			this.inventory.setStackInSlot(0, new ItemStack(ItemInit.TOFU.get(), 1));
			this.inventory.setStackInSlot(1, new ItemStack(ItemInit.TOFU.get(), 1));
			this.inventory.setStackInSlot(2, new ItemStack(ItemInit.TOFU.get(), 1));
			this.inventory.setStackInSlot(3, new ItemStack(ItemInit.SOY_PULP.get(), 1));
		} else if (this.inventory.getStackInSlot(0) == ItemStack.EMPTY
				&& this.inventory.getStackInSlot(1) == ItemStack.EMPTY
				&& this.inventory.getStackInSlot(2) == ItemStack.EMPTY
				&& this.inventory.getStackInSlot(3) == ItemStack.EMPTY) {
			this.level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(TofuPress2.anim, 0));
		}
	}

	@Override
	public void onLoad() {
		super.onLoad();
		this.handler = LazyOptional.of(() -> this.inventory);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.progressc = tag.getInt("progressc");
		if (this.level == null) {
			return;
		}
		final CompoundTag inventory = tag.getCompound("Inventory");
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

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putInt("progressc", progressc);
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
		tag.put("Inventory", inventory);
	}

	private ItemStackHandler createInventory() {
		return new ItemStackHandler(4) {

			// only extract when processing is done (anim == 5)
			@Override
			public ItemStack extractItem(int slot, int amount, boolean simulate) {
				if (getBlockState().getValue(TofuPressC.anim) == 5
						&& (this.getStackInSlot(0).getItem() == ItemInit.TOFU.get()
								|| this.getStackInSlot(3).getItem() == ItemInit.SOY_PULP.get())) {
					return super.extractItem(slot, amount, simulate);
				} else {
					return ItemStack.EMPTY;
				}
			}

			// only insert if it is soybeans and reject other items
			@Override
			public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
				if (stack.getItem() == ItemInit.SOYBEANS.get() && getBlockState().getValue(TofuPress2.anim) == 0) {
					return super.insertItem(slot, stack, simulate);
				} else {
					return stack;
				}
			}

			@Override
			protected int getStackLimit(int slot, @NotNull ItemStack stack) {
				return 1;
			}

			@Override
			public int getSlotLimit(int slot) {
				return 4;
			}

			@Override
			protected void onContentsChanged(int slot) {
				super.onContentsChanged(slot);
				setChanged();
			}
		};
	}

	public ItemStack appendItem(ItemStack item) {
		ItemStack returnitem = ItemStack.EMPTY;
		return returnitem;

	}

	public ItemStack returnItem(int animstate) {
		if (animstate == 5) {
			return new ItemStack(ItemInit.TOFU.get(), 3);
		} else {
			return ItemStack.EMPTY;
		}
	}

	public ItemStack returnPulp(int animstate) {
		if (animstate == 5) {
			return new ItemStack(ItemInit.SOY_PULP.get(), 1);
		} else {
			return ItemStack.EMPTY;
		}
	}

	public int getNumberOfItems(Level level, BlockPos pos, TofuPressEntity2 tpress) {
		int v = 0;
		if (this.inventory.getStackInSlot(0).isEmpty() == false) {
			v++;
		}
		return v;
	}

	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == ForgeCapabilities.ITEM_HANDLER) {
			return this.handler.cast();
		}
		return super.getCapability(cap, side);
	}

}
