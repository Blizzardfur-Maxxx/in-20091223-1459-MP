package net.minecraft.game.level.block;

import java.util.Random;

public final class BlockLog extends Block {
	protected BlockLog() {
		super(17);
		this.blockIndexInTexture = 20;
	}

	public final int quantityDropped(Random random1) {
		return random1.nextInt(3) + 3;
	}

	public final int getBlockTexture(int i1) {
		return i1 == 1 ? 21 : (i1 == 0 ? 21 : 20);
	}
}