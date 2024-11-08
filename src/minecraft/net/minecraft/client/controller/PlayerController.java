package net.minecraft.client.controller;

import net.minecraft.client.Minecraft;
import net.minecraft.client.effect.EffectRenderer;
import net.minecraft.client.effect.EntityDiggingFX;
import net.minecraft.client.player.EntityPlayer;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public class PlayerController {
	protected final Minecraft mc;
	public boolean isInTestMode = false;

	public PlayerController(Minecraft minecraft1) {
		this.mc = minecraft1;
	}

	public void onWorldChange(World world1) {
		world1.multiplayerWorld = true;
	}

	public void displayInventoryGUI() {
	}

	public void clickBlock(int i1, int i2, int i3) {
		this.sendBlockRemoved(i1, i2, i3);
	}

	public boolean canPlace(int i1) {
		return true;
	}

	public void sendBlockRemoved(int i1, int i2, int i3) {
		int i7 = i3;
		int i6 = i2;
		int i5 = i1;
		EffectRenderer effectRenderer4 = this.mc.effectRenderer;
		int i8;
		if((i8 = this.mc.effectRenderer.worldObj.getBlockId(i1, i2, i3)) != 0) {
			Block block18 = Block.blocksList[i8];

			for(int i9 = 0; i9 < 4; ++i9) {
				for(int i10 = 0; i10 < 4; ++i10) {
					for(int i11 = 0; i11 < 4; ++i11) {
						float f12 = (float)i5 + ((float)i9 + 0.5F) / (float)4;
						float f13 = (float)i6 + ((float)i10 + 0.5F) / (float)4;
						float f14 = (float)i7 + ((float)i11 + 0.5F) / (float)4;
						effectRenderer4.addEffect(new EntityDiggingFX(effectRenderer4.worldObj, f12, f13, f14, f12 - (float)i5 - 0.5F, f13 - (float)i6 - 0.5F, f14 - (float)i7 - 0.5F, block18));
					}
				}
			}
		}

		World world15 = this.mc.theWorld;
		Block block16 = Block.blocksList[world15.getBlockId(i1, i2, i3)];
		boolean z17 = world15.setBlockWithNotify(i1, i2, i3, 0);
		if(block16 != null && z17) {
			if(this.mc.isOnlineClient()) {
				this.mc.networkClient.sendTileUpdated(i1, i2, i3, 0, this.mc.thePlayer.inventory.getCurrentItem());
			}
			block16.onBlockDestroyedByPlayer(world15, i1, i2, i3);
		}

	}
	
	public boolean removeResource(int quantity) {
		return true;
	}

	
	public boolean removeResource(EntityPlayer player, int quantity) {
		return false;
	}

	public void sendBlockRemoving(int i1, int i2, int i3) {
	}

	public void resetBlockRemoving() {
	}

	public void setPartialTime(float f1) {
	}

	public float getBlockReachDistance() {
		return 5.0F;
	}

	public boolean sendUseItem(EntityPlayer entityPlayer1, int i2) {
		return false;
	}

	public void preparePlayer(EntityPlayer entityPlayer1) {
	}

	public void onUpdate() {
	}

	public boolean shouldDrawHUD() {
		return true;
	}

	public void flipPlayer(EntityPlayer entityPlayer1) {
	}
}