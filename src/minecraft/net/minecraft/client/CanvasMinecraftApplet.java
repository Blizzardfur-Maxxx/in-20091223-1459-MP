package net.minecraft.client;

import java.awt.Canvas;

public final class CanvasMinecraftApplet extends Canvas {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MinecraftApplet mc;

	CanvasMinecraftApplet(MinecraftApplet minecraftApplet1) {
		this.mc = minecraftApplet1;
	}

	public final synchronized void addNotify() {
		super.addNotify();
		MinecraftApplet minecraftApplet1;
		if((minecraftApplet1 = this.mc).thread == null) {
			minecraftApplet1.thread = new Thread(minecraftApplet1.minecraft);
			minecraftApplet1.thread.start();
		}

	}

	public final synchronized void removeNotify() {
		this.mc.stopGameThread();
		super.removeNotify();
	}
}