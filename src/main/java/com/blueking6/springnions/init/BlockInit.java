package com.blueking6.springnions.init;

import com.blueking6.springnions.springnions;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
	public static final DeferredRegister<Block> BLOCKS = 
			DeferredRegister.create(ForgeRegistries.BLOCKS, springnions.MOD_ID);

	public static final RegistryObject<Block> ONION_CRATE = BLOCKS.register("onion_crate", () -> new OnionCrate(BlockBehaviour.Properties.of(Material.WOOD)));
	public static final RegistryObject<Block> ONION_PLANT = BLOCKS.register("onion_plant", () -> new Onion(BlockBehaviour.Properties.copy(Blocks.CARROTS).noOcclusion()));
	public static final RegistryObject<Block> ONION_SHELF = BLOCKS.register("onion_shelf", () -> new OnionShelf(BlockBehaviour.Properties.of(Material.WOOD)));
	
/*	private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
		RegistryObject<T> toReturn = BLOCKS.register(name, block);
		ItemInit.registerBlockItem(name, toReturn, tab);
		return toReturn;
	}*/
	
	public static void register(IEventBus eventBus) {
		BLOCKS.register(eventBus);
	}
}
