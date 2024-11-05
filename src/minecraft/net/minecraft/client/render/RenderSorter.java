package net.minecraft.client.render;

import java.util.Comparator;

import net.minecraft.client.player.EntityPlayer;

public final class RenderSorter implements Comparator {
	private EntityPlayer player;

	public RenderSorter(EntityPlayer entityPlayer1) {
		this.player = entityPlayer1;
	}

	public final int compare(Object object1, Object object2) {
		WorldRenderer worldRenderer10001 = (WorldRenderer)object1;
		WorldRenderer worldRenderer6 = (WorldRenderer)object2;
		WorldRenderer worldRenderer5 = worldRenderer10001;
		boolean z3 = worldRenderer5.isInFrustrum;
		boolean z4 = worldRenderer6.isInFrustrum;
		return z3 && !z4 ? 1 : ((!z4 || z3) && worldRenderer5.a(this.player) < worldRenderer6.a(this.player) ? 1 : -1);
	}
}