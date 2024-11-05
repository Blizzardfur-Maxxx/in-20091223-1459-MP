package net.minecraft.client.model.md3;

import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public final class MD3Model {
	private MD3Vertices vertices;
	private int displayList = 0;

	public MD3Model(MD3Vertices mD3Vertices1) {
		new HashMap();
		BufferUtils.createFloatBuffer(16);
		this.vertices = mD3Vertices1;
	}

	public final void renderModelVertices(int i1, int i2, float f3) {
		if(this.displayList == 0) {
			MD3Model mD3Model6 = this;
			this.displayList = GL11.glGenLists(this.vertices.totalFrames);

			for(i2 = 0; i2 < mD3Model6.vertices.totalFrames; ++i2) {
				GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
				GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
				GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
				GL11.glNewList(mD3Model6.displayList + i2, GL11.GL_COMPILE);

				for(int i7 = 0; i7 < mD3Model6.vertices.buffersMD3.length; ++i7) {
					MD3Buffers mD3Buffers10000 = mD3Model6.vertices.buffersMD3[i7];
					boolean z5 = false;
					MD3Buffers mD3Buffers4 = mD3Buffers10000;
					MD3Buffers mD3Buffers8 = mD3Buffers10000;
					mD3Buffers10000.triangles.position(0).limit(mD3Buffers8.triangles.capacity());
					mD3Buffers8.xBuffer.position(0).limit(mD3Buffers8.xBuffer.capacity());
					mD3Buffers8.vertices.clear().position(0 * mD3Buffers8.verts * 3).limit(1 * mD3Buffers8.verts * 3);
					mD3Buffers8.normals.clear().position(0 * mD3Buffers8.verts * 3).limit(1 * mD3Buffers8.verts * 3);
					mD3Buffers4.vertices.position(0);
					mD3Buffers4.triangles.position(0);
					mD3Buffers4.normals.position(0);
					mD3Buffers4.xBuffer.position(0);
					GL11.glVertexPointer(3, 0, mD3Buffers4.vertices);
					GL11.glNormalPointer(0, mD3Buffers4.normals);
					GL11.glTexCoordPointer(2, 0, mD3Buffers4.xBuffer);
					GL11.glDrawElements(GL11.GL_TRIANGLES, mD3Buffers4.triangles);
				}

				GL11.glEndList();
				GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
				GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
				GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
			}
		}

		GL11.glCallList(this.displayList);
	}
}