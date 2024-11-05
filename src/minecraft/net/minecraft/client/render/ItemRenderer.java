package net.minecraft.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.game.level.block.Block;

public final class ItemRenderer {
	public Minecraft minecraft;
	public Block itemToRender = null;
	public float equippedProgress = 0.0F;
	public float prevEquippedProgress = 0.0F;
	public int swingProgress = 0;
	public boolean itemSwingState = false;
	RenderBlocks renderBlocksInstance = new RenderBlocks(Tessellator.instance);

	public ItemRenderer(Minecraft minecraft1) {
		this.minecraft = minecraft1;
	}
}