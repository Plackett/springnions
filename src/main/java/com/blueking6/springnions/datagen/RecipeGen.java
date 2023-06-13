package com.blueking6.springnions.datagen;

import java.util.function.Consumer;

import com.blueking6.springnions.init.ItemInit;

import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.ItemUsedOnLocationTrigger;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

public class RecipeGen extends RecipeProvider implements IConditionBuilder {

	public RecipeGen(PackOutput pack) {
		super(pack);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer) {

		ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemInit.GOLDEN_ONION.get()).pattern("///").pattern("/X/")
				.pattern("///").define('/', Items.GOLD_NUGGET).define('X', ItemInit.ONION.get())
				.unlockedBy("plant_onion",
						ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
								LocationPredicate.Builder.location()
										.setBlock(BlockPredicate.Builder.block().of(Blocks.FARMLAND).build()),
								ItemPredicate.Builder.item().of(ItemInit.ONION.get())))
				.save(consumer, new ResourceLocation("springnions", "golden_onion"));
		SimpleCookingRecipeBuilder
				.smelting(Ingredient.of(ItemInit.TOFU.get()), RecipeCategory.FOOD, ItemInit.GRILLED_TOFU.get(), 0.2f,
						320)
				.unlockedBy("obtain_tofu", InventoryChangeTrigger.TriggerInstance.hasItems(ItemInit.TOFU.get()))
				.save(consumer, new ResourceLocation("springnions", "grilled_tofu_smelt"));
		SimpleCookingRecipeBuilder
				.smoking(Ingredient.of(ItemInit.TOFU.get()), RecipeCategory.FOOD, ItemInit.GRILLED_TOFU.get(), 0.2f,
						160)
				.unlockedBy("obtain_tofu", InventoryChangeTrigger.TriggerInstance.hasItems(ItemInit.TOFU.get()))
				.save(consumer, new ResourceLocation("springnions", "grilled_tofu"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemInit.HYDROCHIP.get()).pattern("i/i").pattern("/o/")
				.pattern("i/i").define('i', Items.STICK).define('/', ItemInit.ONION.get()).define('o', Items.IRON_INGOT)
				.group("hchip")
				.unlockedBy("plant_bean",
						ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
								LocationPredicate.Builder.location()
										.setBlock(BlockPredicate.Builder.block().of(Blocks.FARMLAND).build()),
								ItemPredicate.Builder.item().of(ItemInit.SOYBEANS.get())))
				.save(consumer, new ResourceLocation("springnions", "hydrochip"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemInit.HYDROCHIP2.get()).pattern("i/i").pattern("ETE")
				.pattern("i/i").define('i', Items.IRON_INGOT).define('/', ItemInit.GOLDEN_ONION.get())
				.define('E', ItemInit.ONION.get()).define('T', ItemInit.HYDROCHIP.get()).group("hchip")
				.unlockedBy("obtain_tofu", InventoryChangeTrigger.TriggerInstance.hasItems(ItemInit.TOFU.get()))
				.save(consumer, new ResourceLocation("springnions", "hydrochip2"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemInit.HYDROCHIP3.get()).pattern("i/i").pattern("ETE")
				.pattern("/i/").define('i', Items.QUARTZ).define('/', ItemInit.GOLDEN_ONION.get())
				.define('E', Items.REDSTONE).define('T', ItemInit.HYDROCHIP2.get()).group("hchip")
				.unlockedBy("obtain_golden_onion",
						InventoryChangeTrigger.TriggerInstance.hasItems(ItemInit.GOLDEN_ONION.get()))
				.save(consumer, new ResourceLocation("springnions", "hydrochip3"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ItemInit.ONION_CRATE.get())
				.requires(ItemInit.ONION.get(), 9).group("springnions")
				.unlockedBy("plant_onion",
						ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
								LocationPredicate.Builder.location()
										.setBlock(BlockPredicate.Builder.block().of(Blocks.FARMLAND).build()),
								ItemPredicate.Builder.item().of(ItemInit.ONION.get())))
				.save(consumer, new ResourceLocation("springnions", "onion_crate"));
		ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemInit.ONION_SHELF_ITEM.get()).pattern("___").pattern("/o ")
				.pattern("___").define('_', ItemTags.WOODEN_SLABS).define('/', ItemTags.PLANKS)
				.define('o', ItemInit.ONION.get()).group("springnions")
				.unlockedBy("plant_onion",
						ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
								LocationPredicate.Builder.location()
										.setBlock(BlockPredicate.Builder.block().of(Blocks.FARMLAND).build()),
								ItemPredicate.Builder.item().of(ItemInit.ONION.get())))
				.save(consumer, new ResourceLocation("springnions", "onion_shelf"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ItemInit.ONION_STEW.get()).requires(Items.CARROT, 1)
				.requires(Items.POTATO, 1).requires(ItemInit.ONION.get(), 1).requires(Items.BOWL, 1)
				.unlockedBy("plant_onion",
						ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
								LocationPredicate.Builder.location()
										.setBlock(BlockPredicate.Builder.block().of(Blocks.FARMLAND).build()),
								ItemPredicate.Builder.item().of(ItemInit.ONION.get())))
				.save(consumer, new ResourceLocation("springnions", "onion_stew"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ItemInit.ONION.get(), 9)
				.requires(ItemInit.ONION_CRATE.get(), 1).group("springnions")
				.unlockedBy("plant_onion",
						ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
								LocationPredicate.Builder.location()
										.setBlock(BlockPredicate.Builder.block().of(Blocks.FARMLAND).build()),
								ItemPredicate.Builder.item().of(ItemInit.ONION.get())))
				.save(consumer, new ResourceLocation("springnions", "onion"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemInit.SOYMILK_BUCKET.get()).pattern("###").pattern("iWi")
				.pattern(" i ").define('#', ItemInit.SOYBEANS.get()).define('i', Items.IRON_INGOT)
				.define('W', Items.WATER_BUCKET)
				.unlockedBy("plant_bean",
						ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
								LocationPredicate.Builder.location()
										.setBlock(BlockPredicate.Builder.block().of(Blocks.FARMLAND).build()),
								ItemPredicate.Builder.item().of(ItemInit.SOYBEANS.get())))
				.save(consumer, new ResourceLocation("springnions", "soymilk_bucket"));
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ItemInit.TOFUPRESS.get(), 1).pattern("/o/").pattern("CHC")
				.pattern("CMC").define('/', Items.STICK).define('o', ItemTags.PLANKS)
				.define('C', net.minecraftforge.common.Tags.Items.COBBLESTONE)
				.define('M', ItemInit.ONION_SHELF_ITEM.get()).define('H', ItemInit.HYDROCHIP.get()).group("press")
				.unlockedBy("plant_bean",
						ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
								LocationPredicate.Builder.location()
										.setBlock(BlockPredicate.Builder.block().of(Blocks.FARMLAND).build()),
								ItemPredicate.Builder.item().of(ItemInit.SOYBEANS.get())))
				.save(consumer, new ResourceLocation("springnions", "tofu_press"));
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ItemInit.TOFUPRESS2.get(), 1).pattern("/o/").pattern("CHC")
				.pattern("CMC").define('/', Items.STICK).define('o', Items.IRON_INGOT)
				.define('C', net.minecraftforge.common.Tags.Items.COBBLESTONE)
				.define('M', ItemInit.ONION_SHELF_ITEM.get()).define('H', ItemInit.HYDROCHIP2.get()).group("press")
				.unlockedBy("obtain_tofu", InventoryChangeTrigger.TriggerInstance.hasItems(ItemInit.TOFU.get()))
				.save(consumer, new ResourceLocation("springnions", "tofu_press2"));
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ItemInit.TOFUPRESS3.get(), 1).pattern("/o/").pattern("CHC")
				.pattern("CMC").define('/', Items.STICK).define('o', Items.QUARTZ).define('C', Items.GOLD_INGOT)
				.define('M', ItemInit.ONION_SHELF_ITEM.get()).define('H', ItemInit.HYDROCHIP3.get()).group("press")
				.unlockedBy("obtain_golden_onion",
						InventoryChangeTrigger.TriggerInstance.hasItems(ItemInit.GOLDEN_ONION.get()))
				.save(consumer, new ResourceLocation("springnions", "tofu_press3"));
	}

}