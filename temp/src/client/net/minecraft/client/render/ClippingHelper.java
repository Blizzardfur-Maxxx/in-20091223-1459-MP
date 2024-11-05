package net.minecraft.client.render;

public class ClippingHelper {
	public float[][] frustrum = new float[16][16];
	public float[] projectionMatrix = new float[16];
	public float[] modelviewMatrix = new float[16];
	public float[] clippingMatrix = new float[16];

	public final boolean isBoundingBoxInFrustrum(float f1, float f2, float f3, float f4, float f5, float f6) {
		for(int i7 = 0; i7 < 6; ++i7) {
			if(this.frustrum[i7][0] * f1 + this.frustrum[i7][1] * f2 + this.frustrum[i7][2] * f3 + this.frustrum[i7][3] <= 0.0F && this.frustrum[i7][0] * f4 + this.frustrum[i7][1] * f2 + this.frustrum[i7][2] * f3 + this.frustrum[i7][3] <= 0.0F && this.frustrum[i7][0] * f1 + this.frustrum[i7][1] * f5 + this.frustrum[i7][2] * f3 + this.frustrum[i7][3] <= 0.0F && this.frustrum[i7][0] * f4 + this.frustrum[i7][1] * f5 + this.frustrum[i7][2] * f3 + this.frustrum[i7][3] <= 0.0F && this.frustrum[i7][0] * f1 + this.frustrum[i7][1] * f2 + this.frustrum[i7][2] * f6 + this.frustrum[i7][3] <= 0.0F && this.frustrum[i7][0] * f4 + this.frustrum[i7][1] * f2 + this.frustrum[i7][2] * f6 + this.frustrum[i7][3] <= 0.0F && this.frustrum[i7][0] * f1 + this.frustrum[i7][1] * f5 + this.frustrum[i7][2] * f6 + this.frustrum[i7][3] <= 0.0F && this.frustrum[i7][0] * f4 + this.frustrum[i7][1] * f5 + this.frustrum[i7][2] * f6 + this.frustrum[i7][3] <= 0.0F) {
				return false;
			}
		}

		return true;
	}
}