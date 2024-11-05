package net.minecraft.client.model.md3;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public final class MD3Buffers {
	public int verts;
	public int frames;
	public MD3Shader[] shaders;
	public IntBuffer triangles;
	public FloatBuffer xBuffer;
	public FloatBuffer vertices;
	public FloatBuffer normals;
	private float[] data1;
	private float[] data2;

	public MD3Buffers(int i1, int i2, int i3) {
		this.verts = i2;
		this.frames = i3;
		this.triangles = BufferUtils.createIntBuffer(i1 * 3);
		this.xBuffer = BufferUtils.createFloatBuffer(i2 << 1);
		this.vertices = BufferUtils.createFloatBuffer(i2 * (i3 + 2) * 3);
		this.normals = BufferUtils.createFloatBuffer(i2 * (i3 + 2) * 3);
		this.data1 = new float[i2 * 3];
		this.data2 = new float[i2 * 3];
	}
}