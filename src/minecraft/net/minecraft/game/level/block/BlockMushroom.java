package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.level.World;

public final class BlockMushroom extends BlockFlower {
	protected BlockMushroom(int i1, int i2) {
		super(i1, i2);
		float f3 = 0.2F;
		this.setBlockBounds(0.5F - f3, 0.0F, 0.5F - f3, f3 + 0.5F, f3 * 2.0F, f3 + 0.5F);
	}

	public final void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		int i6 = world1.getBlockId(i2, i3 - 1, i4);
		if(world1.isHalfLit(i2, i3, i4) || i6 != Block.stone.blockID && i6 != Block.gravel.blockID && i6 != Block.cobblestone.blockID) {
			world1.setBlockWithNotify(i2, i3, i4, 0);
		}

	}
}