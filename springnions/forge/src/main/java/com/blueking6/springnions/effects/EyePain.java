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

package com.blueking6.springnions.effects;

import org.joml.Math;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class EyePain extends MobEffect {

	public EyePain(MobEffectCategory category, int color) {
		super(category, color);
	}

	@SuppressWarnings("resource")
	@Override
	public void applyEffectTick(LivingEntity entity, int amp) {
		// tears drip from eyes
		if (entity.level().isClientSide && Math.random() >= 0.9) {
			Vec3 tearpos = entity.getEyePosition().add(entity.getViewVector(1).scale(0.2F));
			entity.level().addParticle(ParticleTypes.DRIPPING_WATER, tearpos.x, tearpos.y - 0.2, tearpos.z, 0, -1, 0);
		}
		super.applyEffectTick(entity, amp);
	}

	@Override
	public boolean isDurationEffectTick(int time, int amp) {
		return true;
	}

}
