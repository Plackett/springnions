package com.blueking6.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SpringnionsClientConfigs {
	public static final ForgeConfigSpec.Builder Builder = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec Specs;
	
	static {
		Builder.push("Springnions Config");
		
		Builder.pop();
		Specs = Builder.build();
	}
}
