package net.minecraft.game.level;

public final class NextTickListEntry {
	public int xCoord;
	public int yCoord;
	public int zCoord;
	public int blockID;
	public int scheduledTime;

	public NextTickListEntry(int i1, int i2, int i3, int i4) {
		this.xCoord = i1;
		this.yCoord = i2;
		this.zCoord = i3;
		this.blockID = i4;
	}
}