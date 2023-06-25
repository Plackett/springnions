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

import java.util.ArrayList;
import java.util.List;

import org.joml.Math;

import com.blueking6.springnions.springnions;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class OrganicGeneratorScreen extends AbstractContainerScreen<OrganicGeneratorMenu> {

	private final ResourceLocation GUI = new ResourceLocation(springnions.MOD_ID, "textures/gui/organic_generator.png");

	public OrganicGeneratorScreen(OrganicGeneratorMenu menu, Inventory inv, Component comp) {
		super(menu, inv, comp);
		this.inventoryLabelY = this.imageHeight - 107;
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float pTicks, int X, int Y) {
		int relX = (this.width - (this.imageWidth + 6)) / 2;
		int relY = (this.height - this.imageHeight) / 2;
		graphics.blit(GUI, relX, relY, 0, 0, this.imageWidth + 6, this.imageHeight);
		int k = this.menu.getData(0);
		k = Math.round(k * (16 / 200F));
		graphics.blit(GUI, getGuiLeft() + 80, getGuiTop() + 22, 183, 57, 16, 16 - k);
		this.menu.getEntityy().getCapability(ForgeCapabilities.ENERGY).ifPresent(handler -> {
			int energyAmount = handler.getEnergyStored();
			int energyLevel = Math.round(energyAmount * (40 / 8192F));
			graphics.blit(GUI, getGuiLeft() + 57, getGuiTop() + 17, 182, 16, 18, 40 - energyLevel);
		});
	}

	@Override
	protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
		this.menu.getEntityy().getCapability(ForgeCapabilities.ENERGY).ifPresent(handler -> {
			int energyAmount = handler.getEnergyStored();

			if (isHovering(57, 16, 18, 42, mouseX, mouseY)) {
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
		super.renderLabels(graphics, mouseX, mouseY);
	}

}
