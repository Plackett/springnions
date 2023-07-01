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

	@Override
	public void removeAttributeModifiers(LivingEntity p_19469_, AttributeMap p_19470_, int p_19471_) {
		removeShaders();
		super.removeAttributeModifiers(p_19469_, p_19470_, p_19471_);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amp) {
		applyShader(new ResourceLocation("springnions:eye_pain"));
		super.applyEffectTick(entity, amp);
	}

	@SuppressWarnings("resource")
	public static void applyShader(ResourceLocation location) {
		if (Minecraft.getInstance().gameRenderer.currentEffect() == null) {
			ResourceLocation shader = location.withPrefix("shaders/post/").withSuffix(".json");
			Minecraft.getInstance().gameRenderer.loadEffect(shader);
		}
	}

	@SuppressWarnings("resource")
	public static void removeShaders() {
		if (Minecraft.getInstance().gameRenderer.currentEffect() != null) {
			Minecraft.getInstance().gameRenderer.shutdownEffect();
		}
	}

	@Override
	public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
		return true;
	}

	@Override
	public boolean isInstantenous() {
		return true;
	}

}
