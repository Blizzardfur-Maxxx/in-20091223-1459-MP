package net.minecraft.client.gui;

import net.minecraft.client.RenderHelper;
import net.minecraft.client.Session;
import net.minecraft.client.player.InventoryPlayer;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.level.block.Block;

import org.lwjgl.opengl.GL11;

public final class GuiCreativeInventory extends GuiScreen {
	private RenderBlocks blockRenderer = new RenderBlocks(Tessellator.instance);

	public GuiCreativeInventory() {
		this.allowUserInput = true;
	}

	private int getIsMouseOverSlot(int i1, int i2) {
		for(int i3 = 0; i3 < Session.allowedBlocks.size(); ++i3) {
			int i4 = this.width / 2 + i3 % 9 * 24 + -108 - 3;
			int i5 = this.height / 2 + i3 / 9 * 24 + -60 + 3;
			if(i1 >= i4 && i1 <= i4 + 24 && i2 >= i5 - 12 && i2 <= i5 + 12) {
				return i3;
			}
		}

		return -1;
	}

	public final void drawScreen(int i1, int i2) {
		i1 = this.getIsMouseOverSlot(i1, i2);
		drawGradientRect(this.width / 2 - 120, 30, this.width / 2 + 120, 180, -1878719232, -1070583712);
		int i3;
		if(i1 >= 0) {
			i2 = this.width / 2 + i1 % 9 * 24 + -108;
			i3 = this.height / 2 + i1 / 9 * 24 + -60;
			drawGradientRect(i2 - 3, i3 - 8, i2 + 23, i3 + 24 - 6, -1862270977, -1056964609);
		}

		drawCenteredString(this.fontRenderer, "Select block", this.width / 2, 40, 0xFFFFFF);
		Object object6 = null;
		i3 = this.mc.renderEngine.getTexture("/terrain.png");
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, i3);
		GL11.glPushMatrix();
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_NORMALIZE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		for(i2 = 0; i2 < Session.allowedBlocks.size(); ++i2) {
			Block block7 = (Block)Session.allowedBlocks.get(i2);
			GL11.glPushMatrix();
			int i4 = this.width / 2 + i2 % 9 * 24 + -108;
			int i5 = this.height / 2 + i2 / 9 * 24 + -60;
			GL11.glTranslatef((float)i4, (float)i5, 0.0F);
			GL11.glScalef(10.0F, 10.0F, 10.0F);
			GL11.glTranslatef(1.0F, 0.5F, 8.0F);
			GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			if(i1 == i2) {
				GL11.glScalef(1.6F, 1.6F, 1.6F);
			}

			this.blockRenderer.renderBlockOnInventory(block7);
			GL11.glPopMatrix();
		}

		GL11.glDisable(GL11.GL_NORMALIZE);
		RenderHelper.disableStandardItemLighting();
	}

	protected final void mouseClicked(int i1, int i2, int i3) {
		if(i3 == 0) {
			InventoryPlayer inventoryPlayer10000 = this.mc.thePlayer.inventory;
			i2 = this.getIsMouseOverSlot(i1, i2);
			InventoryPlayer inventoryPlayer4 = inventoryPlayer10000;
			if(i2 >= 0) {
				inventoryPlayer4.replaceSlot((Block)Session.allowedBlocks.get(i2));
			}

			this.mc.displayGuiScreen((GuiScreen)null);
		}

	}
}