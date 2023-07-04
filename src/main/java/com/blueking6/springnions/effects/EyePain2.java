package com.blueking6.springnions.effects;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class EyePain2 extends MobEffect {

	public EyePain2(MobEffectCategory category, int color) {
		super(category, color);
	}

	@SuppressWarnings("resource")
	@Override
	public void removeAttributeModifiers(LivingEntity entity, AttributeMap p_19470_, int p_19471_) {
		if (entity.level().isClientSide == true) {
			EyePain.removeShaders();
		}
		super.removeAttributeModifiers(entity, p_19470_, p_19471_);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amp) {
		if (EyePain.shaderEnabled == false && entity.level().isClientSide) {
			EyePain.applyShader(new ResourceLocation("springnions:eye_pain_ii"));
		}
		super.applyEffectTick(entity, amp);
	}

	@Override
	public boolean isDurationEffectTick(int time, int amp) {
		return true;
	}

}
