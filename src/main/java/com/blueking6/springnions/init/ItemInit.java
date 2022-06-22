package com.blueking6.springnions.init;

import com.blueking6.springnions.springnions;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, springnions.MOD_ID);
	
	public static final RegistryObject<Item> ONION_CRATE = ITEMS.register("onion_crate",
			() -> new ItemNameBlockItem(BlockInit.ONION_CRATE.get(), new Item.Properties().tab(springnions.SPNION_TAB)));
	
	public static final RegistryObject<Item> ONION = ITEMS.register("onion",
			() -> new ItemNameBlockItem(BlockInit.ONION_PLANT.get(), new Item.Properties().tab(springnions.SPNION_TAB).food(FoodInit.ONION)));
	
	public static final RegistryObject<Item> GOLDEN_ONION = ITEMS.register("golden_onion",
			() -> new Item(new Item.Properties().tab(springnions.SPNION_TAB).food(FoodInit.GOLDEN_ONION)));
	
	public static final RegistryObject<Item> ONION_SHELF_ITEM = ITEMS.register("onion_shelf",
			() -> new BlockItem(BlockInit.ONION_SHELF.get(), new Item.Properties().tab(springnions.SPNION_TAB))); 
	
	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}
}
