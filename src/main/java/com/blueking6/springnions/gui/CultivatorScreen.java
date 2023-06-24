package com.blueking6.springnions.gui;

import com.blueking6.springnions.springnions;
import com.blueking6.tools.HoverArea;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.gui.widget.ExtendedButton;
import net.minecraft.resources.ResourceLocation;

public class CultivatorScreen extends AbstractContainerScreen<CultivatorMenu> {

	private final ResourceLocation GUI = new ResourceLocation(springnions.MOD_ID, "textures/gui/cultivator.png");
	private ExtendedButton powerlabel;

	public CultivatorScreen(CultivatorMenu menu, Inventory inv, Component comp) {
		super(menu, inv, comp);
		this.inventoryLabelY = this.imageHeight - 107;
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float pTicks, int X, int Y) {
		int relX = (this.width - (this.imageWidth + 6)) / 2;
		int relY = (this.height - this.imageHeight) / 2;
		graphics.blit(GUI, relX, relY, 0, 0, this.imageWidth + 6, this.imageHeight);
		int k = this.menu.getEntity().litTime;
		if (k > 0) {
			graphics.blit(GUI, relX + 200, relY + 27 + 16 - k, 215, 16 - k, 16, k + 1);
		}
		int l = this.menu.getEntity().getEnergy().getEnergyStored();
		int lratio = (int) (this.menu.getEntity().getEnergy().getEnergyStored()
				/ this.menu.getEntity().getEnergy().getMaxEnergyStored());
		if (l > 0) {
			graphics.blit(GUI, relX + 183, relY + 16 + (40 * lratio), 175, (40 * lratio), 40, (40 * lratio) + 1);
			graphics.blit(GUI, relX + 201, relY + 43, 243, 90, 43, 48);
		}
		this.powerlabel = addRenderableWidget(
				new HoverArea(relX + 8, relY + 15, 18, 42, Component.literal(""), click -> {
				}));
		powerlabel.setTooltip(Tooltip.create(
				Component.literal(ChatFormatting.GOLD + "Power: " + ChatFormatting.DARK_GREEN + l + ChatFormatting.AQUA
						+ " / " + ChatFormatting.DARK_GREEN + "256 " + ChatFormatting.GOLD + "RF"),
				Component.literal("")));
	}

	@Override
	protected void containerTick() {
		powerlabel
				.setTooltip(Tooltip.create(
						Component.literal(ChatFormatting.GOLD + "Power: " + ChatFormatting.DARK_GREEN
								+ this.menu.getEntity().getEnergy().getEnergyStored() + ChatFormatting.AQUA + " / "
								+ ChatFormatting.DARK_GREEN + "256 " + ChatFormatting.GOLD + "RF"),
						Component.literal("")));
		super.containerTick();
	}

}
