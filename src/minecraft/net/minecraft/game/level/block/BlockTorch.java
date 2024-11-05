package net.minecraft.game.level.block;

import net.minecraft.game.physics.AxisAlignedBB;

public final class BlockTorch extends Block {
	protected BlockTorch() {
		super(50, 80);
	}

	public final AxisAlignedBB getCollisionBoundingBoxFromPool(int i1, int i2, int i3) {
		return null;
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final boolean renderAsNormalBlock() {
		return false;
	}

	public final int getRenderType() {
		return 2;
	}
}