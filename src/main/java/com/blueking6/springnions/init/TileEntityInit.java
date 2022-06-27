package com.blueking6.springnions.init;

import com.blueking6.springnions.springnions;
import com.blueking6.springnions.blocks.OnionShelfTile;
import com.blueking6.springnions.blocks.TofuPressEntity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TileEntityInit {
	public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITY = DeferredRegister
			.create(ForgeRegistries.BLOCK_ENTITIES, springnions.MOD_ID);

	public static final RegistryObject<BlockEntityType<OnionShelfTile>> ONION_SHELF = TILE_ENTITY.register(
			"onion_shelf",
			() -> BlockEntityType.Builder.of(OnionShelfTile::new, BlockInit.ONION_SHELF.get()).build(null));

	public static final RegistryObject<BlockEntityType<TofuPressEntity>> TOFU_PRESS = TILE_ENTITY.register(
			"tofu_press",
			() -> BlockEntityType.Builder.of(TofuPressEntity::new, BlockInit.TOFU_PRESS.get()).build(null));
	
	public static void register(IEventBus eventBus) {
		TILE_ENTITY.register(eventBus);
	}

}
