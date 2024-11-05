package net.minecraft.client.render.texture;

public class TextureFX {
	public byte[] imageData = new byte[1024];
	public int iconIndex;
	public boolean anaglyphEnabled = false;

	public TextureFX(int i1) {
		this.iconIndex = i1;
	}

	public void onTick() {
	}
}