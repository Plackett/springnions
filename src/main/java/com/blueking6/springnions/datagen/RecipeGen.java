package com.blueking6.springnions.datagen;

import java.util.function.Consumer;

import com.blueking6.springnions.init.ItemInit;

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
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeGen extends RecipeProvider implements IConditionBuilder {

	public RecipeGen(PackOutput pack) {
		super(pack);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer) {

		ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemInit.GOLDEN_ONION.get()).pattern("///").pattern("/X/")
				.pattern("///").define('/', Items.GOLD_NUGGET).define('X', ItemInit.ONION.get())
				.save(consumer, new ResourceLocation("springnions", "golden_onion"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ItemInit.TOFU.get()), RecipeCategory.FOOD,
				ItemInit.GRILLED_TOFU.get(), 0.2f, 320)
				.save(consumer, new ResourceLocation("springnions", "grilled_tofu_smelt"));
		SimpleCookingRecipeBuilder.smoking(Ingredient.of(ItemInit.TOFU.get()), RecipeCategory.FOOD,
				ItemInit.GRILLED_TOFU.get(), 0.2f, 160)
				.save(consumer, new ResourceLocation("springnions", "grilled_tofu"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemInit.HYDROCHIP.get()).pattern("i/i").pattern("/o/")
				.pattern("i/i").define('i', Items.STICK).define('/', ItemInit.ONION.get()).define('o', Items.IRON_INGOT)
				.group("hchip").save(consumer, new ResourceLocation("springnions", "hydrochip"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemInit.HYDROCHIP2.get()).pattern("i/i").pattern("ETE")
				.pattern("i/i").define('i', Items.IRON_INGOT).define('/', ItemInit.GOLDEN_ONION.get())
				.define('E', ItemInit.ONION.get()).define('T', ItemInit.HYDROCHIP.get()).group("hchip")
				.save(consumer, new ResourceLocation("springnions", "hydrochip2"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemInit.HYDROCHIP3.get()).pattern("i/i").pattern("ETE")
				.pattern("/i/").define('i', Items.QUARTZ).define('/', ItemInit.GOLDEN_ONION.get())
				.define('E', Items.REDSTONE).define('T', ItemInit.HYDROCHIP2.get()).group("hchip")
				.save(consumer, new ResourceLocation("springnions", "hydrochip3"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ItemInit.ONION_CRATE.get())
				.requires(ItemInit.ONION.get(), 9).group("springnions")
				.save(consumer, new ResourceLocation("springnions", "onion_crate"));
		ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemInit.ONION_SHELF_ITEM.get()).pattern("___").pattern("/o ")
				.pattern("___").define('_', ItemTags.WOODEN_SLABS).define('/', ItemTags.PLANKS)
				.define('o', ItemInit.ONION.get()).group("springnions")
				.save(consumer, new ResourceLocation("springnions", "onion_shelf"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ItemInit.ONION_STEW.get()).requires(Items.CARROT, 1)
				.requires(Items.POTATO, 1).requires(ItemInit.ONION.get(), 1).requires(Items.BOWL, 1)
				.save(consumer, new ResourceLocation("springnions", "onion_stew"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ItemInit.ONION.get(), 9)
				.requires(ItemInit.ONION_CRATE.get(), 1).group("springnions")
				.save(consumer, new ResourceLocation("springnions", "onion"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemInit.SOYMILK_BUCKET.get()).pattern("###").pattern("iWi")
				.pattern(" i ").define('#', ItemInit.SOYBEANS.get()).define('i', Items.IRON_INGOT)
				.define('W', Items.WATER_BUCKET).save(consumer, new ResourceLocation("springnions", "soymilk_bucket"));
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ItemInit.TOFUPRESS.get(), 1).pattern("/o/").pattern("CHC")
				.pattern("CMC").define('/', Items.STICK).define('o', ItemTags.PLANKS).define('C', "forge:cobblestone")
				.define('M', ItemInit.ONION_SHELF_ITEM.get()).define('H', ItemInit.HYDROCHIP.get())
				.save(consumer, new ResourceLocation("springnions", "tofu_press"));
	}

}
