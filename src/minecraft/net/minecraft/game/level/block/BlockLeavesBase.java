package net.minecraft.game.level.block;

import net.minecraft.game.level.World;

public class BlockLeavesBase extends Block {
	private boolean graphcisLevel = true;

	protected BlockLeavesBase(int i1, int i2) {
		super(18, 22);
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final boolean shouldSideBeRendered(World world1, int i2, int i3, int i4, int i5) {
		int i6 = world1.getBlockId(i2, i3, i4);
		return !this.graphcisLevel && i6 == this.blockID ? false : super.shouldSideBeRendered(world1, i2, i3, i4, i5);
	}
}