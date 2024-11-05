package net.minecraft.game.level;

import java.io.DataInputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public final class LevelIO {

	public static byte[] loadBlocks(InputStream in) {
		try {
			DataInputStream in1;
			byte[] b1 = new byte[(in1 = new DataInputStream(new GZIPInputStream(in))).readInt()];
			in1.readFully(b1);
			in1.close();
			return b1;
		} catch (Exception exception2) {
			throw new RuntimeException(exception2);
		}
	}
}