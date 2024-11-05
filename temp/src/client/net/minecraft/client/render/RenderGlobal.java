package net.minecraft.client.render;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.EntityPlayer;
import net.minecraft.game.level.World;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public final class RenderGlobal {
	public World worldObj;
	RenderEngine renderEngine;
	int glGenList;
	IntBuffer renderIntBuffer = BufferUtils.createIntBuffer(65536);
	List worldRenderersToUpdate = new ArrayList();
	private WorldRenderer[] sortedWorldRenderers;
	WorldRenderer[] worldRenderers;
	private int renderChunksWide;
	private int renderChunksTall;
	private int renderChunksDeep;
	private int glRenderListBase;
	Minecraft mc;
	public RenderBlocks globalRenderBlocks;
	public RenderManager renderManager = new RenderManager();
	private int[] dummyBuf50k = new int[50000];
	public int cloudOffsetX = 0;
	private float prevSortX = -9999.0F;
	private float prevSortY = -9999.0F;
	private float prevSortZ = -9999.0F;
	public float damagePartialTime;

	public RenderGlobal(Minecraft minecraft1, RenderEngine renderEngine2) {
		this.mc = minecraft1;
		this.renderEngine = renderEngine2;
		this.glGenList = GL11.glGenLists(2);
		this.glRenderListBase = GL11.glGenLists(4096 << 6 << 1);
	}

	public final void loadRenderers() {
		int i1;
		if(this.worldRenderers != null) {
			for(i1 = 0; i1 < this.worldRenderers.length; ++i1) {
				this.worldRenderers[i1].stopRendering();
			}
		}

		this.renderChunksWide = this.worldObj.width / 16;
		this.renderChunksTall = this.worldObj.height / 16;
		this.renderChunksDeep = this.worldObj.length / 16;
		this.worldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
		this.sortedWorldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
		i1 = 0;

		int i2;
		int i4;
		for(i2 = 0; i2 < this.renderChunksWide; ++i2) {
			for(int i3 = 0; i3 < this.renderChunksTall; ++i3) {
				for(i4 = 0; i4 < this.renderChunksDeep; ++i4) {
					this.worldRenderers[(i4 * this.renderChunksTall + i3) * this.renderChunksWide + i2] = new WorldRenderer(this.worldObj, i2 << 4, i3 << 4, i4 << 4, this.glRenderListBase + i1);
					this.sortedWorldRenderers[(i4 * this.renderChunksTall + i3) * this.renderChunksWide + i2] = this.worldRenderers[(i4 * this.renderChunksTall + i3) * this.renderChunksWide + i2];
					i1 += 2;
				}
			}
		}

		for(i2 = 0; i2 < this.worldRenderersToUpdate.size(); ++i2) {
			((WorldRenderer)this.worldRenderersToUpdate.get(i2)).needsUpdate = false;
		}

		this.worldRenderersToUpdate.clear();
		GL11.glNewList(this.glGenList, GL11.GL_COMPILE);
		RenderGlobal renderGlobal9 = this;
		float f10 = 0.5F;
		GL11.glColor4f(0.5F, 0.5F, f10, 1.0F);
		Tessellator tessellator11 = Tessellator.instance;
		float f12 = this.worldObj.getGroundLevel();
		int i5 = 128;
		if(128 > this.worldObj.width) {
			i5 = this.worldObj.width;
		}

		if(i5 > this.worldObj.length) {
			i5 = this.worldObj.length;
		}

		int i6 = 2048 / i5;
		tessellator11.startDrawingQuads();

		int i7;
		for(i7 = -i5 * i6; i7 < renderGlobal9.worldObj.width + i5 * i6; i7 += i5) {
			for(int i8 = -i5 * i6; i8 < renderGlobal9.worldObj.length + i5 * i6; i8 += i5) {
				f10 = f12;
				if(i7 >= 0 && i8 >= 0 && i7 < renderGlobal9.worldObj.width && i8 < renderGlobal9.worldObj.length) {
					f10 = 0.0F;
				}

				tessellator11.addVertexWithUV((float)i7, f10, (float)(i8 + i5), 0.0F, (float)i5);
				tessellator11.addVertexWithUV((float)(i7 + i5), f10, (float)(i8 + i5), (float)i5, (float)i5);
				tessellator11.addVertexWithUV((float)(i7 + i5), f10, (float)i8, (float)i5, 0.0F);
				tessellator11.addVertexWithUV((float)i7, f10, (float)i8, 0.0F, 0.0F);
			}
		}

		tessellator11.draw();
		GL11.glColor3f(0.8F, 0.8F, 0.8F);
		tessellator11.startDrawingQuads();

		for(i7 = 0; i7 < renderGlobal9.worldObj.width; i7 += i5) {
			tessellator11.addVertexWithUV((float)i7, 0.0F, 0.0F, 0.0F, 0.0F);
			tessellator11.addVertexWithUV((float)(i7 + i5), 0.0F, 0.0F, (float)i5, 0.0F);
			tessellator11.addVertexWithUV((float)(i7 + i5), f12, 0.0F, (float)i5, f12);
			tessellator11.addVertexWithUV((float)i7, f12, 0.0F, 0.0F, f12);
			tessellator11.addVertexWithUV((float)i7, f12, (float)renderGlobal9.worldObj.length, 0.0F, f12);
			tessellator11.addVertexWithUV((float)(i7 + i5), f12, (float)renderGlobal9.worldObj.length, (float)i5, f12);
			tessellator11.addVertexWithUV((float)(i7 + i5), 0.0F, (float)renderGlobal9.worldObj.length, (float)i5, 0.0F);
			tessellator11.addVertexWithUV((float)i7, 0.0F, (float)renderGlobal9.worldObj.length, 0.0F, 0.0F);
		}

		GL11.glColor3f(0.6F, 0.6F, 0.6F);

		for(i7 = 0; i7 < renderGlobal9.worldObj.length; i7 += i5) {
			tessellator11.addVertexWithUV(0.0F, f12, (float)i7, 0.0F, 0.0F);
			tessellator11.addVertexWithUV(0.0F, f12, (float)(i7 + i5), (float)i5, 0.0F);
			tessellator11.addVertexWithUV(0.0F, 0.0F, (float)(i7 + i5), (float)i5, f12);
			tessellator11.addVertexWithUV(0.0F, 0.0F, (float)i7, 0.0F, f12);
			tessellator11.addVertexWithUV((float)renderGlobal9.worldObj.width, 0.0F, (float)i7, 0.0F, f12);
			tessellator11.addVertexWithUV((float)renderGlobal9.worldObj.width, 0.0F, (float)(i7 + i5), (float)i5, f12);
			tessellator11.addVertexWithUV((float)renderGlobal9.worldObj.width, f12, (float)(i7 + i5), (float)i5, 0.0F);
			tessellator11.addVertexWithUV((float)renderGlobal9.worldObj.width, f12, (float)i7, 0.0F, 0.0F);
		}

		tessellator11.draw();
		GL11.glEndList();
		GL11.glNewList(this.glGenList + 1, GL11.GL_COMPILE);
		renderGlobal9 = this;
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		f10 = this.worldObj.rgetGroundLevel();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		tessellator11 = Tessellator.instance;
		i4 = 128;
		if(128 > this.worldObj.width) {
			i4 = this.worldObj.width;
		}

		if(i4 > this.worldObj.length) {
			i4 = this.worldObj.length;
		}

		i5 = 2048 / i4;
		tessellator11.startDrawingQuads();

		for(i6 = -i4 * i5; i6 < renderGlobal9.worldObj.width + i4 * i5; i6 += i4) {
			for(i7 = -i4 * i5; i7 < renderGlobal9.worldObj.length + i4 * i5; i7 += i4) {
				float f13 = f10 - 0.1F;
				if(i6 < 0 || i7 < 0 || i6 >= renderGlobal9.worldObj.width || i7 >= renderGlobal9.worldObj.length) {
					tessellator11.addVertexWithUV((float)i6, f13, (float)(i7 + i4), 0.0F, (float)i4);
					tessellator11.addVertexWithUV((float)(i6 + i4), f13, (float)(i7 + i4), (float)i4, (float)i4);
					tessellator11.addVertexWithUV((float)(i6 + i4), f13, (float)i7, (float)i4, 0.0F);
					tessellator11.addVertexWithUV((float)i6, f13, (float)i7, 0.0F, 0.0F);
					tessellator11.addVertexWithUV((float)i6, f13, (float)i7, 0.0F, 0.0F);
					tessellator11.addVertexWithUV((float)(i6 + i4), f13, (float)i7, (float)i4, 0.0F);
					tessellator11.addVertexWithUV((float)(i6 + i4), f13, (float)(i7 + i4), (float)i4, (float)i4);
					tessellator11.addVertexWithUV((float)i6, f13, (float)(i7 + i4), 0.0F, (float)i4);
				}
			}
		}

		tessellator11.draw();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEndList();
		this.markBlocksForUpdate(0, 0, 0, this.worldObj.width, this.worldObj.height, this.worldObj.length);
	}

	public final int sortAndRender(EntityPlayer entityPlayer1, int i2) {
		float f3 = entityPlayer1.posX - this.prevSortX;
		float f4 = entityPlayer1.posY - this.prevSortY;
		float f5 = entityPlayer1.posZ - this.prevSortZ;
		if(f3 * f3 + f4 * f4 + f5 * f5 > 64.0F) {
			this.prevSortX = entityPlayer1.posX;
			this.prevSortY = entityPlayer1.posY;
			this.prevSortZ = entityPlayer1.posZ;
			Arrays.sort(this.sortedWorldRenderers, new EntitySorter(entityPlayer1));
		}

		int i6 = 0;

		for(int i7 = 0; i7 < this.sortedWorldRenderers.length; ++i7) {
			i6 = this.sortedWorldRenderers[i7].getGLCallListForPass(this.dummyBuf50k, i6, i2);
		}

		this.renderIntBuffer.clear();
		this.renderIntBuffer.put(this.dummyBuf50k, 0, i6);
		this.renderIntBuffer.flip();
		if(this.renderIntBuffer.remaining() > 0) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain.png"));
			GL11.glCallLists(this.renderIntBuffer);
		}

		return this.renderIntBuffer.remaining();
	}

	public final void markBlocksForUpdate(int i1, int i2, int i3, int i4, int i5, int i6) {
		i1 /= 16;
		i2 /= 16;
		i3 /= 16;
		i4 /= 16;
		i5 /= 16;
		i6 /= 16;
		if(i1 < 0) {
			i1 = 0;
		}

		if(i2 < 0) {
			i2 = 0;
		}

		if(i3 < 0) {
			i3 = 0;
		}

		if(i4 > this.renderChunksWide - 1) {
			i4 = this.renderChunksWide - 1;
		}

		if(i5 > this.renderChunksTall - 1) {
			i5 = this.renderChunksTall - 1;
		}

		if(i6 > this.renderChunksDeep - 1) {
			i6 = this.renderChunksDeep - 1;
		}

		while(i1 <= i4) {
			for(int i7 = i2; i7 <= i5; ++i7) {
				for(int i8 = i3; i8 <= i6; ++i8) {
					WorldRenderer worldRenderer9;
					if(!(worldRenderer9 = this.worldRenderers[(i8 * this.renderChunksTall + i7) * this.renderChunksWide + i1]).needsUpdate) {
						worldRenderer9.needsUpdate = true;
						this.worldRenderersToUpdate.add(this.worldRenderers[(i8 * this.renderChunksTall + i7) * this.renderChunksWide + i1]);
					}
				}
			}

			++i1;
		}

	}
}