package net.minecraft.game.entity;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.client.net.EntityPos;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;
import net.minecraft.game.level.material.Material;
import net.minecraft.game.physics.AxisAlignedBB;

import util.MathHelper;

public abstract class Entity {
	public World worldObj;
	public float prevPosX;
	public float prevPosY;
	public float prevPosZ;
	public float posX;
	public float posY;
	public float posZ;
	public float motionX;
	public float motionY;
	public float motionZ;
	public float rotationYaw;
	public float rotationPitch;
	public float prevRotationYaw;
	public float prevRotationPitch;
	public AxisAlignedBB boundingBox;
	public boolean onGround = false;
	public boolean horizontalCollision = false;
	protected boolean collision = true;
	public boolean isDead = false;
	public float yOffset = 0.0F;
	private float bbWidth = 0.6F;
	public float bbHeight = 1.8F;
	public float prevDistanceWalkedModified = 0.0F;
	public float distanceWalkedModified = 0.0F;
	public boolean makeStepSound = true;
	protected float fallDistance = 0.0F;
	protected int nextStep = 1;
	public float lastTickPosX;
	public float lastTickPosY;
	public float lastTickPosZ;
	protected float ySize = 0.0F;
	public float stepHeight = 0.0F;
	public int ticksExisted;
	public boolean helmet = Math.random() < (double)0.2F;
	public boolean armor = Math.random() < (double)0.2F;
	public boolean allowAlpha = true;
	public boolean hasHair = true;
	public float entityCollisionReduction = 0.0F;

	public Entity(World world1) {
		new Random();
		this.ticksExisted = 0;
		this.worldObj = world1;
		this.setPosition(0.0F, 0.0F, 0.0F);
	}

	public void preparePlayerToSpawn() {
		if(this.worldObj != null) {
			float f1 = (float)this.worldObj.xSpawn + 0.5F;
			float f2 = (float)this.worldObj.ySpawn;

			for(float f3 = (float)this.worldObj.zSpawn + 0.5F; f2 > 0.0F; ++f2) {
				this.setPosition(f1, f2, f3);
				if(this.worldObj.getCollidingBoundingBoxes(this.boundingBox).size() == 0) {
					break;
				}
			}

			this.motionX = this.motionY = this.motionZ = 0.0F;
			this.rotationYaw = this.worldObj.rotSpawn;
			this.rotationPitch = 0.0F;
		}
	}

	public void remove() {
		this.isDead = true;
	}

	public final void setSize(float f1, float f2) {
		this.bbWidth = f1;
		this.bbHeight = f2;
	}
	
	
	public void setPos(EntityPos pos) {
		if(pos.moving) {
			this.setPosition(pos.x, pos.y, pos.z);
		} else {
			this.setPosition(this.posX, this.posY, this.posZ);
		}

		if(pos.rotating) {
			this.setRot(pos.yRot, pos.xRot);
		} else {
			this.setRot(this.rotationYaw, this.rotationPitch);
		}
	}
	
	public final void setPositionAndRotation(float posX, float f2, float posZ, float rotationYaw, float rotationPitch) {
		this.prevPosX = this.posX = posX;
		this.prevPosY = this.posY = f2 + this.yOffset;
		this.prevPosZ = this.posZ = posZ;
		this.rotationYaw = rotationYaw;
		this.rotationPitch = rotationPitch;
		this.setPosition(this.posX, this.posY, this.posZ);
	}

	public final void setPosition(float f1, float f2, float f3) {
		this.posX = f1;
		this.posY = f2;
		this.posZ = f3;
		float f4 = this.bbWidth / 2.0F;
		float f5 = this.bbHeight / 2.0F;
		this.boundingBox = new AxisAlignedBB(f1 - f4, f2 - f5, f3 - f4, f1 + f4, f2 + f5, f3 + f4);
	}
	
	protected void setRot(float xo, float yo) {
		this.rotationYaw = xo;
		this.rotationPitch = yo;
	}

	public void onEntityUpdate() {
		this.prevDistanceWalkedModified = this.distanceWalkedModified;
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.prevRotationPitch = this.rotationPitch;
		this.prevRotationYaw = this.rotationYaw;
	}

	public final boolean isOffsetPositionInLiquid(float f1, float f2, float f3) {
		float f4 = f3;
		f3 = f2;
		f2 = f1;
		AxisAlignedBB axisAlignedBB5 = this.boundingBox;
		axisAlignedBB5 = new AxisAlignedBB(axisAlignedBB5.x0 + f4, axisAlignedBB5.y0 + f3, axisAlignedBB5.z0 + f4, axisAlignedBB5.x1 + f2, axisAlignedBB5.y1 + f3, axisAlignedBB5.z1 + f4);
		return this.worldObj.getCollidingBoundingBoxes(axisAlignedBB5).size() > 0 ? false : !this.worldObj.getIsAnyLiquid(axisAlignedBB5);
	}

	public void moveEntity(float f1, float f2, float f3) {
		float f4 = this.posX;
		float f5 = this.posZ;
		float f6 = f1;
		float f7 = f2;
		float f8 = f3;
		AxisAlignedBB axisAlignedBB9 = this.boundingBox.copy();
		ArrayList arrayList10 = this.worldObj.getCollidingBoundingBoxes(this.boundingBox.addCoord(f1, f2, f3));

		for(int i11 = 0; i11 < arrayList10.size(); ++i11) {
			f2 = ((AxisAlignedBB)arrayList10.get(i11)).clipYCollide(this.boundingBox, f2);
		}

		this.boundingBox.offset(0.0F, f2, 0.0F);
		if(!this.collision && f7 != f2) {
			f3 = 0.0F;
			f2 = 0.0F;
			f1 = 0.0F;
		}

		boolean z16 = this.onGround || f7 != f2 && f7 < 0.0F;

		int i12;
		for(i12 = 0; i12 < arrayList10.size(); ++i12) {
			f1 = ((AxisAlignedBB)arrayList10.get(i12)).clipXCollide(this.boundingBox, f1);
		}

		this.boundingBox.offset(f1, 0.0F, 0.0F);
		if(!this.collision && f6 != f1) {
			f3 = 0.0F;
			f2 = 0.0F;
			f1 = 0.0F;
		}

		for(i12 = 0; i12 < arrayList10.size(); ++i12) {
			f3 = ((AxisAlignedBB)arrayList10.get(i12)).clipZCollide(this.boundingBox, f3);
		}

		this.boundingBox.offset(0.0F, 0.0F, f3);
		if(!this.collision && f8 != f3) {
			f3 = 0.0F;
			f2 = 0.0F;
			f1 = 0.0F;
		}

		float f17;
		float f18;
		if(this.stepHeight > 0.0F && z16 && this.ySize < 0.05F && (f6 != f1 || f8 != f3)) {
			f18 = f1;
			f17 = f2;
			float f13 = f3;
			f1 = f6;
			f2 = this.stepHeight;
			f3 = f8;
			AxisAlignedBB axisAlignedBB14 = this.boundingBox.copy();
			this.boundingBox = axisAlignedBB9.copy();
			arrayList10 = this.worldObj.getCollidingBoundingBoxes(this.boundingBox.addCoord(f6, f2, f8));

			int i15;
			for(i15 = 0; i15 < arrayList10.size(); ++i15) {
				f2 = ((AxisAlignedBB)arrayList10.get(i15)).clipYCollide(this.boundingBox, f2);
			}

			this.boundingBox.offset(0.0F, f2, 0.0F);
			if(!this.collision && f7 != f2) {
				f3 = 0.0F;
				f2 = 0.0F;
				f1 = 0.0F;
			}

			for(i15 = 0; i15 < arrayList10.size(); ++i15) {
				f1 = ((AxisAlignedBB)arrayList10.get(i15)).clipXCollide(this.boundingBox, f1);
			}

			this.boundingBox.offset(f1, 0.0F, 0.0F);
			if(!this.collision && f6 != f1) {
				f3 = 0.0F;
				f2 = 0.0F;
				f1 = 0.0F;
			}

			for(i15 = 0; i15 < arrayList10.size(); ++i15) {
				f3 = ((AxisAlignedBB)arrayList10.get(i15)).clipZCollide(this.boundingBox, f3);
			}

			this.boundingBox.offset(0.0F, 0.0F, f3);
			if(!this.collision && f8 != f3) {
				f3 = 0.0F;
				f2 = 0.0F;
				f1 = 0.0F;
			}

			if(f18 * f18 + f13 * f13 >= f1 * f1 + f3 * f3) {
				f1 = f18;
				f2 = f17;
				f3 = f13;
				this.boundingBox = axisAlignedBB14.copy();
			} else {
				this.ySize = (float)((double)this.ySize + 0.5D);
			}
		}

		this.horizontalCollision = f6 != f1 || f8 != f3;
		this.onGround = f7 != f2 && f7 < 0.0F;
		if(this.onGround) {
			if(this.fallDistance > 0.0F) {
				this.fall(this.fallDistance);
				this.fallDistance = 0.0F;
			}
		} else if(f2 < 0.0F) {
			this.fallDistance -= f2;
		}

		if(f6 != f1) {
			this.motionX = 0.0F;
		}

		if(f7 != f2) {
			this.motionY = 0.0F;
		}

		if(f8 != f3) {
			this.motionZ = 0.0F;
		}

		this.posX = (this.boundingBox.x0 + this.boundingBox.x1) / 2.0F;
		this.posY = this.boundingBox.y0 + this.yOffset - this.ySize;
		this.posZ = (this.boundingBox.z0 + this.boundingBox.z1) / 2.0F;
		f18 = this.posX - f4;
		f17 = this.posZ - f5;
		this.distanceWalkedModified = (float)((double)this.distanceWalkedModified + (double)MathHelper.sqrt_float(f18 * f18 + f17 * f17) * 0.6D);
		if(this.makeStepSound) {
			int i19 = this.worldObj.getBlockId((int)this.posX, (int)(this.posY - 0.2F - this.yOffset), (int)this.posZ);
			if(this.distanceWalkedModified > (float)this.nextStep && i19 > 0) {
				++this.nextStep;
			}
		}

		this.ySize *= 0.4F;
	}

	protected void fall(float f1) {
	}

	public final boolean handleWaterMovement() {
		return this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0F, -0.4F, 0.0F), Material.water);
	}

	public final boolean isInsideOfMaterial() {
		int i1;
		return (i1 = this.worldObj.getBlockId((int)this.posX, (int)(this.posY + 0.12F), (int)this.posZ)) != 0 ? Block.blocksList[i1].getMaterial().equals(Material.water) : false;
	}

	public final boolean handleLavaMovement() {
		return this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0F, -0.4F, 0.0F), Material.lava);
	}

	public final void moveFlying(float f1, float f2, float f3) {
		float f4;
		if((f4 = MathHelper.sqrt_float(f1 * f1 + f2 * f2)) >= 0.01F) {
			if(f4 < 1.0F) {
				f4 = 1.0F;
			}

			f4 = f3 / f4;
			f1 *= f4;
			f2 *= f4;
			f3 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F);
			f4 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F);
			this.motionX += f1 * f4 - f2 * f3;
			this.motionZ += f2 * f4 + f1 * f3;
		}
	}

	public final float getBrightness() {
		int i1 = (int)this.posX;
		int i2 = (int)(this.posY + this.yOffset / 2.0F);
		int i3 = (int)this.posZ;
		return this.worldObj.getBlockLightValue(i1, i2, i3);
	}

	void addVelocity(float f1, float f2) {
		this.motionX += f1;
		this.motionY = this.motionY;
		this.motionZ += f2;
	}

	public void attackEntityFrom(Entity entity1, int i2) {
	}

	public boolean canBeCollidedWith() {
		return false;
	}

	public boolean canBePushed() {
		return false;
	}
}