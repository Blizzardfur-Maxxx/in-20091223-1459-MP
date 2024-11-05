package net.minecraft.client.render;

import net.minecraft.client.player.EntityPlayer;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

import org.lwjgl.opengl.GL11;

import util.MathHelper;

public final class WorldRenderer {
	private World worldObj;
	private int glRenderList = -1;
	private static Tessellator tessellator = Tessellator.instance;
	public static int chunksUpdated = 0;
	private int posX;
	private int posY;
	private int posZ;
	private int sizeWidth;
	private int sizeHeight;
	private int sizeDepth;
	public boolean isInFrustrum = false;
	private boolean[] skipRenderPass = new boolean[2];
	public boolean needsUpdate;
	private RenderBlocks renderBlocks;

	public WorldRenderer(World world1, int i2, int i3, int i4, int i5) {
		this.renderBlocks = new RenderBlocks(Tessellator.instance, world1);
		this.worldObj = world1;
		this.posX = i2;
		this.posY = i3;
		this.posZ = i4;
		this.sizeWidth = this.sizeHeight = this.sizeDepth = 16;
		MathHelper.sqrt_float((float)(this.sizeWidth * this.sizeWidth + this.sizeHeight * this.sizeHeight + this.sizeDepth * this.sizeDepth));
		this.glRenderList = i5;
		this.setDontDraw();
	}

	public final void updateRenderer() {
		if(this.needsUpdate) {
			++chunksUpdated;
			int i1 = this.posX;
			int i2 = this.posY;
			int i3 = this.posZ;
			int i4 = this.posX + this.sizeWidth;
			int i5 = this.posY + this.sizeHeight;
			int i6 = this.posZ + this.sizeDepth;

			int i7;
			for(i7 = 0; i7 < 2; ++i7) {
				this.skipRenderPass[i7] = true;
			}

			for(i7 = 0; i7 < 2; ++i7) {
				boolean z8 = false;
				boolean z9 = false;
				tessellator.startDrawingQuads();
				GL11.glNewList(this.glRenderList + i7, GL11.GL_COMPILE);

				for(int i10 = i1; i10 < i4; ++i10) {
					for(int i11 = i2; i11 < i5; ++i11) {
						for(int i12 = i3; i12 < i6; ++i12) {
							int i13;
							if((i13 = this.worldObj.getBlockId(i10, i11, i12)) > 0) {
								Block block14;
								if((block14 = Block.blocksList[i13]).getRenderBlockPass() != i7) {
									z8 = true;
								} else {
									z9 |= this.renderBlocks.renderBlockByRenderType(block14, i10, i11, i12);
								}
							}
						}
					}
				}

				tessellator.draw();
				GL11.glEndList();
				if(z9) {
					this.skipRenderPass[i7] = false;
				}

				if(!z8) {
					break;
				}
			}

		}
	}

	public final float a(EntityPlayer entityPlayer1) {
		float f2 = entityPlayer1.posX - (float)this.posX;
		float f3 = entityPlayer1.posY - (float)this.posY;
		float f4 = entityPlayer1.posZ - (float)this.posZ;
		return f2 * f2 + f3 * f3 + f4 * f4;
	}

	private void setDontDraw() {
		for(int i1 = 0; i1 < 2; ++i1) {
			this.skipRenderPass[i1] = true;
		}

	}

	public final void stopRendering() {
		this.setDontDraw();
		this.worldObj = null;
	}

	public final int getGLCallListForPass(int[] i1, int i2, int i3) {
		if(!this.isInFrustrum) {
			return i2;
		} else {
			if(!this.skipRenderPass[i3]) {
				i1[i2++] = this.glRenderList + i3;
			}

			return i2;
		}
	}

	public final void updateInFrustrum(ClippingHelper clippingHelper1) {
		this.isInFrustrum = clippingHelper1.isBoundingBoxInFrustrum((float)this.posX, (float)this.posY, (float)this.posZ, (float)(this.posX + this.sizeWidth), (float)(this.posY + this.sizeHeight), (float)(this.posZ + this.sizeDepth));
	}
}