package net.minecraft.game.physics;

public final class AxisAlignedBB {
	public float x0;
	public float y0;
	public float z0;
	public float x1;
	public float y1;
	public float z1;

	public AxisAlignedBB(float f1, float f2, float f3, float f4, float f5, float f6) {
		this.x0 = f1;
		this.y0 = f2;
		this.z0 = f3;
		this.x1 = f4;
		this.y1 = f5;
		this.z1 = f6;
	}

	public final AxisAlignedBB addCoord(float f1, float f2, float f3) {
		float f4 = this.x0;
		float f5 = this.y0;
		float f6 = this.z0;
		float f7 = this.x1;
		float f8 = this.y1;
		float f9 = this.z1;
		if(f1 < 0.0F) {
			f4 += f1;
		}

		if(f1 > 0.0F) {
			f7 += f1;
		}

		if(f2 < 0.0F) {
			f5 += f2;
		}

		if(f2 > 0.0F) {
			f8 += f2;
		}

		if(f3 < 0.0F) {
			f6 += f3;
		}

		if(f3 > 0.0F) {
			f9 += f3;
		}

		return new AxisAlignedBB(f4, f5, f6, f7, f8, f9);
	}

	public final AxisAlignedBB expand(float f1, float f2, float f3) {
		float f4 = this.x0 - f1;
		float f5 = this.y0 - f2;
		float f6 = this.z0 - f3;
		f1 += this.x1;
		f2 += this.y1;
		float f7 = this.z1 + f3;
		return new AxisAlignedBB(f4, f5, f6, f1, f2, f7);
	}

	public final float clipXCollide(AxisAlignedBB axisAlignedBB1, float f2) {
		if(axisAlignedBB1.y1 > this.y0 && axisAlignedBB1.y0 < this.y1) {
			if(axisAlignedBB1.z1 > this.z0 && axisAlignedBB1.z0 < this.z1) {
				float f3;
				if(f2 > 0.0F && axisAlignedBB1.x1 <= this.x0 && (f3 = this.x0 - axisAlignedBB1.x1) < f2) {
					f2 = f3;
				}

				if(f2 < 0.0F && axisAlignedBB1.x0 >= this.x1 && (f3 = this.x1 - axisAlignedBB1.x0) > f2) {
					f2 = f3;
				}

				return f2;
			} else {
				return f2;
			}
		} else {
			return f2;
		}
	}

	public final float clipYCollide(AxisAlignedBB axisAlignedBB1, float f2) {
		if(axisAlignedBB1.x1 > this.x0 && axisAlignedBB1.x0 < this.x1) {
			if(axisAlignedBB1.z1 > this.z0 && axisAlignedBB1.z0 < this.z1) {
				float f3;
				if(f2 > 0.0F && axisAlignedBB1.y1 <= this.y0 && (f3 = this.y0 - axisAlignedBB1.y1) < f2) {
					f2 = f3;
				}

				if(f2 < 0.0F && axisAlignedBB1.y0 >= this.y1 && (f3 = this.y1 - axisAlignedBB1.y0) > f2) {
					f2 = f3;
				}

				return f2;
			} else {
				return f2;
			}
		} else {
			return f2;
		}
	}

	public final float clipZCollide(AxisAlignedBB axisAlignedBB1, float f2) {
		if(axisAlignedBB1.x1 > this.x0 && axisAlignedBB1.x0 < this.x1) {
			if(axisAlignedBB1.y1 > this.y0 && axisAlignedBB1.y0 < this.y1) {
				float f3;
				if(f2 > 0.0F && axisAlignedBB1.z1 <= this.z0 && (f3 = this.z0 - axisAlignedBB1.z1) < f2) {
					f2 = f3;
				}

				if(f2 < 0.0F && axisAlignedBB1.z0 >= this.z1 && (f3 = this.z1 - axisAlignedBB1.z0) > f2) {
					f2 = f3;
				}

				return f2;
			} else {
				return f2;
			}
		} else {
			return f2;
		}
	}

	public final boolean intersectsWith(AxisAlignedBB axisAlignedBB1) {
		return axisAlignedBB1.x1 >= this.x0 && axisAlignedBB1.x0 <= this.x1 ? (axisAlignedBB1.y1 >= this.y0 && axisAlignedBB1.y0 <= this.y1 ? axisAlignedBB1.z1 >= this.z0 && axisAlignedBB1.z0 <= this.z1 : false) : false;
	}

	public final void offset(float f1, float f2, float f3) {
		this.x0 += f1;
		this.y0 += f2;
		this.z0 += f3;
		this.x1 += f1;
		this.y1 += f2;
		this.z1 += f3;
	}

	public final AxisAlignedBB copy() {
		return new AxisAlignedBB(this.x0, this.y0, this.z0, this.x1, this.y1, this.z1);
	}

	public final boolean isVecInYZ(Vec3D vec3D1) {
		return vec3D1 == null ? false : vec3D1.yCoord >= this.y0 && vec3D1.yCoord <= this.y1 && vec3D1.zCoord >= this.z0 && vec3D1.zCoord <= this.z1;
	}

	public final boolean isVecInXZ(Vec3D vec3D1) {
		return vec3D1 == null ? false : vec3D1.xCoord >= this.x0 && vec3D1.xCoord <= this.x1 && vec3D1.zCoord >= this.z0 && vec3D1.zCoord <= this.z1;
	}

	public final boolean isVecInXY(Vec3D vec3D1) {
		return vec3D1 == null ? false : vec3D1.xCoord >= this.x0 && vec3D1.xCoord <= this.x1 && vec3D1.yCoord >= this.y0 && vec3D1.yCoord <= this.y1;
	}
}