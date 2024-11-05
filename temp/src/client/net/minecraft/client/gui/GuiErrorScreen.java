package net.minecraft.client.gui;

public final class GuiErrorScreen extends GuiScreen {
	private String title;
	private String text;

	public GuiErrorScreen(String string1, String string2) {
		this.title = string1;
		this.text = string2;
	}

	public final void initGui() {
	}

	public final void drawScreen(int i1, int i2) {
		drawGradientRect(0, 0, this.width, this.height, -12574688, -11530224);
		drawCenteredString(this.fontRenderer, this.title, this.width / 2, 90, 0xFFFFFF);
		drawCenteredString(this.fontRenderer, this.text, this.width / 2, 110, 0xFFFFFF);
		super.drawScreen(i1, i2);
	}

	protected final void keyTyped(char c1, int i2) {
	}
}