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
		if (EyePain.shaderEnabled == false && entity.level().isClientSide == true) {
			applyShader(new ResourceLocation("springnions:eye_pain"));
		}
		super.applyEffectTick(entity, amp);
	}

	@SuppressWarnings("resource")
	public static void applyShader(ResourceLocation location) {
		if (Minecraft.getInstance().gameRenderer.currentEffect() == null) {
			ResourceLocation shader = location.withPrefix("shaders/post/").withSuffix(".json");
			Minecraft.getInstance().gameRenderer.loadEffect(shader);
			EyePain.shaderEnabled = true;
		}
	}

	@SuppressWarnings("resource")
	public static void removeShaders() {
		Minecraft.getInstance().gameRenderer.shutdownEffect();
		EyePain.shaderEnabled = false;
	}

	@Override
	public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
		return true;
	}

}
