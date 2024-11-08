package net.minecraft.client.player;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.level.World;
import net.minecraft.game.physics.AxisAlignedBB;

import org.lwjgl.opengl.GL11;

import util.MathHelper;

public class EntityPlayer extends EntityLiving {
	public transient MovementInput movementInput;
	public InventoryPlayer inventory = new InventoryPlayer();
	public float prevCameraYaw;
	public float cameraYaw;
	private int getScore = 0;
	public int getArrows = 20;
	private static int skinID = -1;
	public static BufferedImage skinData;
	public byte userType = 0;
	public Minecraft mc;
	public boolean flying;

	public EntityPlayer(World world1) {
		super(world1);
		if(world1 != null) {
			world1.playerEntity = this;
			world1.releaseEntitySkin(this);
			world1.spawnEntityInWorld(this);
		}

		this.yOffset = 1.62F;
		this.health = 20;
		this.entityAI = new EntityPlayerInput(this);
	}

	public final void preparePlayerToSpawn() {
		this.yOffset = 1.62F;
		this.setSize(0.6F, 1.8F);
		super.preparePlayerToSpawn();
		if(this.worldObj != null) {
			this.worldObj.playerEntity = this;
		}

		this.health = 20;
		this.deathTime = 0;
	}
	
	public void moveEntity(float moveX, float moveY, float moveZ) {
		if (this.flying) {
		        if (moveY < 0) moveY = 0;

		        moveX *= 5;
		        moveZ *= 5;
		        
		        if (this.movementInput.jump) {
		            moveY += 0.5;
		        }
		        if (this.movementInput.sneak) {
		            moveY -= 0.5;
		        }
		        super.moveEntity(moveX, moveY, moveZ);
		} else {
			float f4 = this.posX;
			float f5 = this.posZ;
			float f6 = moveX;
			float f7 = moveY;
			float f8 = moveZ;
			AxisAlignedBB axisAlignedBB9 = this.boundingBox.copy();
			ArrayList arrayList10 = this.worldObj.getCollidingBoundingBoxes(this.boundingBox.addCoord(moveX, moveY, moveZ));

			for(int i11 = 0; i11 < arrayList10.size(); ++i11) {
				moveY = ((AxisAlignedBB)arrayList10.get(i11)).clipYCollide(this.boundingBox, moveY);
			}

			this.boundingBox.offset(0.0F, moveY, 0.0F);
			if(!this.collision && f7 != moveY) {
				moveZ = 0.0F;
				moveY = 0.0F;
				moveX = 0.0F;
			}

			boolean z16 = this.onGround || f7 != moveY && f7 < 0.0F;

			int i12;
			for(i12 = 0; i12 < arrayList10.size(); ++i12) {
				moveX = ((AxisAlignedBB)arrayList10.get(i12)).clipXCollide(this.boundingBox, moveX);
			}

			this.boundingBox.offset(moveX, 0.0F, 0.0F);
			if(!this.collision && f6 != moveX) {
				moveZ = 0.0F;
				moveY = 0.0F;
				moveX = 0.0F;
			}

			for(i12 = 0; i12 < arrayList10.size(); ++i12) {
				moveZ = ((AxisAlignedBB)arrayList10.get(i12)).clipZCollide(this.boundingBox, moveZ);
			}

			this.boundingBox.offset(0.0F, 0.0F, moveZ);
			if(!this.collision && f8 != moveZ) {
				moveZ = 0.0F;
				moveY = 0.0F;
				moveX = 0.0F;
			}

			float moveX7;
			float moveX8;
			if(this.stepHeight > 0.0F && z16 && this.ySize < 0.05F && (f6 != moveX || f8 != moveZ)) {
				moveX8 = moveX;
				moveX7 = moveY;
				float moveX3 = moveZ;
				moveX = f6;
				moveY = this.stepHeight;
				moveZ = f8;
				AxisAlignedBB axisAlignedBB14 = this.boundingBox.copy();
				this.boundingBox = axisAlignedBB9.copy();
				arrayList10 = this.worldObj.getCollidingBoundingBoxes(this.boundingBox.addCoord(f6, moveY, f8));

				int i15;
				for(i15 = 0; i15 < arrayList10.size(); ++i15) {
					moveY = ((AxisAlignedBB)arrayList10.get(i15)).clipYCollide(this.boundingBox, moveY);
				}

				this.boundingBox.offset(0.0F, moveY, 0.0F);
				if(!this.collision && f7 != moveY) {
					moveZ = 0.0F;
					moveY = 0.0F;
					moveX = 0.0F;
				}

				for(i15 = 0; i15 < arrayList10.size(); ++i15) {
					moveX = ((AxisAlignedBB)arrayList10.get(i15)).clipXCollide(this.boundingBox, moveX);
				}

				this.boundingBox.offset(moveX, 0.0F, 0.0F);
				if(!this.collision && f6 != moveX) {
					moveZ = 0.0F;
					moveY = 0.0F;
					moveX = 0.0F;
				}

				for(i15 = 0; i15 < arrayList10.size(); ++i15) {
					moveZ = ((AxisAlignedBB)arrayList10.get(i15)).clipZCollide(this.boundingBox, moveZ);
				}

				this.boundingBox.offset(0.0F, 0.0F, moveZ);
				if(!this.collision && f8 != moveZ) {
					moveZ = 0.0F;
					moveY = 0.0F;
					moveX = 0.0F;
				}

				if(moveX8 * moveX8 + moveX3 * moveX3 >= moveX * moveX + moveZ * moveZ) {
					moveX = moveX8;
					moveY = moveX7;
					moveZ = moveX3;
					this.boundingBox = axisAlignedBB14.copy();
				} else {
					this.ySize = (float)((double)this.ySize + 0.5D);
				}
			}

			this.horizontalCollision = f6 != moveX || f8 != moveZ;
			this.onGround = f7 != moveY && f7 < 0.0F;
			if(this.onGround) {
				if(this.fallDistance > 0.0F) {
					this.fall(this.fallDistance);
					this.fallDistance = 0.0F;
				}
			} else if(moveY < 0.0F) {
				this.fallDistance -= moveY;
			}

			if(f6 != moveX) {
				this.motionX = 0.0F;
			}

			if(f7 != moveY) {
				this.motionY = 0.0F;
			}

			if(f8 != moveZ) {
				this.motionZ = 0.0F;
			}

			this.posX = (this.boundingBox.x0 + this.boundingBox.x1) / 2.0F;
			this.posY = this.boundingBox.y0 + this.yOffset - this.ySize;
			this.posZ = (this.boundingBox.z0 + this.boundingBox.z1) / 2.0F;
			moveX8 = this.posX - f4;
			moveX7 = this.posZ - f5;
			this.distanceWalkedModified = (float)((double)this.distanceWalkedModified + (double)MathHelper.sqrt_float(moveX8 * moveX8 + moveX7 * moveX7) * 0.6D);
			if(this.makeStepSound) {
				int i19 = this.worldObj.getBlockId((int)this.posX, (int)(this.posY - 0.2F - this.yOffset), (int)this.posZ);
				if(this.distanceWalkedModified > (float)this.nextStep && i19 > 0) {
					++this.nextStep;
				}
			}

			this.ySize *= 0.4F;
		}
	}
	public final void onLivingUpdate() {
		InventoryPlayer inventoryPlayer1 = this.inventory;

		for(int i2 = 0; i2 < inventoryPlayer1.animationsToGo.length; ++i2) {
			if(inventoryPlayer1.animationsToGo[i2] > 0) {
				--inventoryPlayer1.animationsToGo[i2];
			}
		}

		this.prevCameraYaw = this.cameraYaw;
		if (this.flying) {
			this.fallDistance = 0;
			this.motionY = 0;
		}
		this.movementInput.updatePlayerMoveState();
		super.onLivingUpdate();
		float f4 = MathHelper.sqrt_float(this.motionX * this.motionX + this.motionZ * this.motionZ);
		float f5 = (float)Math.atan((double)(-this.motionY * 0.2F)) * 15.0F;
		if(f4 > 0.1F) {
			f4 = 0.1F;
		}

		if(!this.onGround || this.health <= 0) {
			f4 = 0.0F;
		}

		if(this.onGround || this.health <= 0) {
			f5 = 0.0F;
		}

		this.cameraYaw += (f4 - this.cameraYaw) * 0.4F;
		this.cameraPitch += (f5 - this.cameraPitch) * 0.8F;
		List list3;
		if(this.health > 0 && (list3 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(1.0F, 0.0F, 1.0F))) != null) {
			for(int i6 = 0; i6 < list3.size(); ++i6) {
				list3.get(i6);
			}
		}

	}

	public final void resetKeyState() {
		this.movementInput.resetKeyState();
	}

	public final void checkKeyForMovementInput(int i1, boolean z2) {
		this.movementInput.checkKeyForMovementInput(i1, z2);
	}

	public final int getScore() {
		return 0;
	}

	public final void onDeath(Entity entity1) {
		this.setSize(0.2F, 0.2F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.motionY = 0.1F;
		if(entity1 != null) {
			this.motionX = -MathHelper.cos((this.attackedAtYaw + this.rotationYaw) * (float)Math.PI / 180.0F) * 0.1F;
			this.motionZ = -MathHelper.sin((this.attackedAtYaw + this.rotationYaw) * (float)Math.PI / 180.0F) * 0.1F;
		} else {
			this.motionX = this.motionZ = 0.0F;
		}

		this.yOffset = 0.1F;
	}

	public final void remove() {
	}

	public static void setupSkinImage(RenderEngine renderEngine0) {
		if(skinData != null) {
			BufferedImage bufferedImage2 = skinData;
			renderEngine0.singleIntBuffer.clear();
			GL11.glGenTextures(renderEngine0.singleIntBuffer);
			int i3 = renderEngine0.singleIntBuffer.get(0);
			renderEngine0.setupTexture(bufferedImage2, i3);
			renderEngine0.textureContentsMap.put(i3, bufferedImage2);
			skinID = i3;
			skinData = null;
		}

		int i4;
		if(skinID < 0) {
			i4 = renderEngine0.getTexture("/char.png");
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, i4);
		} else {
			i4 = skinID;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, i4);
		}
	}
}