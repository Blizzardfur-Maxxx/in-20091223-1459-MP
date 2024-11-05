package net.minecraft.client.gui;

public final class GuiSaveLevel extends GuiLoadLevel {
	public GuiSaveLevel(GuiScreen guiScreen1) {
		super(guiScreen1);
		this.title = "Save level";
	}

	public final void initGui() {
		super.initGui();
		((GuiButton)this.controlList.get(5)).displayString = "Save file...";
	}

	protected final void openLevel(String[] string1) {
		for(int i2 = 0; i2 < 5; ++i2) {
			((GuiButton)this.controlList.get(i2)).displayString = string1[i2];
			((GuiButton)this.controlList.get(i2)).visible = true;
		}

	}

	protected final void openLevel(int i1) {
		this.mc.displayGuiScreen(new GuiNameLevel(this, ((GuiButton)this.controlList.get(i1)).displayString, i1));
	}
}