package net.minecraft.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiScreen extends Gui {
	protected Minecraft mc;
	protected int width;
	protected int height;
	protected List controlList = new ArrayList();
	public boolean allowUserInput = false;
	protected FontRenderer fontRenderer;

	public void drawScreen(int i1, int i2) {
		for(int i3 = 0; i3 < this.controlList.size(); ++i3) {
			GuiButton guiButton10000 = (GuiButton)this.controlList.get(i3);
			GuiButton guiButton4 = null;
			Minecraft minecraft5 = this.mc;
			guiButton4 = guiButton10000;
			if(guiButton10000.visible) {
				FontRenderer fontRenderer8 = minecraft5.fontRenderer;
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft5.renderEngine.getTexture("/gui/gui.png"));
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				byte b9 = 1;
				boolean z6 = i1 >= guiButton4.x && i2 >= guiButton4.y && i1 < guiButton4.x + guiButton4.width && i2 < guiButton4.y + guiButton4.height;
				if(!guiButton4.enabled) {
					b9 = 0;
				} else if(z6) {
					b9 = 2;
				}

				guiButton4.drawTexturedModal(guiButton4.x, guiButton4.y, 0, 46 + b9 * 20, guiButton4.width / 2, guiButton4.height);
				guiButton4.drawTexturedModal(guiButton4.x + guiButton4.width / 2, guiButton4.y, 200 - guiButton4.width / 2, 46 + b9 * 20, guiButton4.width / 2, guiButton4.height);
				if(!guiButton4.enabled) {
					GuiButton.drawCenteredString(fontRenderer8, guiButton4.displayString, guiButton4.x + guiButton4.width / 2, guiButton4.y + (guiButton4.height - 8) / 2, -6250336);
				} else if(z6) {
					GuiButton.drawCenteredString(fontRenderer8, guiButton4.displayString, guiButton4.x + guiButton4.width / 2, guiButton4.y + (guiButton4.height - 8) / 2, 16777120);
				} else {
					GuiButton.drawCenteredString(fontRenderer8, guiButton4.displayString, guiButton4.x + guiButton4.width / 2, guiButton4.y + (guiButton4.height - 8) / 2, 14737632);
				}
			}
		}

	}

	protected void keyTyped(char c1, int i2) {
		if(i2 == 1) {
			this.mc.displayGuiScreen((GuiScreen)null);
			this.mc.setIngameFocus();
		}

	}

	protected void mouseClicked(int i1, int i2, int i3) {
		if(i3 == 0) {
			for(i3 = 0; i3 < this.controlList.size(); ++i3) {
				GuiButton guiButton4;
				GuiButton guiButton5;
				if((guiButton5 = guiButton4 = (GuiButton)this.controlList.get(i3)).enabled && i1 >= guiButton5.x && i2 >= guiButton5.y && i1 < guiButton5.x + guiButton5.width && i2 < guiButton5.y + guiButton5.height) {
					this.actionPerformed(guiButton4);
				}
			}
		}

	}

	protected void actionPerformed(GuiButton guiButton1) {
	}

	public final void setWorldAndResolution(Minecraft minecraft1, int i2, int i3) {
		this.mc = minecraft1;
		this.fontRenderer = minecraft1.fontRenderer;
		this.width = i2;
		this.height = i3;
		this.initGui();
	}

	public void initGui() {
	}

	public final void handleMouseInput() {
		if(Mouse.getEventButtonState()) {
			int i1 = Mouse.getEventX() * this.width / this.mc.displayWidth;
			int i2 = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
			this.mouseClicked(i1, i2, Mouse.getEventButton());
		}

	}

	public final void handleKeyboardInput() {
		if(Keyboard.getEventKeyState()) {
			this.keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
		}

	}

	public void updateScreen() {
	}

	public void onClose() {
	}
}