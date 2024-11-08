package net.minecraft.game.physics;

import util.MathHelper;

public final class Vec3D {
	public float xCoord;
	public float yCoord;
	public float zCoord;

	public Vec3D(float f1, float f2, float f3) {
		this.xCoord = f1;
		this.yCoord = f2;
		this.zCoord = f3;
	}

	public final Vec3D addVector(float f1, float f2, float f3) {
		return new Vec3D(this.xCoord + f1, this.yCoord + f2, this.zCoord + f3);
	}

	public final float distanceTo(Vec3D vec3D1) {
		float f2 = vec3D1.xCoord - this.xCoord;
		float f3 = vec3D1.yCoord - this.yCoord;
		float f4 = vec3D1.zCoord - this.zCoord;
		return MathHelper.sqrt_float(f2 * f2 + f3 * f3 + f4 * f4);
	}

	public final float squaredDistanceTo(Vec3D vec3D1) {
		float f2 = vec3D1.xCoord - this.xCoord;
		float f3 = vec3D1.yCoord - this.yCoord;
		float f4 = vec3D1.zCoord - this.zCoord;
		return f2 * f2 + f3 * f3 + f4 * f4;
	}

	public final Vec3D getIntermediateWithXValue(Vec3D vec3D1, float f2) {
		float f3 = vec3D1.xCoord - this.xCoord;
		float f4 = vec3D1.yCoord - this.yCoord;
		float f5 = vec3D1.zCoord - this.zCoord;
		return f3 * f3 < 1.0E-7F ? null : ((f2 = (f2 - this.xCoord) / f3) >= 0.0F && f2 <= 1.0F ? new Vec3D(this.xCoord + f3 * f2, this.yCoord + f4 * f2, this.zCoord + f5 * f2) : null);
	}

	public final Vec3D getIntermediateWithYValue(Vec3D vec3D1, float f2) {
		float f3 = vec3D1.xCoord - this.xCoord;
		float f4 = vec3D1.yCoord - this.yCoord;
		float f5 = vec3D1.zCoord - this.zCoord;
		return f4 * f4 < 1.0E-7F ? null : ((f2 = (f2 - this.yCoord) / f4) >= 0.0F && f2 <= 1.0F ? new Vec3D(this.xCoord + f3 * f2, this.yCoord + f4 * f2, this.zCoord + f5 * f2) : null);
	}

	public final Vec3D getIntermediateWithZValue(Vec3D vec3D1, float f2) {
		float f3 = vec3D1.xCoord - this.xCoord;
		float f4 = vec3D1.yCoord - this.yCoord;
		float f5;
		float f10000 = f5 = vec3D1.zCoord - this.zCoord;
		return f10000 * f10000 < 1.0E-7F ? null : ((f2 = (f2 - this.zCoord) / f5) >= 0.0F && f2 <= 1.0F ? new Vec3D(this.xCoord + f3 * f2, this.yCoord + f4 * f2, this.zCoord + f5 * f2) : null);
	}

	public final String toString() {
		return "(" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")";
	}
}