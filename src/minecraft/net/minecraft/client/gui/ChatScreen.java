package net.minecraft.client.gui;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.net.Client;
import net.minecraft.client.net.Packet;

public final class ChatScreen extends GuiScreen {
	private String typedMsg = "";
	private int counter = 0;

	public final void initGui() {
		Keyboard.enableRepeatEvents(true);
	}

	public final void removed() {
		Keyboard.enableRepeatEvents(false);
	}

	public final void tick() {
		++this.counter;
	}

	protected final void keyTyped(char character, int key) {
		if(key == 1) {
			this.mc.displayGuiScreen((GuiScreen)null);
		} else if(key == 28) {
			String string3 = this.typedMsg.trim();
			Client client10000 = this.mc.networkClient;
			Client eventCharacter1 = client10000;
			if((string3 = string3.trim()).length() > 0) {
				eventCharacter1.serverConnection.sendPacket(Packet.CHAT_MESSAGE, new Object[]{-1, string3});
			}

			this.mc.displayGuiScreen((GuiScreen)null);
		} else {
			if(key == 14 && this.typedMsg.length() > 0) {
				this.typedMsg = this.typedMsg.substring(0, this.typedMsg.length() - 1);
			}

			if(" !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_\'abcdefghijklmnopqrstuvwxyz{|}~\u2302\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb".indexOf(character) >= 0 && this.typedMsg.length() < 100) {
				this.typedMsg = this.typedMsg + character;
			}

		}
	}

	public final void drawScreen(int xMouse, int yMouse) {
		drawRect(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);
		drawString(this.fontRenderer, "> " + this.typedMsg + (this.counter / 6 % 2 == 0 ? "_" : ""), 4, this.height - 12, 14737632);
	}

	protected final void mouseClicked(int x, int y, int buttonNum) {
		if(buttonNum == 0 && this.mc.ingameGUI.hoveredUsername != null) {
			if(this.typedMsg.length() > 0 && !this.typedMsg.endsWith(" ")) {
				this.typedMsg = this.typedMsg + " ";
			}

			this.typedMsg = this.typedMsg + this.mc.ingameGUI.hoveredUsername;
			x = 64 - (this.mc.session.username.length() + 2);
			if(this.typedMsg.length() > x) {
				this.typedMsg = this.typedMsg.substring(0, x);
			}
		}

	}
}