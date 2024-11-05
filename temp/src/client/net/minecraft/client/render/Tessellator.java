package net.minecraft.client.render;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public final class Tessellator {
	private FloatBuffer byteBuffer = BufferUtils.createFloatBuffer(524288);
	private float[] rawBuffer = new float[524288];
	private int vertexCount = 0;
	private float textureU;
	private float textureV;
	private float r;
	private float g;
	private float b;
	private boolean hasColor = false;
	private boolean hasTexture = false;
	private int colors = 3;
	private int addedVertices = 0;
	private boolean drawMode = false;
	public static Tessellator instance = new Tessellator();

	public final void draw() {
		if(this.vertexCount > 0) {
			this.byteBuffer.clear();
			this.byteBuffer.put(this.rawBuffer, 0, this.addedVertices);
			this.byteBuffer.flip();
			if(this.hasTexture && this.hasColor) {
				GL11.glInterleavedArrays(GL11.GL_T2F_C3F_V3F, 0, this.byteBuffer);
			} else if(this.hasTexture) {
				GL11.glInterleavedArrays(GL11.GL_T2F_V3F, 0, this.byteBuffer);
			} else if(this.hasColor) {
				GL11.glInterleavedArrays(GL11.GL_C3F_V3F, 0, this.byteBuffer);
			} else {
				GL11.glInterleavedArrays(GL11.GL_V3F, 0, this.byteBuffer);
			}

			GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
			if(this.hasTexture) {
				GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
			}

			if(this.hasColor) {
				GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
			}

			GL11.glDrawArrays(GL11.GL_QUADS, GL11.GL_POINTS, this.vertexCount);
			GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
			if(this.hasTexture) {
				GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
			}

			if(this.hasColor) {
				GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
			}
		}

		this.reset();
	}

	private void reset() {
		this.vertexCount = 0;
		this.byteBuffer.clear();
		this.addedVertices = 0;
	}

	public final void startDrawingQuads() {
		if(this.vertexCount > 0) {
			new RuntimeException("OMG ALREADY VERTICES!");
		}

		this.reset();
		this.hasColor = false;
		this.hasTexture = false;
		this.drawMode = false;
	}

	public final void setColorOpaque_F(float f1, float f2, float f3) {
		if(!this.drawMode) {
			if(!this.hasColor) {
				this.colors += 3;
			}

			this.hasColor = true;
			this.r = f1;
			this.g = f2;
			this.b = f3;
		}
	}

	public final void addVertexWithUV(float f1, float f2, float f3, float f4, float f5) {
		if(!this.hasTexture) {
			this.colors += 2;
		}

		this.hasTexture = true;
		this.textureU = f4;
		this.textureV = f5;
		this.addVertex(f1, f2, f3);
	}

	public final void addVertex(float f1, float f2, float f3) {
		if(this.hasTexture) {
			this.rawBuffer[this.addedVertices++] = this.textureU;
			this.rawBuffer[this.addedVertices++] = this.textureV;
		}

		if(this.hasColor) {
			this.rawBuffer[this.addedVertices++] = this.r;
			this.rawBuffer[this.addedVertices++] = this.g;
			this.rawBuffer[this.addedVertices++] = this.b;
		}

		this.rawBuffer[this.addedVertices++] = f1;
		this.rawBuffer[this.addedVertices++] = f2;
		this.rawBuffer[this.addedVertices++] = f3;
		++this.vertexCount;
		if(this.vertexCount % 4 == 0 && this.addedVertices >= 524288 - (this.colors << 2)) {
			this.draw();
		}

	}

	public final void setColorOpaque_I(int i1) {
		int i2 = i1 >> 16 & 255;
		int i3 = i1 >> 8 & 255;
		i1 &= 255;
		byte b10001 = (byte)i2;
		byte b10002 = (byte)i3;
		byte b6 = (byte)i1;
		byte b5 = b10002;
		byte b4 = b10001;
		if(!this.drawMode) {
			if(!this.hasColor) {
				this.colors += 3;
			}

			this.hasColor = true;
			this.r = (float)(b4 & 255) / 255.0F;
			this.g = (float)(b5 & 255) / 255.0F;
			this.b = (float)(b6 & 255) / 255.0F;
		}

	}

	public final void disableColor() {
		this.drawMode = true;
	}

	public static void setNormal(float f0, float f1, float f2) {
		GL11.glNormal3f(f0, f1, f2);
	}
}