package com.blueking6.springnions.entities;

import java.util.List;

import javax.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;

import com.blueking6.springnions.blocks.Cultivator;
import com.blueking6.springnions.init.EntityInit;
import com.blueking6.tools.AdaptedItemHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class CultivatorEntity extends BlockEntity implements MenuProvider {

	public static final int SLOT_INPUT = 0;
	public static final int SLOT_INPUT_COUNT = 1;

	public static final int SLOT_OUTPUT = 0;
	public static final int SLOT_OUTPUT_COUNT = 9;

	public static final int SLOT_COUNT = SLOT_INPUT_COUNT + SLOT_OUTPUT_COUNT;

	private boolean cooldown = false;

	@Nonnull
	private ItemStackHandler createItemHandler(int slots) {
		return new ItemStackHandler(slots) {
			@Override
			protected void onContentsChanged(int slot) {
				setChanged();
			}
		};
	}

	private final ItemStackHandler inputItems = createItemHandler(SLOT_INPUT_COUNT);
	private final ItemStackHandler outputItems = createItemHandler(SLOT_OUTPUT_COUNT);
	private final LazyOptional<IItemHandler> itemHandler = LazyOptional
			.of(() -> new CombinedInvWrapper(inputItems, outputItems));
	private final LazyOptional<IItemHandler> inputItemHandler = LazyOptional
			.of(() -> new AdaptedItemHandler(inputItems) {
				@Override
				public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
					return ItemStack.EMPTY;
				}
			});
	private final LazyOptional<IItemHandler> outputItemHandler = LazyOptional
			.of(() -> new AdaptedItemHandler(outputItems) {
				@Override
				public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
					return stack;
				}
			});

	public CultivatorEntity(BlockPos pos, BlockState state) {
		super(EntityInit.CULTIVATOR.get(), pos, state);
	}

	@Override
	public Component getDisplayName() {
		return Component.literal("Cultivator");
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
		return super.getCapability(cap, side);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		itemHandler.invalidate();
		inputItemHandler.invalidate();
		outputItemHandler.invalidate();
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		tag.put("ITEMS_INPUT_TAG", inputItems.serializeNBT());
		tag.put("ITEMS_OUTPUT_TAG", outputItems.serializeNBT());
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
	}

	public void tick() {
		if (!this.getLevel().isClientSide() && cooldown) {
			if (Cultivator.isMature(this.getBlockState(), (ServerLevel) this.getLevel(),
					this.getBlockPos().above()) == true) {
				System.out.print("mature!");
				this.getLevel().setBlockAndUpdate(this.getBlockPos().above(),
						this.getLevel().getBlockState(this.getBlockPos().above()).getBlock().defaultBlockState());
				attemptInsert(Cultivator.Harvest(getBlockState(), (ServerLevel) getLevel(), getBlockPos().above()));
			}
		}
	}

	// attempt to insert items into a container
	@SuppressWarnings("deprecation")
	public void attemptInsert(List<ItemStack> itemlist) {
		// loop through all items
		for (int v = 0; v < itemlist.size(); v++) {
			ItemStack item = itemlist.get(v);
			// loop through all slots
			for (int i = 0; i < SLOT_OUTPUT_COUNT; i++) {
				ItemStack current = outputItems.getStackInSlot(i);
				// just add item if the slot is empty
				if (current == ItemStack.EMPTY) {
					outputItems.setStackInSlot(i, item);
					item = ItemStack.EMPTY;
					// check item count and see if you can add more to the stack
				} else if (current.getItem() == item.getItem()
						&& current.getItem().getMaxStackSize() > current.getCount()) {
					if (current.getItem().getMaxStackSize() > current.getCount() + item.getCount()) {
						current.setCount(current.getItem().getMaxStackSize());
						item = ItemStack.EMPTY;
					} else {
						item.setCount(item.getCount() - (current.getItem().getMaxStackSize() - current.getCount()));
						current.setCount(current.getItem().getMaxStackSize());
					}
					outputItems.setStackInSlot(i, current);
				}
			}
			// drop itemstack on the ground if it can't fit into the inventory of the machine
			if (item != ItemStack.EMPTY) {
				this.getLevel().addFreshEntity(new ItemEntity(this.getLevel(), (double) this.getBlockPos().getX(),
						(double) this.getBlockPos().getY(), (double) this.getBlockPos().getZ(), item));
			}
		}
	}

	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inv, Player plr) {
		return null;
	}

	public ItemStackHandler getInputItems() {
		return inputItems;
	}

	public ItemStackHandler getOutputItems() {
		return outputItems;
	}

}
