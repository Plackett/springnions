package com.blueking6.springnions.entities;

import com.blueking6.springnions.gui.CultivatorMenu;
import com.blueking6.springnions.init.EntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

public class CultivatorEntity extends BlockEntity implements MenuProvider {
	private final ItemStackHandler itemHandler = new ItemStackHandler(10) {
		@Override
		protected void onContentsChanged(int slot) {
			setChanged();
		}
	};

	LazyOptional<ItemStackHandler> handler = LazyOptional.empty();

	private ContainerData data;

	public CultivatorEntity(BlockPos pos, BlockState state) {
		super(EntityInit.CULTIVATOR.get(), pos, state);
		this.data = new ContainerData() {

			@Override
			public int get(int p_39284_) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public void set(int p_39285_, int p_39286_) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public int getCount() {
				return 10;
			}
		};
	}

	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inv, Player plr) {
		return new CultivatorMenu(id,inv,this,this.data);
	}

	@Override
	public Component getDisplayName() {
		return Component.literal("Harvester");
	}

	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == ForgeCapabilities.ITEM_HANDLER) {
			return handler.cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		handler.invalidate();
	}

	@Override
	public void onLoad() {
		handler = LazyOptional.of(() -> itemHandler);
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		tag.put("inventory", itemHandler.serializeNBT());
		super.saveAdditional(tag);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		itemHandler.deserializeNBT(tag.getCompound("inventory"));
	}

	public void tick() {
		// TODO Auto-generated method stub
		
	}
	
	

}
