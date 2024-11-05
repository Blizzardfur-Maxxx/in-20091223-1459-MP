package net.minecraft.client.render;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;

import net.minecraft.client.GameSettings;
import net.minecraft.client.render.texture.TextureFX;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class RenderEngine {
	public HashMap textureMap = new HashMap();
	public HashMap textureContentsMap = new HashMap();
	public IntBuffer singleIntBuffer = BufferUtils.createIntBuffer(1);
	public ByteBuffer imageData = BufferUtils.createByteBuffer(262144);
	public List textureList = new ArrayList();
	public GameSettings options;
	boolean clampTexture = false;

	public RenderEngine(GameSettings gameSettings1) {
		this.options = gameSettings1;
	}

	public final int getTexture(String string1) {
		Integer integer2;
		if((integer2 = (Integer)this.textureMap.get(string1)) != null) {
			return integer2.intValue();
		} else {
			try {
				this.singleIntBuffer.clear();
				GL11.glGenTextures(this.singleIntBuffer);
				int i4 = this.singleIntBuffer.get(0);
				if(string1.startsWith("##")) {
					this.setupTexture(unwrapImageByColumns(ImageIO.read(RenderEngine.class.getResourceAsStream(string1.substring(2)))), i4);
				} else {
					this.setupTexture(ImageIO.read(RenderEngine.class.getResourceAsStream(string1)), i4);
				}

				this.textureMap.put(string1, i4);
				return i4;
			} catch (IOException iOException3) {
				throw new RuntimeException("!!");
			}
		}
	}

	public static BufferedImage unwrapImageByColumns(BufferedImage bufferedImage0) {
		int i1 = bufferedImage0.getWidth() / 16;
		BufferedImage bufferedImage2;
		Graphics graphics3 = (bufferedImage2 = new BufferedImage(16, bufferedImage0.getHeight() * i1, 2)).getGraphics();

		for(int i4 = 0; i4 < i1; ++i4) {
			graphics3.drawImage(bufferedImage0, -i4 << 4, i4 * bufferedImage0.getHeight(), (ImageObserver)null);
		}

		graphics3.dispose();
		return bufferedImage2;
	}

	public final void setupTexture(BufferedImage bufferedImage1, int i2) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, i2);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		i2 = bufferedImage1.getWidth();
		int i3 = bufferedImage1.getHeight();
		int[] i4 = new int[i2 * i3];
		byte[] b5 = new byte[i2 * i3 << 2];
		bufferedImage1.getRGB(0, 0, i2, i3, i4, 0, i2);

		for(int i11 = 0; i11 < i4.length; ++i11) {
			int i6 = i4[i11] >>> 24;
			int i7 = i4[i11] >> 16 & 255;
			int i8 = i4[i11] >> 8 & 255;
			int i9 = i4[i11] & 255;
			if(this.options != null && this.options.anaglyph) {
				int i10 = (i7 * 30 + i8 * 59 + i9 * 11) / 100;
				i8 = (i7 * 30 + i8 * 70) / 100;
				i9 = (i7 * 30 + i9 * 70) / 100;
				i7 = i10;
			}

			b5[i11 << 2] = (byte)i7;
			b5[(i11 << 2) + 1] = (byte)i8;
			b5[(i11 << 2) + 2] = (byte)i9;
			b5[(i11 << 2) + 3] = (byte)i6;
		}

		this.imageData.clear();
		this.imageData.put(b5);
		this.imageData.position(0).limit(b5.length);
		if(this.clampTexture) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		}

		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, i2, i3, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
	}

	public final void registerTextureFX(TextureFX textureFX1) {
		this.textureList.add(textureFX1);
		textureFX1.onTick();
	}
}