package net.minecraft.game.level.block;

import java.util.Random;

public final class BlockOre extends Block {
	public BlockOre(int i1, int i2) {
		super(i1, i2);
	}

	public final int quantityDropped(Random random1) {
		return random1.nextInt(3) + 1;
	}
}