package net.minecraft.game.level.material;

public final class Material {
	private static Material[] materials = new Material[4];
	public static final Material air = new Material(0);
	public static final Material water = new Material(1);
	public static final Material lava = new Material(2);

	private Material(int i1) {
		materials[i1] = this;
	}
}