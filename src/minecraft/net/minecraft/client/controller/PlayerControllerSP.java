package net.minecraft.client.controller;

import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.EntityPlayer;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.level.MobSpawner;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public final class PlayerControllerSP extends PlayerController {
	public PlayerControllerSP(Minecraft minecraft1) {
		super(minecraft1);
	}

	private int curBlockX;
	private int curBlockY;
	private int curBlockZ;
	private int curBlockDamage;
	private int prevBlockDamage;
	private int blockHitWait;
	private MobSpawner mobSpawner;

	public final void preparePlayer(EntityPlayer entityPlayer1) {
		entityPlayer1.inventory.mainInventory[5] = Block.stairSingle.blockID;
		entityPlayer1.inventory.stackSize[5] = 99;
		entityPlayer1.inventory.mainInventory[6] = Block.stone.blockID;
		entityPlayer1.inventory.stackSize[6] = 99;
		entityPlayer1.inventory.mainInventory[7] = Block.waterMoving.blockID;
		entityPlayer1.inventory.stackSize[7] = 99;
		entityPlayer1.inventory.mainInventory[8] = Block.lavaMoving.blockID;
		entityPlayer1.inventory.stackSize[8] = 99;
	}

	public final void sendBlockRemoved(int i1, int i2, int i3) {
		int i4 = this.mc.theWorld.getBlockId(i1, i2, i3);
		Block.blocksList[i4].dropBlockAsItem(this.mc.theWorld);
		super.sendBlockRemoved(i1, i2, i3);
	}
	
	public final boolean removeResource(int quantity) {
		return this.mc.thePlayer.inventory.consumeInventoryItem(quantity);
	}

	public final boolean canPlace(int i1) {
		return this.mc.thePlayer.inventory.consumeInventoryItem(i1);
	}

	public final void clickBlock(int i1, int i2, int i3) {
		int i4;
		if((i4 = this.mc.theWorld.getBlockId(i1, i2, i3)) > 0 && Block.blocksList[i4].blockStrength() == 0) {
			this.sendBlockRemoved(i1, i2, i3);
		}

	}

	public final void resetBlockRemoving() {
		this.curBlockDamage = 0;
		this.blockHitWait = 0;
	}

	public final void sendBlockRemoving(int i1, int i2, int i3) {
		if(this.blockHitWait > 0) {
			--this.blockHitWait;
		} else if(i1 == this.curBlockX && i2 == this.curBlockY && i3 == this.curBlockZ) {
			int i4;
			if((i4 = this.mc.theWorld.getBlockId(i1, i2, i3)) != 0) {
				Block block5 = Block.blocksList[i4];
				this.prevBlockDamage = block5.blockStrength();
				++this.curBlockDamage;
				if(this.curBlockDamage == this.prevBlockDamage + 1) {
					this.sendBlockRemoved(i1, i2, i3);
					this.curBlockDamage = 0;
					this.blockHitWait = 5;
				}

			}
		} else {
			this.curBlockDamage = 0;
			this.curBlockX = i1;
			this.curBlockY = i2;
			this.curBlockZ = i3;
		}
	}

	public final void setPartialTime(float f1) {
		if(this.curBlockDamage <= 0) {
			this.mc.renderGlobal.damagePartialTime = 0.0F;
		} else {
			this.mc.renderGlobal.damagePartialTime = ((float)this.curBlockDamage + f1 - 1.0F) / (float)this.prevBlockDamage;
		}
	}

	public final float getBlockReachDistance() {
		return 4.0F;
	}

	public final boolean sendUseItem(EntityPlayer entityPlayer1, int i2) {
		Block block3;
		if((block3 = Block.blocksList[i2]) == Block.mushroomRed && this.mc.thePlayer.inventory.consumeInventoryItem(i2)) {
			entityPlayer1.attackEntityFrom((Entity)null, 3);
			return true;
		} else if(block3 == Block.mushroomBrown && this.mc.thePlayer.inventory.consumeInventoryItem(i2)) {
			boolean z4 = false;
			if(entityPlayer1.health > 0) {
				entityPlayer1.health += 5;
				if(entityPlayer1.health > 20) {
					entityPlayer1.health = 20;
				}

				entityPlayer1.scoreValue = entityPlayer1.heartsHalvesLife / 2;
			}

			return true;
		} else {
			return false;
		}
	}

	public final void onWorldChange(World world1) {
		super.onWorldChange(world1);
		this.mobSpawner = new MobSpawner(world1);
		int i2 = world1.width * world1.length * world1.height / 64 / 64 / 8;

		for (int i3 = 0; i3 < i2; ++i3) {
		    if (!world1.networkMode) {
		        this.mobSpawner.performSpawning(i2, world1.playerEntity, (LoadingScreenRenderer)null);
		    }
		}


	}

	public final void onUpdate(World world1) {
        if (this.mobSpawner != null) {
        	if (!world1.networkMode) {
        		this.mobSpawner.performSpawning();
        	}
        }
	}
}
