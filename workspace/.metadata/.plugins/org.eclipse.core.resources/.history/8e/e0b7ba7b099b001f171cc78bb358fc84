package net.minecraft.game.level;

import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.game.entity.AILiving;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.level.material.Material;

public final class MobSpawner {
	private World worldObj;

	public MobSpawner(World world1) {
		this.worldObj = world1;
	}

	public final void performSpawning() {
		int i1 = this.worldObj.width * this.worldObj.length * this.worldObj.height / 64 / 64 / 64;
		if(this.worldObj.random.nextInt(100) < i1 && this.worldObj.entitiesInLevelList(EntityLiving.class) < i1 * 20) {
			this.performSpawning(i1, this.worldObj.playerEntity, (LoadingScreenRenderer)null);
		}

	}

	public final int performSpawning(int i1, Entity entity2, LoadingScreenRenderer loadingScreenRenderer3) {
		int i19 = 0;

		for(int i4 = 0; i4 < i1; ++i4) {
			this.worldObj.random.nextInt(5);
			int i5 = this.worldObj.random.nextInt(this.worldObj.width);
			int i6 = (int)(Math.min(this.worldObj.random.nextFloat(), this.worldObj.random.nextFloat()) * (float)this.worldObj.height);
			int i7 = this.worldObj.random.nextInt(this.worldObj.length);
			if(!this.worldObj.isBlockNormalCube(i5, i6, i7) && this.worldObj.getBlockMaterial(i5, i6, i7) == Material.air && (!this.worldObj.isHalfLit(i5, i6, i7) || this.worldObj.random.nextInt(5) == 0)) {
				for(int i8 = 0; i8 < 8; ++i8) {
					int i9 = i5;
					int i10 = i6;
					int i11 = i7;

					for(int i12 = 0; i12 < 3; ++i12) {
						i9 += this.worldObj.random.nextInt(6) - this.worldObj.random.nextInt(6);
						i10 += this.worldObj.random.nextInt(1) - this.worldObj.random.nextInt(1);
						i11 += this.worldObj.random.nextInt(6) - this.worldObj.random.nextInt(6);
						if(i9 >= 0 && i11 >= 1 && i10 >= 0 && i10 < this.worldObj.height - 2 && i9 < this.worldObj.width && i11 < this.worldObj.length && this.worldObj.isBlockNormalCube(i9, i10 - 1, i11) && !this.worldObj.isBlockNormalCube(i9, i10, i11) && !this.worldObj.isBlockNormalCube(i9, i10 + 1, i11)) {
							float f13 = (float)i9 + 0.5F;
							float f14 = (float)i10 + 1.0F;
							float f15 = (float)i11 + 0.5F;
							float f16;
							float f17;
							float f18;
							if(entity2 != null) {
								f16 = f13 - entity2.posX;
								f17 = f14 - entity2.posY;
								f18 = f15 - entity2.posZ;
								if(f16 * f16 + f17 * f17 + f18 * f18 < 256.0F) {
									continue;
								}
							} else {
								f16 = f13 - (float)this.worldObj.xSpawn;
								f17 = f14 - (float)this.worldObj.ySpawn;
								f18 = f15 - (float)this.worldObj.zSpawn;
								if(f16 * f16 + f17 * f17 + f18 * f18 < 256.0F) {
									continue;
								}
							}

							EntityLiving entityLiving21 = new EntityLiving(this.worldObj);
							f18 = this.worldObj.random.nextFloat() * 360.0F;
							EntityLiving entityLiving20;
							entityLiving21.prevPosX = (entityLiving20 = entityLiving21).posX = f13;
							entityLiving20.prevPosY = entityLiving20.posY = f14;
							entityLiving20.prevPosZ = entityLiving20.posZ = f15;
							entityLiving20.rotationYaw = f18;
							entityLiving20.rotationPitch = 0.0F;
							entityLiving20.setPosition(f13, f14, f15);
							entityLiving21.entityAI = new AILiving();
							if(this.worldObj.checkIfAABBIsClear(entityLiving21.boundingBox)) {
								++i19;
								this.worldObj.spawnEntityInWorld(entityLiving21);
							}
						}
					}
				}
			}
		}

		return i19;
	}
}