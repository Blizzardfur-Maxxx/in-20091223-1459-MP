package net.minecraft.client.gui;

public class GuiButton extends Gui {
	int width;
	int height;
	public int x;
	public int y;
	public String displayString;
	public int id;
	public boolean enabled;
	public boolean visible;

	public GuiButton(int i1, int i2, int i3, String string4) {
		this(i1, i2, i3, 200, string4);
	}

	protected GuiButton(int i1, int i2, int i3, int i4, String string5) {
		this.width = 200;
		this.height = 20;
		this.enabled = true;
		this.visible = true;
		this.id = i1;
		this.x = i2;
		this.y = i3;
		this.width = i4;
		this.height = 20;
		this.displayString = string5;
	}
}