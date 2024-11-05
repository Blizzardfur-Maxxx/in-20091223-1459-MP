package net.minecraft.client.player;

import net.minecraft.game.entity.AILiving;

final class EntityPlayerInput extends AILiving {
	private EntityPlayer player;

	EntityPlayerInput(EntityPlayer entityPlayer1) {
		this.player = entityPlayer1;
	}

	public final void updatePlayerActionState() {
		this.moveStrafing = this.player.movementInput.moveStrafe;
		this.moveForward = this.player.movementInput.moveForward;
		this.isJumping = this.player.movementInput.jump;
	}
}