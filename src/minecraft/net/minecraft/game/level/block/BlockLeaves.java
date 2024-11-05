package net.minecraft.game.level.block;

import java.util.Random;

public final class BlockLeaves extends BlockLeavesBase {
	protected BlockLeaves() {
		super(18, 22);
	}

	public final int quantityDropped(Random random1) {
		return random1.nextInt(10) == 0 ? 1 : 0;
	}
}