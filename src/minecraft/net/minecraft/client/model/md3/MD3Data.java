package net.minecraft.client.model.md3;

import net.minecraft.game.physics.Vec3D;

public final class MD3Data {
	public String name;
	public Vec3D[] b;
	public Vec3D[] c;
	public Vec3D[] d;
	public Vec3D[] e;

	public MD3Data(int i1) {
		this.b = new Vec3D[i1];
		this.c = new Vec3D[i1];
		this.d = new Vec3D[i1];
		this.e = new Vec3D[i1];
	}
}