package com.blueking6.springnions;

import org.apache.commons.lang3.Validate;

import com.blueking6.springnions.init.BlockInit;
import com.blueking6.springnions.init.FluidInit;
import com.blueking6.springnions.init.ItemInit;
import com.blueking6.springnions.init.TileEntityInit;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.DispenseFluidContainer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("springnions")
public class springnions {

	public static final String MOD_ID = "springnions";

	public static final CreativeModeTab SPNION_TAB = new CreativeModeTab(MOD_ID) {
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack makeIcon() {
			return new ItemStack(ItemInit.ONION_CRATE.get());
		}
	};

	public springnions() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		ItemInit.register(bus);
		BlockInit.register(bus);
		TileEntityInit.register(bus);
		FluidInit.register(bus);

		bus.addListener(this::clientSetup);
		bus.addListener(this::loadComplete);

		MinecraftForge.EVENT_BUS.register(this);

	}

	private void clientSetup(final FMLClientSetupEvent event) {
		ItemBlockRenderTypes.setRenderLayer(BlockInit.ONION_PLANT.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockInit.ONION_SHELF.get(), RenderType.cutoutMipped());
	}
	
	public void loadComplete(FMLLoadCompleteEvent event) {
		BlockState state = Fluids.WATER.defaultFluidState().createLegacyBlock();
		BlockState state2 = Fluids.WATER.getFluidType().getBlockForFluidState(null, null,
				Fluids.WATER.defaultFluidState());
		Validate.isTrue(state.getBlock() == Blocks.WATER && state2 == state);
		ItemStack stack = Fluids.WATER.getFluidType().getBucket(new FluidStack(Fluids.WATER, 1));
		Validate.isTrue(stack.getItem() == Fluids.WATER.getBucket());
		event.enqueueWork(
				() -> DispenserBlock.registerBehavior(ItemInit.SOYMILK_BUCKET.get(), DispenseFluidContainer.getInstance()));
	}
}
