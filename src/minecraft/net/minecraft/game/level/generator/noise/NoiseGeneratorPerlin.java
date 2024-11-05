package net.minecraft.game.level.generator.noise;

import java.util.Random;

public final class NoiseGeneratorPerlin extends NoiseGenerator {
	private int[] permutations;

	public NoiseGeneratorPerlin() {
		this(new Random());
	}

	public NoiseGeneratorPerlin(Random random1) {
		this.permutations = new int[512];

		int i2;
		for(i2 = 0; i2 < 256; this.permutations[i2] = i2++) {
		}

		for(i2 = 0; i2 < 256; ++i2) {
			int i3 = random1.nextInt(256 - i2) + i2;
			int i4 = this.permutations[i2];
			this.permutations[i2] = this.permutations[i3];
			this.permutations[i3] = i4;
			this.permutations[i2 + 256] = this.permutations[i2];
		}

	}

	private static double generateNoise(double d0) {
		return d0 * d0 * d0 * (d0 * (d0 * 6.0D - 15.0D) + 10.0D);
	}

	private static double lerp(double d0, double d2, double d4) {
		return d2 + d0 * (d4 - d2);
	}

	private static double grad(int i0, double d1, double d3, double d5) {
		double d8 = (i0 &= 15) < 8 ? d1 : d3;
		double d10 = i0 < 4 ? d3 : (i0 != 12 && i0 != 14 ? d5 : d1);
		return ((i0 & 1) == 0 ? d8 : -d8) + ((i0 & 2) == 0 ? d10 : -d10);
	}

	public final double generateNoise(double d1, double d3) {
		double d10 = 0.0D;
		double d8 = d3;
		double d6 = d1;
		int i18 = (int)Math.floor(d1) & 255;
		int i2 = (int)Math.floor(d3) & 255;
		int i19 = (int)Math.floor(0.0D) & 255;
		d6 -= Math.floor(d6);
		d8 -= Math.floor(d8);
		d10 = 0.0D - Math.floor(0.0D);
		double d12 = generateNoise(d6);
		double d14 = generateNoise(d8);
		double d16 = generateNoise(d10);
		int i4 = this.permutations[i18] + i2;
		int i5 = this.permutations[i4] + i19;
		i4 = this.permutations[i4 + 1] + i19;
		i18 = this.permutations[i18 + 1] + i2;
		i2 = this.permutations[i18] + i19;
		i18 = this.permutations[i18 + 1] + i19;
		return lerp(d16, lerp(d14, lerp(d12, grad(this.permutations[i5], d6, d8, d10), grad(this.permutations[i2], d6 - 1.0D, d8, d10)), lerp(d12, grad(this.permutations[i4], d6, d8 - 1.0D, d10), grad(this.permutations[i18], d6 - 1.0D, d8 - 1.0D, d10))), lerp(d14, lerp(d12, grad(this.permutations[i5 + 1], d6, d8, d10 - 1.0D), grad(this.permutations[i2 + 1], d6 - 1.0D, d8, d10 - 1.0D)), lerp(d12, grad(this.permutations[i4 + 1], d6, d8 - 1.0D, d10 - 1.0D), grad(this.permutations[i18 + 1], d6 - 1.0D, d8 - 1.0D, d10 - 1.0D))));
	}
}