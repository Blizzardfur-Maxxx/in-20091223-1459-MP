package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.level.World;

public final class BlockSapling extends BlockFlower {
	protected BlockSapling(int i1) {
		super(6, 15);
		float f2 = 0.4F;
		this.setBlockBounds(0.5F - f2, 0.0F, 0.5F - f2, f2 + 0.5F, f2 * 2.0F, f2 + 0.5F);
	}

	public final void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		int i6 = world1.getBlockId(i2, i3 - 1, i4);
		if(world1.isHalfLit(i2, i3, i4) && (i6 == Block.dirt.blockID || i6 == Block.grass.blockID)) {
			if(random5.nextInt(5) == 0) {
				world1.setTileNoUpdate(i2, i3, i4, 0);
				if(!world1.growTrees(i2, i3, i4)) {
					world1.setTileNoUpdate(i2, i3, i4, this.blockID);
				}
			}

		} else {
			world1.setBlockWithNotify(i2, i3, i4, 0);
		}
	}
}