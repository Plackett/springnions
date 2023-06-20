package com.blueking6.springnions.gui;

import com.blueking6.springnions.springnions;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;

public class CultivatorScreen extends AbstractContainerScreen<CultivatorMenu> {

	private final ResourceLocation GUI = new ResourceLocation(springnions.MOD_ID, "textures/gui/cultivator.png");

	public CultivatorScreen(CultivatorMenu menu, Inventory inv, Component comp) {
		super(menu, inv, comp);
		this.inventoryLabelY = this.imageHeight - 110;
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float pTicks, int X, int Y) {
		int relX = (this.width - this.imageWidth) / 2;
		int relY = (this.height - this.imageHeight) / 2;
		graphics.blit(GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
	}

}
