package com.blueking6.springnions.init;

import com.blueking6.springnions.springnions;
import com.blueking6.springnions.effects.EyePain;
import com.blueking6.springnions.effects.EyePain2;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EffectInit {

	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS,
			springnions.MOD_ID);

	public static final RegistryObject<MobEffect> EYE_PAIN = EFFECTS.register("eye_pain",
			() -> new EyePain(MobEffectCategory.HARMFUL, 227202182));
	public static final RegistryObject<MobEffect> EYE_PAIN_2 = EFFECTS.register("eye_pain_ii",
			() -> new EyePain2(MobEffectCategory.HARMFUL, 227202182));

	public static void register(IEventBus bus) {
		EFFECTS.register(bus);

	}
}
