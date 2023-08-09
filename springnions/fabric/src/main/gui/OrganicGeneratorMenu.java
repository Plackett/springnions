package com.blueking6.springnions.gui;

import com.blueking6.config.SpringnionsCommonConfigs;
import com.blueking6.springnions.entities.OrganicGeneratorEntity;
import com.blueking6.springnions.init.BlockInit;
import com.blueking6.springnions.init.MenuInit;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.SlotItemHandler;

public class OrganicGeneratorMenu extends AbstractContainerMenu {

	private OrganicGeneratorEntity entity;
	private ContainerData data;
	private BlockPos pos;
	public static int SLOT_INPUT = 0;
	public static int SLOT_COUNT = 1;

	public OrganicGeneratorMenu(int windowId, Player player, BlockPos pos) {
		super(MenuInit.ORGANIC_GENERATOR.get(), windowId);
		this.pos = pos;
		if (player.level().getBlockEntity(pos) instanceof OrganicGeneratorEntity entity) {
			this.entity = entity;
			this.data = entity.dataAccess;
			if (SpringnionsCommonConfigs.ORGANIC_GENERATOR_RATE.get() > 0) {
				addSlot(new SlotItemHandler(entity.getInputItems(), SLOT_INPUT, 80, 39));
			}
			addDataSlot(new DataSlot() {

				@Override
				public int get() {
					return entity.getCapability(ForgeCapabilities.ENERGY).map(IEnergyStorage::getEnergyStored)
							.orElse(0);
				}

				@Override
				public void set(int value) {
					entity.getCapability(ForgeCapabilities.ENERGY).ifPresent(handler -> {
						if (handler.getEnergyStored() > 0) {
							handler.extractEnergy(handler.getEnergyStored(), false);
						}
						if (value > 0) {
							handler.receiveEnergy(value, false);
						}
					});
				}

			});
		}
		layoutPlayerInventorySlots(player.getInventory(), 8, 71);
		this.addDataSlots(this.data);
	}

	private int addSlotRange(Container playerInventory, int index, int x, int y, int amount, int dx) {
		for (int i = 0; i < amount; i++) {
			addSlot(new Slot(playerInventory, index, x, y));
			x += dx;
			index++;
		}
		return index;
	}

	private int addSlotBox(Container playerInventory, int index, int x, int y, int horAmount, int dx, int verAmount,
			int dy) {
		for (int j = 0; j < verAmount; j++) {
			index = addSlotRange(playerInventory, index, x, y, horAmount, dx);
			y += dy;
		}
		return index;
	}

	private void layoutPlayerInventorySlots(Container playerInventory, int leftCol, int topRow) {
		// Player inventory
		addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

		// Hotbar
		topRow += 58;
		addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot.hasItem()) {
			ItemStack stack = slot.getItem();
			itemstack = stack.copy();
			if (index < SLOT_COUNT) {
				if (!this.moveItemStackTo(stack, SLOT_COUNT, Inventory.INVENTORY_SIZE + SLOT_COUNT, true)) {
					return ItemStack.EMPTY;
				}
			}
			if (!this.moveItemStackTo(stack, SLOT_INPUT, SLOT_INPUT + 1, false)) {
				if (index < 27 + SLOT_COUNT) {
					if (!this.moveItemStackTo(stack, 27 + SLOT_COUNT, 36 + SLOT_COUNT, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index < Inventory.INVENTORY_SIZE + SLOT_COUNT
						&& !this.moveItemStackTo(stack, SLOT_COUNT, 27 + SLOT_COUNT, false)) {
					return ItemStack.EMPTY;
				}
			}

			if (stack.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (stack.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, stack);
		}

		return itemstack;
	}

	@Override
	public boolean stillValid(Player player) {
		return stillValid(ContainerLevelAccess.create(player.level(), pos), player, BlockInit.ORGANIC_GENERATOR.get());
	}

	public int getData(int i) {
		return this.data.get(i);
	}

	public OrganicGeneratorEntity getEntityy() {
		return this.entity;
	}

}
