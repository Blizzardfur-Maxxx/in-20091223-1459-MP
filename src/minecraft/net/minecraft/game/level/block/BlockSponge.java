package net.minecraft.game.level.block;

import net.minecraft.game.level.World;

public final class BlockSponge extends Block {
	protected BlockSponge() {
		super(19);
		this.blockIndexInTexture = 48;
	}

	public final void onBlockAdded(World world1, int i2, int i3, int i4) {
		for(int i7 = i2 - 2; i7 <= i2 + 2; ++i7) {
			for(int i5 = i3 - 2; i5 <= i3 + 2; ++i5) {
				for(int i6 = i4 - 2; i6 <= i4 + 2; ++i6) {
					if(world1.isWater(i7, i5, i6)) {
						world1.setBlock(i7, i5, i6, 0);
					}
				}
			}
		}

	}

	public final void onBlockRemoval(World world1, int i2, int i3, int i4) {
		for(int i7 = i2 - 2; i7 <= i2 + 2; ++i7) {
			for(int i5 = i3 - 2; i5 <= i3 + 2; ++i5) {
				for(int i6 = i4 - 2; i6 <= i4 + 2; ++i6) {
					world1.notifyBlocksOfNeighborChange(i7, i5, i6, world1.getBlockId(i7, i5, i6));
				}
			}
		}

	}
}