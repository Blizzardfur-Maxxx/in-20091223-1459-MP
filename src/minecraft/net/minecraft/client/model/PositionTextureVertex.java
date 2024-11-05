package net.minecraft.client.model;

import net.minecraft.game.physics.Vec3D;

public final class PositionTextureVertex {
	private Vec3D vec3D;

	public PositionTextureVertex(float f1, float f2, float f3, float f4, float f5) {
		this(new Vec3D(f1, f2, f3));
	}

	public final PositionTextureVertex setTexturePosition(float f1, float f2) {
		return new PositionTextureVertex(this);
	}

	private PositionTextureVertex(PositionTextureVertex positionTextureVertex1) {
		this.vec3D = positionTextureVertex1.vec3D;
	}

	private PositionTextureVertex(Vec3D vec3D1) {
		this.vec3D = vec3D1;
	}
}