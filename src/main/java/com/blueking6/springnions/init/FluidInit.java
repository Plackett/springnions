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

import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;

import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer.FogMode;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import com.blueking6.springnions.springnions;
import com.mojang.blaze3d.shaders.FogShape;

import java.util.function.Consumer;

public class FluidInit {

	public static final ResourceLocation FLUID_STILL = new ResourceLocation("springnions:block/soymilk_still");
	public static final ResourceLocation FLUID_FLOWING = new ResourceLocation("springnions:block/soymilk_flow");
	public static final ResourceLocation FLUID_OVERLAY = new ResourceLocation("minecraft:block/water_overlay");

	public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister
			.create(ForgeRegistries.Keys.FLUID_TYPES, springnions.MOD_ID);
	public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS,
			springnions.MOD_ID);

	private static ForgeFlowingFluid.Properties makeProperties() {
		return new ForgeFlowingFluid.Properties(SOYMILK_TYPE, SOYMILK, SOYMILK_FLOWING).bucket(ItemInit.SOYMILK_BUCKET)
				.block(BlockInit.SOYMILK_BLOCK);
	}

	public static RegistryObject<FluidType> SOYMILK_TYPE = FLUID_TYPES.register("soymilk",
			() -> new FluidType(FluidType.Properties.create().supportsBoating(true).canSwim(true)
					.canConvertToSource(false).temperature(200)) {
				@Override
				public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
					consumer.accept(new IClientFluidTypeExtensions() {
						@Override
						public ResourceLocation getStillTexture() {
							return FLUID_STILL;
						}

						@Override
						public ResourceLocation getFlowingTexture() {
							return FLUID_FLOWING;
						}

						@Nullable
						@Override
						public ResourceLocation getOverlayTexture() {
							return FLUID_OVERLAY;
						}

						@Override
						public int getTintColor() {
							return 0xFFD5D2C7;
						}

						@Override
						public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level,
								int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
							return IClientFluidTypeExtensions.super.modifyFogColor(camera, partialTick, level,
									renderDistance, darkenWorldAmount, new Vector3f(159, 213, 85));
						}

						@Override
						public void modifyFogRender(Camera camera, FogMode mode, float renderDistance,
								float partialTick, float nearDistance, float farDistance, FogShape shape) {
							IClientFluidTypeExtensions.super.modifyFogRender(camera, FogMode.FOG_SKY, renderDistance,
									partialTick, nearDistance, farDistance, FogShape.SPHERE);
						}

					});
				}
			});

	public static RegistryObject<FlowingFluid> SOYMILK = FLUIDS.register("soymilk",
			() -> new ForgeFlowingFluid.Source(makeProperties()));
	public static RegistryObject<FlowingFluid> SOYMILK_FLOWING = FLUIDS.register("soymilk_flowing",
			() -> new ForgeFlowingFluid.Flowing(makeProperties()));

	public FluidInit() {

	}

	public static void register(IEventBus eventBus) {
		FLUIDS.register(eventBus);
		FLUID_TYPES.register(eventBus);
	}

}
