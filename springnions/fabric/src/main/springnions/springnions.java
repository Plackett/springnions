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

package com.blueking6.springnions;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;

import com.blueking6.config.SpringnionsClientConfigs;
import com.blueking6.config.SpringnionsCommonConfigs;
import com.blueking6.springnions.datagen.AdvancementGen;
import com.blueking6.springnions.datagen.RecipeGen;
import com.blueking6.springnions.gui.CultivatorScreen;
import com.blueking6.springnions.gui.OrganicGeneratorScreen;
import com.blueking6.springnions.init.BlockInit;
import com.blueking6.springnions.init.CreativeTabInit;
import com.blueking6.springnions.init.EffectInit;
import com.blueking6.springnions.init.FluidInit;
import com.blueking6.springnions.init.ItemInit;
import com.blueking6.springnions.init.MenuInit;
import com.blueking6.springnions.init.PotionInit;
import com.blueking6.springnions.init.SoundInit;
import com.blueking6.tools.PotionBrewingRecipe;
import com.blueking6.springnions.init.EntityInit;
import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.DispenseFluidContainer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod("springnions")
public class springnions {

	public static final String MOD_ID = "springnions";
	private static final Logger LOGGER = LogUtils.getLogger();
	private static boolean markShaderForRemoval = false;
	private static boolean markShaderForAddition = true;

	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event) {
		// Do something when the server starts
		LOGGER.info("✿ Springnions Starting... ✿");
		LOGGER.info("✿ Have a great day!       ✿");
	}

	@SubscribeEvent
	public void buildContents(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeTabInit.SPRINGNIONSTAB.getKey()) {
			event.accept(ItemInit.ONION.get());
			event.accept(ItemInit.ONION_CRATE.get());
			event.accept(BlockInit.ONION_SHELF.get());
			event.accept(ItemInit.GOLDEN_ONION.get());
			event.accept(ItemInit.SOYBEANS.get());
			event.accept(ItemInit.SOYMILK_BUCKET.get());
			event.accept(ItemInit.TOFU.get());
			event.accept(ItemInit.GRILLED_TOFU.get());
			event.accept(ItemInit.TOFUPRESS.get());
			event.accept(ItemInit.TOFUPRESS2.get());
			event.accept(ItemInit.TOFUPRESS3.get());
			event.accept(ItemInit.TOFUPRESSC.get());
			event.accept(ItemInit.ONION_STEW.get());
			event.accept(ItemInit.HYDROCHIP.get());
			event.accept(ItemInit.HYDROCHIP2.get());
			event.accept(ItemInit.HYDROCHIP3.get());
			event.accept(ItemInit.SOY_PULP.get());
			event.accept(ItemInit.CULTIVATOR.get());
			event.accept(ItemInit.ORGANIC_GENERATOR.get());
		}
	}

	public void gatherData(GatherDataEvent event) {
		DataGenerator gen = event.getGenerator();
		PackOutput packOutput = gen.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		gen.addProvider(event.includeServer(), new ForgeAdvancementProvider(packOutput, lookupProvider,
				event.getExistingFileHelper(), List.of(new AdvancementGen())));
		gen.addProvider(event.includeServer(), new RecipeGen(packOutput));
	}

	public springnions() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus fbus = MinecraftForge.EVENT_BUS;

		ItemInit.register(bus);
		BlockInit.register(bus);
		EntityInit.register(bus);
		FluidInit.register(bus);
		SoundInit.register(bus);
		CreativeTabInit.register(bus);
		MenuInit.register(bus);
		EffectInit.register(bus);
		PotionInit.register(bus);

		bus.addListener(this::clientSetup);
		bus.addListener(this::commonSetup);
		bus.addListener(this::loadComplete);
		bus.addListener(this::buildContents);
		bus.addListener(this::gatherData);

		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, SpringnionsClientConfigs.Specs,
				"springnions-client.toml");
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SpringnionsCommonConfigs.Specs,
				"springnions-common.toml");

		if (FMLLoader.getDist().isClient()) {
			clientUtil(bus, fbus);
		}
		MinecraftForge.EVENT_BUS.register(this);

	}

	private static void clientUtil(IEventBus bus, IEventBus fbus) {
		fbus.addListener(springnions::playerTick);
		fbus.addListener(springnions::removeEffect);
		fbus.addListener(springnions::addedEffect);
		fbus.addListener(springnions::expiredEffect);
	}

	@SuppressWarnings("resource")
	static void playerTick(final TickEvent.PlayerTickEvent event) {
		Optional<ResourceLocation> option0 = Optional.of(new ResourceLocation("springnions:eye_pain"));
		Optional<ResourceLocation> option1 = Optional.of(new ResourceLocation("springnions:eye_pain_ii"));
		ResourceLocation loc0 = option0
				.map(resourceLocation -> resourceLocation.withPrefix("shaders/post/").withSuffix(".json")).get();
		ResourceLocation loc1 = option1
				.map(resourceLocation -> resourceLocation.withPrefix("shaders/post/").withSuffix(".json")).get();
		// check if player has effect
		if (event.player.hasEffect(EffectInit.EYE_PAIN.get()) && option0.isPresent() && option1.isPresent()
				&& event.side.isClient()) {
			GameRenderer render = Minecraft.getInstance().gameRenderer;
			// change shader if higher amplitude
			if (event.player.getEffect(EffectInit.EYE_PAIN.get()).getAmplifier() >= 1) {
				loc0 = loc1;
			}
			// first tick add shader if not marked for removal
			if (markShaderForAddition == true && markShaderForRemoval == false) {
				render.loadEffect(loc0);
				markShaderForAddition = false;
				// remove it if its marked for removal
			} else if (markShaderForRemoval == true) {
				render.shutdownEffect();
				markShaderForRemoval = false;
			}
			// backup removal method for if the potion effect is removed before it runs out
		} else if (markShaderForRemoval == true && Minecraft.getInstance().gameRenderer.currentEffect() != null
				&& event.side.isClient()) {
			Minecraft.getInstance().gameRenderer.shutdownEffect();
			markShaderForRemoval = false;
		}
	}

	static void removeEffect(final MobEffectEvent.Remove event) {
		// check if shader is still active
		if (event.getEffectInstance().getEffect() == EffectInit.EYE_PAIN.get()
				&& Minecraft.getInstance().gameRenderer.currentEffect() != null) {
			markShaderForRemoval = true;
		}
	}

	static void addedEffect(final MobEffectEvent.Added event) {
		if (event.getEffectInstance().getEffect() == EffectInit.EYE_PAIN.get()) {
			markShaderForAddition = true;
		}
	}

	static void expiredEffect(final MobEffectEvent.Expired event) {
		// check if shader is still active
		if (event.getEffectInstance().getEffect() == EffectInit.EYE_PAIN.get()
				&& Minecraft.getInstance().gameRenderer.currentEffect() != null) {
			markShaderForRemoval = true;
		}
	}

	@SuppressWarnings({ "deprecation" })
	private void clientSetup(final FMLClientSetupEvent event) {
		ItemBlockRenderTypes.setRenderLayer(BlockInit.ONION_PLANT.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockInit.SOY_PLANT.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockInit.ONION_SHELF.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockInit.TOFU_PRESS.get(), RenderType.cutoutMipped());
		event.enqueueWork(() -> {
			MenuScreens.register(MenuInit.CULTIVATOR.get(), CultivatorScreen::new);
			MenuScreens.register(MenuInit.ORGANIC_GENERATOR.get(), OrganicGeneratorScreen::new);
		});
	}

	@SubscribeEvent
	public void itemCrafted(final ItemCraftedEvent event) {
		// give eye pain for 15 minutes at high strength for crafting cultivator
		if (event.getCrafting().getItem() == ItemInit.CULTIVATOR.get()) {
			event.getEntity().addEffect(new MobEffectInstance(EffectInit.EYE_PAIN.get(), 18000, 1));
		}
		// give eye pain for 5 minutes at weak strength for crafting generator
		if (event.getCrafting().getItem() == ItemInit.ORGANIC_GENERATOR.get()) {
			event.getEntity().addEffect(new MobEffectInstance(EffectInit.EYE_PAIN.get(), 6000, 0));
		}
		// give eye pain for 15 seconds at weak strength for crafting a golden onion
		if (event.getCrafting().getItem() == ItemInit.GOLDEN_ONION.get()) {
			event.getEntity().addEffect(new MobEffectInstance(EffectInit.EYE_PAIN.get(), 300, 0));
		}
		// give eye pain for 15 seconds at weak strength for crafting an onion crate
		if (event.getCrafting().getItem() == ItemInit.ONION_CRATE.get()) {
			event.getEntity().addEffect(new MobEffectInstance(EffectInit.EYE_PAIN.get(), 300, 0));
		}
		// give eye pain for 15 seconds at weak strength for crafting an onion shelf
		if (event.getCrafting().getItem() == ItemInit.ONION_SHELF_ITEM.get()) {
			event.getEntity().addEffect(new MobEffectInstance(EffectInit.EYE_PAIN.get(), 300, 0));
		}
		// give eye pain for 30 seconds at weak strength for crafting a tofu press lv1
		if (event.getCrafting().getItem() == ItemInit.TOFUPRESS.get()) {
			event.getEntity().addEffect(new MobEffectInstance(EffectInit.EYE_PAIN.get(), 600, 0));
		}
		// give eye pain for 60 seconds at weak strength for crafting a tofu press lv2
		if (event.getCrafting().getItem() == ItemInit.TOFUPRESS2.get()) {
			event.getEntity().addEffect(new MobEffectInstance(EffectInit.EYE_PAIN.get(), 1200, 0));
		}
		// give eye pain for 60 seconds at high strength for crafting a tofu press lv3
		if (event.getCrafting().getItem() == ItemInit.TOFUPRESS3.get()) {
			event.getEntity().addEffect(new MobEffectInstance(EffectInit.EYE_PAIN.get(), 1200, 1));
		}
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			ComposterBlock.COMPOSTABLES.put(ItemInit.SOY_PULP.get(), 0.5F);
			BrewingRecipeRegistry.addRecipe(
					new PotionBrewingRecipe(Potions.WATER, ItemInit.ONION.get(), PotionInit.EYE_PAIN_POTION.get()));
			BrewingRecipeRegistry.addRecipe(new PotionBrewingRecipe(PotionInit.EYE_PAIN_POTION.get(),
					ItemInit.GOLDEN_ONION.get(), PotionInit.EYE_PAIN_POTION_II.get()));
		});
	}

	public void loadComplete(FMLLoadCompleteEvent event) {
		BlockState state = Fluids.WATER.defaultFluidState().createLegacyBlock();
		BlockState state2 = Fluids.WATER.getFluidType().getBlockForFluidState(null, null,
				Fluids.WATER.defaultFluidState());
		Validate.isTrue(state.getBlock() == Blocks.WATER && state2 == state);
		ItemStack stack = Fluids.WATER.getFluidType().getBucket(new FluidStack(Fluids.WATER, 1));
		Validate.isTrue(stack.getItem() == Fluids.WATER.getBucket());
		event.enqueueWork(() -> DispenserBlock.registerBehavior(ItemInit.SOYMILK_BUCKET.get(),
				DispenseFluidContainer.getInstance()));
	}
}
