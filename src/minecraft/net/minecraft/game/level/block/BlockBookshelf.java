package net.minecraft.game.level.block;

import java.util.Random;

public final class BlockBookshelf extends Block {
	public BlockBookshelf() {
		super(47, 35);
	}

	public final int getBlockTexture(int i1) {
		return i1 <= 1 ? 4 : this.blockIndexInTexture;
	}

	public final int quantityDropped(Random random1) {
		return 0;
	}
}