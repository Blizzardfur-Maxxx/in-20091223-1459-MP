package net.minecraft.client;

import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;

import net.minecraft.client.player.EntityPlayer;

final class ThreadDownloadSkin extends Thread {
	private Minecraft mc;

	ThreadDownloadSkin(Minecraft minecraft1) {
		this.mc = minecraft1;
	}

	public final void run() {
		if(this.mc.session != null) {
			HttpURLConnection httpURLConnection1 = null;

			try {
				(httpURLConnection1 = (HttpURLConnection)(new URL("http://www.minecraft.net/skin/" + this.mc.session.username + ".png")).openConnection()).setDoInput(true);
				httpURLConnection1.setDoOutput(false);
				httpURLConnection1.connect();
				if(httpURLConnection1.getResponseCode() == 404) {
					return;
				}

				EntityPlayer.skinData = ImageIO.read(httpURLConnection1.getInputStream());
				return;
			} catch (Exception exception4) {
				exception4.printStackTrace();
			} finally {
				httpURLConnection1.disconnect();
			}

		}
	}
}