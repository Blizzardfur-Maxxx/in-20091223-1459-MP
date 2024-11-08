package net.minecraft.game.entity;

import net.minecraft.game.level.World;

import util.MathHelper;

public class EntityLiving extends Entity {
	public int heartsHalvesLife = 20;
	public float renderYawOffset = 0.0F;
	public float prevRenderYawOffset = 0.0F;
	private float rotationYawHead;
	private float prevRotationYawHead;
	public int maxAir = 10;
	public int health;
	public int prevHealth;
	public int scoreValue = 0;
	public int air = 300;
	public int hurtTime;
	public int maxHurtTime;
	public float attackedAtYaw = 0.0F;
	public int deathTime = 0;
	private int attackTime = 0;
	public float prevCameraPitch;
	public float cameraPitch;
	public AI entityAI = null;
	public AILiving allLiving;

	public EntityLiving(World world1) {
		super(world1);
		Math.random();
		this.health = 20;
		Math.random();
		this.setPosition(this.posX, this.posY, this.posZ);
		Math.random();
		Math.random();
		this.stepHeight = 0.5F;
		//this.allLiving.isJumping = false;
	}

	public final boolean canBeCollidedWith() {
		return !this.isDead;
	}

	public final boolean canBePushed() {
		return !this.isDead;
	}

	public final void onEntityUpdate() {
		super.onEntityUpdate();
		this.prevCameraPitch = this.cameraPitch;
		if(this.attackTime > 0) {
			--this.attackTime;
		}

		if(this.hurtTime > 0) {
			--this.hurtTime;
		}

		if(this.scoreValue > 0) {
			--this.scoreValue;
		}

		if(this.health <= 0) {
			++this.deathTime;
			if(this.deathTime > 20) {
				this.remove();
			}
		}

		if(this.isInsideOfMaterial()) {
			if(this.air > 0) {
				--this.air;
			} else {
				this.attackEntityFrom((Entity)null, 2);
			}
		} else {
			this.air = this.maxAir;
		}

		if(this.handleWaterMovement()) {
			this.fallDistance = 0.0F;
		}

		if(this.handleLavaMovement()) {
			this.attackEntityFrom((Entity)null, 10);
		}

		this.prevRenderYawOffset = this.renderYawOffset;
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
		++this.ticksExisted;
		this.onLivingUpdate();
		float f1 = this.posX - this.prevPosX;
		float f2 = this.posZ - this.prevPosZ;
		float f3 = MathHelper.sqrt_float(f1 * f1 + f2 * f2);
		float f4 = this.renderYawOffset;
		float f5 = 0.0F;
		float f6 = 0.0F;
		if(f3 > 0.05F) {
			f6 = 1.0F;
			f5 = f3 * 3.0F;
			f4 = (float)Math.atan2((double)f2, (double)f1) * 180.0F / (float)Math.PI - 90.0F;
		}

		if(!this.onGround) {
			f6 = 0.0F;
		}

		this.rotationYawHead += (f6 - this.rotationYawHead) * 0.3F;

		for(f1 = f4 - this.renderYawOffset; f1 < -180.0F; f1 += 360.0F) {
		}

		while(f1 >= 180.0F) {
			f1 -= 360.0F;
		}

		this.renderYawOffset += f1 * 0.1F;

		for(f1 = this.rotationYaw - this.renderYawOffset; f1 < -180.0F; f1 += 360.0F) {
		}

		while(f1 >= 180.0F) {
			f1 -= 360.0F;
		}

		boolean z7 = f1 < -90.0F || f1 >= 90.0F;
		if(f1 < -75.0F) {
			f1 = -75.0F;
		}

		if(f1 >= 75.0F) {
			f1 = 75.0F;
		}

		this.renderYawOffset = this.rotationYaw - f1;
		this.renderYawOffset += f1 * 0.1F;
		if(z7) {
			f5 = -f5;
		}

		while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		while(this.renderYawOffset - this.prevRenderYawOffset < -180.0F) {
			this.prevRenderYawOffset -= 360.0F;
		}

		while(this.renderYawOffset - this.prevRenderYawOffset >= 180.0F) {
			this.prevRenderYawOffset += 360.0F;
		}

		while(this.rotationPitch - this.prevRotationPitch < -180.0F) {
			this.prevRotationPitch -= 360.0F;
		}

		while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		this.prevRotationYawHead += f5;
	}

	public void onLivingUpdate() {
		if(this.entityAI != null) {
			this.entityAI.onLivingUpdate(this.worldObj, this);
		}
	}

	public final void attackEntityFrom(Entity entity1, int i2) {
		if(this.worldObj.survivalWorld) {
			if(this.health > 0) {
				if((float)this.scoreValue > (float)this.heartsHalvesLife / 2.0F) {
					if(this.prevHealth - i2 >= this.health) {
						return;
					}

					this.health = this.prevHealth - i2;
				} else {
					this.prevHealth = this.health;
					this.scoreValue = this.heartsHalvesLife;
					this.health -= i2;
					this.hurtTime = this.maxHurtTime = 10;
				}

				this.attackedAtYaw = 0.0F;
				if(entity1 != null) {
					float f7 = entity1.posX - this.posX;
					float f3 = entity1.posZ - this.posZ;
					this.attackedAtYaw = (float)(Math.atan2((double)f3, (double)f7) * 180.0D / (double)(float)Math.PI) - this.rotationYaw;
					float f5 = MathHelper.sqrt_float(f7 * f7 + f3 * f3);
					float f6 = 0.4F;
					this.motionX /= 2.0F;
					this.motionY /= 2.0F;
					this.motionZ /= 2.0F;
					this.motionX -= f7 / f5 * f6;
					this.motionY += 0.4F;
					this.motionZ -= f3 / f5 * f6;
					if(this.motionY > 0.4F) {
						this.motionY = 0.4F;
					}
				} else {
					this.attackedAtYaw = (float)((int)(Math.random() * 2.0D) * 180);
				}

				if(this.health <= 0) {
					this.onDeath(entity1);
				}

			}
		}
	}

	public void onDeath(Entity entity1) {
	}

	protected final void fall(float f1) {
		int i2;
		if((i2 = (int)Math.ceil((double)(f1 - 3.0F))) > 0) {
			this.attackEntityFrom((Entity)null, i2);
		}

	}
}