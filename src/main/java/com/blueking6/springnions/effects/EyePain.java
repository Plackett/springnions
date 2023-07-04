package com.blueking6.springnions.effects;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class EyePain extends MobEffect {

	public EyePain(MobEffectCategory category, int color) {
		super(category, color);
	}

	public static boolean shaderEnabled = false;

	@SuppressWarnings("resource")
	@Override
	public void removeAttributeModifiers(LivingEntity entity, AttributeMap p_19470_, int p_19471_) {
		if(entity.level().isClientSide == true) {
			removeShaders();
		}
		super.removeAttributeModifiers(entity, p_19470_, p_19471_);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amp) {
		if (shaderEnabled == false && entity.level().isClientSide == true) {
			applyShader(new ResourceLocation("springnions:eye_pain"));
		}
		super.applyEffectTick(entity, amp);
	}

	@SuppressWarnings("resource")
	public static void applyShader(ResourceLocation location) {
		if (Minecraft.getInstance().gameRenderer.currentEffect() == null) {
			ResourceLocation shader = location.withPrefix("shaders/post/").withSuffix(".json");
			Minecraft.getInstance().gameRenderer.loadEffect(shader);
			shaderEnabled = true;
		}
	}

	@SuppressWarnings("resource")
	public static void removeShaders() {
		Minecraft.getInstance().gameRenderer.shutdownEffect();
		shaderEnabled = false;
	}

	@Override
	public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
		return true;
	}

}
