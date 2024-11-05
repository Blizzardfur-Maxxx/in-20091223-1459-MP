package net.minecraft.game.level.block;

import net.minecraft.game.level.World;

public final class BlockGlass extends Block {
	private boolean graphicsLevle = false;

	protected BlockGlass() {
		super(20, 49);
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final boolean shouldSideBeRendered(World world1, int i2, int i3, int i4, int i5) {
		return world1.getBlockId(i2, i3, i4) == this.blockID ? false : super.shouldSideBeRendered(world1, i2, i3, i4, i5);
	}
}