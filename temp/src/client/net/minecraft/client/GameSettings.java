package net.minecraft.client;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import javax.imageio.ImageIO;

import net.minecraft.client.render.RenderEngine;

import org.lwjgl.input.Keyboard;

public final class GameSettings {
	private static final String[] RENDER_DISTANCES = new String[]{"FAR", "NORMAL", "SHORT", "TINY"};
	private boolean music = true;
	private boolean sound = true;
	public boolean invertMouse = false;
	public boolean showFPS = false;
	public int renderDistance = 0;
	public boolean viewBobbing = true;
	public boolean anaglyph = false;
	public boolean limitFramerate = false;
	public KeyBinding keyBindForward = new KeyBinding("Forward", Keyboard.KEY_W);
	public KeyBinding keyBindLeft = new KeyBinding("Left", Keyboard.KEY_A);
	public KeyBinding keyBindBack = new KeyBinding("Back", Keyboard.KEY_S);
	public KeyBinding keyBindRight = new KeyBinding("Right", Keyboard.KEY_D);
	public KeyBinding keyBindJump = new KeyBinding("Jump", Keyboard.KEY_SPACE);
	public KeyBinding keyBindInventory = new KeyBinding("Build", Keyboard.KEY_B);
	private KeyBinding keyBindChat = new KeyBinding("Chat", Keyboard.KEY_T);
	public KeyBinding keyBindToggleFog = new KeyBinding("Toggle fog", Keyboard.KEY_F);
	public KeyBinding keyBindSave = new KeyBinding("Save location", Keyboard.KEY_RETURN);
	public KeyBinding keyBindLoad = new KeyBinding("Load location", Keyboard.KEY_R);
	public KeyBinding[] keyBindings = new KeyBinding[]{this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindInventory, this.keyBindChat, this.keyBindToggleFog, this.keyBindSave, this.keyBindLoad};
	private Minecraft mc;
	private File optionsFile;
	public int numberOfOptions = 8;

	public GameSettings(Minecraft minecraft1, File file2) {
		this.mc = minecraft1;
		this.optionsFile = new File(file2, "options.txt");
		this.loadOptions();
	}

	public final String setKeyBindingString(int i1) {
		return this.keyBindings[i1].keyDescription + ": " + Keyboard.getKeyName(this.keyBindings[i1].keyCode);
	}

	public final void setKeyBinding(int i1, int i2) {
		this.keyBindings[i1].keyCode = i2;
		this.saveOptions();
	}

	public final void setOptionValue(int i1, int i2) {
		if(i1 == 0) {
			this.music = !this.music;
		}

		if(i1 == 1) {
			this.sound = !this.sound;
		}

		if(i1 == 2) {
			this.invertMouse = !this.invertMouse;
		}

		if(i1 == 3) {
			this.showFPS = !this.showFPS;
		}

		if(i1 == 4) {
			this.renderDistance = this.renderDistance + i2 & 3;
		}

		if(i1 == 5) {
			this.viewBobbing = !this.viewBobbing;
		}

		if(i1 == 6) {
			this.anaglyph = !this.anaglyph;
			RenderEngine renderEngine7 = this.mc.renderEngine;
			Iterator iterator3 = this.mc.renderEngine.textureContentsMap.keySet().iterator();

			int i4;
			BufferedImage bufferedImage5;
			while(iterator3.hasNext()) {
				i4 = ((Integer)iterator3.next()).intValue();
				bufferedImage5 = (BufferedImage)renderEngine7.textureContentsMap.get(i4);
				renderEngine7.setupTexture(bufferedImage5, i4);
			}

			iterator3 = renderEngine7.textureMap.keySet().iterator();

			while(iterator3.hasNext()) {
				String string8 = (String)iterator3.next();

				try {
					if(string8.startsWith("##")) {
						bufferedImage5 = RenderEngine.unwrapImageByColumns(ImageIO.read(RenderEngine.class.getResourceAsStream(string8.substring(2))));
					} else {
						bufferedImage5 = ImageIO.read(RenderEngine.class.getResourceAsStream(string8));
					}

					i4 = ((Integer)renderEngine7.textureMap.get(string8)).intValue();
					renderEngine7.setupTexture(bufferedImage5, i4);
				} catch (IOException iOException6) {
					iOException6.printStackTrace();
				}
			}
		}

		if(i1 == 7) {
			this.limitFramerate = !this.limitFramerate;
		}

		this.saveOptions();
	}

	public final String setOptionString(int i1) {
		return i1 == 0 ? "Music: " + (this.music ? "ON" : "OFF") : (i1 == 1 ? "Sound: " + (this.sound ? "ON" : "OFF") : (i1 == 2 ? "Invert mouse: " + (this.invertMouse ? "ON" : "OFF") : (i1 == 3 ? "Show FPS: " + (this.showFPS ? "ON" : "OFF") : (i1 == 4 ? "Render distance: " + RENDER_DISTANCES[this.renderDistance] : (i1 == 5 ? "View bobbing: " + (this.viewBobbing ? "ON" : "OFF") : (i1 == 6 ? "3d anaglyph: " + (this.anaglyph ? "ON" : "OFF") : (i1 == 7 ? "Limit framerate: " + (this.limitFramerate ? "ON" : "OFF") : "")))))));
	}

	private void loadOptions() {
		try {
			if(this.optionsFile.exists()) {
				BufferedReader bufferedReader1 = new BufferedReader(new FileReader(this.optionsFile));
				String string2 = null;

				while((string2 = bufferedReader1.readLine()) != null) {
					String[] string5;
					if((string5 = string2.split(":"))[0].equals("music")) {
						this.music = string5[1].equals("true");
					}

					if(string5[0].equals("sound")) {
						this.sound = string5[1].equals("true");
					}

					if(string5[0].equals("invertYMouse")) {
						this.invertMouse = string5[1].equals("true");
					}

					if(string5[0].equals("showFrameRate")) {
						this.showFPS = string5[1].equals("true");
					}

					if(string5[0].equals("viewDistance")) {
						this.renderDistance = Integer.parseInt(string5[1]);
					}

					if(string5[0].equals("bobView")) {
						this.viewBobbing = string5[1].equals("true");
					}

					if(string5[0].equals("anaglyph3d")) {
						this.anaglyph = string5[1].equals("true");
					}

					if(string5[0].equals("limitFramerate")) {
						this.limitFramerate = string5[1].equals("true");
					}

					for(int i3 = 0; i3 < this.keyBindings.length; ++i3) {
						if(string5[0].equals("key_" + this.keyBindings[i3].keyDescription)) {
							this.keyBindings[i3].keyCode = Integer.parseInt(string5[1]);
						}
					}
				}

				bufferedReader1.close();
			}
		} catch (Exception exception4) {
			System.out.println("Failed to load options");
			exception4.printStackTrace();
		}
	}

	private void saveOptions() {
		try {
			PrintWriter printWriter1;
			(printWriter1 = new PrintWriter(new FileWriter(this.optionsFile))).println("music:" + this.music);
			printWriter1.println("sound:" + this.sound);
			printWriter1.println("invertYMouse:" + this.invertMouse);
			printWriter1.println("showFrameRate:" + this.showFPS);
			printWriter1.println("viewDistance:" + this.renderDistance);
			printWriter1.println("bobView:" + this.viewBobbing);
			printWriter1.println("anaglyph3d:" + this.anaglyph);
			printWriter1.println("limitFramerate:" + this.limitFramerate);

			for(int i2 = 0; i2 < this.keyBindings.length; ++i2) {
				printWriter1.println("key_" + this.keyBindings[i2].keyDescription + ":" + this.keyBindings[i2].keyCode);
			}

			printWriter1.close();
		} catch (Exception exception3) {
			System.out.println("Failed to save options");
			exception3.printStackTrace();
		}
	}
}