package com.blueking6.springnions.init;

import com.blueking6.springnions.springnions;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PotionInit {
	public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS,
			springnions.MOD_ID);

	public static final RegistryObject<Potion> EYE_PAIN_POTION = POTIONS.register("onion_juice",
			() -> new Potion(new MobEffectInstance(EffectInit.EYE_PAIN.get(), 3600)));
	public static final RegistryObject<Potion> EYE_PAIN_POTION_II = POTIONS.register("potent_onion_juice",
			() -> new Potion(new MobEffectInstance(EffectInit.EYE_PAIN_2.get(), 9600)));

	public static void register(IEventBus bus) {
		POTIONS.register(bus);

	}
}
