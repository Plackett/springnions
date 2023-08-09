//MIT License
//
//Copyright (c) 2023 Blueking6
//
//Permission is hereby granted, free of charge, to any person obtaining a copy
//of this software and associated documentation files (the "Software"), to deal
//in the Software without restriction, including without limitation the rights
//to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//copies of the Software, and to permit persons to whom the Software is
//furnished to do so, subject to the following conditions:
//
//The above copyright notice and this permission notice shall be included in all
//copies or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//SOFTWARE.

package com.blueking6.springnions.init;

import com.blueking6.springnions.springnions;
import com.blueking6.springnions.gui.CultivatorMenu;
import com.blueking6.springnions.gui.OrganicGeneratorMenu;

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
	public static final RegistryObject<MenuType<OrganicGeneratorMenu>> ORGANIC_GENERATOR = MENUS
			.register("organic_generator_menu", () -> IForgeMenuType.create(
					(windowId, inv, data) -> new OrganicGeneratorMenu(windowId, inv.player, data.readBlockPos())));

	public static void register(IEventBus eventBus) {
		MENUS.register(eventBus);
	}
}
