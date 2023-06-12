package com.blueking6.springnions.init;

import com.blueking6.springnions.springnions;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundInit {
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS,
			springnions.MOD_ID);
	
	public static final RegistryObject<SoundEvent> TOFU_PRESS_PROCESS = registerSoundEvent("tofu_press_process");
	
	private static RegistryObject<SoundEvent> registerSoundEvent(String name){
		return SOUNDS.register(name, () -> SoundEvent.createFixedRangeEvent(new ResourceLocation(springnions.MOD_ID, name),10f));
	}
	
	public static void register(IEventBus eventBus) {
		SOUNDS.register(eventBus);
	}
}
