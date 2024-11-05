package net.minecraft.game.entity;

import java.util.List;
import java.util.Random;

import net.minecraft.game.level.World;

import util.MathHelper;

public class AILiving extends AI {
	public Random rand = new Random();
	public float moveStrafing;
	public float moveForward;
	public float randomYawVelocity;
	private EntityLiving entityLiving;
	public boolean isJumping = false;
	private int fire = 0;
	public float moveSpeed = 0.7F;
	private int entityAge = 0;

	public final void onLivingUpdate(World world1, EntityLiving entityLiving2) {
		++this.entityAge;
		Entity entity3;
		float f4;
		float f5;
		if(this.entityAge > 600 && this.rand.nextInt(800) == 0 && (entity3 = world1.getPlayerEntity()) != null) {
			f4 = entity3.posX - entityLiving2.posX;
			f5 = entity3.posY - entityLiving2.posY;
			float f10 = entity3.posZ - entityLiving2.posZ;
			if(f4 * f4 + f5 * f5 + f10 * f10 < 1024.0F) {
				this.entityAge = 0;
			} else {
				entityLiving2.remove();
			}
		}

		this.entityLiving = entityLiving2;
		if(this.fire > 0) {
			--this.fire;
		}

		if(entityLiving2.health <= 0) {
			this.isJumping = false;
			this.moveStrafing = 0.0F;
			this.moveForward = 0.0F;
			this.randomYawVelocity = 0.0F;
		} else {
			this.updatePlayerActionState();
		}

		boolean z11 = entityLiving2.handleWaterMovement();
		boolean z12 = entityLiving2.handleLavaMovement();
		if(this.isJumping) {
			if(z11) {
				entityLiving2.motionY += 0.04F;
			} else if(z12) {
				entityLiving2.motionY += 0.04F;
			} else if(entityLiving2.onGround) {
				entity3 = null;
				this.entityLiving.motionY = 0.42F;
			}
		}

		this.moveStrafing *= 0.98F;
		this.moveForward *= 0.98F;
		this.randomYawVelocity *= 0.9F;
		f4 = this.moveForward;
		float f8 = this.moveStrafing;
		if(entityLiving2.handleWaterMovement()) {
			f5 = entityLiving2.posY;
			entityLiving2.moveFlying(f8, f4, 0.02F);
			entityLiving2.moveEntity(entityLiving2.motionX, entityLiving2.motionY, entityLiving2.motionZ);
			entityLiving2.motionX *= 0.8F;
			entityLiving2.motionY *= 0.8F;
			entityLiving2.motionZ *= 0.8F;
			entityLiving2.motionY = (float)((double)entityLiving2.motionY - 0.02D);
			if(entityLiving2.horizontalCollision && entityLiving2.isOffsetPositionInLiquid(entityLiving2.motionX, entityLiving2.motionY + 0.6F - entityLiving2.posY + f5, entityLiving2.motionZ)) {
				entityLiving2.motionY = 0.3F;
			}
		} else if(entityLiving2.handleLavaMovement()) {
			f5 = entityLiving2.posY;
			entityLiving2.moveFlying(f8, f4, 0.02F);
			entityLiving2.moveEntity(entityLiving2.motionX, entityLiving2.motionY, entityLiving2.motionZ);
			entityLiving2.motionX *= 0.5F;
			entityLiving2.motionY *= 0.5F;
			entityLiving2.motionZ *= 0.5F;
			entityLiving2.motionY = (float)((double)entityLiving2.motionY - 0.02D);
			if(entityLiving2.horizontalCollision && entityLiving2.isOffsetPositionInLiquid(entityLiving2.motionX, entityLiving2.motionY + 0.6F - entityLiving2.posY + f5, entityLiving2.motionZ)) {
				entityLiving2.motionY = 0.3F;
			}
		} else {
			entityLiving2.moveFlying(f8, f4, entityLiving2.onGround ? 0.1F : 0.02F);
			entityLiving2.moveEntity(entityLiving2.motionX, entityLiving2.motionY, entityLiving2.motionZ);
			entityLiving2.motionX *= 0.91F;
			entityLiving2.motionY *= 0.98F;
			entityLiving2.motionZ *= 0.91F;
			entityLiving2.motionY = (float)((double)entityLiving2.motionY - 0.08D);
			if(entityLiving2.onGround) {
				f5 = 0.6F;
				entityLiving2.motionX *= f5;
				entityLiving2.motionZ *= f5;
			}
		}

		List list15;
		if((list15 = world1.getEntitiesWithinAABBExcludingEntity(entityLiving2, entityLiving2.boundingBox.expand(0.2F, 0.0F, 0.2F))) != null && list15.size() > 0) {
			for(int i13 = 0; i13 < list15.size(); ++i13) {
				Entity entity14;
				if((entity14 = (Entity)list15.get(i13)).canBePushed()) {
					f4 = entityLiving2.posX - entity14.posX;
					float f6 = entityLiving2.posZ - entity14.posZ;
					float f7;
					if((f7 = f4 * f4 + f6 * f6) >= 0.01F) {
						f7 = MathHelper.sqrt_float(f7);
						f4 /= f7;
						f6 /= f7;
						f4 /= f7;
						f6 /= f7;
						f4 *= 0.05F;
						f6 *= 0.05F;
						entity14.addVelocity(-f4, -f6);
						entityLiving2.addVelocity(f4, f6);
					}
				}
			}
		}

	}

	protected void updatePlayerActionState() {
		if(this.rand.nextFloat() < 0.07F) {
			this.moveStrafing = (this.rand.nextFloat() - 0.5F) * this.moveSpeed;
			this.moveForward = this.rand.nextFloat() * this.moveSpeed;
		}

		this.isJumping = this.rand.nextFloat() < 0.01F;
		if(this.rand.nextFloat() < 0.04F) {
			this.randomYawVelocity = (this.rand.nextFloat() - 0.5F) * 60.0F;
		}

		this.entityLiving.rotationYaw += this.randomYawVelocity;
		this.entityLiving.rotationPitch = 0.0F;
		boolean z1 = this.entityLiving.handleWaterMovement();
		boolean z2 = this.entityLiving.handleLavaMovement();
		if(z1 || z2) {
			this.isJumping = this.rand.nextFloat() < 0.8F;
		}

	}
}