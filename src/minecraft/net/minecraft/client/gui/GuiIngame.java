package net.minecraft.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.ChatLine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.controller.PlayerControllerSP;
import net.minecraft.client.player.InventoryPlayer;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.level.block.Block;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import util.MathHelper;

public final class GuiIngame extends Gui {
	public List chatMessageList = new ArrayList();
	private Random rand = new Random();
	private Minecraft mc;
	private int ingameWidth;
	private int ingameHeight;
	public int updateCounter = 0;
	private RenderBlocks blockRenderer = new RenderBlocks(Tessellator.instance);
	public String testMessage = null;
	public String hoveredUsername = null;

	public GuiIngame(Minecraft minecraft1, int i2, int i3) {
		this.mc = minecraft1;
		this.ingameWidth = i2 * 240 / i3;
		this.ingameHeight = i3 * 240 / i3;
	}

	public final void renderGameOverlay(float f1, boolean playerAlive, int w, int h) {
		FontRenderer fontRenderer2 = this.mc.fontRenderer;
		this.mc.entityRenderer.setupOverlayRendering();
		RenderEngine renderEngine3 = this.mc.renderEngine;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/gui.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		InventoryPlayer inventoryPlayer4 = this.mc.thePlayer.inventory;
		this.zLevel = -90.0F;
		this.drawTexturedModal(this.ingameWidth / 2 - 91, this.ingameHeight - 22, 0, 0, 182, 22);
		this.drawTexturedModal(this.ingameWidth / 2 - 91 - 1 + inventoryPlayer4.currentItem * 20, this.ingameHeight - 22 - 1, 0, 22, 24, 22);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/icons.png"));
		this.drawTexturedModal(this.ingameWidth / 2 - 7, this.ingameHeight / 2 - 7, 0, 0, 16, 16);
		boolean z5 = this.mc.thePlayer.scoreValue / 3 % 2 == 1;
		if(this.mc.thePlayer.scoreValue < 10) {
			z5 = false;
		}

		int i6 = this.mc.thePlayer.health;
		int i7 = this.mc.thePlayer.prevHealth;
		this.rand.setSeed((long)(this.updateCounter * 312871));
		int i8;
		int i10;
		int i11;
		int i18;
		if(this.mc.playerController.shouldDrawHUD()) {
			for(i8 = 0; i8 < 10; ++i8) {
				byte b9 = 0;
				if(z5) {
					b9 = 1;
				}

				i10 = this.ingameWidth / 2 - 91 + (i8 << 3);
				i11 = this.ingameHeight - 32;
				if(i6 <= 4) {
					i11 += this.rand.nextInt(2);
				}

				this.drawTexturedModal(i10, i11, 16 + b9 * 9, 0, 9, 9);
				if(z5) {
					if((i8 << 1) + 1 < i7) {
						this.drawTexturedModal(i10, i11, 70, 0, 9, 9);
					}

					if((i8 << 1) + 1 == i7) {
						this.drawTexturedModal(i10, i11, 79, 0, 9, 9);
					}
				}

				if((i8 << 1) + 1 < i6) {
					this.drawTexturedModal(i10, i11, 52, 0, 9, 9);
				}

				if((i8 << 1) + 1 == i6) {
					this.drawTexturedModal(i10, i11, 61, 0, 9, 9);
				}
			}

			if(this.mc.thePlayer.isInsideOfMaterial()) {
				i8 = (int)Math.ceil((double)(this.mc.thePlayer.air - 2) * 10.0D / 300.0D);
				i18 = (int)Math.ceil((double)this.mc.thePlayer.air * 10.0D / 300.0D) - i8;

				for(i10 = 0; i10 < i8 + i18; ++i10) {
					if(i10 < i8) {
						this.drawTexturedModal(this.ingameWidth / 2 - 91 + (i10 << 3), this.ingameHeight - 32 - 9, 16, 18, 9, 9);
					} else {
						this.drawTexturedModal(this.ingameWidth / 2 - 91 + (i10 << 3), this.ingameHeight - 32 - 9, 25, 18, 9, 9);
					}
				}
			}
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_NORMALIZE);
		GL11.glPushMatrix();
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
		String string21;

		for(i8 = 0; i8 < inventoryPlayer4.mainInventory.length; ++i8) {
			i18 = this.ingameWidth / 2 - 90 + i8 * 20;
			i10 = this.ingameHeight - 16;
			if((i11 = inventoryPlayer4.mainInventory[i8]) > 0) {
				GL11.glPushMatrix();
				GL11.glTranslatef((float)i18, (float)i10, -50.0F);
				if(inventoryPlayer4.animationsToGo[i8] > 0) {
					float f12;
					float f10000 = f12 = ((float)inventoryPlayer4.animationsToGo[i8] - f1) / 5.0F;
					float f13 = -MathHelper.sin(f10000 * f10000 * (float)Math.PI) * 8.0F;
					float f16 = MathHelper.sin(f12 * f12 * (float)Math.PI) + 1.0F;
					f12 = MathHelper.sin(f12 * (float)Math.PI) + 1.0F;
					GL11.glTranslatef(10.0F, f13 + 10.0F, 0.0F);
					GL11.glScalef(f16, f12, 1.0F);
					GL11.glTranslatef(-10.0F, -10.0F, 0.0F);
				}

				GL11.glScalef(10.0F, 10.0F, 10.0F);
				GL11.glTranslatef(1.0F, 0.5F, 0.0F);
				GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				int i14 = renderEngine3.getTexture("/terrain.png");
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, i14);
				this.blockRenderer.renderBlockOnInventory(Block.blocksList[i11]);
				GL11.glPopMatrix();
				if(inventoryPlayer4.stackSize[i8] > 1) {
					String string15 = "" + inventoryPlayer4.stackSize[i8];
					fontRenderer2.drawStringWithShadow(string15, i18 + 19 - fontRenderer2.getWidth(string15), i10 + 6, 0xFFFFFF);
				}
			}
		}

		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_NORMALIZE);
		fontRenderer2.drawStringWithShadow("0.31", 2, 2, 0xFFFFFF);
		if(this.mc.options.showFPS) {
			fontRenderer2.drawStringWithShadow(this.mc.debug, 2, 12, 0xFFFFFF);
		}
		
	    if (this.mc.isOnlineClient()) {
	    	fontRenderer2.drawStringWithShadow("MP", fontRenderer2.getWidth("0.31") + 3, 2, 0xFF0000);
	    }


		if(this.mc.playerController instanceof PlayerControllerSP) {
			String string17 = "Score: &e" + this.mc.thePlayer.getScore();
			fontRenderer2.drawStringWithShadow(string17, this.ingameWidth - fontRenderer2.getWidth(string17) - 2, 2, 0xFFFFFF);
			fontRenderer2.drawStringWithShadow("Arrows: " + this.mc.thePlayer.getArrows, this.ingameWidth / 2 + 8, this.ingameHeight - 33, 0xFFFFFF);
		}
		byte b26 = 10;
		boolean z27 = false;
		if(this.mc.currentScreen instanceof ChatScreen) {
			b26 = 20;
			z27 = true;
		}
		for(i10 = 0; i10 < this.chatMessageList.size() && i10 < b26; ++i10) {
			if(((GuiMessage)this.chatMessageList.get(i10)).counter < 200 || z27) {
				fontRenderer2.drawStringWithShadow(((GuiMessage)this.chatMessageList.get(i10)).message, 2, this.ingameHeight - 8 - i10 * 9 - 20, 0xFFFFFF);
			}
		}
		i18 = this.ingameWidth / 2;
		i10 = this.ingameHeight / 2;
		this.hoveredUsername = null;
		if(Keyboard.isKeyDown(Keyboard.KEY_TAB) && this.mc.networkClient != null && this.mc.networkClient.isConnected()) {
			List list22 = this.mc.networkClient.getUsernames();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.7F);
			GL11.glVertex2f((float)(i18 + 128), (float)(i10 - 68 - 12));
			GL11.glVertex2f((float)(i18 - 128), (float)(i10 - 68 - 12));
			GL11.glColor4f(0.2F, 0.2F, 0.2F, 0.8F);
			GL11.glVertex2f((float)(i18 - 128), (float)(i10 + 68));
			GL11.glVertex2f((float)(i18 + 128), (float)(i10 + 68));
			GL11.glEnd();
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			string21 = "Connected players:";
			fontRenderer2.drawStringWithShadow(string21, i18 - fontRenderer2.getWidth(string21) / 2, i10 - 64 - 12, 0xFFFFFF);

			for(i11 = 0; i11 < list22.size(); ++i11) {
				int i28 = i18 + i11 % 2 * 120 - 120;
				int i17 = i10 - 64 + (i11 / 2 << 3);
				if(playerAlive && w >= i28 && h >= i17 && w < i28 + 120 && h < i17 + 8) {
					this.hoveredUsername = (String)list22.get(i11);
					fontRenderer2.drawString((String)list22.get(i11), i28 + 2, i17, 0xFFFFFF);
				} else {
					fontRenderer2.drawString((String)list22.get(i11), i28, i17, 15658734);
				}
			}
		}

	}
	public final void addMessage(String message) {
		this.chatMessageList.add(0, new GuiMessage(message));

		while(this.chatMessageList.size() > 50) {
			this.chatMessageList.remove(this.chatMessageList.size() - 1);
		}

	}
}