package net.minecraft.client.render;

import java.util.Comparator;

import net.minecraft.client.player.EntityPlayer;

public final class EntitySorter implements Comparator {
	private EntityPlayer player;

	public EntitySorter(EntityPlayer entityPlayer1) {
		this.player = entityPlayer1;
	}

	public final int compare(Object object1, Object object2) {
		WorldRenderer worldRenderer10001 = (WorldRenderer)object1;
		WorldRenderer worldRenderer4 = (WorldRenderer)object2;
		WorldRenderer worldRenderer3 = worldRenderer10001;
		return worldRenderer3.a(this.player) < worldRenderer4.a(this.player) ? -1 : 1;
	}
}