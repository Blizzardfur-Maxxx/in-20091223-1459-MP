package net.minecraft.client.player;

import net.minecraft.game.level.block.Block;

public final class InventoryPlayer {
	public int[] mainInventory = new int[9];
	public int[] stackSize = new int[9];
	public int[] animationsToGo = new int[9];
	public int currentItem = 0;

	public InventoryPlayer() {
		for(int i1 = 0; i1 < 9; ++i1) {
			this.mainInventory[i1] = -1;
			this.stackSize[i1] = 0;
		}

	}

	public final int getCurrentItem() {
		return this.mainInventory[this.currentItem];
	}

	public final int getInventorySlotContainItem(int i1) {
		for(int i2 = 0; i2 < this.mainInventory.length; ++i2) {
			if(i1 == this.mainInventory[i2]) {
				return i2;
			}
		}

		return -1;
	}

	public final void replaceSlot(Block block1) {
		if(block1 != null) {
			int i2;
			if((i2 = this.getInventorySlotContainItem(block1.blockID)) >= 0) {
				this.mainInventory[i2] = this.mainInventory[this.currentItem];
			}

			this.mainInventory[this.currentItem] = block1.blockID;
		}

	}

	public final boolean consumeInventoryItem(int i1) {
		if((i1 = this.getInventorySlotContainItem(i1)) < 0) {
			return false;
		} else {
			if(--this.stackSize[i1] <= 0) {
				this.mainInventory[i1] = -1;
			}

			return true;
		}
	}
}