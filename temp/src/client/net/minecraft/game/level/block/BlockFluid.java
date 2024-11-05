package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;

public class BlockFluid extends Block {
	protected Material material;
	protected int stillId;
	protected int movingId;

	protected BlockFluid(int i1, Material material2) {
		super(i1);
		this.material = material2;
		this.blockIndexInTexture = 14;
		if(material2 == Material.lava) {
			this.blockIndexInTexture = 30;
		}

		Block.isBlockContainer[i1] = true;
		this.movingId = i1;
		this.stillId = i1 + 1;
		float f3 = 0.01F;
		float f4 = 0.1F;
		this.setBlockBounds(f3, 0.0F - f4 + f3, f3, f3 + 1.0F, 1.0F - f4 + f3, f3 + 1.0F);
		this.setTickOnLoad(true);
	}

	public final int getBlockTexture(int i1) {
		return this.material == Material.lava ? this.blockIndexInTexture : (i1 == 1 ? this.blockIndexInTexture : (i1 == 0 ? this.blockIndexInTexture : this.blockIndexInTexture + 32));
	}

	public final boolean renderAsNormalBlock() {
		return false;
	}

	public final void onBlockPlaced(World world1, int i2, int i3, int i4) {
		world1.scheduleBlockUpdate(i2, i3, i4, this.movingId);
	}

	public void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		boolean z7 = false;
		i4 = i4;
		i3 = i3;
		i2 = i2;
		world1 = world1;
		BlockFluid blockFluid8 = this;
		boolean z9 = false;

		boolean z6;
		do {
			--i3;
			if(world1.getBlockId(i2, i3, i4) != 0 || !blockFluid8.canFlow(world1, i2, i3, i4)) {
				break;
			}

			if(z6 = world1.setBlockWithNotify(i2, i3, i4, blockFluid8.movingId)) {
				z9 = true;
			}
		} while(z6 && blockFluid8.material != Material.lava);

		++i3;
		if(blockFluid8.material == Material.water || !z9) {
			z9 = z9 | blockFluid8.flow(world1, i2 - 1, i3, i4) | blockFluid8.flow(world1, i2 + 1, i3, i4) | blockFluid8.flow(world1, i2, i3, i4 - 1) | blockFluid8.flow(world1, i2, i3, i4 + 1);
		}

		if(!z9) {
			world1.setTileNoUpdate(i2, i3, i4, blockFluid8.stillId);
		} else {
			world1.scheduleBlockUpdate(i2, i3, i4, blockFluid8.movingId);
		}
	}

	private boolean canFlow(World world1, int i2, int i3, int i4) {
		if(this.material == Material.water) {
			for(int i7 = i2 - 2; i7 <= i2 + 2; ++i7) {
				for(int i5 = i3 - 2; i5 <= i3 + 2; ++i5) {
					for(int i6 = i4 - 2; i6 <= i4 + 2; ++i6) {
						if(world1.getBlockId(i7, i5, i6) == Block.sponge.blockID) {
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	private boolean flow(World world1, int i2, int i3, int i4) {
		if(world1.getBlockId(i2, i3, i4) == 0) {
			if(!this.canFlow(world1, i2, i3, i4)) {
				return false;
			}

			if(world1.setBlockWithNotify(i2, i3, i4, this.movingId)) {
				world1.scheduleBlockUpdate(i2, i3, i4, this.movingId);
			}
		}

		return false;
	}

	public final float getBlockBrightness(World world1, int i2, int i3, int i4) {
		return this.material == Material.lava ? 100.0F : world1.getBlockLightValue(i2, i3, i4);
	}

	public final boolean shouldSideBeRendered(World world1, int i2, int i3, int i4, int i5) {
		int i6;
		return i2 >= 0 && i3 >= 0 && i4 >= 0 && i2 < world1.width && i4 < world1.length ? ((i6 = world1.getBlockId(i2, i3, i4)) != this.movingId && i6 != this.stillId ? (i5 == 1 && (world1.getBlockId(i2 - 1, i3, i4) == 0 || world1.getBlockId(i2 + 1, i3, i4) == 0 || world1.getBlockId(i2, i3, i4 - 1) == 0 || world1.getBlockId(i2, i3, i4 + 1) == 0) ? true : super.shouldSideBeRendered(world1, i2, i3, i4, i5)) : false) : false;
	}

	public final AxisAlignedBB getCollisionBoundingBoxFromPool(int i1, int i2, int i3) {
		return null;
	}

	public final boolean isOpaqueCube() {
		return false;
	}

	public final Material getMaterial() {
		return this.material;
	}

	public void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		if(i5 != 0) {
			Material material6 = Block.blocksList[i5].getMaterial();
			if(this.material == Material.water && material6 == Material.lava || material6 == Material.water && this.material == Material.lava) {
				world1.setBlockWithNotify(i2, i3, i4, Block.stone.blockID);
				return;
			}
		}

		world1.scheduleBlockUpdate(i2, i3, i4, i5);
	}

	public final int tickRate() {
		return this.material == Material.lava ? 25 : 5;
	}

	public final void dropBlockAsItemWithChance(World world1, float f2) {
	}

	public final void dropBlockAsItem(World world1) {
	}

	public final int quantityDropped(Random random1) {
		return 0;
	}

	public final int getRenderBlockPass() {
		return this.material == Material.water ? 1 : 0;
	}
}