package net.minecraft.client;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Canvas;

public class MinecraftApplet extends Applet {
	private Canvas mcCanvas;
	Minecraft mc;
	Thread mcThread = null;

	public void init() {
		this.mcCanvas = new CanvasMinecraftApplet(this);
		boolean z1 = false;
		if(this.getParameter("fullscreen") != null) {
			z1 = this.getParameter("fullscreen").equalsIgnoreCase("true");
		}

		this.mc = new Minecraft(this.mcCanvas, this.getWidth(), this.getHeight(), z1);
		this.mc.minecraftUri = this.getDocumentBase().getHost();
		if(this.getDocumentBase().getPort() > 0) {
			this.mc.minecraftUri = this.mc.minecraftUri + ":" + this.getDocumentBase().getPort();
		}

		Minecraft minecraft10000;
		if(this.getParameter("username") != null && this.getParameter("sessionid") != null) {
			minecraft10000 = this.mc;
			String string10003 = this.getParameter("username");
			this.getParameter("sessionid");
			minecraft10000.session = new Session(string10003);
			if(this.getParameter("mppass") != null) {
				this.getParameter("mppass");
			}
		}

		if(this.getParameter("loadmap_user") != null && this.getParameter("loadmap_id") != null) {
			this.getParameter("loadmap_user");
			Integer.parseInt(this.getParameter("loadmap_id"));
		} else if(this.getParameter("server") != null && this.getParameter("port") != null) {
			minecraft10000 = this.mc;
			String string10001 = this.getParameter("server");
			Integer.parseInt(this.getParameter("port"));
			minecraft10000.serverIp = string10001;
		}

		this.mc.appletMode = true;
		this.setLayout(new BorderLayout());
		this.add(this.mcCanvas, "Center");
		this.mcCanvas.setFocusable(true);
		this.validate();
	}

	public void start() {
		this.mc.isGamePaused = false;
	}

	public void stop() {
		this.mc.isGamePaused = true;
	}

	public void destroy() {
		this.shutdown();
	}

	public final void shutdown() {
		if(this.mcThread != null) {
			Object object1 = null;
			this.mc.running = false;

			try {
				this.mcThread.join(1000L);
			} catch (InterruptedException interruptedException3) {
				try {
					this.mc.shutdownMinecraftApplet();
				} catch (Exception exception2) {
					exception2.printStackTrace();
				}
			}

			this.mcThread = null;
		}
	}
}