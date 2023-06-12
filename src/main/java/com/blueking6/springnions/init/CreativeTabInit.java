package com.blueking6.springnions.init;

import com.blueking6.springnions.springnions;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeTabInit {
	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB,
			springnions.MOD_ID);

	public static RegistryObject<CreativeModeTab> SPRINGNIONSTAB = TABS.register("springnions",
			() -> CreativeModeTab.builder().withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
					.icon(() -> new ItemStack(ItemInit.ONION.get()))
					.title(Component.translatable("itemGroup." + springnions.MOD_ID)).build());

	public static void register(IEventBus eventBus) {
		TABS.register(eventBus);
	}
}
