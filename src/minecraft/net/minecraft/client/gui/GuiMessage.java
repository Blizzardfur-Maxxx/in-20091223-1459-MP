package net.minecraft.client.gui;

public final class GuiMessage {
	public String message;
	int counter;

	public GuiMessage(String message) {
		this.message = message;
		this.counter = 0;
	}
}