package com.blueking6.springnions.gui;

import com.blueking6.springnions.springnions;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.Builder;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.gui.widget.ExtendedButton;
import net.minecraft.resources.ResourceLocation;

public class CultivatorScreen extends AbstractContainerScreen<CultivatorMenu> {

	private final ResourceLocation GUI = new ResourceLocation(springnions.MOD_ID, "textures/gui/cultivator.png");
	private GuiGraphics graphics;
	private ExtendedButton powerlabel;
	public int relX = (this.width - (this.imageWidth + 6)) / 2;
	public int relY = (this.height - this.imageHeight) / 2;

	public CultivatorScreen(CultivatorMenu menu, Inventory inv, Component comp) {
		super(menu, inv, comp);
		this.inventoryLabelY = this.imageHeight - 107;
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float pTicks, int X, int Y) {
		this.graphics = graphics;
		graphics.blit(GUI, relX, relY, 0, 0, this.imageWidth + 6, this.imageHeight);
	}

	@Override
	protected void init() {
		// add invisible button above the power meter at (6,23) relative to the
		// background that displays the current power when you hover over it
		this.powerlabel = addRenderableWidget(
				new ExtendedButton(relX + 6, relY + 23, 18, 42, Component.literal("Power Label"), click -> {
					// will replace the first number with the current power once I figure out how to
					// access block entity data from screen
					this.graphics.renderTooltip(this.font,
							Component.literal(ChatFormatting.AQUA + "Power: " + ChatFormatting.DARK_GREEN + "0 "
									+ ChatFormatting.AQUA + "/ " + ChatFormatting.DARK_GREEN + "256"),
							relX + 6, relY + 23);
				}));
	}

}
