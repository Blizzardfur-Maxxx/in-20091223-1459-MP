package net.minecraft.game.level.block;

public final class BlockOreBlock extends Block {
	public BlockOreBlock(int i1, int i2) {
		super(i1);
		this.blockIndexInTexture = i2;
	}

	public final int getBlockTexture(int i1) {
		return i1 == 1 ? this.blockIndexInTexture - 16 : (i1 == 0 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture);
	}
}