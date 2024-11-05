package net.minecraft.client.gui;

public final class GuiNewLevel extends GuiScreen {
	private GuiScreen parentScreen;

	public GuiNewLevel(GuiScreen guiScreen1) {
		this.parentScreen = guiScreen1;
	}

	public final void initGui() {
		this.controlList.clear();
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4, "Small"));
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 24, "Normal"));
		this.controlList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 48, "Huge"));
		this.controlList.add(new GuiButton(3, this.width / 2 - 100, this.height / 4 + 120, "Cancel"));
	}

	protected final void actionPerformed(GuiButton guiButton1) {
		if(guiButton1.id == 3) {
			this.mc.displayGuiScreen(this.parentScreen);
		} else {
			this.mc.generateLevel(guiButton1.id);
			this.mc.displayGuiScreen((GuiScreen)null);
			this.mc.setIngameFocus();
		}
	}

	public final void drawScreen(int i1, int i2) {
		drawGradientRect(0, 0, this.width, this.height, 1610941696, -1607454624);
		drawCenteredString(this.fontRenderer, "Generate new level", this.width / 2, 40, 0xFFFFFF);
		super.drawScreen(i1, i2);
	}
}