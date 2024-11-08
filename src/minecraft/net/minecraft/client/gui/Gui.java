package net.minecraft.client.gui;

import net.minecraft.client.render.Tessellator;

import org.lwjgl.opengl.GL11;

public class Gui {
	protected float zLevel = 0.0F;

	protected static void drawRect(int i0, int i1, int i2, int i3, int i4) {
		float f5 = (float)(i4 >>> 24) / 255.0F;
		float f6 = (float)(i4 >> 16 & 255) / 255.0F;
		float f7 = (float)(i4 >> 8 & 255) / 255.0F;
		float f9 = (float)(i4 & 255) / 255.0F;
		Tessellator tessellator8 = Tessellator.instance;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(f6, f7, f9, f5);
		tessellator8.startDrawingQuads();
		tessellator8.addVertex((float)i0, (float)i3, 0.0F);
		tessellator8.addVertex((float)i2, (float)i3, 0.0F);
		tessellator8.addVertex((float)i2, (float)i1, 0.0F);
		tessellator8.addVertex((float)i0, (float)i1, 0.0F);
		tessellator8.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	protected static void drawGradientRect(int i0, int i1, int i2, int i3, int i4, int i5) {
		float f6 = (float)(i4 >>> 24) / 255.0F;
		float f7 = (float)(i4 >> 16 & 255) / 255.0F;
		float f8 = (float)(i4 >> 8 & 255) / 255.0F;
		float f12 = (float)(i4 & 255) / 255.0F;
		float f9 = (float)(i5 >>> 24) / 255.0F;
		float f10 = (float)(i5 >> 16 & 255) / 255.0F;
		float f11 = (float)(i5 >> 8 & 255) / 255.0F;
		float f13 = (float)(i5 & 255) / 255.0F;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(f7, f8, f12, f6);
		GL11.glVertex2f((float)i2, (float)i1);
		GL11.glVertex2f((float)i0, (float)i1);
		GL11.glColor4f(f10, f11, f13, f9);
		GL11.glVertex2f((float)i0, (float)i3);
		GL11.glVertex2f((float)i2, (float)i3);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public static void drawCenteredString(FontRenderer fontRenderer0, String string1, int i2, int i3, int i4) {
		fontRenderer0.drawStringWithShadow(string1, i2 - fontRenderer0.getWidth(string1) / 2, i3, i4);
	}
	
	public static void drawString(FontRenderer fontRenderer0, String string1, int i2, int i3, int i4) {
		fontRenderer0.drawStringWithShadow(string1, i2, i3, i4);
	}

	public final void drawTexturedModal(int i1, int i2, int i3, int i4, int i5, int i6) {
		float f7 = 0.00390625F;
		float f8 = 0.00390625F;
		Tessellator tessellator9 = Tessellator.instance;
		Tessellator.instance.startDrawingQuads();
		tessellator9.addVertexWithUV((float)i1, (float)(i2 + i6), this.zLevel, (float)i3 * f7, (float)(i4 + i6) * f8);
		tessellator9.addVertexWithUV((float)(i1 + i5), (float)(i2 + i6), this.zLevel, (float)(i3 + i5) * f7, (float)(i4 + i6) * f8);
		tessellator9.addVertexWithUV((float)(i1 + i5), (float)i2, this.zLevel, (float)(i3 + i5) * f7, (float)i4 * f8);
		tessellator9.addVertexWithUV((float)i1, (float)i2, this.zLevel, (float)i3 * f7, (float)i4 * f8);
		tessellator9.draw();
	}	
}