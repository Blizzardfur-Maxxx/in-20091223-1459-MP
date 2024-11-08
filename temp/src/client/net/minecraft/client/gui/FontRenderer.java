package net.minecraft.client.gui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import net.minecraft.client.GameSettings;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.Tessellator;

import org.lwjgl.opengl.GL11;

public final class FontRenderer {
	private int[] charWidth = new int[256];
	private int fontTextureName = 0;
	private GameSettings options;

	public FontRenderer(GameSettings gameSettings1, String string2, RenderEngine renderEngine3) {
		this.options = gameSettings1;

		BufferedImage bufferedImage14;
		try {
			bufferedImage14 = ImageIO.read(RenderEngine.class.getResourceAsStream(string2));
		} catch (IOException iOException13) {
			throw new RuntimeException(iOException13);
		}

		int i4 = bufferedImage14.getWidth();
		int i5 = bufferedImage14.getHeight();
		int[] i6 = new int[i4 * i5];
		bufferedImage14.getRGB(0, 0, i4, i5, i6, 0, i4);

		for(int i15 = 0; i15 < 128; ++i15) {
			i5 = i15 % 16;
			int i7 = i15 / 16;
			int i8 = 0;

			for(boolean z9 = false; i8 < 8 && !z9; ++i8) {
				int i10 = (i5 << 3) + i8;
				z9 = true;

				for(int i11 = 0; i11 < 8 && z9; ++i11) {
					int i12 = ((i7 << 3) + i11) * i4;
					if((i6[i10 + i12] & 255) > 128) {
						z9 = false;
					}
				}
			}

			if(i15 == 32) {
				i8 = 4;
			}

			this.charWidth[i15] = i8;
		}

		this.fontTextureName = renderEngine3.getTexture(string2);
	}

	public final void drawStringWithShadow(String string1, int i2, int i3, int i4) {
		this.renderString(string1, i2 + 1, i3 + 1, i4, true);
		this.renderString(string1, i2, i3, i4, false);
	}

	private void renderString(String string1, int i2, int i3, int i4, boolean z5) {
		if(string1 != null) {
			char[] c12 = string1.toCharArray();
			if(z5) {
				i4 = (i4 & 16579836) >> 2;
			}

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.fontTextureName);
			Tessellator tessellator6 = Tessellator.instance;
			Tessellator.instance.startDrawingQuads();
			tessellator6.setColorOpaque_I(i4);
			int i7 = 0;

			for(int i8 = 0; i8 < c12.length; ++i8) {
				int i9;
				if(c12[i8] == 38 && c12.length > i8 + 1) {
					if((i4 = "0123456789abcdef".indexOf(c12[i8 + 1])) < 0) {
						i4 = 15;
					}

					i9 = (i4 & 8) << 3;
					int i10 = (i4 & 1) * 191 + i9;
					int i11 = ((i4 & 2) >> 1) * 191 + i9;
					i4 = ((i4 & 4) >> 2) * 191 + i9;
					if(this.options.anaglyph) {
						i9 = (i4 * 30 + i11 * 59 + i10 * 11) / 100;
						i11 = (i4 * 30 + i11 * 70) / 100;
						i10 = (i4 * 30 + i10 * 70) / 100;
						i4 = i9;
					}

					i4 = i4 << 16 | i11 << 8 | i10;
					i8 += 2;
					if(z5) {
						i4 = (i4 & 16579836) >> 2;
					}

					tessellator6.setColorOpaque_I(i4);
				}

				i4 = c12[i8] % 16 << 3;
				i9 = c12[i8] / 16 << 3;
				float f13 = 7.99F;
				tessellator6.addVertexWithUV((float)(i2 + i7), (float)i3 + f13, 0.0F, (float)i4 / 128.0F, ((float)i9 + f13) / 128.0F);
				tessellator6.addVertexWithUV((float)(i2 + i7) + f13, (float)i3 + f13, 0.0F, ((float)i4 + f13) / 128.0F, ((float)i9 + f13) / 128.0F);
				tessellator6.addVertexWithUV((float)(i2 + i7) + f13, (float)i3, 0.0F, ((float)i4 + f13) / 128.0F, (float)i9 / 128.0F);
				tessellator6.addVertexWithUV((float)(i2 + i7), (float)i3, 0.0F, (float)i4 / 128.0F, (float)i9 / 128.0F);
				i7 += this.charWidth[c12[i8]];
			}

			tessellator6.draw();
		}
	}

	public final int getWidth(String string1) {
		if(string1 == null) {
			return 0;
		} else {
			char[] c4 = string1.toCharArray();
			int i2 = 0;

			for(int i3 = 0; i3 < c4.length; ++i3) {
				if(c4[i3] == 38) {
					++i3;
				} else {
					i2 += this.charWidth[c4[i3]];
				}
			}

			return i2;
		}
	}
}