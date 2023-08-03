package com.blueking6.tools;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.widget.ExtendedButton;

public class HoverArea extends ExtendedButton{

	public HoverArea(int xPos, int yPos, int width, int height, Component displayString, OnPress handler) {
		super(xPos, yPos, width, height, displayString, handler);
	}

	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		
	}

}
