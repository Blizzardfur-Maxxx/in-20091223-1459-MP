package net.minecraft.game.level.block;

import java.util.Random;

import net.minecraft.game.level.World;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;

public class Block {
	public static final Block[] blocksList = new Block[256];
	public static final boolean[] tickOnLoad = new boolean[256];
	private static boolean[] opaqueCubeLookup = new boolean[256];
	public static final int[] lightOpacity = new int[256];
	private static boolean[] canBlockGrass = new boolean[256];
	public static final boolean[] isBlockContainer = new boolean[256];
	public static final int[] lightValue = new int[256];
	public static final Block stone = (new BlockStone(1, 1)).setHardness(1.0F);
	public static final Block grass = (new BlockGrass()).setHardness(0.6F);
	public static final Block dirt = (new BlockDirt()).setHardness(0.5F);
	public static final Block cobblestone = (new Block(4, 16)).setHardness(1.5F);
	public static final Block planks = (new Block(5, 4)).setHardness(1.5F);
	public static final Block sapling = (new BlockSapling(6)).setHardness(0.0F);
	public static final Block bedrock = (new Block(7, 17)).setHardness(999.0F);
	public static final Block waterMoving = (new BlockFluid(8, Material.water)).setHardness(100.0F).setLightOpacity(2);
	public static final Block waterStill = (new BlockStationary(9, Material.water)).setHardness(100.0F).setLightOpacity(2);
	public static final Block lavaMoving = (new BlockFluid(10, Material.lava)).setHardness(0.0F).setLightValue(0.8F);
	public static final Block lavaStill = (new BlockStationary(11, Material.lava)).setHardness(100.0F).setLightValue(0.8F);
	public static final Block sand = (new BlockSand(12, 18)).setHardness(0.5F);
	public static final Block gravel = (new BlockSand(13, 19)).setHardness(0.6F);
	public static final Block oreGold = (new BlockOre(14, 32)).setHardness(3.0F);
	public static final Block oreIron = (new BlockOre(15, 33)).setHardness(3.0F);
	public static final Block oreCoal = (new BlockOre(16, 34)).setHardness(3.0F);
	public static final Block log = (new BlockLog()).setHardness(2.5F);
	public static final Block leaves = (new BlockLeaves()).setHardness(0.2F).setLightOpacity(1);
	public static final Block sponge = (new BlockSponge()).setHardness(0.6F);
	public static final Block glass = (new BlockGlass()).setHardness(0.3F);
	public static final Block clothRed = (new Block(21, 64)).setHardness(0.8F);
	public static final Block clothOrange = (new Block(22, 65)).setHardness(0.8F);
	public static final Block clothYellow = (new Block(23, 66)).setHardness(0.8F);
	public static final Block clothChartreuse = (new Block(24, 67)).setHardness(0.8F);
	public static final Block clothGreen = (new Block(25, 68)).setHardness(0.8F);
	public static final Block clothSpringGreen = (new Block(26, 69)).setHardness(0.8F);
	public static final Block clothCyan = (new Block(27, 70)).setHardness(0.8F);
	public static final Block clothCapri = (new Block(28, 71)).setHardness(0.8F);
	public static final Block clothUltramarine = (new Block(29, 72)).setHardness(0.8F);
	public static final Block clothViolet = (new Block(30, 73)).setHardness(0.8F);
	public static final Block clothPurple = (new Block(31, 74)).setHardness(0.8F);
	public static final Block clothMagenta = (new Block(32, 75)).setHardness(0.8F);
	public static final Block clothRose = (new Block(33, 76)).setHardness(0.8F);
	public static final Block clothDarkGray = (new Block(34, 77)).setHardness(0.8F);
	public static final Block clothGray = (new Block(35, 78)).setHardness(0.8F);
	public static final Block clothWhite = (new Block(36, 79)).setHardness(0.8F);
	public static final Block plantYellow = (new BlockFlower(37, 13)).setHardness(0.0F);
	public static final Block plantRed = (new BlockFlower(38, 12)).setHardness(0.0F);
	public static final Block mushroomBrown = (new BlockMushroom(39, 29)).setHardness(0.0F);
	public static final Block mushroomRed = (new BlockMushroom(40, 28)).setHardness(0.0F);
	public static final Block goldBlock = (new BlockOreBlock(41, 40)).setHardness(3.0F);
	public static final Block ironBlock = (new BlockOreBlock(42, 39)).setHardness(5.0F);
	public static final Block stairDouble = (new BlockStep(43, true)).setHardness(2.0F);
	public static final Block stairSingle = (new BlockStep(44, false)).setHardness(2.0F);
	public static final Block brick = (new Block(45, 7)).setHardness(2.0F);
	public static final Block tnt = (new BlockTNT()).setHardness(0.0F);
	public static final Block bookShelf = (new BlockBookshelf()).setHardness(1.5F);
	public static final Block cobblestoneMossy = (new Block(48, 36)).setHardness(1.0F);
	public static final Block obsidian = (new BlockStone(49, 37)).setHardness(10.0F);
	public static final Block torch = (new BlockTorch()).setHardness(0.0F).setLightValue(1.0F);
	public int blockIndexInTexture;
	public final int blockID;
	private int blockHardness;
	public float minX;
	public float minY;
	public float minZ;
	public float maxX;
	public float maxY;
	public float maxZ;
	public float blockParticleGravity;

	protected Block(int i1) {
		this.blockParticleGravity = 1.0F;
		blocksList[i1] = this;
		this.blockID = i1;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		opaqueCubeLookup[i1] = this.isOpaqueCube();
		lightOpacity[i1] = this.isOpaqueCube() ? 255 : 0;
		canBlockGrass[i1] = this.renderAsNormalBlock();
		isBlockContainer[i1] = false;
	}

	private Block setLightOpacity(int i1) {
		lightOpacity[this.blockID] = i1;
		return this;
	}

	private Block setLightValue(float f1) {
		lightValue[this.blockID] = (int)(8.0F * f1);
		return this;
	}

	public boolean renderAsNormalBlock() {
		return true;
	}

	public int getRenderType() {
		return 0;
	}

	protected final Block setHardness(float f1) {
		this.blockHardness = (int)(f1 * 20.0F);
		return this;
	}

	protected final void setTickOnLoad(boolean z1) {
		tickOnLoad[this.blockID] = z1;
	}

	protected final void setBlockBounds(float f1, float f2, float f3, float f4, float f5, float f6) {
		this.minX = f1;
		this.minY = f2;
		this.minZ = f3;
		this.maxX = f4;
		this.maxY = f5;
		this.maxZ = f6;
	}

	protected Block(int i1, int i2) {
		this(i1);
		this.blockIndexInTexture = i2;
	}

	public float getBlockBrightness(World world1, int i2, int i3, int i4) {
		return world1.getBlockLightValue(i2, i3, i4);
	}

	public boolean shouldSideBeRendered(World world1, int i2, int i3, int i4, int i5) {
		return !world1.isBlockNormalCube(i2, i3, i4);
	}

	public int getBlockTexture(int i1) {
		return this.blockIndexInTexture;
	}

	public final AxisAlignedBB getSelectedBoundingBoxFromPool(int i1, int i2, int i3) {
		return new AxisAlignedBB((float)i1 + this.minX, (float)i2 + this.minY, (float)i3 + this.minZ, (float)i1 + this.maxX, (float)i2 + this.maxY, (float)i3 + this.maxZ);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(int i1, int i2, int i3) {
		return new AxisAlignedBB((float)i1 + this.minX, (float)i2 + this.minY, (float)i3 + this.minZ, (float)i1 + this.maxX, (float)i2 + this.maxY, (float)i3 + this.maxZ);
	}

	public boolean isOpaqueCube() {
		return true;
	}

	public void updateTick(World world1, int i2, int i3, int i4, Random random5) {
	}

	public void onBlockDestroyedByPlayer(World world1, int i2, int i3, int i4) {
	}

	public Material getMaterial() {
		return Material.air;
	}

	public void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
	}

	public void onBlockPlaced(World world1, int i2, int i3, int i4) {
	}

	public int tickRate() {
		return 5;
	}

	public void onBlockAdded(World world1, int i2, int i3, int i4) {
	}

	public void onBlockRemoval(World world1, int i2, int i3, int i4) {
	}

	public int quantityDropped(Random random1) {
		return 1;
	}

	public final int blockStrength() {
		return this.blockHardness;
	}

	public void dropBlockAsItem(World world1) {
		this.dropBlockAsItemWithChance(world1, 1.0F);
	}

	public void dropBlockAsItemWithChance(World world1, float f2) {
		int i3 = this.quantityDropped(world1.random);

		for(int i4 = 0; i4 < i3; ++i4) {
			if(world1.random.nextFloat() <= 1.0F) {
				world1.random.nextFloat();
				world1.random.nextFloat();
				world1.random.nextFloat();
			}
		}

	}

	public final MovingObjectPosition collisionRayTrace(int i1, int i2, int i3, Vec3D vec3D4, Vec3D vec3D5) {
		vec3D4 = vec3D4.addVector((float)(-i1), (float)(-i2), (float)(-i3));
		vec3D5 = vec3D5.addVector((float)(-i1), (float)(-i2), (float)(-i3));
		Vec3D vec3D6 = vec3D4.getIntermediateWithXValue(vec3D5, this.minX);
		Vec3D vec3D7 = vec3D4.getIntermediateWithXValue(vec3D5, this.maxX);
		Vec3D vec3D8 = vec3D4.getIntermediateWithYValue(vec3D5, this.minY);
		Vec3D vec3D9 = vec3D4.getIntermediateWithYValue(vec3D5, this.maxY);
		Vec3D vec3D10 = vec3D4.getIntermediateWithZValue(vec3D5, this.minZ);
		vec3D5 = vec3D4.getIntermediateWithZValue(vec3D5, this.maxZ);
		if(!this.isVecInsideYZBounds(vec3D6)) {
			vec3D6 = null;
		}

		if(!this.isVecInsideYZBounds(vec3D7)) {
			vec3D7 = null;
		}

		if(!this.isVecInsideXZBounds(vec3D8)) {
			vec3D8 = null;
		}

		if(!this.isVecInsideXZBounds(vec3D9)) {
			vec3D9 = null;
		}

		if(!this.isVecInsideXYBounds(vec3D10)) {
			vec3D10 = null;
		}

		if(!this.isVecInsideXYBounds(vec3D5)) {
			vec3D5 = null;
		}

		Vec3D vec3D11 = null;
		if(vec3D6 != null) {
			vec3D11 = vec3D6;
		}

		if(vec3D7 != null && (vec3D11 == null || vec3D4.distanceTo(vec3D7) < vec3D4.distanceTo(vec3D11))) {
			vec3D11 = vec3D7;
		}

		if(vec3D8 != null && (vec3D11 == null || vec3D4.distanceTo(vec3D8) < vec3D4.distanceTo(vec3D11))) {
			vec3D11 = vec3D8;
		}

		if(vec3D9 != null && (vec3D11 == null || vec3D4.distanceTo(vec3D9) < vec3D4.distanceTo(vec3D11))) {
			vec3D11 = vec3D9;
		}

		if(vec3D10 != null && (vec3D11 == null || vec3D4.distanceTo(vec3D10) < vec3D4.distanceTo(vec3D11))) {
			vec3D11 = vec3D10;
		}

		if(vec3D5 != null && (vec3D11 == null || vec3D4.distanceTo(vec3D5) < vec3D4.distanceTo(vec3D11))) {
			vec3D11 = vec3D5;
		}

		if(vec3D11 == null) {
			return null;
		} else {
			byte b12 = -1;
			if(vec3D11 == vec3D6) {
				b12 = 4;
			}

			if(vec3D11 == vec3D7) {
				b12 = 5;
			}

			if(vec3D11 == vec3D8) {
				b12 = 0;
			}

			if(vec3D11 == vec3D9) {
				b12 = 1;
			}

			if(vec3D11 == vec3D10) {
				b12 = 2;
			}

			if(vec3D11 == vec3D5) {
				b12 = 3;
			}

			return new MovingObjectPosition(i1, i2, i3, b12, vec3D11.addVector((float)i1, (float)i2, (float)i3));
		}
	}

	private boolean isVecInsideYZBounds(Vec3D vec3D1) {
		return vec3D1 == null ? false : vec3D1.yCoord >= this.minY && vec3D1.yCoord <= this.maxY && vec3D1.zCoord >= this.minZ && vec3D1.zCoord <= this.maxZ;
	}

	private boolean isVecInsideXZBounds(Vec3D vec3D1) {
		return vec3D1 == null ? false : vec3D1.xCoord >= this.minX && vec3D1.xCoord <= this.maxX && vec3D1.zCoord >= this.minZ && vec3D1.zCoord <= this.maxZ;
	}

	private boolean isVecInsideXYBounds(Vec3D vec3D1) {
		return vec3D1 == null ? false : vec3D1.xCoord >= this.minX && vec3D1.xCoord <= this.maxX && vec3D1.yCoord >= this.minY && vec3D1.yCoord <= this.maxY;
	}

	public int getRenderBlockPass() {
		return 0;
	}
}