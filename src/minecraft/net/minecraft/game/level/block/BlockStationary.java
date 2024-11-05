package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;

public final class BlockStationary extends BlockFluid {
	protected BlockStationary(int i1, Material material2) {
		super(i1, material2);
		this.movingId = i1 - 1;
		this.stillId = i1;
		this.setTickOnLoad(false);
	}

	public final void updateTick(World world1, int i2, int i3, int i4, Random random5) {
	}

	public final void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		boolean z6 = false;
		if(world1.getBlockId(i2 - 1, i3, i4) == 0) {
			z6 = true;
		}

		if(world1.getBlockId(i2 + 1, i3, i4) == 0) {
			z6 = true;
		}

		if(world1.getBlockId(i2, i3, i4 - 1) == 0) {
			z6 = true;
		}

		if(world1.getBlockId(i2, i3, i4 + 1) == 0) {
			z6 = true;
		}

		if(world1.getBlockId(i2, i3 - 1, i4) == 0) {
			z6 = true;
		}

		if(i5 != 0) {
			Material material7 = Block.blocksList[i5].getMaterial();
			if(this.material == Material.water && material7 == Material.lava || material7 == Material.water && this.material == Material.lava) {
				world1.setBlockWithNotify(i2, i3, i4, Block.stone.blockID);
				return;
			}
		}

		if(z6) {
			world1.setTileNoUpdate(i2, i3, i4, this.movingId);
			world1.scheduleBlockUpdate(i2, i3, i4, this.movingId);
		}

	}
}