package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.level.World;

public final class BlockGrass extends Block {
	protected BlockGrass() {
		super(2);
		this.blockIndexInTexture = 3;
		this.setTickOnLoad(true);
	}

	public final int getBlockTexture(int i1) {
		return i1 == 1 ? 0 : (i1 == 0 ? 2 : 3);
	}

	public final void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		if(random5.nextInt(4) == 0) {
			if(!world1.isHalfLit(i2, i3 + 1, i4)) {
				world1.setBlockWithNotify(i2, i3, i4, Block.dirt.blockID);
			} else {
				for(int i9 = 0; i9 < 4; ++i9) {
					int i6 = i2 + random5.nextInt(3) - 1;
					int i7 = i3 + random5.nextInt(5) - 3;
					int i8 = i4 + random5.nextInt(3) - 1;
					if(world1.getBlockId(i6, i7, i8) == Block.dirt.blockID && world1.isHalfLit(i6, i7, i8)) {
						world1.setBlockWithNotify(i6, i7, i8, Block.grass.blockID);
					}
				}

			}
		}
	}
}