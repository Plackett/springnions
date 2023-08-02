package com.blueking6.tools;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.common.brewing.IBrewingRecipe;

public class PotionBrewingRecipe implements IBrewingRecipe {
	private final Potion input;
	private final Item ingredient;
	private final Potion output;

	public PotionBrewingRecipe(Potion input, Item item, Potion output) {
		this.input = input;
		this.ingredient = item;
		this.output = output;
	}

	@Override
	public boolean isInput(ItemStack input) {
		return PotionUtils.getPotion(input) == this.input;
	}

	@Override
	public boolean isIngredient(ItemStack ingredient) {
		return ingredient.getItem() == this.ingredient;
	}

	@Override
	public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
		if (!isInput(input) || !isIngredient(ingredient)) {
			return ItemStack.EMPTY;
		}
		ItemStack item = new ItemStack(input.getItem());
		item.setTag(new CompoundTag());
		PotionUtils.setPotion(item, output);
		return item;
	}

}
