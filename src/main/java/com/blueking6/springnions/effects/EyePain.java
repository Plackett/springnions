package com.blueking6.springnions.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;

public class EyePain extends MobEffect {

	public EyePain(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public void removeAttributeModifiers(LivingEntity entity, AttributeMap map, int i) {
		super.removeAttributeModifiers(entity, map, i);
	}

	@Override
	public void addAttributeModifiers(LivingEntity entity, AttributeMap map, int i) {
		if(entity instanceof Player player && entity.level().isClientSide) {
		}
		super.addAttributeModifiers(entity, map, i);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amp) {
		super.applyEffectTick(entity, amp);
	}

	@Override
	public boolean isDurationEffectTick(int time, int amp) {
		return super.isDurationEffectTick(time, amp);
	}

}
