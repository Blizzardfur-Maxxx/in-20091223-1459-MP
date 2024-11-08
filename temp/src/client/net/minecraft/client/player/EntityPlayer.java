package net.minecraft.client.player;

import java.awt.image.BufferedImage;
import java.util.List;

import net.minecraft.client.render.RenderEngine;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.level.World;

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

	public final void onLivingUpdate() {
		InventoryPlayer inventoryPlayer1 = this.inventory;

		for(int i2 = 0; i2 < inventoryPlayer1.animationsToGo.length; ++i2) {
			if(inventoryPlayer1.animationsToGo[i2] > 0) {
				--inventoryPlayer1.animationsToGo[i2];
			}
		}

		this.prevCameraYaw = this.cameraYaw;
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