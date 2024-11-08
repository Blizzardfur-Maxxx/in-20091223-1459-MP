package net.minecraft.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.game.level.block.Block;

public final class Session {
	public static List allowedBlocks;
	public String username;

	public Session(String string1) {
		this.username = string1;
	}

	static {
		(allowedBlocks = new ArrayList()).add(Block.stone);
		allowedBlocks.add(Block.cobblestone);
		allowedBlocks.add(Block.brick);
		allowedBlocks.add(Block.dirt);
		allowedBlocks.add(Block.planks);
		allowedBlocks.add(Block.log);
		allowedBlocks.add(Block.leaves);
		allowedBlocks.add(Block.torch);
		allowedBlocks.add(Block.stairSingle);
		allowedBlocks.add(Block.glass);
		allowedBlocks.add(Block.cobblestoneMossy);
		allowedBlocks.add(Block.sapling);
		allowedBlocks.add(Block.plantYellow);
		allowedBlocks.add(Block.plantRed);
		allowedBlocks.add(Block.mushroomBrown);
		allowedBlocks.add(Block.mushroomRed);
		allowedBlocks.add(Block.sand);
		allowedBlocks.add(Block.gravel);
		allowedBlocks.add(Block.sponge);
		allowedBlocks.add(Block.clothRed);
		allowedBlocks.add(Block.clothOrange);
		allowedBlocks.add(Block.clothYellow);
		allowedBlocks.add(Block.clothChartreuse);
		allowedBlocks.add(Block.clothGreen);
		allowedBlocks.add(Block.clothSpringGreen);
		allowedBlocks.add(Block.clothCyan);
		allowedBlocks.add(Block.clothCapri);
		allowedBlocks.add(Block.clothUltramarine);
		allowedBlocks.add(Block.clothViolet);
		allowedBlocks.add(Block.clothPurple);
		allowedBlocks.add(Block.clothMagenta);
		allowedBlocks.add(Block.clothRose);
		allowedBlocks.add(Block.clothDarkGray);
		allowedBlocks.add(Block.clothGray);
		allowedBlocks.add(Block.clothWhite);
		allowedBlocks.add(Block.oreCoal);
		allowedBlocks.add(Block.oreIron);
		allowedBlocks.add(Block.oreGold);
		allowedBlocks.add(Block.ironBlock);
		allowedBlocks.add(Block.goldBlock);
		allowedBlocks.add(Block.bookShelf);
		allowedBlocks.add(Block.tnt);
		allowedBlocks.add(Block.obsidian);
	}
}