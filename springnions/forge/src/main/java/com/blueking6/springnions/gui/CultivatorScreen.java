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

package com.blueking6.springnions.gui;

import com.blueking6.config.SpringnionsCommonConfigs;
import com.blueking6.springnions.springnions;
import net.minecraft.ChatFormatting;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraft.resources.ResourceLocation;
import java.util.ArrayList;
import java.util.List;

import org.joml.Math;

public class CultivatorScreen extends AbstractContainerScreen<CultivatorMenu> {

	private final ResourceLocation GUI = new ResourceLocation(springnions.MOD_ID, "textures/gui/cultivator.png");

	public CultivatorScreen(CultivatorMenu menu, Inventory inv, Component comp) {
		super(menu, inv, comp);
		this.inventoryLabelY = this.imageHeight - 107;
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float pTicks, int X, int Y) {
		int relX = (this.width - (this.imageWidth + 6)) / 2;
		int relY = (this.height - this.imageHeight) / 2;
		graphics.blit(GUI, relX, relY, 0, 0, this.imageWidth + 6, this.imageHeight);
		// only render the fire if it can generate power
		if (SpringnionsCommonConfigs.CULTIVATOR_RATE.get() > 0) {
			int k = this.menu.getData(0);
			k = Math.round(k * (16 / 200F));
			graphics.blit(GUI, getGuiLeft() + 26, getGuiTop() + 22, 183, 57, 16, 16 - k);
		} else {
			graphics.blit(GUI, getGuiLeft() + 26, getGuiTop() + 22, 183, 74, 16, 16);
			graphics.blit(GUI, getGuiLeft() + 25, getGuiTop() + 38, 182, 73, 18, 18);
		}
		// only render the power meter if it can store power
		if (SpringnionsCommonConfigs.CULTIVATOR_CAPACITY.get() > 0) {
			this.menu.getEntity().getCapability(ForgeCapabilities.ENERGY).ifPresent(handler -> {
				int energyAmount = handler.getEnergyStored();
				int energyLevel = Math.round(energyAmount * (40 / 1024F));
				graphics.blit(GUI, getGuiLeft() + 5, getGuiTop() + 16, 182, 16, 18, 40 - energyLevel);
			});
		} else {
			graphics.blit(GUI, getGuiLeft() + 5, getGuiTop() + 15, 200, 64, 18, 42);
		}
		int energyBuffer = this.menu.getData(1);
		int energyBufferLevel = Math.round(energyBuffer * (48 / 256F));
		graphics.blit(GUI, getGuiLeft() + 53, getGuiTop() + 16, 207, 16, 43, 48 - energyBufferLevel);
	}

	@Override
	protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
		// only render power meter tooltip if it can store power
		if (SpringnionsCommonConfigs.CULTIVATOR_CAPACITY.get() > 0) {
			this.menu.getEntity().getCapability(ForgeCapabilities.ENERGY).ifPresent(handler -> {
				int energyAmount = handler.getEnergyStored();

				if (isHovering(8, 15, 18, 42, mouseX, mouseY)) {
					List<FormattedCharSequence> tooltipList = new ArrayList<>();
					tooltipList
							.add(Component
									.literal(ChatFormatting.GOLD + "Power: " + ChatFormatting.DARK_GREEN + energyAmount
											+ ChatFormatting.AQUA + " / " + ChatFormatting.DARK_GREEN
											+ handler.getMaxEnergyStored() + ChatFormatting.GOLD + " FE")
									.getVisualOrderText());

					graphics.renderTooltip(this.font, tooltipList, mouseX - 165, mouseY - 20);
				}
			});
		}
		super.renderLabels(graphics, mouseX, mouseY);
	}

	public void render(GuiGraphics graphics, int mouseX, int mouseY, float ticks) {
		this.renderBackground(graphics);
		super.render(graphics, mouseX, mouseY, ticks);
		this.renderTooltip(graphics, mouseX, mouseY);
	}

}
