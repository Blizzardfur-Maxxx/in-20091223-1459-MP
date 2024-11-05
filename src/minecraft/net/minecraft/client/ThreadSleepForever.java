package net.minecraft.client;

final class ThreadSleepForever extends Thread {
	ThreadSleepForever() {
		this.setDaemon(true);
		this.start();
	}

	public final void run() {
		while(true) {
			try {
				Thread.sleep(2147483647L);
			} catch (InterruptedException interruptedException1) {
			}
		}
	}
}