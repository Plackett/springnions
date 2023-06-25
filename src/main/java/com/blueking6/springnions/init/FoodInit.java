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

import net.minecraft.world.food.FoodProperties;

public class FoodInit {
	public static final FoodProperties ONION = (new FoodProperties.Builder()).nutrition(4).saturationMod(0.6f).build();
	public static final FoodProperties GOLDEN_ONION = (new FoodProperties.Builder()).nutrition(7).saturationMod(1.2f)
			.build();
	public static final FoodProperties ONION_STEW = (new FoodProperties.Builder().nutrition(10).saturationMod(0.9f)
			.build());
	public static final FoodProperties TOFU = (new FoodProperties.Builder()).nutrition(5).saturationMod(0.3F).build();
	public static final FoodProperties GRILLED_TOFU = (new FoodProperties.Builder()).nutrition(7).saturationMod(0.6F)
			.build();
}
