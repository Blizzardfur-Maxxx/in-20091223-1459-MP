package net.minecraft.game.level.block;

import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;

public final class BlockSand extends Block {
	public BlockSand(int i1, int i2) {
		super(i1, i2);
	}

	public final void onBlockPlaced(World world1, int i2, int i3, int i4) {
		this.fall(world1, i2, i3, i4);
	}

	public final void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		this.fall(world1, i2, i3, i4);
	}

	private void fall(World world1, int i2, int i3, int i4) {
		int i11 = i2;
		int i5 = i3;
		int i6 = i4;

		while(true) {
			int i9 = i5 - 1;
			Material material7 = null;
			int i12;
			if(!((i12 = world1.getBlockId(i11, i9, i6)) == 0 ? true : ((material7 = Block.blocksList[i12].getMaterial()) == Material.water ? true : material7 == Material.lava)) || i5 <= 0) {
				if(i5 != i3) {
					if((i12 = world1.getBlockId(i11, i5, i6)) > 0 && Block.blocksList[i12].getMaterial() != Material.air) {
						world1.setTileNoUpdate(i11, i5, i6, 0);
					}

					world1.swap(i2, i3, i4, i11, i5, i6);
				}

				return;
			}

			--i5;
		}
	}
}