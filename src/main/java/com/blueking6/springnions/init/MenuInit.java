package com.blueking6.springnions.init;

import com.blueking6.springnions.springnions;
import com.blueking6.springnions.gui.CultivatorMenu;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuInit {
	public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES,
			springnions.MOD_ID);
	public static final RegistryObject<MenuType<CultivatorMenu>> CULTIVATOR = MENUS.register("cultivator_menu",
			() -> IForgeMenuType
					.create((windowId, inv, data) -> new CultivatorMenu(windowId, inv.player, data.readBlockPos())));

	public static void register(IEventBus eventBus) {
		MENUS.register(eventBus);
	}
}
