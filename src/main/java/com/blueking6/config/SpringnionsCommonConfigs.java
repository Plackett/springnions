package com.blueking6.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SpringnionsCommonConfigs {
	public static final ForgeConfigSpec.Builder Builder = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec Specs;
	
	public static final ForgeConfigSpec.ConfigValue<Integer> TOFU_PRESS_SPEED;
	public static final ForgeConfigSpec.ConfigValue<Integer> SOYBEAN_RETURNS;
	public static final ForgeConfigSpec.ConfigValue<Integer> ONION_RETURNS;
	
	static {
		Builder.push("Springnions Config");
		
		TOFU_PRESS_SPEED = Builder.comment("Constant that is divided by power to calculate how fast tofu presses operate, defaults to 300").defineInRange("Speed Constant", 300, 1, 10000);
		SOYBEAN_RETURNS = Builder.comment("Maximum amount of extra soybeans that you can get from one plant, defaults to 3").defineInRange("Bonus Count Soy", 3,0,100);
		ONION_RETURNS = Builder.comment("Maximum amount of extra onions that you can get from one plant, defaults to 3").defineInRange("Bonus Count Onion", 3,0,100);
		
		Builder.pop();
		Specs = Builder.build();
	}
}
