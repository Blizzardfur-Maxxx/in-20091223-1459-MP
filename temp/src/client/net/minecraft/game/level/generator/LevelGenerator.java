package net.minecraft.game.level.generator;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.game.level.block.Block;

import util.MathHelper;

public final class LevelGenerator {
	public LoadingScreenRenderer progressBar;
	public int width;
	public int depth;
	public int height;
	public Random rand = new Random();
	public byte[] blocksByteArray;
	public int waterLevel;
	private int[] floodFillBlocks = new int[1048576];

	public LevelGenerator(LoadingScreenRenderer loadingScreenRenderer1) {
		this.progressBar = loadingScreenRenderer1;
	}

	public void populateOre(int i1, int i2, int i3, int i4) {
		byte b25 = (byte)i1;
		i4 = this.width;
		int i5 = this.depth;
		int i6 = this.height;
		int i7 = i4 * i5 * i6 / 256 / 64 * i2 / 100;

		for(int i8 = 0; i8 < i7; ++i8) {
			this.setNextPhase(i8 * 100 / (i7 - 1) / 4 + i3 * 100 / 4);
			float f9 = this.rand.nextFloat() * (float)i4;
			float f10 = this.rand.nextFloat() * (float)i6;
			float f11 = this.rand.nextFloat() * (float)i5;
			int i12 = (int)((this.rand.nextFloat() + this.rand.nextFloat()) * 75.0F * (float)i2 / 100.0F);
			float f13 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
			float f14 = 0.0F;
			float f15 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
			float f16 = 0.0F;

			for(int i17 = 0; i17 < i12; ++i17) {
				f9 += MathHelper.sin(f13) * MathHelper.cos(f15);
				f11 += MathHelper.cos(f13) * MathHelper.cos(f15);
				f10 += MathHelper.sin(f15);
				f13 += f14 * 0.2F;
				f14 = f14 * 0.9F + (this.rand.nextFloat() - this.rand.nextFloat());
				f15 = (f15 + f16 * 0.5F) * 0.5F;
				f16 = f16 * 0.9F + (this.rand.nextFloat() - this.rand.nextFloat());
				float f18 = MathHelper.sin((float)i17 * (float)Math.PI / (float)i12) * (float)i2 / 100.0F + 1.0F;

				for(int i19 = (int)(f9 - f18); i19 <= (int)(f9 + f18); ++i19) {
					for(int i20 = (int)(f10 - f18); i20 <= (int)(f10 + f18); ++i20) {
						for(int i21 = (int)(f11 - f18); i21 <= (int)(f11 + f18); ++i21) {
							float f22 = (float)i19 - f9;
							float f23 = (float)i20 - f10;
							float f24 = (float)i21 - f11;
							if(f22 * f22 + f23 * f23 * 2.0F + f24 * f24 < f18 * f18 && i19 >= 1 && i20 >= 1 && i21 >= 1 && i19 < this.width - 1 && i20 < this.height - 1 && i21 < this.depth - 1) {
								int i26 = (i20 * this.depth + i21) * this.width + i19;
								if(this.blocksByteArray[i26] == Block.stone.blockID) {
									this.blocksByteArray[i26] = b25;
								}
							}
						}
					}
				}
			}
		}

	}

	public void setNextPhase(int i1) {
		this.progressBar.setLoadingProgress(i1);
	}

	public long floodFill(int i1, int i2, int i3, int i4, int i5) {
		byte b20 = (byte)i5;
		ArrayList arrayList21 = new ArrayList();
		byte b6 = 0;
		int i7 = 1;

		int i8;
		for(i8 = 1; 1 << i7 < this.width; ++i7) {
		}

		while(1 << i8 < this.depth) {
			++i8;
		}

		int i9 = this.depth - 1;
		int i10 = this.width - 1;
		int i22 = b6 + 1;
		this.floodFillBlocks[0] = ((i2 << i8) + i3 << i7) + i1;
		long j13 = 0L;
		i1 = this.width * this.depth;

		while(i22 > 0) {
			--i22;
			i2 = this.floodFillBlocks[i22];
			if(i22 == 0 && arrayList21.size() > 0) {
				this.floodFillBlocks = (int[])arrayList21.remove(arrayList21.size() - 1);
				i22 = this.floodFillBlocks.length;
			}

			i3 = i2 >> i7 & i9;
			int i11 = i2 >> i7 + i8;

			int i12;
			int i15;
			for(i15 = i12 = i2 & i10; i12 > 0 && this.blocksByteArray[i2 - 1] == 0; --i2) {
				--i12;
			}

			while(i15 < this.width && this.blocksByteArray[i2 + i15 - i12] == 0) {
				++i15;
			}

			int i16 = i2 >> i7 & i9;
			int i17 = i2 >> i7 + i8;
			if(i16 != i3 || i17 != i11) {
				System.out.println("Diagonal flood!?");
			}

			boolean z23 = false;
			boolean z24 = false;
			boolean z18 = false;

			for(j13 += (long)(i15 - i12); i12 < i15; ++i12) {
				this.blocksByteArray[i2] = b20;
				boolean z19;
				if(i3 > 0) {
					if((z19 = this.blocksByteArray[i2 - this.width] == 0) && !z23) {
						if(i22 == this.floodFillBlocks.length) {
							arrayList21.add(this.floodFillBlocks);
							this.floodFillBlocks = new int[1048576];
							i22 = 0;
						}

						this.floodFillBlocks[i22++] = i2 - this.width;
					}

					z23 = z19;
				}

				if(i3 < this.depth - 1) {
					if((z19 = this.blocksByteArray[i2 + this.width] == 0) && !z24) {
						if(i22 == this.floodFillBlocks.length) {
							arrayList21.add(this.floodFillBlocks);
							this.floodFillBlocks = new int[1048576];
							i22 = 0;
						}

						this.floodFillBlocks[i22++] = i2 + this.width;
					}

					z24 = z19;
				}

				if(i11 > 0) {
					byte b25 = this.blocksByteArray[i2 - i1];
					if((b20 == Block.lavaMoving.blockID || b20 == Block.lavaStill.blockID) && (b25 == Block.waterMoving.blockID || b25 == Block.waterStill.blockID)) {
						this.blocksByteArray[i2 - i1] = (byte)Block.stone.blockID;
					}

					if((z19 = b25 == 0) && !z18) {
						if(i22 == this.floodFillBlocks.length) {
							arrayList21.add(this.floodFillBlocks);
							this.floodFillBlocks = new int[1048576];
							i22 = 0;
						}

						this.floodFillBlocks[i22++] = i2 - i1;
					}

					z18 = z19;
				}

				++i2;
			}
		}

		return j13;
	}
}