package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.level.World;
import net.minecraft.game.physics.AxisAlignedBB;

public class BlockFlower extends Block {
	protected BlockFlower(int i1, int i2) {
		super(i1);
		this.blockIndexInTexture = i2;
		this.setTickOnLoad(true);
		float f3 = 0.2F;
		this.setBlockBounds(0.5F - f3, 0.0F, 0.5F - f3, f3 + 0.5F, f3 * 3.0F, f3 + 0.5F);
	}

	public void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		if(!world1.multiplayerWorld) {
			int i6 = world1.getBlockId(i2, i3 - 1, i4);
			if(!world1.isHalfLit(i2, i3, i4) || i6 != Block.dirt.blockID && i6 != Block.grass.blockID) {
				world1.setBlockWithNotify(i2, i3, i4, 0);
			}

		}
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
		return 1;
	}
}