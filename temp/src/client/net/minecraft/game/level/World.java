package net.minecraft.game.level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.client.render.RenderGlobal;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;

public final class World {
	private int MAX_TICKS = 100;
	private static float[] lightBrightnessTable = new float[9];
	public int width;
	public int length;
	public int height;
	private byte[] blocks;
	private byte[] data;
	public int xSpawn;
	public int ySpawn;
	public int zSpawn;
	public float rotSpawn;
	private ArrayList worldAccesses = new ArrayList();
	private ArrayList ticksList = new ArrayList();
	private int[] heightMap;
	public Random random = new Random();
	private int randId = this.random.nextInt();
	public EntityMap entityMap;
	public int waterLevel;
	public int skyColor;
	public int fogColor;
	public int cloudColor;
	private int updateLCG = 0;
	private int playTime = 0;
	public Entity playerEntity;
	public boolean multiplayerWorld = false;
	public boolean survivalWorld = true;
	private int[] floodFillCounters = new int[1048576];
	private byte[] coords = new byte[1048576];
	private int[] floodedBlocks = new int[1048576];
	private int explosionTime = 0;

	public final void load() {
		if(this.blocks == null) {
			throw new RuntimeException("The level is corrupt!");
		} else {
			this.worldAccesses = new ArrayList();
			this.heightMap = new int[this.width * this.length];
			Arrays.fill(this.heightMap, this.height);
			this.updateSkylight(0, 0, this.width, this.length);
			this.random = new Random();
			this.randId = this.random.nextInt();
			this.ticksList = new ArrayList();
			if(this.waterLevel == 0) {
				this.waterLevel = this.height / 2;
			}

			if(this.skyColor == 0) {
				this.skyColor = 10079487;
			}

			if(this.fogColor == 0) {
				this.fogColor = 0xFFFFFF;
			}

			if(this.cloudColor == 0) {
				this.cloudColor = 0xFFFFFF;
			}

			if(this.xSpawn == 0 && this.ySpawn == 0 && this.zSpawn == 0) {
				this.findSpawn();
			}

			if(this.entityMap == null) {
				this.entityMap = new EntityMap(this.width, this.height, this.length);
			}

		}
	}

	public final void generate(int i1, int i2, int i3, byte[] b4) {
		this.width = i1;
		this.length = i3;
		this.height = i2;
		this.blocks = b4;
		this.data = new byte[b4.length];
		this.heightMap = new int[i1 * i3];
		Arrays.fill(this.heightMap, this.height);
		this.updateSkylight(0, 0, i1, i3);

		for(i1 = 0; i1 < this.worldAccesses.size(); ++i1) {
			((RenderGlobal)this.worldAccesses.get(i1)).loadRenderers();
		}

		this.ticksList.clear();
		this.findSpawn();
		this.load();
		System.gc();
	}

	private void findSpawn() {
		Random random1 = new Random();
		int i2 = 0;

		int i3;
		int i4;
		int i5;
		do {
			++i2;
			i3 = random1.nextInt(this.width / 2) + this.width / 4;
			i4 = random1.nextInt(this.length / 2) + this.length / 4;
			i5 = this.getFirstUncoveredBlock(i3, i4) + 1;
			if(i2 == 10000) {
				this.xSpawn = i3;
				this.ySpawn = -100;
				this.zSpawn = i4;
				return;
			}
		} while((float)i5 <= (float)this.waterLevel);

		this.xSpawn = i3;
		this.ySpawn = i5;
		this.zSpawn = i4;
	}

	private void updateSkylight(int i1, int i2, int i3, int i4) {
		for(int i5 = i1; i5 < i1 + i3; ++i5) {
			for(int i6 = i2; i6 < i2 + i4; ++i6) {
				int i7 = this.heightMap[i5 + i6 * this.width];

				int i8;
				for(i8 = this.height - 1; i8 > 0 && Block.lightOpacity[this.getBlockId(i5, i8, i6)] == 0; --i8) {
				}

				this.heightMap[i5 + i6 * this.width] = i8 + 1;
				if(i7 != i8) {
					int i9 = i7 < i8 ? i7 : i8;
					i7 = i7 > i8 ? i7 : i8;
					this.updateLight(i5, i9, i6, i5 + 1, i7, i6 + 1);
				}
			}
		}

	}

	private void updateLight(int i1, int i2, int i3, int i4, int i5, int i6) {
		int i7 = 0;

		int i8;
		int i9;
		int i10;
		for(i8 = i1; i8 < i4; ++i8) {
			for(i9 = i3; i9 < i6; ++i9) {
				for(i10 = i2; i10 < i5; ++i10) {
					this.floodFillCounters[i7++] = i8 << 20 | i10 << 10 | i9;
				}
			}
		}

		while(i7 > 0) {
			--i7;
			i9 = (i8 = this.floodFillCounters[i7]) >> 20 & 1023;
			i10 = i8 >> 10 & 1023;
			i8 &= 1023;
			int i11 = this.heightMap[i9 + i8 * this.width];
			i11 = i10 >= i11 ? 8 : 0;
			byte b12 = this.blocks[(i10 * this.length + i8) * this.width + i9];
			if(i11 < Block.lightValue[b12]) {
				i11 = Block.lightValue[b12];
			}

			int i16;
			if((i16 = Block.lightOpacity[b12]) > 100) {
				i11 = 0;
			} else {
				if((i16 = i16) == 0) {
					i16 = 1;
				}

				int i13;
				if(i9 > 0 && (i13 = (this.data[(i10 * this.length + i8) * this.width + (i9 - 1)] & 255) - i16) > i11) {
					i11 = i13;
				}

				if(i9 < this.width - 1 && (i13 = (this.data[(i10 * this.length + i8) * this.width + i9 + 1] & 255) - i16) > i11) {
					i11 = i13;
				}

				if(i10 > 0 && (i13 = (this.data[((i10 - 1) * this.length + i8) * this.width + i9] & 255) - i16) > i11) {
					i11 = i13;
				}

				if(i10 < this.height - 1 && (i13 = (this.data[((i10 + 1) * this.length + i8) * this.width + i9] & 255) - i16) > i11) {
					i11 = i13;
				}

				if(i8 > 0 && (i13 = (this.data[(i10 * this.length + (i8 - 1)) * this.width + i9] & 255) - i16) > i11) {
					i11 = i13;
				}

				if(i8 < this.length - 1 && (i13 = (this.data[(i10 * this.length + i8 + 1) * this.width + i9] & 255) - i16) > i11) {
					i11 = i13;
				}
			}

			if(i9 < i1) {
				i1 = i9;
			} else if(i9 > i4) {
				i4 = i9;
			}

			if(i10 > i5) {
				i5 = i10;
			} else if(i10 < i2) {
				i2 = i10;
			}

			if(i8 < i3) {
				i3 = i8;
			} else if(i8 > i6) {
				i6 = i8;
			}

			if((this.data[(i10 * this.length + i8) * this.width + i9] & 255) != i11) {
				this.data[(i10 * this.length + i8) * this.width + i9] = (byte)i11;
				if(i9 > 0) {
					this.floodFillCounters[i7++] = i9 - 1 << 20 | i10 << 10 | i8;
				}

				if(i9 < this.width - 1) {
					this.floodFillCounters[i7++] = i9 + 1 << 20 | i10 << 10 | i8;
				}

				if(i10 > 0) {
					this.floodFillCounters[i7++] = i9 << 20 | i10 - 1 << 10 | i8;
				}

				if(i10 < this.height - 1) {
					this.floodFillCounters[i7++] = i9 << 20 | i10 + 1 << 10 | i8;
				}

				if(i8 > 0) {
					this.floodFillCounters[i7++] = i9 << 20 | i10 << 10 | i8 - 1;
				}

				if(i8 < this.length - 1) {
					this.floodFillCounters[i7++] = i9 << 20 | i10 << 10 | i8 + 1;
				}
			}
		}

		Iterator iterator14 = this.worldAccesses.iterator();

		while(iterator14.hasNext()) {
			RenderGlobal renderGlobal10000 = (RenderGlobal)iterator14.next();
			Object object15 = null;
			renderGlobal10000.markBlocksForUpdate(i1 - 1, i2 - 1, i3 - 1, i4 + 1, i5 + 1, i6 + 1);
		}

	}

	public final void addRenderer(RenderGlobal renderGlobal1) {
		this.worldAccesses.add(renderGlobal1);
	}

	public final void finalize() {
	}

	public final void removeRenderer(RenderGlobal renderGlobal1) {
		this.worldAccesses.remove(renderGlobal1);
	}

	public final ArrayList getCollidingBoundingBoxes(AxisAlignedBB axisAlignedBB1) {
		ArrayList arrayList2 = new ArrayList();
		int i3 = (int)axisAlignedBB1.x0;
		int i4 = (int)axisAlignedBB1.x1 + 1;
		int i5 = (int)axisAlignedBB1.y0;
		int i6 = (int)axisAlignedBB1.y1 + 1;
		int i7 = (int)axisAlignedBB1.z0;
		int i8 = (int)axisAlignedBB1.z1 + 1;
		if(axisAlignedBB1.x0 < 0.0F) {
			--i3;
		}

		if(axisAlignedBB1.y0 < 0.0F) {
			--i5;
		}

		if(axisAlignedBB1.z0 < 0.0F) {
			--i7;
		}

		while(i3 < i4) {
			for(int i9 = i5; i9 < i6; ++i9) {
				for(int i10 = i7; i10 < i8; ++i10) {
					AxisAlignedBB axisAlignedBB11;
					if(i3 >= 0 && i9 >= 0 && i10 >= 0 && i3 < this.width && i9 < this.height && i10 < this.length) {
						Block block12;
						if((block12 = Block.blocksList[this.getBlockId(i3, i9, i10)]) != null && (axisAlignedBB11 = block12.getCollisionBoundingBoxFromPool(i3, i9, i10)) != null && axisAlignedBB1.intersectsWith(axisAlignedBB11)) {
							arrayList2.add(axisAlignedBB11);
						}
					} else if((i3 < 0 || i9 < 0 || i10 < 0 || i3 >= this.width || i10 >= this.length) && (axisAlignedBB11 = Block.bedrock.getCollisionBoundingBoxFromPool(i3, i9, i10)) != null && axisAlignedBB1.intersectsWith(axisAlignedBB11)) {
						arrayList2.add(axisAlignedBB11);
					}
				}
			}

			++i3;
		}

		return arrayList2;
	}

	public final void swap(int i1, int i2, int i3, int i4, int i5, int i6) {
		int i7 = this.getBlockId(i1, i2, i3);
		int i8 = this.getBlockId(i4, i5, i6);
		this.setBlock(i1, i2, i3, i8);
		this.setBlock(i4, i5, i6, i7);
		this.notifyBlocksOfNeighborChange(i1, i2, i3, i8);
		this.notifyBlocksOfNeighborChange(i4, i5, i6, i7);
	}

	public final boolean setBlock(int i1, int i2, int i3, int i4) {
		if(i1 >= 0 && i2 >= 0 && i3 >= 0 && i1 < this.width && i2 < this.height && i3 < this.length) {
			if(i4 == this.blocks[(i2 * this.length + i3) * this.width + i1]) {
				return false;
			} else {
				if(i4 == 0 && (i1 == 0 || i3 == 0 || i1 == this.width - 1 || i3 == this.length - 1)) {
					float f10000 = (float)i2;
					Object object5 = null;
					if(f10000 >= (float)this.waterLevel - 2.0F && (float)i2 < (float)this.waterLevel) {
						i4 = Block.waterMoving.blockID;
					}
				}

				byte b8 = this.blocks[(i2 * this.length + i3) * this.width + i1];
				this.blocks[(i2 * this.length + i3) * this.width + i1] = (byte)i4;
				if(b8 != 0) {
					Block.blocksList[b8].onBlockRemoval(this, i1, i2, i3);
				}

				if(i4 != 0) {
					Block.blocksList[i4].onBlockAdded(this, i1, i2, i3);
				}

				this.updateSkylight(i1, i3, 1, 1);
				this.updateLight(i1, i2, i3, i1 + 1, i2 + 1, i3 + 1);

				for(i4 = 0; i4 < this.worldAccesses.size(); ++i4) {
					((RenderGlobal)this.worldAccesses.get(i4)).markBlocksForUpdate(i1 - 1, i2 - 1, i3 - 1, i1 + 1, i2 + 1, i3 + 1);
				}

				return true;
			}
		} else {
			return false;
		}
	}

	public final boolean setBlockWithNotify(int i1, int i2, int i3, int i4) {
		if(this.setBlock(i1, i2, i3, i4)) {
			this.notifyBlocksOfNeighborChange(i1, i2, i3, i4);
			return true;
		} else {
			return false;
		}
	}

	public final void notifyBlocksOfNeighborChange(int i1, int i2, int i3, int i4) {
		this.notifyBlockOfNeighborChange(i1 - 1, i2, i3, i4);
		this.notifyBlockOfNeighborChange(i1 + 1, i2, i3, i4);
		this.notifyBlockOfNeighborChange(i1, i2 - 1, i3, i4);
		this.notifyBlockOfNeighborChange(i1, i2 + 1, i3, i4);
		this.notifyBlockOfNeighborChange(i1, i2, i3 - 1, i4);
		this.notifyBlockOfNeighborChange(i1, i2, i3 + 1, i4);
	}

	public final boolean setTileNoUpdate(int i1, int i2, int i3, int i4) {
		if(i1 >= 0 && i2 >= 0 && i3 >= 0 && i1 < this.width && i2 < this.height && i3 < this.length) {
			if(i4 == this.blocks[(i2 * this.length + i3) * this.width + i1]) {
				return false;
			} else {
				this.blocks[(i2 * this.length + i3) * this.width + i1] = (byte)i4;
				this.updateLight(i1, i2, i3, i1 + 1, i2 + 1, i3 + 1);
				return true;
			}
		} else {
			return false;
		}
	}

	private void notifyBlockOfNeighborChange(int i1, int i2, int i3, int i4) {
		if(i1 >= 0 && i2 >= 0 && i3 >= 0 && i1 < this.width && i2 < this.height && i3 < this.length) {
			Block block5;
			if((block5 = Block.blocksList[this.blocks[(i2 * this.length + i3) * this.width + i1]]) != null) {
				block5.onNeighborBlockChange(this, i1, i2, i3, i4);
			}

		}
	}

	public final boolean isHalfLit(int i1, int i2, int i3) {
		return i1 >= 0 && i2 >= 0 && i3 >= 0 && i1 < this.width && i2 < this.height && i3 < this.length ? this.getBlockLightValue(i1, i2, i3) > 0.5F : true;
	}

	public final int getBlockId(int i1, int i2, int i3) {
		return i1 >= 0 && i2 >= 0 && i3 >= 0 && i1 < this.width && i2 < this.height && i3 < this.length ? this.blocks[(i2 * this.length + i3) * this.width + i1] & 255 : 0;
	}

	public final boolean isBlockNormalCube(int i1, int i2, int i3) {
		Block block4;
		return (block4 = Block.blocksList[this.getBlockId(i1, i2, i3)]) == null ? false : block4.isOpaqueCube();
	}

	public final void updateEntities() {
		EntityMap entityMap9 = this.entityMap;

		for(int i1 = 0; i1 < entityMap9.entities.size(); ++i1) {
			Entity entity2;
			Entity entity10000 = entity2 = (Entity)entityMap9.entities.get(i1);
			entity10000.lastTickPosX = entity10000.posX;
			entity2.lastTickPosY = entity2.posY;
			entity2.lastTickPosZ = entity2.posZ;
			entity2.onEntityUpdate();
			++entity2.ticksExisted;
			if(entity2.isDead) {
				entityMap9.entities.remove(i1--);
				entityMap9.slot0.init(entity2.lastTickPosX, entity2.lastTickPosY, entity2.lastTickPosZ).remove(entity2);
			} else {
				int i3 = (int)(entity2.lastTickPosX / 16.0F);
				int i4 = (int)(entity2.lastTickPosY / 16.0F);
				int i5 = (int)(entity2.lastTickPosZ / 16.0F);
				int i6 = (int)(entity2.posX / 16.0F);
				int i7 = (int)(entity2.posY / 16.0F);
				int i8 = (int)(entity2.posZ / 16.0F);
				if(i3 != i6 || i4 != i7 || i5 != i8) {
					EntityMapSlot entityMapSlot12 = entityMap9.slot0.init(entity2.lastTickPosX, entity2.lastTickPosY, entity2.lastTickPosZ);
					EntityMapSlot entityMapSlot10 = entityMap9.slot1.init(entity2.posX, entity2.posY, entity2.posZ);
					if(!entityMapSlot12.equals(entityMapSlot10)) {
						entityMapSlot12.remove(entity2);
						entityMapSlot10.add(entity2);
					}
				}
			}
		}

	}

	public final void tick() {
		++this.playTime;
		int i1 = 1;

		int i2;
		for(i2 = 1; 1 << i1 < this.width; ++i1) {
		}

		while(1 << i2 < this.length) {
			++i2;
		}

		int i3 = this.length - 1;
		int i4 = this.width - 1;
		int i5 = this.height - 1;
		int i6;
		if((i6 = this.ticksList.size()) > this.MAX_TICKS) {
			i6 = this.MAX_TICKS;
		}

		int i7;
		int i10;
		for(i7 = 0; i7 < i6; ++i7) {
			NextTickListEntry nextTickListEntry8;
			if((nextTickListEntry8 = (NextTickListEntry)this.ticksList.remove(0)).scheduledTime > 0) {
				--nextTickListEntry8.scheduledTime;
				this.ticksList.add(nextTickListEntry8);
			} else {
				int i12 = nextTickListEntry8.zCoord;
				int i11 = nextTickListEntry8.yCoord;
				i10 = nextTickListEntry8.xCoord;
				byte b9;
				if(i10 >= 0 && i11 >= 0 && i12 >= 0 && i10 < this.width && i11 < this.height && i12 < this.length && (b9 = this.blocks[(nextTickListEntry8.yCoord * this.length + nextTickListEntry8.zCoord) * this.width + nextTickListEntry8.xCoord]) == nextTickListEntry8.blockID && b9 > 0) {
					Block.blocksList[b9].updateTick(this, nextTickListEntry8.xCoord, nextTickListEntry8.yCoord, nextTickListEntry8.zCoord, this.random);
				}
			}
		}

		this.updateLCG += this.width * this.length * this.height;
		i6 = this.updateLCG / 200;
		this.updateLCG -= i6 * 200;

		for(i7 = 0; i7 < i6; ++i7) {
			this.randId = this.randId * 3 + 1013904223;
			int i13;
			int i14 = (i13 = this.randId >> 2) & i4;
			i10 = i13 >> i1 & i3;
			i13 = i13 >> i1 + i2 & i5;
			byte b15 = this.blocks[(i13 * this.length + i10) * this.width + i14];
			if(Block.tickOnLoad[b15]) {
				Block.blocksList[b15].updateTick(this, i14, i13, i10, this.random);
			}
		}

	}

	public final int entitiesInLevelList(Class class1) {
		int i2 = 0;

		for(int i3 = 0; i3 < this.entityMap.entities.size(); ++i3) {
			Entity entity4 = (Entity)this.entityMap.entities.get(i3);
			if(class1.isAssignableFrom(entity4.getClass())) {
				++i2;
			}
		}

		return i2;
	}

	public final float getGroundLevel() {
		return (float)this.waterLevel - 2.0F;
	}

	public final float rgetGroundLevel() {
		return (float)this.waterLevel;
	}

	public final boolean getIsAnyLiquid(AxisAlignedBB axisAlignedBB1) {
		int i2 = (int)axisAlignedBB1.x0;
		int i3 = (int)axisAlignedBB1.x1 + 1;
		int i4 = (int)axisAlignedBB1.y0;
		int i5 = (int)axisAlignedBB1.y1 + 1;
		int i6 = (int)axisAlignedBB1.z0;
		int i7 = (int)axisAlignedBB1.z1 + 1;
		if(axisAlignedBB1.x0 < 0.0F) {
			--i2;
		}

		if(axisAlignedBB1.y0 < 0.0F) {
			--i4;
		}

		if(axisAlignedBB1.z0 < 0.0F) {
			--i6;
		}

		if(i2 < 0) {
			i2 = 0;
		}

		if(i4 < 0) {
			i4 = 0;
		}

		if(i6 < 0) {
			i6 = 0;
		}

		if(i3 > this.width) {
			i3 = this.width;
		}

		if(i5 > this.height) {
			i5 = this.height;
		}

		if(i7 > this.length) {
			i7 = this.length;
		}

		for(int i10 = i2; i10 < i3; ++i10) {
			for(i2 = i4; i2 < i5; ++i2) {
				for(int i8 = i6; i8 < i7; ++i8) {
					Block block9;
					if((block9 = Block.blocksList[this.getBlockId(i10, i2, i8)]) != null && block9.getMaterial() != Material.air) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public final boolean handleMaterialAcceleration(AxisAlignedBB axisAlignedBB1, Material material2) {
		int i3 = (int)axisAlignedBB1.x0;
		int i4 = (int)axisAlignedBB1.x1 + 1;
		int i5 = (int)axisAlignedBB1.y0;
		int i6 = (int)axisAlignedBB1.y1 + 1;
		int i7 = (int)axisAlignedBB1.z0;
		int i8 = (int)axisAlignedBB1.z1 + 1;
		if(axisAlignedBB1.x0 < 0.0F) {
			--i3;
		}

		if(axisAlignedBB1.y0 < 0.0F) {
			--i5;
		}

		if(axisAlignedBB1.z0 < 0.0F) {
			--i7;
		}

		if(i3 < 0) {
			i3 = 0;
		}

		if(i5 < 0) {
			i5 = 0;
		}

		if(i7 < 0) {
			i7 = 0;
		}

		if(i4 > this.width) {
			i4 = this.width;
		}

		if(i6 > this.height) {
			i6 = this.height;
		}

		if(i8 > this.length) {
			i8 = this.length;
		}

		for(int i11 = i3; i11 < i4; ++i11) {
			for(i3 = i5; i3 < i6; ++i3) {
				for(int i9 = i7; i9 < i8; ++i9) {
					Block block10;
					if((block10 = Block.blocksList[this.getBlockId(i11, i3, i9)]) != null && block10.getMaterial() == material2) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public final void scheduleBlockUpdate(int i1, int i2, int i3, int i4) {
		NextTickListEntry nextTickListEntry5 = new NextTickListEntry(i1, i2, i3, i4);
		if(i4 > 0) {
			nextTickListEntry5.scheduledTime = Block.blocksList[i4].tickRate();
		}

		this.ticksList.add(nextTickListEntry5);
	}

	public final boolean checkIfAABBIsClear(AxisAlignedBB axisAlignedBB1) {
		return this.entityMap.getEntitiesWithinAABBExcludingEntity((Entity)null, axisAlignedBB1).size() == 0;
	}

	public final List getEntitiesWithinAABBExcludingEntity(Entity entity1, AxisAlignedBB axisAlignedBB2) {
		return this.entityMap.getEntitiesWithinAABBExcludingEntity(entity1, axisAlignedBB2);
	}

	public final boolean isSolid(float f1, float f2, float f3, float f4) {
		return this.isSolid(f1 - f4, f2 - f4, f3 - f4) ? true : (this.isSolid(f1 - f4, f2 - f4, f3 + f4) ? true : (this.isSolid(f1 - f4, f2 + f4, f3 - f4) ? true : (this.isSolid(f1 - f4, f2 + f4, f3 + f4) ? true : (this.isSolid(f1 + f4, f2 - f4, f3 - f4) ? true : (this.isSolid(f1 + f4, f2 - f4, f3 + f4) ? true : (this.isSolid(f1 + f4, f2 + f4, f3 - f4) ? true : this.isSolid(f1 + f4, f2 + f4, f3 + f4)))))));
	}

	private boolean isSolid(float f1, float f2, float f3) {
		int i4;
		return (i4 = this.getBlockId((int)f1, (int)f2, (int)f3)) > 0 && Block.blocksList[i4].isOpaqueCube();
	}

	private int getFirstUncoveredBlock(int i1, int i2) {
		int i3;
		for(i3 = this.height; (this.getBlockId(i1, i3 - 1, i2) == 0 || Block.blocksList[this.getBlockId(i1, i3 - 1, i2)].getMaterial() != Material.air) && i3 > 0; --i3) {
		}

		return i3;
	}

	public final void setSpawnLocation(int i1, int i2, int i3, float f4) {
		this.xSpawn = i1;
		this.ySpawn = i2;
		this.zSpawn = i3;
		this.rotSpawn = f4;
	}

	public final float getBlockLightValue(int i1, int i2, int i3) {
		return i1 >= 0 && i2 >= 0 && i3 >= 0 && i1 < this.width && i2 < this.height && i3 < this.length ? lightBrightnessTable[this.data[(i2 * this.length + i3) * this.width + i1]] : 0.0F;
	}

	public final Material getBlockMaterial(int i1, int i2, int i3) {
		int i4;
		return (i4 = this.getBlockId(i1, i2, i3)) == 0 ? Material.air : Block.blocksList[i4].getMaterial();
	}

	public final boolean isWater(int i1, int i2, int i3) {
		int i4;
		return (i4 = this.getBlockId(i1, i2, i3)) > 0 && Block.blocksList[i4].getMaterial() == Material.water;
	}

	public final MovingObjectPosition rayTraceBlocks(Vec3D vec3D1, Vec3D vec3D2) {
		if(!Float.isNaN(vec3D1.xCoord) && !Float.isNaN(vec3D1.yCoord) && !Float.isNaN(vec3D1.zCoord)) {
			if(!Float.isNaN(vec3D2.xCoord) && !Float.isNaN(vec3D2.yCoord) && !Float.isNaN(vec3D2.zCoord)) {
				int i3 = (int)Math.floor((double)vec3D2.xCoord);
				int i4 = (int)Math.floor((double)vec3D2.yCoord);
				int i5 = (int)Math.floor((double)vec3D2.zCoord);
				int i6 = (int)Math.floor((double)vec3D1.xCoord);
				int i7 = (int)Math.floor((double)vec3D1.yCoord);
				int i8 = (int)Math.floor((double)vec3D1.zCoord);
				int i9 = 20;

				while(i9-- >= 0) {
					if(Float.isNaN(vec3D1.xCoord) || Float.isNaN(vec3D1.yCoord) || Float.isNaN(vec3D1.zCoord)) {
						return null;
					}

					if(i6 == i3 && i7 == i4 && i8 == i5) {
						return null;
					}

					float f10 = 999.0F;
					float f11 = 999.0F;
					float f12 = 999.0F;
					if(i3 > i6) {
						f10 = (float)i6 + 1.0F;
					}

					if(i3 < i6) {
						f10 = (float)i6;
					}

					if(i4 > i7) {
						f11 = (float)i7 + 1.0F;
					}

					if(i4 < i7) {
						f11 = (float)i7;
					}

					if(i5 > i8) {
						f12 = (float)i8 + 1.0F;
					}

					if(i5 < i8) {
						f12 = (float)i8;
					}

					float f13 = 999.0F;
					float f14 = 999.0F;
					float f15 = 999.0F;
					float f16 = vec3D2.xCoord - vec3D1.xCoord;
					float f17 = vec3D2.yCoord - vec3D1.yCoord;
					float f18 = vec3D2.zCoord - vec3D1.zCoord;
					if(f10 != 999.0F) {
						f13 = (f10 - vec3D1.xCoord) / f16;
					}

					if(f11 != 999.0F) {
						f14 = (f11 - vec3D1.yCoord) / f17;
					}

					if(f12 != 999.0F) {
						f15 = (f12 - vec3D1.zCoord) / f18;
					}

					boolean z19 = false;
					byte b24;
					if(f13 < f14 && f13 < f15) {
						if(i3 > i6) {
							b24 = 4;
						} else {
							b24 = 5;
						}

						vec3D1.xCoord = f10;
						vec3D1.yCoord += f17 * f13;
						vec3D1.zCoord += f18 * f13;
					} else if(f14 < f15) {
						if(i4 > i7) {
							b24 = 0;
						} else {
							b24 = 1;
						}

						vec3D1.xCoord += f16 * f14;
						vec3D1.yCoord = f11;
						vec3D1.zCoord += f18 * f14;
					} else {
						if(i5 > i8) {
							b24 = 2;
						} else {
							b24 = 3;
						}

						vec3D1.xCoord += f16 * f15;
						vec3D1.yCoord += f17 * f15;
						vec3D1.zCoord = f12;
					}

					Vec3D vec3D20;
					i6 = (int)((vec3D20 = new Vec3D(vec3D1.xCoord, vec3D1.yCoord, vec3D1.zCoord)).xCoord = (float)Math.floor((double)vec3D1.xCoord));
					if(b24 == 5) {
						--i6;
						++vec3D20.xCoord;
					}

					i7 = (int)(vec3D20.yCoord = (float)Math.floor((double)vec3D1.yCoord));
					if(b24 == 1) {
						--i7;
						++vec3D20.yCoord;
					}

					i8 = (int)(vec3D20.zCoord = (float)Math.floor((double)vec3D1.zCoord));
					if(b24 == 3) {
						--i8;
						++vec3D20.zCoord;
					}

					int i21 = this.getBlockId(i6, i7, i8);
					Block block23 = Block.blocksList[i21];
					if(i21 > 0 && block23.getMaterial() == Material.air) {
						MovingObjectPosition movingObjectPosition22;
						if(block23.renderAsNormalBlock()) {
							if((movingObjectPosition22 = block23.collisionRayTrace(i6, i7, i8, vec3D1, vec3D2)) != null) {
								return movingObjectPosition22;
							}
						} else if((movingObjectPosition22 = block23.collisionRayTrace(i6, i7, i8, vec3D1, vec3D2)) != null) {
							return movingObjectPosition22;
						}
					}
				}

				return null;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public final boolean growTrees(int i1, int i2, int i3) {
		int i4 = this.random.nextInt(3) + 4;
		boolean z5 = true;

		int i6;
		int i8;
		int i9;
		for(i6 = i2; i6 <= i2 + 1 + i4; ++i6) {
			byte b7 = 1;
			if(i6 == i2) {
				b7 = 0;
			}

			if(i6 >= i2 + 1 + i4 - 2) {
				b7 = 2;
			}

			for(i8 = i1 - b7; i8 <= i1 + b7 && z5; ++i8) {
				for(i9 = i3 - b7; i9 <= i3 + b7 && z5; ++i9) {
					if(i8 >= 0 && i6 >= 0 && i9 >= 0 && i8 < this.width && i6 < this.height && i9 < this.length) {
						int i10000 = this.blocks[(i6 * this.length + i9) * this.width + i8] & 255;
						boolean z10 = false;
						if(i10000 != 0) {
							z5 = false;
						}
					} else {
						z5 = false;
					}
				}
			}
		}

		if(!z5) {
			return false;
		} else if((this.blocks[((i2 - 1) * this.length + i3) * this.width + i1] & 255) == Block.grass.blockID && i2 < this.height - i4 - 1) {
			this.setBlockWithNotify(i1, i2 - 1, i3, Block.dirt.blockID);

			int i13;
			for(i13 = i2 - 3 + i4; i13 <= i2 + i4; ++i13) {
				i8 = i13 - (i2 + i4);
				i9 = 1 - i8 / 2;

				for(int i14 = i1 - i9; i14 <= i1 + i9; ++i14) {
					int i12 = i14 - i1;

					for(i6 = i3 - i9; i6 <= i3 + i9; ++i6) {
						int i11 = i6 - i3;
						if(Math.abs(i12) != i9 || Math.abs(i11) != i9 || this.random.nextInt(2) != 0 && i8 != 0) {
							this.setBlockWithNotify(i14, i13, i6, Block.leaves.blockID);
						}
					}
				}
			}

			for(i13 = 0; i13 < i4; ++i13) {
				this.setBlockWithNotify(i1, i2 + i13, i3, Block.log.blockID);
			}

			return true;
		} else {
			return false;
		}
	}

	public final Entity getPlayerEntity() {
		return this.playerEntity;
	}

	public final void spawnEntityInWorld(Entity entity1) {
		EntityMap entityMap2 = this.entityMap;
		this.entityMap.entities.add(entity1);
		entityMap2.slot0.init(entity1.posX, entity1.posY, entity1.posZ).add(entity1);
		entity1.lastTickPosX = entity1.posX;
		entity1.lastTickPosY = entity1.posY;
		entity1.lastTickPosZ = entity1.posZ;
		entity1.worldObj = this;
	}

	public final void releaseEntitySkin(Entity entity1) {
		EntityMap entityMap2;
		(entityMap2 = this.entityMap).slot0.init(entity1.lastTickPosX, entity1.lastTickPosY, entity1.lastTickPosZ).remove(entity1);
		entityMap2.entities.remove(entity1);
	}

	public final Entity findSubclassOf(Class class1) {
		for(int i2 = 0; i2 < this.entityMap.entities.size(); ++i2) {
			Entity entity3 = (Entity)this.entityMap.entities.get(i2);
			if(class1.isAssignableFrom(entity3.getClass())) {
				return entity3;
			}
		}

		return null;
	}

	public final int getMapHeight(int i1, int i2) {
		return this.heightMap[i1 + i2 * this.width];
	}

	public final boolean createExplosion(int i1, int i2, int i3, int i4) {
		if(i1 >= 0 && i2 >= 0 && i3 >= 0 && i1 < this.width && i2 < this.height && i3 < this.length) {
			byte b5 = this.blocks[(i2 * this.length + i3) * this.width + i1];
			Arrays.fill(this.coords, (byte)0);
			long j6 = System.nanoTime();
			byte b8 = 0;
			int i14 = b8 + 1;
			this.floodedBlocks[0] = i1 + (i3 << 10);
			int i9 = 0;

			while(true) {
				int i10;
				do {
					if(i14 <= 0) {
						++this.explosionTime;
						long j15 = System.nanoTime();
						System.out.println((double)(j15 - j6) / 1000000.0D + " ms for " + i9 + " tiles");
						return true;
					}

					--i14;
					i10 = this.floodedBlocks[i14];
				} while(this.coords[i10] == 1);

				i1 = i10 % 1024;

				for(i3 = i10 / 1024; i1 > 0 && this.coords[i10 - 1] == 0 && this.blocks[(i2 * this.length + i3) * this.width + i1 - 1] == b5; --i10) {
					--i1;
				}

				boolean z11 = false;

				for(boolean z12 = false; i1 < this.width && this.coords[i10] == 0 && this.blocks[(i2 * this.length + i3) * this.width + i1] == b5; ++i1) {
					byte b13;
					boolean z16;
					if(i3 > 0) {
						b13 = this.blocks[(i2 * this.length + i3 - 1) * this.width + i1];
						if((z16 = this.coords[i10 - 1024] == 0 && b13 == b5) && !z11) {
							this.floodedBlocks[i14++] = i10 - 1024;
						}

						z11 = z16;
					}

					if(i3 < this.length - 1) {
						b13 = this.blocks[(i2 * this.length + i3 + 1) * this.width + i1];
						if((z16 = this.coords[i10 + 1024] == 0 && b13 == b5) && !z12) {
							this.floodedBlocks[i14++] = i10 + 1024;
						}

						z12 = z16;
					}

					if(this.explosionTime % 2 == 1) {
						this.setBlock(i1, i2, i3, i4);
					}

					this.coords[i10] = 1;
					++i9;
					++i10;
				}
			}
		} else {
			return false;
		}
	}

	static {
		for(int i0 = 0; i0 <= 8; ++i0) {
			float f1 = 1.0F - (float)i0 / 8.0F;
			lightBrightnessTable[i0] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * 0.9F + 0.1F;
		}

	}
}