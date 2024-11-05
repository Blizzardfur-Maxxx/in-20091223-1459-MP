package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.level.World;

public final class BlockTNT extends Block {
	public BlockTNT() {
		super(46, 8);
	}

	public final int getBlockTexture(int i1) {
		return i1 == 0 ? this.blockIndexInTexture + 2 : (i1 == 1 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture);
	}

	public final int quantityDropped(Random random1) {
		return 0;
	}

	public final void onBlockDestroyedByPlayer(World world1, int i2, int i3, int i4) {
		world1.createExplosion(i2, i3 - 1, i4, Block.planks.blockID);
	}
}