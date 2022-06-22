package com.blueking6.springnions.init;

import net.minecraft.world.food.FoodProperties;

public class FoodInit {
	public static final FoodProperties ONION = (new FoodProperties.Builder()).nutrition(4).saturationMod(0.6f). build();
	public static final FoodProperties GOLDEN_ONION = (new FoodProperties.Builder()).nutrition(7).saturationMod(1.2f).build();
}
