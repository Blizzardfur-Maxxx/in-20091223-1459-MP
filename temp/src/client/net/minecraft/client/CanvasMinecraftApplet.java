package net.minecraft.client;

import java.awt.Canvas;

final class CanvasMinecraftApplet extends Canvas {
	private MinecraftApplet mc;

	CanvasMinecraftApplet(MinecraftApplet minecraftApplet1) {
		this.mc = minecraftApplet1;
	}

	public final synchronized void addNotify() {
		super.addNotify();
		MinecraftApplet minecraftApplet1;
		if((minecraftApplet1 = this.mc).mcThread == null) {
			minecraftApplet1.mcThread = new Thread(minecraftApplet1.mc);
			minecraftApplet1.mcThread.start();
		}

	}

	public final synchronized void removeNotify() {
		this.mc.shutdown();
		super.removeNotify();
	}
}