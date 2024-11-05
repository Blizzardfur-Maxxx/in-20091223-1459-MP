package net.minecraft.client.gui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class GuiLoadLevel extends GuiScreen implements Runnable {
	private GuiScreen parent;
	private boolean finished = false;
	private boolean loaded = false;
	private String[] levels = null;
	private String status = "";
	protected String title = "Load level";

	public GuiLoadLevel(GuiScreen guiScreen1) {
		this.parent = guiScreen1;
	}

	public void run() {
		try {
			this.status = "Getting level list..";
			URL uRL1 = new URL("http://" + this.mc.minecraftUri + "/listmaps.jsp?user=" + this.mc.session.username);
			BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(uRL1.openConnection().getInputStream()));
			this.levels = bufferedReader3.readLine().split(";");
			if(this.levels.length < 5) {
				this.status = this.levels[0];
				this.finished = true;
			} else {
				this.openLevel(this.levels);
				this.loaded = true;
			}
		} catch (Exception exception2) {
			exception2.printStackTrace();
			this.status = "Failed to load levels";
			this.finished = true;
		}
	}

	protected void openLevel(String[] string1) {
		for(int i2 = 0; i2 < 5; ++i2) {
			((GuiButton)this.controlList.get(i2)).enabled = !string1[i2].equals("-");
			((GuiButton)this.controlList.get(i2)).displayString = string1[i2];
			((GuiButton)this.controlList.get(i2)).visible = true;
		}

	}

	public void initGui() {
		(new Thread(this)).start();

		for(int i1 = 0; i1 < 5; ++i1) {
			this.controlList.add(new GuiButton(i1, this.width / 2 - 100, this.height / 6 + i1 * 24, "---"));
			((GuiButton)this.controlList.get(i1)).visible = false;
		}

		this.controlList.add(new GuiButton(5, this.width / 2 - 100, this.height / 6 + 120 + 12, "Load file..."));
		this.controlList.add(new GuiButton(6, this.width / 2 - 100, this.height / 6 + 168, "Cancel"));
		((GuiButton)this.controlList.get(5)).visible = false;
	}

	protected final void actionPerformed(GuiButton guiButton1) {
		if(guiButton1.enabled) {
			if(this.loaded && guiButton1.id < 5) {
				this.openLevel(guiButton1.id);
			}

			if(this.finished || this.loaded && guiButton1.id == 6) {
				this.mc.displayGuiScreen(this.parent);
			}

		}
	}

	protected void openLevel(int i1) {
		this.mc.displayGuiScreen((GuiScreen)null);
		this.mc.setIngameFocus();
	}

	public final void drawScreen(int i1, int i2) {
		drawGradientRect(0, 0, this.width, this.height, 1610941696, -1607454624);
		drawCenteredString(this.fontRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
		if(!this.loaded) {
			drawCenteredString(this.fontRenderer, this.status, this.width / 2, this.height / 2 - 4, 0xFFFFFF);
		}

		super.drawScreen(i1, i2);
	}
}