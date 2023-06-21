package com.blueking6.springnions.datagen;

import java.util.function.Consumer;

import com.blueking6.springnions.springnions;
import com.blueking6.springnions.init.BlockInit;
import com.blueking6.springnions.init.ItemInit;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.ItemUsedOnLocationTrigger;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

public class AdvancementGen implements ForgeAdvancementProvider.AdvancementGenerator {

	@Override
	public void generate(Provider registries, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {

		Advancement base = Advancement.Builder.advancement().display(ItemInit.ONION.get(),
				Component.translatable("Springnions!"), Component.translatable("Onion-Related Advancements"),
				new ResourceLocation("springnions:textures/block/onion_shelf.png"), FrameType.TASK, true, true, false)
				.addCriterion("has_onion", InventoryChangeTrigger.TriggerInstance.hasItems(ItemInit.ONION.get()))
				.save(saver, new ResourceLocation(springnions.MOD_ID, "springnions"), existingFileHelper);

		Advancement soy = Advancement.Builder.advancement()
				.display(ItemInit.SOYBEANS.get(), Component.translatable("Beans, Beans, the Musical Fruit"),
						Component.translatable("Plant your first soybean plant."),
						new ResourceLocation("springnions:textures/block/onion_shelf.png"), FrameType.TASK, true, true,
						false)
				.addCriterion("plant_bean",
						ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
								LocationPredicate.Builder.location()
										.setBlock(BlockPredicate.Builder.block().of(Blocks.FARMLAND).build()),
								ItemPredicate.Builder.item().of(ItemInit.SOYBEANS.get())))
				.parent(base)
				.rewards(AdvancementRewards.Builder.recipe(new ResourceLocation("springnions:tofu_press"))
						.addRecipe(new ResourceLocation("springnions:soymik_bucket"))
						.addRecipe(new ResourceLocation("springnions:hydrochip")))
				.save(saver, new ResourceLocation(springnions.MOD_ID, "bean_planter"), existingFileHelper);

		Advancement onion = Advancement.Builder.advancement().display(ItemInit.ONION.get(),
				Component.translatable("Onions of Layers"), Component.translatable("Plant your first onion plant."),
				new ResourceLocation("springnions:textures/block/onion_shelf.png"), FrameType.TASK, true, true, false)
				.addCriterion("plant_onion",
						ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
								LocationPredicate.Builder.location()
										.setBlock(BlockPredicate.Builder.block().of(Blocks.FARMLAND).build()),
								ItemPredicate.Builder.item().of(ItemInit.ONION.get())))
				.parent(base)
				.rewards(AdvancementRewards.Builder.recipe(new ResourceLocation("springnions:onion_crate"))
						.addRecipe(new ResourceLocation("springnions:onion_shelf"))
						.addRecipe(new ResourceLocation("springnions:golden_onion"))
						.addRecipe(new ResourceLocation("springnions:onion")))
				.save(saver, new ResourceLocation(springnions.MOD_ID, "onion_planter"), existingFileHelper);

		Advancement gold = Advancement.Builder.advancement()
				.display(ItemInit.GOLDEN_ONION.get(), Component.translatable("Not A Twilight Reference"),
						Component.translatable("Obtain the fabled: Golden Onion."),
						new ResourceLocation("springnions:textures/block/onion_shelf.png"), FrameType.CHALLENGE, true,
						true, true)
				.addCriterion("obtained_golden_onion",
						InventoryChangeTrigger.TriggerInstance.hasItems(ItemInit.GOLDEN_ONION.get()))
				.parent(onion)
				.rewards(AdvancementRewards.Builder.recipe(new ResourceLocation("springnions:tofu_press2"))
						.addRecipe(new ResourceLocation("springnions:tofu_press3")))
				.save(saver, new ResourceLocation(springnions.MOD_ID, "golden_onion_advancement"), existingFileHelper);

		Advancement.Builder.advancement()
				.display(ItemInit.ONION_SHELF_ITEM.get(), Component.translatable("Layers of Onions"),
						Component.translatable("Store an Onion Crate in an Onion Shelf."),
						new ResourceLocation("springnions:textures/block/onion_shelf.png"), FrameType.GOAL, true, true,
						false)
				.addCriterion("used_shelf",
						ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
								LocationPredicate.Builder.location().setBlock(
										BlockPredicate.Builder.block().of(BlockInit.ONION_SHELF.get()).build()),
								ItemPredicate.Builder.item().of(ItemInit.ONION_CRATE.get())))
				.parent(onion)
				.save(saver, new ResourceLocation(springnions.MOD_ID, "onion_shelf_advancement"), existingFileHelper);

		Advancement press = Advancement.Builder.advancement()
				.display(ItemInit.TOFUPRESS.get(), Component.translatable("Fresh Off The Presses"),
						Component.translatable("Make your first batch of tofu."),
						new ResourceLocation("springnions:textures/block/onion_shelf.png"), FrameType.TASK, true, true,
						true)
				.addCriterion("has_tofu", InventoryChangeTrigger.TriggerInstance.hasItems(ItemInit.TOFU.get()))
				.rewards(AdvancementRewards.Builder.recipe(new ResourceLocation("springnions:hydrochip2"))
						.addRecipe(new ResourceLocation("springnions:hydrochip3")))
				.parent(soy).save(saver, new ResourceLocation(springnions.MOD_ID, "presses"), existingFileHelper);

		Advancement.Builder.advancement()
				.display(ItemInit.TOFUPRESS3.get(), Component.translatable("Maximum Overdrive"),
						Component.translatable("Construct the fastest tofu press possible."),
						new ResourceLocation("springnions:textures/block/onion_shelf.png"), FrameType.TASK, true, true,
						true)
				.addCriterion("has_press", InventoryChangeTrigger.TriggerInstance.hasItems(ItemInit.TOFUPRESS3.get()))
				.parent(press)
				.save(saver, new ResourceLocation(springnions.MOD_ID, "maximum_overdrive"), existingFileHelper);

		Advancement.Builder.advancement()
				.display(ItemInit.CULTIVATOR.get(), Component.translatable("This Is The World Now"),
						Component.translatable("Achieve ultimate automation for your plants... At a price."),
						new ResourceLocation("springnions:textures/block/onion_shelf.png"), FrameType.CHALLENGE, true,
						true, true)
				.addCriterion("has_cult", InventoryChangeTrigger.TriggerInstance.hasItems(ItemInit.CULTIVATOR.get()))
				.parent(gold).save(saver, new ResourceLocation(springnions.MOD_ID, "cultivator"), existingFileHelper);

	}

}
