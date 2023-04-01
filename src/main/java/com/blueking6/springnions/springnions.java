package com.blueking6.springnions;

import org.apache.commons.lang3.Validate;

import com.blueking6.config.SpringnionsClientConfigs;
import com.blueking6.config.SpringnionsCommonConfigs;
import com.blueking6.springnions.init.BlockInit;
import com.blueking6.springnions.init.FluidInit;
import com.blueking6.springnions.init.ItemInit;
import com.blueking6.springnions.init.SoundInit;
import com.blueking6.springnions.init.TileEntityInit;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
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
	
	// Registered on the MOD event bus
	// Assume we have RegistryObject<Item> and RegistryObject<Block> called ITEM and BLOCK
	@SubscribeEvent
	public void registerTabs(CreativeModeTabEvent.Register event) {
	  event.registerCreativeModeTab(new ResourceLocation(MOD_ID, "springnions"), builder ->
	    // Set name of tab to display
	    builder.title(Component.translatable("Springnions"))
	    // Set icon of creative tab
	    .icon(() -> new ItemStack(ItemInit.ONION_CRATE.get()))
	    // Add default items to tab
	    .displayItems((params, output) -> {
	      output.accept(ItemInit.ONION.get());
	      output.accept(ItemInit.ONION_CRATE.get());
	      output.accept(BlockInit.ONION_SHELF.get());
	      output.accept(ItemInit.GOLDEN_ONION.get());
	      output.accept(ItemInit.SOYBEANS.get());
	      output.accept(ItemInit.SOYMILK_BUCKET.get());
	      output.accept(ItemInit.TOFU.get());
	      output.accept(ItemInit.GRILLED_TOFU.get());
	      output.accept(ItemInit.TOFUPRESS.get());
	      output.accept(ItemInit.TOFUPRESS2.get());
	      output.accept(ItemInit.TOFUPRESS3.get());
	      output.accept(ItemInit.TOFUPRESSC.get());
	      output.accept(ItemInit.ONION_STEW.get());
	      output.accept(ItemInit.HYDROCHIP.get());
	      output.accept(ItemInit.HYDROCHIP2.get());
	      output.accept(ItemInit.HYDROCHIP3.get());
	    })
	  );
	}

	public springnions() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		ItemInit.register(bus);
		BlockInit.register(bus);
		TileEntityInit.register(bus);
		FluidInit.register(bus);
		SoundInit.register(bus);

		bus.addListener(this::clientSetup);
		bus.addListener(this::loadComplete);
		bus.addListener(this::registerTabs);
		
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, SpringnionsClientConfigs.Specs, "springnions-client.toml");
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SpringnionsCommonConfigs.Specs, "springnions-common.toml");

		MinecraftForge.EVENT_BUS.register(this);

	}

	@SuppressWarnings("removal")
	private void clientSetup(final FMLClientSetupEvent event) {
		ItemBlockRenderTypes.setRenderLayer(BlockInit.ONION_PLANT.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockInit.SOY_PLANT.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockInit.ONION_SHELF.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockInit.TOFU_PRESS.get(), RenderType.cutoutMipped());
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
