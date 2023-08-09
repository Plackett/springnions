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

package com.blueking6.springnions.init;

import com.blueking6.springnions.springnions;
import com.blueking6.springnions.entities.CultivatorEntity;
import com.blueking6.springnions.entities.OnionShelfEntity;
import com.blueking6.springnions.entities.OrganicGeneratorEntity;
import com.blueking6.springnions.entities.TofuPressEntity;
import com.blueking6.springnions.entities.TofuPressEntity2;
import com.blueking6.springnions.entities.TofuPressEntity3;
import com.blueking6.springnions.entities.TofuPressEntityC;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityInit {
	public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITY = DeferredRegister
			.create(ForgeRegistries.BLOCK_ENTITY_TYPES, springnions.MOD_ID);

	public static final RegistryObject<BlockEntityType<OnionShelfEntity>> ONION_SHELF = TILE_ENTITY.register(
			"onion_shelf",
			() -> BlockEntityType.Builder.of(OnionShelfEntity::new, BlockInit.ONION_SHELF.get()).build(null));

	public static final RegistryObject<BlockEntityType<TofuPressEntity>> TOFU_PRESS = TILE_ENTITY.register("tofu_press",
			() -> BlockEntityType.Builder.of(TofuPressEntity::new, BlockInit.TOFU_PRESS.get()).build(null));

	public static final RegistryObject<BlockEntityType<TofuPressEntity2>> TOFU_PRESS2 = TILE_ENTITY.register(
			"tofu_press2",
			() -> BlockEntityType.Builder.of(TofuPressEntity2::new, BlockInit.TOFU_PRESS2.get()).build(null));

	public static final RegistryObject<BlockEntityType<TofuPressEntity3>> TOFU_PRESS3 = TILE_ENTITY.register(
			"tofu_press3",
			() -> BlockEntityType.Builder.of(TofuPressEntity3::new, BlockInit.TOFU_PRESS3.get()).build(null));

	public static final RegistryObject<BlockEntityType<TofuPressEntityC>> TOFU_PRESSC = TILE_ENTITY.register(
			"tofu_pressc",
			() -> BlockEntityType.Builder.of(TofuPressEntityC::new, BlockInit.TOFU_PRESSC.get()).build(null));

	public static final RegistryObject<BlockEntityType<CultivatorEntity>> CULTIVATOR = TILE_ENTITY.register(
			"cultivator",
			() -> BlockEntityType.Builder.of(CultivatorEntity::new, BlockInit.CULTIVATOR.get()).build(null));

	public static final RegistryObject<BlockEntityType<OrganicGeneratorEntity>> ORGANIC_GENERATOR = TILE_ENTITY
			.register("organic_generator", () -> BlockEntityType.Builder
					.of(OrganicGeneratorEntity::new, BlockInit.ORGANIC_GENERATOR.get()).build(null));

	public static void register(IEventBus eventBus) {
		TILE_ENTITY.register(eventBus);
	}

}
