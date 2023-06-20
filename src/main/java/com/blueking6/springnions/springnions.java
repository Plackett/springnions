package com.blueking6.springnions;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;

import com.blueking6.config.SpringnionsClientConfigs;
import com.blueking6.config.SpringnionsCommonConfigs;
import com.blueking6.springnions.datagen.AdvancementGen;
import com.blueking6.springnions.datagen.RecipeGen;
import com.blueking6.springnions.gui.CultivatorScreen;
import com.blueking6.springnions.init.BlockInit;
import com.blueking6.springnions.init.CreativeTabInit;
import com.blueking6.springnions.init.FluidInit;
import com.blueking6.springnions.init.ItemInit;
import com.blueking6.springnions.init.MenuInit;
import com.blueking6.springnions.init.SoundInit;
import com.blueking6.springnions.init.EntityInit;
import com.mojang.logging.LogUtils;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.DispenseFluidContainer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("springnions")
public class springnions {

	public static final String MOD_ID = "springnions";
	private static final Logger LOGGER = LogUtils.getLogger();

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

		ItemInit.register(bus);
		BlockInit.register(bus);
		EntityInit.register(bus);
		FluidInit.register(bus);
		SoundInit.register(bus);
		CreativeTabInit.register(bus);
		MenuInit.register(bus);

		bus.addListener(this::clientSetup);
		bus.addListener(this::loadComplete);
		bus.addListener(this::buildContents);
		bus.addListener(this::gatherData);

		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, SpringnionsClientConfigs.Specs,
				"springnions-client.toml");
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SpringnionsCommonConfigs.Specs,
				"springnions-common.toml");

		MinecraftForge.EVENT_BUS.register(this);

	}

	@SuppressWarnings({ "deprecation" })
	private void clientSetup(final FMLClientSetupEvent event) {
		ItemBlockRenderTypes.setRenderLayer(BlockInit.ONION_PLANT.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockInit.SOY_PLANT.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockInit.ONION_SHELF.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockInit.TOFU_PRESS.get(), RenderType.cutoutMipped());
		event.enqueueWork(() -> {
			MenuScreens.register(MenuInit.CULTIVATOR.get(), CultivatorScreen::new);
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
