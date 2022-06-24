package com.blueking6.springnions.init;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.blueking6.springnions.springnions;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.IFluidTypeRenderProperties;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FluidInit {
	
	public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, springnions.MOD_ID);
	
	public static final ResourceLocation FLUID_STILL = new ResourceLocation("minecraft:block/water_still");
    public static final ResourceLocation FLUID_FLOWING = new ResourceLocation("minecraft:block/water_flow");
    public static final ResourceLocation FLUID_OVERLAY = new ResourceLocation("minecraft:block/water_overlay");
	
    private static ForgeFlowingFluid.Properties makeProperties()
    {
        return new ForgeFlowingFluid.Properties(SOYMILK_TYPE, SOYMILK, SOYMILK_FLOWING)
                .bucket(ItemInit.SOYMILK_BUCKET).block(BlockInit.SOYMILK_BLOCK);
    }
	
    public static RegistryObject<FluidType> SOYMILK_TYPE = FLUID_TYPES.register("test_fluid", () -> new FluidType(FluidType.Properties.create())
    {
        @Override
        public void initializeClient(Consumer<IFluidTypeRenderProperties> consumer)
        {
            consumer.accept(new IFluidTypeRenderProperties()
            {
                @Override
                public ResourceLocation getStillTexture()
                {
                    return FLUID_STILL;
                }

                @Override
                public ResourceLocation getFlowingTexture()
                {
                    return FLUID_FLOWING;
                }

                @Nullable
                @Override
                public ResourceLocation getOverlayTexture()
                {
                    return FLUID_OVERLAY;
                }

                @Override
                public int getColorTint()
                {
                    return 0x3F1080FF;
                }
            });
        }
    });
    
    public static final DeferredRegister<Fluid> FLUIDS
            = DeferredRegister.create(ForgeRegistries.FLUIDS, springnions.MOD_ID);
    
    public static final RegistryObject<FlowingFluid> SOYMILK = FLUIDS.register("soymilk", () -> new ForgeFlowingFluid.Source(makeProperties()));
    public static final RegistryObject<FlowingFluid> SOYMILK_FLOWING = FLUIDS.register("soymilk_flowing", () -> new ForgeFlowingFluid.Flowing(makeProperties()));

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
