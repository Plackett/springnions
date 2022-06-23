package com.blueking6.springnions;

import com.blueking6.springnions.init.BlockInit;
import com.blueking6.springnions.init.ItemInit;
import com.blueking6.springnions.init.TileEntityInit;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("springnions")
public class springnions {
	
	public static final String MOD_ID = "springnions";
	
	public static final CreativeModeTab SPNION_TAB = new CreativeModeTab(MOD_ID) {
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack makeIcon( ) {
			return new ItemStack(ItemInit.ONION_CRATE.get());
		}
	};
	
	public springnions() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		
		ItemInit.register(bus);
		BlockInit.register(bus);
		TileEntityInit.register(bus);
		
		bus.addListener(this::clientSetup);
		
		MinecraftForge.EVENT_BUS.register(this);
		
		
	}
	
	private void clientSetup(final FMLClientSetupEvent event) {
		ItemBlockRenderTypes.setRenderLayer(BlockInit.ONION_PLANT.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(BlockInit.ONION_SHELF.get(), RenderType.cutoutMipped());
	}
}
