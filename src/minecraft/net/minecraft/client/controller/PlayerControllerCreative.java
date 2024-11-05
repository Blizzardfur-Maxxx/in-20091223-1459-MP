package net.minecraft.client.controller;

import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Session;
import net.minecraft.client.gui.GuiCreativeInventory;
import net.minecraft.client.player.EntityPlayer;
import net.minecraft.game.level.MobSpawner;
import net.minecraft.game.level.World;
import net.minecraft.game.level.block.Block;

public final class PlayerControllerCreative extends PlayerController {
	private MobSpawner mobSpawner;

	public PlayerControllerCreative(Minecraft minecraft1) {
		super(minecraft1);
		this.isInTestMode = true;
	}

	public final void displayInventoryGUI() {
		this.mc.displayGuiScreen(new GuiCreativeInventory());
	}

	public final void flipPlayer(EntityPlayer entityPlayer1) {
		for(int i2 = 0; i2 < 9; ++i2) {
			entityPlayer1.inventory.stackSize[i2] = 1;
			if(entityPlayer1.inventory.mainInventory[i2] <= 0) {
				entityPlayer1.inventory.mainInventory[i2] = ((Block)Session.allowedBlocks.get(i2)).blockID;
			}
		}

	}

	public final boolean shouldDrawHUD() {
		return false;
	}

	public final void onWorldChange(World world1) {
		super.onWorldChange(world1);
		world1.survivalWorld = false;
		this.mobSpawner = new MobSpawner(world1);
		int i2 = world1.width * world1.length * world1.height / 64 / 64 / 8;

		for(int i3 = 0; i3 < i2; ++i3) {
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