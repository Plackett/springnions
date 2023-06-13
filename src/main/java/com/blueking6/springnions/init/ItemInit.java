package com.blueking6.springnions.init;

import com.blueking6.springnions.springnions;
import com.blueking6.springnions.items.SoyPulp;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
			springnions.MOD_ID);

	public static final RegistryObject<Item> ONION_CRATE = ITEMS.register("onion_crate",
			() -> new ItemNameBlockItem(BlockInit.ONION_CRATE.get(), new Item.Properties()));

	public static final RegistryObject<Item> ONION = ITEMS.register("onion",
			() -> new ItemNameBlockItem(BlockInit.ONION_PLANT.get(), new Item.Properties().food(FoodInit.ONION)));

	public static final RegistryObject<Item> SOYBEANS = ITEMS.register("soybeans",
			() -> new ItemNameBlockItem(BlockInit.SOY_PLANT.get(), new Item.Properties()));

	public static final RegistryObject<Item> GOLDEN_ONION = ITEMS.register("golden_onion",
			() -> new Item(new Item.Properties().food(FoodInit.GOLDEN_ONION)));

	public static final RegistryObject<Item> HYDROCHIP = ITEMS.register("hydrochip",
			() -> new Item(new Item.Properties()));

	public static final RegistryObject<Item> HYDROCHIP2 = ITEMS.register("hydrochip2",
			() -> new Item(new Item.Properties()));

	public static final RegistryObject<Item> HYDROCHIP3 = ITEMS.register("hydrochip3",
			() -> new Item(new Item.Properties()));

	public static final RegistryObject<Item> ONION_STEW = ITEMS.register("onion_stew",
			() -> new BowlFoodItem(new Item.Properties().food(FoodInit.ONION_STEW)));

	public static final RegistryObject<Item> ONION_SHELF_ITEM = ITEMS.register("onion_shelf",
			() -> new BlockItem(BlockInit.ONION_SHELF.get(), new Item.Properties()));

	public static final RegistryObject<Item> SOYMILK_BUCKET = ITEMS.register("soymilk_bucket",
			() -> new BucketItem(FluidInit.SOYMILK, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

	public static final RegistryObject<Item> TOFU = ITEMS.register("tofu",
			() -> new Item(new Item.Properties().food(FoodInit.TOFU)));

	public static final RegistryObject<Item> GRILLED_TOFU = ITEMS.register("grilled_tofu",
			() -> new Item(new Item.Properties().food(FoodInit.GRILLED_TOFU)));

	public static final RegistryObject<Item> TOFUPRESS = ITEMS.register("tofu_press",
			() -> new ItemNameBlockItem(BlockInit.TOFU_PRESS.get(), new Item.Properties()));

	public static final RegistryObject<Item> TOFUPRESS2 = ITEMS.register("tofu_press2",
			() -> new ItemNameBlockItem(BlockInit.TOFU_PRESS2.get(), new Item.Properties()));

	public static final RegistryObject<Item> TOFUPRESS3 = ITEMS.register("tofu_press3",
			() -> new ItemNameBlockItem(BlockInit.TOFU_PRESS3.get(), new Item.Properties()));

	public static final RegistryObject<Item> TOFUPRESSC = ITEMS.register("tofu_pressc",
			() -> new ItemNameBlockItem(BlockInit.TOFU_PRESSC.get(), new Item.Properties()));

	public static final RegistryObject<Item> SOY_PULP = ITEMS.register("soy_pulp",
			() -> new SoyPulp(new Item.Properties()));

	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}
}
