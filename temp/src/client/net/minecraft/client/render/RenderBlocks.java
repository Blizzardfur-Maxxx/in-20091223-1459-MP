package net.minecraft.client.render;

import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

import org.lwjgl.opengl.GL11;

public final class RenderBlocks {
	private Tessellator tessellator;
	private World blockAccess;
	int overrideBlockTexture = -1;
	boolean flipTexture = false;

	public RenderBlocks(Tessellator tessellator1, World world2) {
		this.tessellator = tessellator1;
		this.blockAccess = world2;
	}

	public RenderBlocks(Tessellator tessellator1) {
		this.tessellator = tessellator1;
	}

	public final boolean renderBlockByRenderType(Block block1, int i2, int i3, int i4) {
		int i5;
		float f6;
		float f10;
		if((i5 = block1.getRenderType()) != 0) {
			if(i5 == 1) {
				f10 = block1.getBlockBrightness(this.blockAccess, i2, i3, i4);
				this.tessellator.setColorOpaque_F(f10, f10, f10);
				this.renderBlockPlant(block1, (float)i2, (float)i3, (float)i4);
				return true;
			} else if(i5 == 2) {
				f10 = block1.getBlockBrightness(this.blockAccess, i2, i3, i4);
				this.tessellator.setColorOpaque_F(f10, f10, f10);
				f6 = 0.5F;
				f10 = 0.2F;
				if(this.blockAccess.isBlockNormalCube(i2 - 1, i3, i4)) {
					this.renderBlockTorch(block1, (float)i2, (float)i3 + f10, (float)i4, -f6, 0.0F);
				} else if(this.blockAccess.isBlockNormalCube(i2 + 1, i3, i4)) {
					this.renderBlockTorch(block1, (float)i2, (float)i3 + f10, (float)i4, f6, 0.0F);
				} else if(this.blockAccess.isBlockNormalCube(i2, i3, i4 - 1)) {
					this.renderBlockTorch(block1, (float)i2, (float)i3 + f10, (float)i4, 0.0F, -f6);
				} else if(this.blockAccess.isBlockNormalCube(i2, i3, i4 + 1)) {
					this.renderBlockTorch(block1, (float)i2, (float)i3 + f10, (float)i4, 0.0F, f6);
				} else {
					this.renderBlockTorch(block1, (float)i2, (float)i3, (float)i4, 0.0F, 0.0F);
				}

				return true;
			} else {
				return false;
			}
		} else {
			boolean z9 = false;
			f6 = 0.5F;
			float f7 = 0.8F;
			float f8 = 0.6F;
			if(this.flipTexture || block1.shouldSideBeRendered(this.blockAccess, i2, i3 - 1, i4, 0)) {
				f10 = block1.getBlockBrightness(this.blockAccess, i2, i3 - 1, i4);
				this.tessellator.setColorOpaque_F(f6 * f10, f6 * f10, f6 * f10);
				this.renderBlockBottom(block1, i2, i3, i4, block1.getBlockTexture(0));
				z9 = true;
			}

			if(this.flipTexture || block1.shouldSideBeRendered(this.blockAccess, i2, i3 + 1, i4, 1)) {
				f10 = block1.getBlockBrightness(this.blockAccess, i2, i3 + 1, i4);
				this.tessellator.setColorOpaque_F(f10, f10, f10);
				this.renderBlockTop(block1, i2, i3, i4, block1.getBlockTexture(1));
				z9 = true;
			}

			if(this.flipTexture || block1.shouldSideBeRendered(this.blockAccess, i2, i3, i4 - 1, 2)) {
				f10 = block1.getBlockBrightness(this.blockAccess, i2, i3, i4 - 1);
				this.tessellator.setColorOpaque_F(f7 * f10, f7 * f10, f7 * f10);
				this.renderBlockNorth(block1, i2, i3, i4, block1.getBlockTexture(2));
				z9 = true;
			}

			if(this.flipTexture || block1.shouldSideBeRendered(this.blockAccess, i2, i3, i4 + 1, 3)) {
				f10 = block1.getBlockBrightness(this.blockAccess, i2, i3, i4 + 1);
				this.tessellator.setColorOpaque_F(f7 * f10, f7 * f10, f7 * f10);
				this.renderBlockSouth(block1, i2, i3, i4, block1.getBlockTexture(3));
				z9 = true;
			}

			if(this.flipTexture || block1.shouldSideBeRendered(this.blockAccess, i2 - 1, i3, i4, 4)) {
				f10 = block1.getBlockBrightness(this.blockAccess, i2 - 1, i3, i4);
				this.tessellator.setColorOpaque_F(f8 * f10, f8 * f10, f8 * f10);
				this.renderBlockWest(block1, i2, i3, i4, block1.getBlockTexture(4));
				z9 = true;
			}

			if(this.flipTexture || block1.shouldSideBeRendered(this.blockAccess, i2 + 1, i3, i4, 5)) {
				f10 = block1.getBlockBrightness(this.blockAccess, i2 + 1, i3, i4);
				this.tessellator.setColorOpaque_F(f8 * f10, f8 * f10, f8 * f10);
				this.renderBlockEast(block1, i2, i3, i4, block1.getBlockTexture(5));
				z9 = true;
			}

			return z9;
		}
	}

	private void renderBlockTorch(Block block1, float f2, float f3, float f4, float f5, float f6) {
		int i15 = block1.getBlockTexture(0);
		if(this.overrideBlockTexture >= 0) {
			i15 = this.overrideBlockTexture;
		}

		int i7 = (i15 & 15) << 4;
		i15 &= 240;
		float f8 = (float)i7 / 256.0F;
		float f17 = ((float)i7 + 15.99F) / 256.0F;
		float f9 = (float)i15 / 256.0F;
		float f16 = ((float)i15 + 15.99F) / 256.0F;
		f2 += 0.5F;
		f4 += 0.5F;
		float f10 = f2 - 0.5F;
		float f11 = f2 + 0.5F;
		float f12 = f4 - 0.5F;
		float f13 = f4 + 0.5F;
		float f14 = 0.0625F;
		this.tessellator.addVertexWithUV(f2 - f14, f3 + 1.0F, f12, f8, f9);
		this.tessellator.addVertexWithUV(f2 - f14 + f5, f3, f12 + f6, f8, f16);
		this.tessellator.addVertexWithUV(f2 - f14 + f5, f3, f13 + f6, f17, f16);
		this.tessellator.addVertexWithUV(f2 - f14, f3 + 1.0F, f13, f17, f9);
		this.tessellator.addVertexWithUV(f2 + f14, f3 + 1.0F, f13, f8, f9);
		this.tessellator.addVertexWithUV(f2 + f5 + f14, f3, f13 + f6, f8, f16);
		this.tessellator.addVertexWithUV(f2 + f5 + f14, f3, f12 + f6, f17, f16);
		this.tessellator.addVertexWithUV(f2 + f14, f3 + 1.0F, f12, f17, f9);
		this.tessellator.addVertexWithUV(f10, f3 + 1.0F, f4 + f14, f8, f9);
		this.tessellator.addVertexWithUV(f10 + f5, f3, f4 + f14 + f6, f8, f16);
		this.tessellator.addVertexWithUV(f11 + f5, f3, f4 + f14 + f6, f17, f16);
		this.tessellator.addVertexWithUV(f11, f3 + 1.0F, f4 + f14, f17, f9);
		this.tessellator.addVertexWithUV(f11, f3 + 1.0F, f4 - f14, f8, f9);
		this.tessellator.addVertexWithUV(f11 + f5, f3, f4 - f14 + f6, f8, f16);
		this.tessellator.addVertexWithUV(f10 + f5, f3, f4 - f14 + f6, f17, f16);
		this.tessellator.addVertexWithUV(f10, f3 + 1.0F, f4 - f14, f17, f9);
	}

	private void renderBlockPlant(Block block1, float f2, float f3, float f4) {
		int i10 = block1.getBlockTexture(0);
		if(this.overrideBlockTexture >= 0) {
			i10 = this.overrideBlockTexture;
		}

		int i5 = (i10 & 15) << 4;
		i10 &= 240;
		float f6 = (float)i5 / 256.0F;
		float f12 = ((float)i5 + 15.99F) / 256.0F;
		float f7 = (float)i10 / 256.0F;
		float f11 = ((float)i10 + 15.99F) / 256.0F;
		float f8 = f2 + 0.5F - 0.45F;
		f2 = f2 + 0.5F + 0.45F;
		float f9 = f4 + 0.5F - 0.45F;
		f4 = f4 + 0.5F + 0.45F;
		this.tessellator.addVertexWithUV(f8, f3 + 1.0F, f9, f6, f7);
		this.tessellator.addVertexWithUV(f8, f3, f9, f6, f11);
		this.tessellator.addVertexWithUV(f2, f3, f4, f12, f11);
		this.tessellator.addVertexWithUV(f2, f3 + 1.0F, f4, f12, f7);
		this.tessellator.addVertexWithUV(f2, f3 + 1.0F, f4, f6, f7);
		this.tessellator.addVertexWithUV(f2, f3, f4, f6, f11);
		this.tessellator.addVertexWithUV(f8, f3, f9, f12, f11);
		this.tessellator.addVertexWithUV(f8, f3 + 1.0F, f9, f12, f7);
		this.tessellator.addVertexWithUV(f8, f3 + 1.0F, f4, f6, f7);
		this.tessellator.addVertexWithUV(f8, f3, f4, f6, f11);
		this.tessellator.addVertexWithUV(f2, f3, f9, f12, f11);
		this.tessellator.addVertexWithUV(f2, f3 + 1.0F, f9, f12, f7);
		this.tessellator.addVertexWithUV(f2, f3 + 1.0F, f9, f6, f7);
		this.tessellator.addVertexWithUV(f2, f3, f9, f6, f11);
		this.tessellator.addVertexWithUV(f8, f3, f4, f12, f11);
		this.tessellator.addVertexWithUV(f8, f3 + 1.0F, f4, f12, f7);
	}

	private void renderBlockBottom(Block block1, int i2, int i3, int i4, int i5) {
		if(this.overrideBlockTexture >= 0) {
			i5 = this.overrideBlockTexture;
		}

		int i6 = (i5 & 15) << 4;
		i5 &= 240;
		float f7 = (float)i6 / 256.0F;
		float f14 = ((float)i6 + 15.99F) / 256.0F;
		float f8 = (float)i5 / 256.0F;
		float f15 = ((float)i5 + 15.99F) / 256.0F;
		float f9 = (float)i2 + block1.minX;
		float f12 = (float)i2 + block1.maxX;
		float f13 = (float)i3 + block1.minY;
		float f10 = (float)i4 + block1.minZ;
		float f11 = (float)i4 + block1.maxZ;
		this.tessellator.addVertexWithUV(f9, f13, f11, f7, f15);
		this.tessellator.addVertexWithUV(f9, f13, f10, f7, f8);
		this.tessellator.addVertexWithUV(f12, f13, f10, f14, f8);
		this.tessellator.addVertexWithUV(f12, f13, f11, f14, f15);
	}

	private void renderBlockTop(Block block1, int i2, int i3, int i4, int i5) {
		if(this.overrideBlockTexture >= 0) {
			i5 = this.overrideBlockTexture;
		}

		int i6 = (i5 & 15) << 4;
		i5 &= 240;
		float f7 = (float)i6 / 256.0F;
		float f14 = ((float)i6 + 15.99F) / 256.0F;
		float f8 = (float)i5 / 256.0F;
		float f15 = ((float)i5 + 15.99F) / 256.0F;
		float f9 = (float)i2 + block1.minX;
		float f12 = (float)i2 + block1.maxX;
		float f13 = (float)i3 + block1.maxY;
		float f10 = (float)i4 + block1.minZ;
		float f11 = (float)i4 + block1.maxZ;
		this.tessellator.addVertexWithUV(f12, f13, f11, f14, f15);
		this.tessellator.addVertexWithUV(f12, f13, f10, f14, f8);
		this.tessellator.addVertexWithUV(f9, f13, f10, f7, f8);
		this.tessellator.addVertexWithUV(f9, f13, f11, f7, f15);
	}

	private void renderBlockNorth(Block block1, int i2, int i3, int i4, int i5) {
		if(this.overrideBlockTexture >= 0) {
			i5 = this.overrideBlockTexture;
		}

		int i6 = (i5 & 15) << 4;
		i5 &= 240;
		float f7 = (float)i6 / 256.0F;
		float f14 = ((float)i6 + 15.99F) / 256.0F;
		float f8 = 0.0F;
		float f9 = 0.0F;
		if(block1.minY >= 0.0F && block1.maxY <= 1.0F) {
			f8 = ((float)i5 + block1.minY * 15.99F) / 256.0F;
			f9 = ((float)i5 + block1.maxY * 15.99F) / 256.0F;
		} else {
			f8 = (float)i5 / 256.0F;
			f9 = ((float)i5 + 15.99F) / 256.0F;
		}

		float f15 = (float)i2 + block1.minX;
		float f12 = (float)i2 + block1.maxX;
		float f10 = (float)i3 + block1.minY;
		float f13 = (float)i3 + block1.maxY;
		float f11 = (float)i4 + block1.minZ;
		this.tessellator.addVertexWithUV(f15, f13, f11, f14, f8);
		this.tessellator.addVertexWithUV(f12, f13, f11, f7, f8);
		this.tessellator.addVertexWithUV(f12, f10, f11, f7, f9);
		this.tessellator.addVertexWithUV(f15, f10, f11, f14, f9);
	}

	private void renderBlockSouth(Block block1, int i2, int i3, int i4, int i5) {
		if(this.overrideBlockTexture >= 0) {
			i5 = this.overrideBlockTexture;
		}

		int i6 = (i5 & 15) << 4;
		i5 &= 240;
		float f7 = (float)i6 / 256.0F;
		float f14 = ((float)i6 + 15.99F) / 256.0F;
		float f8 = 0.0F;
		float f9 = 0.0F;
		if(block1.minY >= 0.0F && block1.maxY <= 1.0F) {
			f8 = ((float)i5 + block1.minY * 15.99F) / 256.0F;
			f9 = ((float)i5 + block1.maxY * 15.99F) / 256.0F;
		} else {
			f8 = (float)i5 / 256.0F;
			f9 = ((float)i5 + 15.99F) / 256.0F;
		}

		float f15 = (float)i2 + block1.minX;
		float f12 = (float)i2 + block1.maxX;
		float f10 = (float)i3 + block1.minY;
		float f13 = (float)i3 + block1.maxY;
		float f11 = (float)i4 + block1.maxZ;
		this.tessellator.addVertexWithUV(f15, f13, f11, f7, f8);
		this.tessellator.addVertexWithUV(f15, f10, f11, f7, f9);
		this.tessellator.addVertexWithUV(f12, f10, f11, f14, f9);
		this.tessellator.addVertexWithUV(f12, f13, f11, f14, f8);
	}

	private void renderBlockWest(Block block1, int i2, int i3, int i4, int i5) {
		if(this.overrideBlockTexture >= 0) {
			i5 = this.overrideBlockTexture;
		}

		int i6 = (i5 & 15) << 4;
		i5 &= 240;
		float f7 = (float)i6 / 256.0F;
		float f14 = ((float)i6 + 15.99F) / 256.0F;
		float f8 = 0.0F;
		float f9 = 0.0F;
		if(block1.minY >= 0.0F && block1.maxY <= 1.0F) {
			f8 = ((float)i5 + block1.minY * 15.99F) / 256.0F;
			f9 = ((float)i5 + block1.maxY * 15.99F) / 256.0F;
		} else {
			f8 = (float)i5 / 256.0F;
			f9 = ((float)i5 + 15.99F) / 256.0F;
		}

		float f12 = (float)i2 + block1.minX;
		float f15 = (float)i3 + block1.minY;
		float f13 = (float)i3 + block1.maxY;
		float f10 = (float)i4 + block1.minZ;
		float f11 = (float)i4 + block1.maxZ;
		this.tessellator.addVertexWithUV(f12, f13, f11, f14, f8);
		this.tessellator.addVertexWithUV(f12, f13, f10, f7, f8);
		this.tessellator.addVertexWithUV(f12, f15, f10, f7, f9);
		this.tessellator.addVertexWithUV(f12, f15, f11, f14, f9);
	}

	private void renderBlockEast(Block block1, int i2, int i3, int i4, int i5) {
		if(this.overrideBlockTexture >= 0) {
			i5 = this.overrideBlockTexture;
		}

		int i6 = (i5 & 15) << 4;
		i5 &= 240;
		float f7 = (float)i6 / 256.0F;
		float f14 = ((float)i6 + 15.99F) / 256.0F;
		float f8 = 0.0F;
		float f9 = 0.0F;
		if(block1.minY >= 0.0F && block1.maxY <= 1.0F) {
			f8 = ((float)i5 + block1.minY * 15.99F) / 256.0F;
			f9 = ((float)i5 + block1.maxY * 15.99F) / 256.0F;
		} else {
			f8 = (float)i5 / 256.0F;
			f9 = ((float)i5 + 15.99F) / 256.0F;
		}

		float f12 = (float)i2 + block1.maxX;
		float f15 = (float)i3 + block1.minY;
		float f13 = (float)i3 + block1.maxY;
		float f10 = (float)i4 + block1.minZ;
		float f11 = (float)i4 + block1.maxZ;
		this.tessellator.addVertexWithUV(f12, f15, f11, f7, f9);
		this.tessellator.addVertexWithUV(f12, f15, f10, f14, f9);
		this.tessellator.addVertexWithUV(f12, f13, f10, f14, f8);
		this.tessellator.addVertexWithUV(f12, f13, f11, f7, f8);
	}

	public final void renderBlockOnInventory(Block block1) {
		int i2;
		if((i2 = block1.getRenderType()) == 0) {
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			this.tessellator.startDrawingQuads();
			Tessellator.setNormal(0.0F, -1.0F, 0.0F);
			this.renderBlockBottom(block1, 0, 0, 0, block1.getBlockTexture(0));
			Tessellator.setNormal(0.0F, 1.0F, 0.0F);
			this.renderBlockTop(block1, 0, 0, 0, block1.getBlockTexture(1));
			Tessellator.setNormal(0.0F, 0.0F, -1.0F);
			this.renderBlockNorth(block1, 0, 0, 0, block1.getBlockTexture(2));
			Tessellator.setNormal(0.0F, 0.0F, 1.0F);
			this.renderBlockSouth(block1, 0, 0, 0, block1.getBlockTexture(3));
			Tessellator.setNormal(-1.0F, 0.0F, 0.0F);
			this.renderBlockWest(block1, 0, 0, 0, block1.getBlockTexture(4));
			Tessellator.setNormal(0.0F, 0.0F, 0.0F);
			this.renderBlockEast(block1, 0, 0, 0, block1.getBlockTexture(5));
			this.tessellator.draw();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		} else if(i2 == 1) {
			this.tessellator.startDrawingQuads();
			Tessellator.setNormal(0.0F, -1.0F, 0.0F);
			this.renderBlockPlant(block1, -0.5F, -0.5F, -0.5F);
			this.tessellator.draw();
		} else {
			if(i2 == 2) {
				this.tessellator.startDrawingQuads();
				Tessellator.setNormal(0.0F, -1.0F, 0.0F);
				this.renderBlockTorch(block1, -0.5F, -0.5F, -0.5F, 0.0F, 0.0F);
				this.tessellator.draw();
			}

		}
	}
}