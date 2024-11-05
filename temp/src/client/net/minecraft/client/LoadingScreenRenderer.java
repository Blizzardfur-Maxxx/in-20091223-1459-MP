package net.minecraft.client;

import net.minecraft.client.render.Tessellator;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public final class LoadingScreenRenderer {
	private String text = "";
	public Minecraft minecraft;
	public String title = "";
	private long start = System.currentTimeMillis();

	public LoadingScreenRenderer(Minecraft minecraft1) {
		this.minecraft = minecraft1;
	}

	public final void displayProgressMessage(String string1) {
		if(!this.minecraft.running) {
			throw new MinecraftError();
		} else {
			this.text = string1;
			this.setLoadingProgress(-1);
		}
	}

	public final void setLoadingProgress(int i1) {
		if(!this.minecraft.running) {
			throw new MinecraftError();
		} else {
			long j2;
			if((j2 = System.currentTimeMillis()) - this.start >= 20L) {
				this.start = j2;
				int i8 = this.minecraft.displayWidth * 240 / this.minecraft.displayHeight;
				int i3 = this.minecraft.displayHeight * 240 / this.minecraft.displayHeight;
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
				Tessellator tessellator4 = Tessellator.instance;
				int i5 = this.minecraft.renderEngine.getTexture("/dirt.png");
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, i5);
				float f9 = 32.0F;
				tessellator4.startDrawingQuads();
				tessellator4.setColorOpaque_I(4210752);
				tessellator4.addVertexWithUV(0.0F, (float)i3, 0.0F, 0.0F, (float)i3 / f9);
				tessellator4.addVertexWithUV((float)i8, (float)i3, 0.0F, (float)i8 / f9, (float)i3 / f9);
				tessellator4.addVertexWithUV((float)i8, 0.0F, 0.0F, (float)i8 / f9, 0.0F);
				tessellator4.addVertexWithUV(0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
				tessellator4.draw();
				if(i1 >= 0) {
					i5 = i8 / 2 - 50;
					int i6 = i3 / 2 + 16;
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					tessellator4.startDrawingQuads();
					tessellator4.setColorOpaque_I(8421504);
					tessellator4.addVertex((float)i5, (float)i6, 0.0F);
					tessellator4.addVertex((float)i5, (float)(i6 + 2), 0.0F);
					tessellator4.addVertex((float)(i5 + 100), (float)(i6 + 2), 0.0F);
					tessellator4.addVertex((float)(i5 + 100), (float)i6, 0.0F);
					tessellator4.setColorOpaque_I(8454016);
					tessellator4.addVertex((float)i5, (float)i6, 0.0F);
					tessellator4.addVertex((float)i5, (float)(i6 + 2), 0.0F);
					tessellator4.addVertex((float)(i5 + i1), (float)(i6 + 2), 0.0F);
					tessellator4.addVertex((float)(i5 + i1), (float)i6, 0.0F);
					tessellator4.draw();
					GL11.glEnable(GL11.GL_TEXTURE_2D);
				}

				this.minecraft.fontRenderer.drawStringWithShadow(this.title, (i8 - this.minecraft.fontRenderer.getWidth(this.title)) / 2, i3 / 2 - 4 - 16, 0xFFFFFF);
				this.minecraft.fontRenderer.drawStringWithShadow(this.text, (i8 - this.minecraft.fontRenderer.getWidth(this.text)) / 2, i3 / 2 - 4 + 8, 0xFFFFFF);
				Display.update();

				try {
					Thread.yield();
				} catch (Exception exception7) {
				}
			}
		}
	}
}