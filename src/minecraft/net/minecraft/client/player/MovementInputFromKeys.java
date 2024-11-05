package net.minecraft.client.player;

import net.minecraft.client.GameSettings;

public final class MovementInputFromKeys extends MovementInput {
	private boolean[] keys = new boolean[10];
	private GameSettings settings;

	public MovementInputFromKeys(GameSettings gameSettings1) {
		this.settings = gameSettings1;
	}

	public final void checkKeyForMovementInput(int i1, boolean z2) {
		byte b3 = -1;
		if(i1 == this.settings.keyBindForward.keyCode) {
			b3 = 0;
		}

		if(i1 == this.settings.keyBindBack.keyCode) {
			b3 = 1;
		}

		if(i1 == this.settings.keyBindLeft.keyCode) {
			b3 = 2;
		}

		if(i1 == this.settings.keyBindRight.keyCode) {
			b3 = 3;
		}

		if(i1 == this.settings.keyBindJump.keyCode) {
			b3 = 4;
		}
		
		if(i1 == this.settings.keyBindSneak.keyCode) {
			b3 = 5;
		}

		if(b3 >= 0) {
			this.keys[b3] = z2;
		}

	}

	public final void resetKeyState() {
		for(int i1 = 0; i1 < 10; ++i1) {
			this.keys[i1] = false;
		}

	}

	public final void updatePlayerMoveState() {
		this.moveStrafe = 0.0F;
		this.moveForward = 0.0F;
		if(this.keys[0]) {
			--this.moveForward;
		}

		if(this.keys[1]) {
			++this.moveForward;
		}

		if(this.keys[2]) {
			--this.moveStrafe;
		}

		if(this.keys[3]) {
			++this.moveStrafe;
		}

		this.jump = this.keys[4];
		
		this.sneak = this.keys[5];

	}
}