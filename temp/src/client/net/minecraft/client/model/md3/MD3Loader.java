package net.minecraft.client.model.md3;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;

import net.minecraft.game.physics.Vec3D;

public class MD3Loader {
	public final MD3Vertices loadModel(String string1) throws IOException {
		DataInputStream dataInputStream5 = new DataInputStream(MD3Loader.class.getResourceAsStream(string1));
		ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
		byte[] b3 = new byte[4096];
		boolean z4 = false;

		int i7;
		while((i7 = dataInputStream5.read(b3)) >= 0) {
			byteArrayOutputStream2.write(b3, 0, i7);
		}

		dataInputStream5.close();
		byteArrayOutputStream2.close();
		ByteBuffer byteBuffer6 = ByteBuffer.wrap(byteArrayOutputStream2.toByteArray());
		return this.loadMD3Data(byteBuffer6);
	}

	private MD3Vertices loadMD3Data(ByteBuffer byteBuffer1) throws IOException {
		byteBuffer1.order(ByteOrder.LITTLE_ENDIAN);
		String string10000 = loadMD3Info(byteBuffer1, 4);
		MD3Vertices mD3Vertices2 = null;
		if(!string10000.equals("IDP3")) {
			throw new IOException("Not a valid MD3 file (bad magic number)");
		} else {
			mD3Vertices2 = new MD3Vertices();
			byteBuffer1.getInt();
			loadMD3Info(byteBuffer1, 64);
			byteBuffer1.getInt();
			int i3 = byteBuffer1.getInt();
			System.out.println(i3 + " frames");
			int i4 = byteBuffer1.getInt();
			int i5 = byteBuffer1.getInt();
			byteBuffer1.getInt();
			int i6 = byteBuffer1.getInt();
			byteBuffer1.getInt();
			int i7 = byteBuffer1.getInt();
			byteBuffer1.getInt();
			mD3Vertices2.totalFrames = i3;
			mD3Vertices2.frameArray = new MD3FrameArray[i3];
			mD3Vertices2.modelMap = new HashMap();
			mD3Vertices2.buffersMD3 = new MD3Buffers[i5];
			byteBuffer1.position(i6);

			for(i6 = 0; i6 < i3; ++i6) {
				MD3FrameArray[] mD3FrameArray14 = mD3Vertices2.frameArray;
				MD3FrameArray mD3FrameArray11 = new MD3FrameArray();
				getMD3Vec(byteBuffer1);
				getMD3Vec(byteBuffer1);
				getMD3Vec(byteBuffer1);
				byteBuffer1.getFloat();
				loadMD3Info(byteBuffer1, 16);
				mD3FrameArray14[i6] = mD3FrameArray11;
			}

			MD3Data[] mD3Data13 = new MD3Data[i4];

			int i8;
			for(i8 = 0; i8 < i4; ++i8) {
				mD3Data13[i8] = new MD3Data(i3);
			}

			for(i8 = 0; i8 < i3; ++i8) {
				for(int i9 = 0; i9 < i4; ++i9) {
					MD3Data mD3Data10;
					(mD3Data10 = mD3Data13[i9]).name = loadMD3Info(byteBuffer1, 64);
					mD3Data10.b[i8] = getMD3Vec(byteBuffer1);
					mD3Data10.c[i8] = getMD3Vec(byteBuffer1);
					mD3Data10.d[i8] = getMD3Vec(byteBuffer1);
					mD3Data10.e[i8] = getMD3Vec(byteBuffer1);
				}
			}

			for(i8 = 0; i8 < i4; ++i8) {
				mD3Vertices2.modelMap.put(mD3Data13[i8].name, mD3Data13[i8]);
			}

			byteBuffer1.position(i7);

			for(i8 = 0; i8 < i5; ++i8) {
				mD3Vertices2.buffersMD3[i8] = this.getMD3Buffer(byteBuffer1, i7);
			}

			return mD3Vertices2;
		}
	}

	private MD3Buffers getMD3Buffer(ByteBuffer byteBuffer1, int i2) throws IOException {
		i2 = byteBuffer1.position();
		String string10000 = loadMD3Info(byteBuffer1, 4);
		String string3 = null;
		if(!string10000.equals("IDP3")) {
			throw new IOException("Not a valid MD3 file (bad surface magic number)");
		} else {
			string3 = loadMD3Info(byteBuffer1, 64);
			System.out.println("Name: " + string3);
			byteBuffer1.getInt();
			int i20 = byteBuffer1.getInt();
			int i4 = byteBuffer1.getInt();
			int i5 = byteBuffer1.getInt();
			int i17 = byteBuffer1.getInt();
			MD3Buffers mD3Buffers6 = new MD3Buffers(i17, i5, i20);
			int i7 = byteBuffer1.getInt() + i2;
			int i8 = byteBuffer1.getInt() + i2;
			int i9 = byteBuffer1.getInt() + i2;
			i2 += byteBuffer1.getInt();
			byteBuffer1.getInt();
			mD3Buffers6.verts = i5;
			mD3Buffers6.shaders = new MD3Shader[i4];
			System.out.println("Triangles: " + i17);
			System.out.println("OFS_SHADERS: " + i8 + " (current location: " + byteBuffer1.position() + ")");
			byteBuffer1.position(i8);

			for(i8 = 0; i8 < i4; ++i8) {
				MD3Shader[] mD3Shader22 = mD3Buffers6.shaders;
				MD3Shader mD3Shader10 = null;
				mD3Shader10 = new MD3Shader();
				loadMD3Info(byteBuffer1, 64);
				byteBuffer1.getInt();
				mD3Shader22[i8] = mD3Shader10;
			}

			System.out.println("OFS_TRIANGLES: " + i7 + " (current location: " + byteBuffer1.position() + ")");
			byteBuffer1.position(i7);

			for(i8 = 0; i8 < i17 * 3; ++i8) {
				mD3Buffers6.triangles.put(byteBuffer1.getInt());
			}

			System.out.println("OFS_ST: " + i9 + " (current location: " + byteBuffer1.position() + ")");
			byteBuffer1.position(i9);

			for(i8 = 0; i8 < i5 << 1; ++i8) {
				mD3Buffers6.xBuffer.put(byteBuffer1.getFloat());
			}

			System.out.println("OFS_XYZ_NORMAL: " + i2 + " (current location: " + byteBuffer1.position() + ")");
			byteBuffer1.position(i2);

			for(i8 = 0; i8 < i5 * i20; ++i8) {
				mD3Buffers6.vertices.put((float)byteBuffer1.getShort() / 64.0F);
				mD3Buffers6.vertices.put((float)byteBuffer1.getShort() / 64.0F);
				mD3Buffers6.vertices.put((float)byteBuffer1.getShort() / 64.0F);
				double d13 = (double)(byteBuffer1.get() & 255) * Math.PI * 2.0D / 255.0D;
				double d15;
				float f18 = (float)(Math.cos(d15 = (double)(byteBuffer1.get() & 255) * Math.PI * 2.0D / 255.0D) * Math.sin(d13));
				float f19 = (float)(Math.sin(d15) * Math.sin(d13));
				float f21 = (float)Math.cos(d13);
				mD3Buffers6.normals.put(f18);
				mD3Buffers6.normals.put(f19);
				mD3Buffers6.normals.put(f21);
			}

			return mD3Buffers6;
		}
	}

	private static Vec3D getMD3Vec(ByteBuffer byteBuffer0) {
		float f1 = byteBuffer0.getFloat();
		float f2 = byteBuffer0.getFloat();
		float f3 = byteBuffer0.getFloat();
		return new Vec3D(f1, f2, f3);
	}

	private static String loadMD3Info(ByteBuffer byteBuffer0, int i1) {
		byte[] b3 = new byte[i1];
		byteBuffer0.get(b3);

		for(int i2 = 0; i2 < b3.length; ++i2) {
			if(b3[i2] == 0) {
				return new String(b3, 0, i2);
			}
		}

		return new String(b3);
	}
}