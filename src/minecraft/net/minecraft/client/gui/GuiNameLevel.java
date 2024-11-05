package net.minecraft.client.gui;

import org.lwjgl.input.Keyboard;

public final class GuiNameLevel extends GuiScreen {
	private GuiScreen parent;
	private String title = "Enter level name:";
	private String name;
	private int id = 0;

	public GuiNameLevel(GuiScreen guiScreen1, String string2, int i3) {
		this.parent = guiScreen1;
		this.name = string2;
		if(this.name.equals("-")) {
			this.name = "";
		}

	}

	public final void initGui() {
		this.controlList.clear();
		Keyboard.enableRepeatEvents(true);
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, "Save"));
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 144, "Cancel"));
		((GuiButton)this.controlList.get(0)).enabled = this.name.trim().length() > 1;
	}

	public final void onClose() {
		Keyboard.enableRepeatEvents(false);
	}

	public final void updateScreen() {
		++this.id;
	}

	protected final void actionPerformed(GuiButton guiButton1) {
		if(guiButton1.enabled) {
			if(guiButton1.id == 0 && this.name.trim().length() > 1) {
				this.name.trim();
				this.mc.displayGuiScreen((GuiScreen)null);
				this.mc.setIngameFocus();
			}

			if(guiButton1.id == 1) {
				this.mc.displayGuiScreen(this.parent);
			}

		}
	}

	protected final void keyTyped(char c1, int i2) {
		if(i2 == 14 && this.name.length() > 0) {
			this.name = this.name.substring(0, this.name.length() - 1);
		}

		if("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ,.:-_\'*!\"#%/()=+?[]{}<>".indexOf(c1) >= 0 && this.name.length() < 64) {
			this.name = this.name + c1;
		}

		((GuiButton)this.controlList.get(0)).enabled = this.name.trim().length() > 1;
	}

	public final void drawScreen(int i1, int i2) {
		drawGradientRect(0, 0, this.width, this.height, 1610941696, -1607454624);
		drawCenteredString(this.fontRenderer, this.title, this.width / 2, 40, 0xFFFFFF);
		int i3 = this.width / 2 - 100;
		int i4 = this.height / 2 - 10;
		drawRect(i3 - 1, i4 - 1, i3 + 200 + 1, i4 + 20 + 1, -6250336);
		drawRect(i3, i4, i3 + 200, i4 + 20, 0xFF000000);
		FontRenderer fontRenderer10000 = this.fontRenderer;
		String string10001 = this.name + (this.id / 6 % 2 == 0 ? "_" : "");
		int i10002 = i3 + 4;
		int i10003 = i4 + 6;
		int i6 = 14737632;
		int i5 = i10003;
		i3 = i10002;
		fontRenderer10000.drawStringWithShadow(string10001, i3, i5, i6);
		super.drawScreen(i1, i2);
	}
}