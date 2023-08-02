package com.blueking6.tools;

import net.minecraftforge.energy.EnergyStorage;

public class ModifiedEnergyStorage extends EnergyStorage{

	public ModifiedEnergyStorage(int capacity) {
		super(capacity);
	}
	
	public void setEnergy(int amount) {
		this.energy = amount;
	}

}
