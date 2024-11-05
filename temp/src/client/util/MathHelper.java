package util;

public final class MathHelper {
	private static float[] SIN_TABLE = new float[65536];

	public static final float sin(float f0) {
		return SIN_TABLE[(int)(f0 * 10430.378F) & 65535];
	}

	public static final float cos(float f0) {
		return SIN_TABLE[(int)(f0 * 10430.378F + 16384.0F) & 65535];
	}

	public static final float sqrt_float(float f0) {
		return (float)Math.sqrt((double)f0);
	}

	static {
		for(int i0 = 0; i0 < 65536; ++i0) {
			SIN_TABLE[i0] = (float)Math.sin((double)i0 * Math.PI * 2.0D / 65536.0D);
		}

	}
}