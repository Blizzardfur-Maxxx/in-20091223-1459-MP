package net.minecraft.client;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Canvas;

public class MinecraftApplet extends Applet {
	private static final long serialVersionUID = 1L;
	private Canvas canvas;
	public Minecraft minecraft;
	public Thread thread = null;

	public void init() {
		this.canvas = new CanvasMinecraftApplet(this);
		boolean z1 = false;
		if(this.getParameter("fullscreen") != null) {
			z1 = this.getParameter("fullscreen").equalsIgnoreCase("true");
		}

		this.minecraft = new Minecraft(this.canvas, this.getWidth(), this.getHeight(), z1);
		this.minecraft.minecraftUri = this.getDocumentBase().getHost();
		if(this.getDocumentBase().getPort() > 0) {
			this.minecraft.minecraftUri = this.minecraft.minecraftUri + ":" + this.getDocumentBase().getPort();
		}

		if(this.getParameter("username") != null && this.getParameter("sessionid") != null) {
			this.minecraft.session = new Session(this.getParameter("username"), this.getParameter("sessionid"));
			if(this.getParameter("mppass") != null) {
				this.minecraft.session.mpPass = this.getParameter("mppass");
			}
		}

		if(this.getParameter("loadmap_user") != null && this.getParameter("loadmap_id") != null) {
			this.getParameter("loadmap_user");
			Integer.parseInt(this.getParameter("loadmap_id"));
		} else if(this.getParameter("server") != null && this.getParameter("port") != null) {
			Minecraft minecraft10000 = this.minecraft;
			String string10001 = this.getParameter("server");
			int i3 = Integer.parseInt(this.getParameter("port"));
			String string2 = string10001;
			Minecraft minecraft4 = minecraft10000;
			minecraft10000.serverIp = string2;
			minecraft4.port = i3;
		}

		this.minecraft.appletMode = true;
		this.setLayout(new BorderLayout());
		this.add(this.canvas, "Center");
		this.canvas.setFocusable(true);
		this.validate();
	}

	public void startGameThread() {
		if(this.thread == null) {
			this.thread = new Thread(this.minecraft);
			this.thread.start();
		}
	}

	public void start() {
		this.minecraft.isGamePaused = false;
	}

	public void stop() {
		this.minecraft.isGamePaused = true;
	}

	public void destroy() {
		this.stopGameThread();
	}

	public void stopGameThread() {
		if(this.thread != null) {
			Minecraft minecraft1 = this.minecraft;
			this.minecraft.running = false;

			try {
				this.thread.join(1000L);
			} catch (InterruptedException interruptedException3) {
				try {
					this.minecraft.shutdownMinecraftApplet();
				} catch (Exception exception2) {
					exception2.printStackTrace();
				}
			}

			this.thread = null;
		}
	}
}